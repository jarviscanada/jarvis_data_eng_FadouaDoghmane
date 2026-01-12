-- Show table schema 
\d+ retail;

-- Q1 Show first 10 rows
select 
  * 
from 
  retail 
limit 
  10;

-- Q2 Check # of records
select 
  count(*) 
from 
  retail;

-- Q3 Number of clients 
select 
  count(
    distinct(customer_id)
  ) 
from 
  retail;

-- Q4 Invoice date range
select 
  min(invoice_date), 
  max(invoice_date) 
from 
  retail;

-- Q5 Number of SKU/merchants
select 
  count(
    distinct(stock_code)
  ) 
from 
  retail;

-- Q6 Calculate average invoice amount excluding invoices with a negative amount
select 
  avg(subquery.total) as avg_total 
from 
  (
    select 
      invoice_no, 
      sum(
        case when quantity > 0 then quantity * unit_price else 0 end
      ) as total 
    from 
      retail 
    group by 
      invoice_no 
    having 
      sum(
        case when quantity > 0 then quantity * unit_price else 0 end
      ) > 0
  ) as subquery;

-- Q7 Calculate total revenue
select 
  sum(quantity * unit_price) as total_revenue 
from 
  retail;

-- Q8 Calculate total revenue by YYYYMM
select 
  to_char(invoice_date, 'YYYYMM') as date_yyyymm, 
  sum(quantity * unit_price) as total_revenue_YYYMM 
from 
  retail 
group by 
  date_yyyymm 
order by 
  date_yyyymm asc;

