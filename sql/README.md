# SQL Exercises Documentation

This document summarizes the SQL exercises completed, including table setup, data modification, joins, aggregation, and string queries. Each section contains a description of the exercise, solution, and corresponding SQL queries. These exercises were performed using PostgreSQL and cover practical SQL skills such as data manipulation, querying, joins, aggregation functions, and window functions.

---

## Table Setup (DDL)

The database `exercises` contains a schema `cd` with three main tables: `members`, `facilities`, and `bookings`.

- **members**: Stores member information including names, contact details, who recommended them, and join dates.
- **facilities**: Lists all available facilities, their costs, initial outlay, and monthly maintenance.
- **bookings**: Tracks facility bookings by members, including the number of slots booked and start times.

Primary keys are defined for all tables, and foreign keys maintain relationships between `members` and `bookings` as well as `facilities` and `bookings`. Indexes were added on commonly queried columns to improve performance.

---

## Modifying Data

### Question 1
**Solution:** Inserted a new facility with specific details.

```sql
INSERT INTO cd.facilities(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES (9, 'Spa', 20, 30, 100000, 800);
```

### Question 2
**Solution:** Inserted a new facility with automatic `facid` assignment.

```sql
INSERT INTO cd.facilities(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
SELECT MAX(facid) + 1, 'Spa', 20, 30, 100000, 800 FROM cd.facilities;
```

### Question 3
**Solution:** Updated the `initialoutlay` of facility 1.

```sql
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;
```

### Question 4
**Solution:** Increased facility 1's costs by 10% relative to facility 0.

```sql
UPDATE cd.facilities fac
SET membercost = (SELECT membercost*1.1 FROM cd.facilities WHERE facid = 0),
    guestcost = (SELECT guestcost*1.1 FROM cd.facilities WHERE facid = 0)
WHERE fac.facid = 1;
```

### Question 5
**Solution:** Deleted all bookings.

```sql
DELETE FROM cd.bookings;
```

### Question 6
**Solution:** Deleted a member with a specific `memid`.

```sql
DELETE FROM cd.members
WHERE memid = 37;
```

---

## Basics

### Question 1
**Solution:** Selected facilities with member cost greater than 0 but less than 1/50 of monthly maintenance.

```sql
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost > 0 AND membercost < monthlymaintenance/50;
```

### Question 2
**Solution:** Selected all facilities containing "Tennis".

```sql
SELECT *
FROM cd.facilities
WHERE name LIKE '%Tennis%';
```

### Question 3
**Solution:** Selected facilities with `facid` 1 or 5.

```sql
SELECT *
FROM cd.facilities
WHERE facid IN (1,5);
```

### Question 4
**Solution:** Members who joined on or after 2012-09-01.

```sql
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01';
```

### Question 5
**Solution:** Combined member surnames and facility names, removing duplicates.

```sql
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;
```

---

## Join Queries

### Question 1
**Solution:** Booking start times for member "David Farrell".

```sql
SELECT b.starttime
FROM cd.bookings AS b
JOIN cd.members AS m ON b.memid = m.memid
WHERE m.firstname='David' AND m.surname='Farrell';
```

### Question 2
**Solution:** Bookings for Tennis Court facilities on 2012-09-21 ordered by time.

```sql
SELECT b.starttime AS start, f.name
FROM cd.facilities AS f
JOIN cd.bookings AS b ON b.facid = f.facid
WHERE f.name LIKE 'Tennis Court %'
  AND b.starttime >= '2012-09-21'
  AND b.starttime < '2012-09-22'
ORDER BY b.starttime;
```

### Question 3
**Solution:** Each member and the member who recommended them.

```sql
SELECT mem.firstname AS memfname, mem.surname AS memsname,
       rec.firstname AS recfname, rec.surname AS recsname
FROM cd.members mem
LEFT JOIN cd.members rec ON rec.memid = mem.recommendedby
ORDER BY memsname, memfname;
```

### Question 4
**Solution:** Distinct recommending members.

```sql
SELECT DISTINCT rec.firstname, rec.surname
FROM cd.members mem
JOIN cd.members rec ON rec.memid = mem.recommendedby
ORDER BY surname, firstname;
```

### Question 5
**Solution:** Each member with the full name of who recommended them.

```sql
SELECT DISTINCT mem.firstname || ' ' || mem.surname AS member,
       (SELECT rec.firstname || ' ' || rec.surname
        FROM cd.members rec
        WHERE mem.recommendedby = rec.memid)
FROM cd.members mem
ORDER BY member;
```

---

## Aggregation Queries

### Question 1
**Solution:** Count of members each recommending member has referred.

```sql
SELECT recommendedby, COUNT(*) AS count
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;
```

### Question 2
**Solution:** Total booking slots per facility.

```sql
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
GROUP BY facid
ORDER BY facid;
```

### Question 3
**Solution:** Total booking slots per facility in September 2012.

```sql
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime >= '2012-09-01' AND starttime < '2012-10-01'
GROUP BY facid
ORDER BY SUM(slots);
```

### Question 4
**Solution:** Monthly total slots per facility for 2012.

```sql
SELECT facid, EXTRACT(MONTH FROM starttime) AS month, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE EXTRACT(YEAR FROM starttime) = 2012
GROUP BY facid, month
ORDER BY facid, month;
```

### Question 5
**Solution:** Count of distinct members who made bookings.

```sql
SELECT COUNT(DISTINCT memid) FROM cd.bookings;
```

### Question 6
**Solution:** Earliest booking per member since 2012-09-01.

```sql
SELECT mem.surname, mem.firstname, mem.memid, MIN(b.starttime) AS starttime
FROM cd.members mem
JOIN cd.bookings b ON b.memid = mem.memid
WHERE b.starttime >= '2012-09-01 00:00:00'
GROUP BY mem.surname, mem.firstname, mem.memid
ORDER BY memid;
```

### Question 7
**Solution:** Running total of members ordered by join date.

```sql
SELECT COUNT(*) OVER (ORDER BY joindate) AS row_number, firstname, surname
FROM cd.members
ORDER BY joindate;
```

### Question 8
**Solution:** Row number per member ordered by join date.

```sql
SELECT ROW_NUMBER() OVER (ORDER BY joindate), firstname, surname
FROM cd.members
ORDER BY joindate;
```

### Question 9
**Solution:** Facility with the maximum total booking slots.

```sql
SELECT facid, total
FROM (
  SELECT f.facid, SUM(b.slots) AS total, RANK() OVER (ORDER BY SUM(b.slots) DESC) AS r
  FROM cd.facilities f
  LEFT JOIN cd.bookings b ON b.facid = f.facid
  GROUP BY f.facid
) sub
WHERE r = 1;
```

---

## String Queries

### Question 1
**Solution:** Format member names as "Surname, Firstname".

```sql
SELECT surname || ', ' || firstname AS name
FROM cd.members;
```

### Question 2
**Solution:** Members with parentheses in telephone numbers.

```sql
SELECT memid, telephone
FROM cd.members
WHERE telephone ~ '[()]';
```

### Question 3
**Solution:** Count members by first letter of surname.

```sql
SELECT SUBSTRING(surname, 1, 1) AS letter, COUNT(*)
FROM cd.members
GROUP BY letter
ORDER BY letter;
```

