# README.md

# SQL Queries Documentation

This document explains the solutions to each SQL query from the exercises. Each question is numbered and includes a short explanation of the solution.

---

## Modifying Data

### Question 1
**Solution:** Inserted a new facility with `facid = 9`, name "Spa", member cost 20, guest cost 30, initial outlay 100000, and monthly maintenance 800.  

### Question 2
**Solution:** Inserted a new facility using a `SELECT` statement to automatically assign the next `facid` based on the current maximum `facid`.  

### Question 3
**Solution:** Updated the `initialoutlay` of the facility with `facid = 1` to 10000.  

### Question 4
**Solution:** Updated facility 1s `membercost` and `guestcost` to 10% higher than facility 0s costs.  

### Question 5
**Solution:** Deleted all bookings from the `cd.bookings` table.  

### Question 6
**Solution:** Deleted the member with `memid = 37` from the `cd.members` table.  

---

## Basics

### Question 1
**Solution:** Selected facilities where the member cost is positive but less than 1/50th of the monthly maintenance.  

### Question 2
**Solution:** Selected all facilities containing "Tennis" in their name.  

### Question 3
**Solution:** Selected facilities with `facid` 1 or 5.  

### Question 4
**Solution:** Selected members who joined on or after September 1, 2012.  

### Question 5
**Solution:** Combined member surnames and facility names into a single list, removing duplicates using `UNION`.  

---

## Join Queries

### Question 1
**Solution:** Selected booking start times for the member "David Farrell".  

### Question 2
**Solution:** Selected bookings for Tennis Court facilities on September 21, 2012, ordered by start time.  

### Question 3
**Solution:** Selected each member and the member who recommended them (if any) using a left join.  

### Question 4
**Solution:** Selected distinct recommending members.  

### Question 5
**Solution:** Selected each member with the full name of the member who recommended them.  

---

## Aggregation

### Question 1
**Solution:** Counted how many members each recommending member has referred.  

### Question 2
**Solution:** Calculated total booking slots per facility.  

### Question 3
**Solution:** Calculated total booking slots per facility in September 2012.  

### Question 4
**Solution:** Calculated monthly total slots per facility for the year 2012.  

### Question 5
**Solution:** Counted the total number of distinct members who made bookings.  

### Question 6
**Solution:** Found the earliest booking per member from September 1, 2012.  

### Question 7
**Solution:** Produced a running total of members ordered by join date using `count() over`.  

### Question 8
**Solution:** Assigned a unique row number to each member ordered by join date using `row_number() over`.  

### Question 9
**Solution:** Selected the facility (or facilities) with the maximum total booking slots using `rank()` to handle ties.  

---

## String Queries

### Question 1
**Solution:** Formatted member names as "Surname, Firstname".  

### Question 2
**Solution:** Selected members whose telephone numbers contain parentheses using a regex pattern.  

### Question 3
**Solution:** Counted members by the first letter of their surname, ordered alphabetically.  

---

