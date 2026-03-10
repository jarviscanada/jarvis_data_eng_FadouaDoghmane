# Databricks Fundamentals Project

## Introduction

This project demonstrates how to build **modern data pipelines using Databricks**. It showcases key data engineering concepts such as **ETL workflows, Delta Lake storage, the Medallion Architecture (Bronze → Silver → Gold), and Delta Live Tables (DLT)**.

The project consists of two main parts:

1. **Fraud Detection Data Pipeline** using a traditional ETL approach in Databricks
2. **Stock Market Data Pipeline** implemented using **Delta Live Tables (DLT)**

The goal is to simulate a real-world **data engineering workflow**, from ingesting raw data to producing analytics-ready datasets that can power dashboards.

Key technologies used in this project include:

* **Databricks**
* **PySpark**
* **Delta Lake**
* **Delta Live Tables (DLT)**
* **Databricks SQL Dashboards**

---

# ETL in Databricks

The first part of the project implements an **ETL pipeline** using the **Medallion Architecture**, which organizes data into layers of increasing quality and structure.

## Architecture

Bronze → Silver → Gold

Each layer progressively improves the data:

| Layer  | Description                                             |
| ------ | ------------------------------------------------------- |
| Bronze | Raw data ingestion with minimal transformations         |
| Silver | Cleaned and standardized datasets                       |
| Gold   | Aggregated tables designed for analytics and dashboards |

---

## Bronze Layer

The Bronze layer ingests raw data from several datasets:

* `transactions`
* `cards`
* `users`
* `mcc_codes`
* `fraud_labels`

These datasets are loaded as **Delta tables** in their raw form, preserving the original structure of the source data.

The goal of the Bronze layer is to provide a **reliable raw data source** for downstream transformations.

---

## Silver Layer

The Silver layer performs **data cleaning and enrichment**.

Key transformations include:

* Standardizing column names
* Converting currency fields to numeric values
* Removing invalid or null records
* Enriching transactions with additional information such as:

  * Merchant Category Codes
  * Fraud labels

Three cleaned datasets are produced:

* `silver.transactions`
* `silver.cards`
* `silver.users`

The **transactions table is enhanced with merchant category and fraud indicators**, making it suitable for analytics and fraud analysis.

---

## Gold Layer

The Gold layer creates **aggregated datasets designed for reporting and dashboards**.

Four analytics tables are generated:

| Gold Table               | Description                                                    |
| ------------------------ | -------------------------------------------------------------- |
| `fraud_time_metrics`     | Fraud activity trends by date, week, and time of day           |
| `fraud_user_metrics`     | Users with the highest number of fraudulent transactions       |
| `fraud_merchant_metrics` | Merchant categories and merchants with the highest fraud rates |
| `fraud_amount_metrics`   | Fraud analysis based on transaction amount segments            |

These tables allow the analysis of key questions such as:

* Which days of the week have the highest fraud activity?
* How does the fraud rate evolve over time?
* Which users generate the most fraudulent transactions?
* Which merchant categories are most affected by fraud?
* Are high-value transactions more likely to be fraudulent?

---

## Dashboard

A **Databricks SQL dashboard** was created using the Gold tables.

Example visualizations include:

* Fraud rate trends over time
* Fraud distribution by time of day
* Top users with fraudulent transactions
* Merchant categories with the highest fraud rates
* Comparison of fraud vs non-fraud transaction amounts

Dashboard filters allow analysis by:

* Date
* Merchant category
* Transaction amount segment
* User

---

# DLT in Databricks

The second part of the project demonstrates how to build a **Delta Live Tables (DLT) pipeline**.

DLT simplifies pipeline development by providing:

* Automated dependency management
* Incremental processing
* Built-in data quality capabilities
* Simplified pipeline orchestration

---

## Bronze Layer (DLT)

The Bronze layer ingests stock market data from the **Alpha Vantage API**.

Stock symbols included:

* AAPL
* MSFT
* TSLA
* GOOGL

The raw data is stored in the table:

`bronze_stock`

Fields include:

* symbol
* date
* open
* high
* low
* close
* volume

---

## Silver Layer (DLT)

The Silver layer performs **data cleaning and transformation**.

Transformations include:

* Casting numeric columns to proper data types
* Converting the date field to a date format
* Removing rows with null values
* Calculating the **daily return percentage**

Output table:

`silver_stock`

---

## Gold Layer (DLT)

The Gold layer generates analytics-ready stock metrics.

Additional features include:

* **7-day moving average of closing price**
* **30-day moving average of closing price**
* Daily return percentage

Final analytics table:

`gold_stock_summary`

This dataset can be used to analyze stock price trends and build financial dashboards.

---

# Future Improvements

Potential improvements for this project include:

* Implementing **data quality rules using DLT expectations**
* Adding **streaming ingestion** for real-time data processing
* Training **machine learning models for fraud detection**
* Adding additional financial indicators to the stock pipeline
* Connecting the datasets to **Power BI or Tableau dashboards**
* Implementing **CI/CD for automated pipeline deployment**

---

This project demonstrates the core capabilities of **Databricks as a data engineering platform**, including scalable data pipelines, Delta Lake storage, and automated pipeline management with Delta Live Tables.
