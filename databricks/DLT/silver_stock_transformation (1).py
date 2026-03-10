# Databricks notebook source
# ======================================
# Silver Layer Transformation (DLT)
# ======================================

import dlt
from pyspark.sql.functions import col, to_date, round

@dlt.table(
    name="silver_stock",
    comment="Cleaned and enriched stock data from bronze layer"
)
def silver_stock():

    # Read Bronze table from DLT pipeline
    bronze_df = dlt.read("bronze_stock")

    # Data Cleaning / Transformation
    silver_df = bronze_df.withColumn("open", col("open").cast("double")) \
                         .withColumn("high", col("high").cast("double")) \
                         .withColumn("low", col("low").cast("double")) \
                         .withColumn("close", col("close").cast("double")) \
                         .withColumn("volume", col("volume").cast("long"))

    # Convert date column
    silver_df = silver_df.withColumn("date", to_date(col("date"), "yyyy-MM-dd"))

    # Remove bad rows
    silver_df = silver_df.dropna(subset=["symbol","date","open","high","low","close","volume"])

    # Calculate daily return %
    silver_df = silver_df.withColumn(
        "daily_return_pct",
        round((col("close") - col("open")) / col("open") * 100, 2)
    )

    return silver_df