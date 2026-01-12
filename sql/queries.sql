SQL QUERIES

# Modifying Data

# Question 1

INSERT INTO cd.facilities(
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
VALUES 
  (9, 'Spa', 20, 30, 100000, 800);

# Question 2

INSERT INTO cd.facilities(
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
SELECT 
  (
    SELECT 
      MAX(facid) 
    FROM 
      cd.facilities
  )+ 1, 
  'Spa', 
  20, 
  30, 
  100000, 
  800;

# Question 3

update 
  cd.facilities 
set 
  initialoutlay = 10000 
where 
  facid = 1;

# Question 4

update 
  cd.facilities fac 
set 
  membercost = (
    select 
      membercost * 1.1 
    from 
      cd.facilities 
    where 
      facid = 0
  ), 
  guestcost = (
    select 
      guestcost * 1.1 
    from 
      cd.facilities 
    where 
      facid = 0
  ) 
where 
  fac.facid = 1;

# Question 5

delete from 
  cd.bookings;


# Question 6

delete from 
  cd.members 
where 
  memid = 37;

# Basics

# Question 1

select 
  facid, 
  name, 
  membercost, 
  monthlymaintenance 
from 
  cd.facilities 
where 
  membercost > 0 
  and (
    membercost < monthlymaintenance / 50
  );


# Question 2

select 
  * 
from 
  cd.facilities 
where 
  name like '%Tennis%';


# Question 3

select 
  * 
from 
  cd.facilities 
where 
  facid in (1, 5);


# Question 4

select 
  memid, 
  surname, 
  firstname, 
  joindate 
from 
  cd.members 
where 
  joindate >= '2012-09-01';

# Question 5

select 
  surname 
from 
  cd.members 
union 
select 
  name 
from 
  cd.facilities;
  
# Join

# Question 1

select 
  b.starttime 
from 
  cd.bookings as b 
  join cd.members as m on b.memid = m.memid 
where 
  m.firstname = 'David' 
  and m.surname = 'Farrell';

# Question 2

select 
  b.starttime as start, 
  f.name 
from 
  cd.facilities as f 
  join cd.bookings as b on b.facid = f.facid 
where 
  f.name like 'Tennis Court %' 
  and b.starttime >= '2012-09-21' 
  and b.starttime < '2012-09-22' 
order by 
  b.starttime;

# Question 3

Select 
  mem.firstname as memfname, 
  mem.surname as memsname, 
  rec.firstname as recfname, 
  rec.surname as recsname 
from 
  cd.members mem 
  left outer join cd.members rec on rec.memid = mem.recommendedby 
order by 
  memsname, 
  memfname;


# Question 4

select 
  distinct rec.firstname, 
  rec.surname 
from 
  cd.members as mem 
  join cd.members as rec on rec.memid = mem.recommendedby 
order by 
  surname, 
  firstname;


# Question 5

select 
  distinct mem.firstname || ' ' || mem.surname as member, 
  (
    select 
      rec.firstname || ' ' || rec.surname as recommendedby 
    from 
      cd.members rec 
    where 
      mem.recommendedby = rec.memid
  ) 
from 
  cd.members mem 
order by 
  member;

# Aggregation

# Question 1

select 
  recommendedby, 
  count(*) as count 
from 
  cd.members 
where 
  recommendedby is not null 
group by 
  recommendedby 
order by 
  recommendedby;

# Question 2

select 
  facid, 
  sum(slots) as "Total Slots" 
from 
  cd.bookings as b 
group by 
  b.facid 
order by 
  b.facid;


# Question 3

select 
  facid, 
  sum(slots) as "Total Slots" 
from 
  cd.bookings 
where 
  starttime >= '2012-09-01' 
  and starttime < '2012-10-01' 
group by 
  facid 
order by 
  sum(slots);


# Question 4

select 
  facid, 
  extract (
    month 
    from 
      starttime
  ) as month, 
  sum(slots) as "Total Slots" 
from 
  cd.bookings 
where 
  extract(
    year 
    from 
      starttime
  ) = 2012 
group by 
  facid, 
  month 
order by 
  facid, 
  month;

# Question 5

select count(distinct memid) from cd.bookings;

# Question 6

select 
  mem.surname, 
  mem.firstname, 
  mem.memid, 
  min(b.starttime) as starttime 
from 
  cd.members mem 
  join cd.bookings b on b.memid = mem.memid 
where 
  b.starttime >= '2012-09-01 00:00:00' 
group by 
  mem.surname, 
  mem.firstname, 
  mem.memid 
order by 
  memid;

# Question 7

select 
  count(*) over(
    order by 
      joindate
  ) as row_number, 
  firstname, 
  surname 
from 
  cd.members 
order by 
  joindate;


# Question 8

select 
  row_number() over (
    order by 
      joindate
  ), 
  firstname, 
  surname 
from 
  cd.members 
order by 
  joindate;


# Question 9

select 
  facid, 
  total 
from 
  (
    select 
      f.facid, 
      sum(b.slots) as "total", 
      rank() over (
        order by 
          sum(b.slots) desc
      ) as r 
    from 
      cd.facilities as f 
      left join cd.bookings as b on b.facid = f.facid 
    group by 
      f.facid
  ) 
where 
  r = 1;

# String

# Question 1 

select 
  surname || ', ' || firstname as name 
from 
  cd.members;


# Question 2

select 
  memid, 
  telephone 
from 
  cd.members 
where 
  telephone ~ '[()]';

# Question 3

select 
  substring(surname, 1, 1) as letter, 
  count(*) 
from 
  cd.members 
group by 
  letter 
order by 
  letter;


