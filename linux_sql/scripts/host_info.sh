#!/bin/bash

set -euo pipefail
IFS=$'\n\t'

if [ "$#" -ne 5 ]; then
	echo "Usage: $0 psql_host psql_port db_name psql_user psql_password"
	exit 1
fi

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

hostname=$(hostname -f)
lscpu_out=$(lscpu)

cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | grep "^Model name:" | cut -d: -f2- | xargs)
cpu_mhz=$(grep "cpu MHz" /proc/cpuinfo | head -1 | cut -d: -f2- | xargs)
l2_cache=$(lscpu | awk '/L2 cache/ {print $3}' | sed 's/K//')
total_mem=$(vmstat --unit M | tail -1 | awk '{print $4}')
timestamp=$(date "+%Y-%m-%d %H:%M:%S")

export PGPASSWORD=$psql_password

insert_stmt=$(cat <<-END
INSERT INTO host_info (
	hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp
) VALUES (
	'$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', '$total_mem', '$timestamp'
)
ON CONFLICT (hostname) DO UPDATE
SET
  cpu_number = EXCLUDED.cpu_number,
  cpu_architecture = EXCLUDED.cpu_architecture,
  cpu_model = EXCLUDED.cpu_model,
  cpu_mhz = EXCLUDED.cpu_mhz,
  l2_cache = EXCLUDED.l2_cache,
  total_mem = EXCLUDED.total_mem,
  timestamp = EXCLUDED.timestamp;
END
)

psql -h "$psql_host" -p "$psql_port" -d "$db_name" -U "$psql_user" -c "$insert_stmt"

exit $?
