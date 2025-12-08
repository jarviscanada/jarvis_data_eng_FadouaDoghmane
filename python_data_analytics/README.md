
# LGS Retail Data Analytics Project

## Introduction
This project focuses on analyzing LGS retail transaction data to provide actionable insights that help increase revenue, improve customer retention, and optimize marketing strategies. The dataset includes invoices, product details, quantities, prices, customer IDs, and purchase dates. The project involves two main parts: (1) setting up a Data Warehouse for OLAP to efficiently store and analyze transactional data, and (2) data analytics and customer segmentation using Python (pandas, NumPy, matplotlib) and RFM analysis to evaluate customer value.

## Business Use Case
- **Revenue growth**: Identify high-value customers and design targeted promotions.
- **Customer retention**: Detect at-risk customers to reduce churn.
- **Marketing efficiency**: Segment customers for precise campaigns.
- **Operational insights**: Track monthly sales, active users, and new vs. returning customers.

## Project Architecture
### Data Warehouse Setup
Transactional data is stored in Microsoft SQL Server (OLTP). To perform complex analysis without affecting transactional performance, the data is loaded into a **PostgreSQL data warehouse (OLAP)** via Docker.  

**Architecture Diagram:**
```
[ OLTP Database (MS SQL Server) ] --> [ Export CSV/SQL Dump ] --> [ PostgreSQL Data Warehouse (Jarvis PSQL) ]
                                    --> [ Python Analytics (Jupyter Notebook) ] --> [ RFM & Insights Dashboards ]
```
- **Docker Containers**: Separate containers for PostgreSQL and Jupyter Notebook connected via a Docker bridge network.
- **Data Loading**: SQL dump of retail data loaded into PostgreSQL using `psql` CLI.
- **Data Profiling**: SQL queries for table schema, row counts, unique customers, revenue, and invoice range.

### Example SQL Exploration
- First 10 rows: `SELECT * FROM retail LIMIT 10;`
- Total records: `SELECT COUNT(*) FROM retail;`
- Unique customers: `SELECT COUNT(DISTINCT customer_id) FROM retail;`
- Invoice date range: `SELECT MAX(invoice_date), MIN(invoice_date) FROM retail;`
- Total revenue: `SELECT SUM(quantity * unit_price) FROM retail;`
- Monthly revenue: 
```sql
SELECT EXTRACT(YEAR FROM invoice_date)*100 + EXTRACT(MONTH FROM invoice_date) AS yyyymm,
       SUM(quantity * unit_price) AS total
FROM retail
GROUP BY yyyymm
ORDER BY yyyymm;
```

## Data Analytics and Wrangling
- **Load Data**: Use pandas `read_sql` to load PostgreSQL table into a DataFrame.
- **Preprocessing**: Remove invalid transactions (negative quantity, missing customer IDs) and calculate `TotalPrice`.
- **Exploration**: `df.head()`, `df.info()`, `df.describe()`.

### Monthly Metrics
- **Monthly Sales**: Total sales per month with trend plots.
- **Monthly Growth**: Month-over-month sales percentage change.
- **Active Users**: Unique customers per month.
- **New vs Existing Users**: Identify new customers by first purchase month.

### RFM Analysis
- **Metrics**:
  - Recency: Days since last purchase
  - Frequency: Total purchases
  - Monetary: Total spend
- **RFM Score**: Combined three-digit score from Recency, Frequency, and Monetary.
- **Customer Segments**: Champions, Loyal, At Risk, Hibernating, etc.

## Deliverables
- Jupyter Notebook: [https://github.com/jarviscanada/jarvis_data_eng_FadouaDoghmane/blob/feature/python_data_analytics/python_data_analytics/python_data_wrangling/retail_data_analytics_wrangling.ipynb](./retail_data_analytics_wrangling.ipynb)
- SQL Queries: `psql/data_explore.sql`
- Plots and visualizations: Monthly sales, growth, active users, customer segments
- RFM Table: Customer ID, Recency, Frequency, Monetary, RFM Score, Segment

## Technologies Used
- Python: pandas, NumPy, matplotlib, seaborn
- PostgreSQL: Data warehouse for OLAP
- Docker: Containerized PostgreSQL and Jupyter Notebook
- SQL: Data profiling, aggregation, and analysis

## Improvements (Future Work)
1. Automated ETL pipeline for new transactional data.
2. Predictive analytics to forecast customer churn and sales trends.
3. Interactive dashboards using Plotly, Dash, or Power BI.

## Conclusion
The project demonstrates setting up a data warehouse, performing retail data analytics, and segmenting customers using RFM. Insights help LGS improve marketing strategy, retain customers, and boost revenue.

