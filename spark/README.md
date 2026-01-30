# PySpark Data Analytics Project

## Introduction
This project demonstrates end-to-end **data analytics using PySpark** on both **Zeppelin notebooks** and **Databricks**. The business context revolves around analyzing **World Development Indicators (WDI)** and **retail datasets** to extract meaningful insights, perform aggregations, and implement transformations for data engineering purposes.

The project leverages modern big data technologies:
- **Datasets:** WDI CSV/Parquet, Retail CSV (`retail.csv`)
- **Platforms:** Apache Spark, Zeppelin (GCP), Databricks (Azure)
- **Languages / APIs:** PySpark, Spark SQL, Structured APIs (DataFrames)
- **Storage / Metadata:** Hive Metastore, DBFS, HDFS
- **Cloud:** GCP, Azure
- **Processing:** Distributed computation using Spark Executors, Tasks, Partitions

---

## Databricks and Hadoop Implementation

### Dataset and Analytics
The retail dataset (`retail.csv`) is processed using **PySpark DataFrames** in **Azure Databricks**. Key analytics tasks include:
- Data wrangling and cleaning
- Aggregations (group by, sum, average)
- Creating temporary views for SQL queries
- Persisting data to Hive Metastore for further querying  

📓 **Databricks Notebooks:**
- [`Retail Data Analytics with PySpark.ipynb`](spark/notebook/Retail%20Data%20Analytics%20with%20PySpark.ipynb)  
- [`Retail Data Analytics with PySpark.dbc`](spark/notebook/Retail%20Data%20Analytics%20with%20PySpark.dbc)

### Architecture (Explained in Words)
The **Databricks architecture** consists of:
- **DBFS** (Databricks File System) storing the raw and processed datasets.
- **Hive Metastore** storing table metadata and schemas.
- **Databricks Cluster** containing a **driver** node and multiple **executor** nodes.  
    - The **driver** schedules tasks, manages SparkSession, and coordinates the execution plan.
    - The **executors** run tasks in parallel on partitions of data.
- **Data Flow:** CSV files are loaded into **Spark DataFrames** → transformed/aggregated using PySpark → stored in **Hive tables** or DBFS → results are displayed in notebooks.

---

## Zeppelin and Hadoop Implementation

### Dataset and Analytics
The WDI dataset is processed using **PySpark DataFrames** in **Zeppelin notebooks** running on **GCP Dataproc**. Key analytics work includes:
- Reading CSV/Parquet data into Spark DataFrames
- Creating Hive tables and temporary views
- Data transformation and aggregation for analytics
- Visualizing results within Zeppelin notebooks

📓 **Zeppelin Notebook:**  
- [`Spark Dataframe - WDI Data Analytics.json`](spark/notebook/Spark%20Dataframe%20-%20WDI%20Data%20Analytics.json)

### Architecture (Explained in Words)
The **Zeppelin architecture** consists of:
- **HDFS** storing the WDI datasets.
- **Hive Metastore** storing table schemas and metadata.
- **Zeppelin Notebook UI** where users write Spark code (PySpark, SQL).
- **Dataproc Cluster** running the Spark jobs:
    - **Driver** coordinates task scheduling and SparkSession.
    - **Executors** process tasks in parallel across partitions.
- **Data Flow:** HDFS → Spark DataFrames → transformations/aggregations → Hive Table → results displayed in Zeppelin.

---

## Future Improvements
1. **Real-time Analytics:** Integrate Spark Streaming or Structured Streaming to handle live data pipelines.  
2. **Automated ETL Pipelines:** Use Airflow or Azure Data Factory to schedule data ingestion, transformation, and storage.  
3. **Enhanced Visualization:** Integrate BI tools (Power BI, Tableau) on top of Databricks and Zeppelin for dashboards.  
4. **Scalability Testing:** Test with larger datasets to benchmark performance and optimize cluster configuration.  
5. **Data Quality Checks:** Implement validation and error handling during ingestion and transformation steps.
