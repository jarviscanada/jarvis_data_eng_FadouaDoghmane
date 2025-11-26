-- 1. Group hosts by CPU number, sort by memory
SELECT
    cpu_number,
    id AS host_id,
    total_mem
FROM host_info
ORDER BY cpu_number, total_mem DESC;

-- 2. Average memory usage (%) over 5-minute intervals
SELECT
    hu.host_id,
    hi.hostname,
    date_trunc('hour', hu."timestamp") + (date_part('minute', hu."timestamp")::int / 5) * interval '5 min' AS interval_start,
    AVG(100 * (hi.total_mem - hu.memory_free) / hi.total_mem) AS avg_used_mem_percentage
FROM host_usage hu
JOIN host_info hi ON hu.host_id = hi.id
WHERE hu.memory_free <= hi.total_mem
GROUP BY hu.host_id, hi.hostname, interval_start
ORDER BY hu.host_id, interval_start;

-- 3. Detect host failures (fewer than 3 data points per 5-min interval)
WITH rounded AS (
    SELECT
        host_id,
        date_trunc('hour', "timestamp") + (date_part('minute', "timestamp")::int / 5) * interval '5 min' AS interval_start,
        COUNT(*) AS cnt
    FROM host_usage
    GROUP BY host_id, interval_start
)
SELECT
    r.host_id,
    hi.hostname,
    r.interval_start,
    r.cnt
FROM rounded r
JOIN host_info hi ON r.host_id = hi.id
WHERE r.cnt < 3
ORDER BY r.host_id, r.interval_start;

