# Databricks notebook source
import dlt
from pyspark.sql.functions import col, avg, max, min, sum, round, year, month
from pyspark.sql.window import Window

@dlt.table(
    name="gold_stock_summary",
    comment="Daily + Monthly stock analytics"
)
def gold_stock_summary():

    silver_df = dlt.read("silver_stock")

    window_7 = Window.partitionBy("symbol").orderBy("date").rowsBetween(-6, 0)
    window_30 = Window.partitionBy("symbol").orderBy("date").rowsBetween(-29, 0)

    gold_df = silver_df.withColumn(
        "ma_7_close", round(avg("close").over(window_7),2)
    ).withColumn(
        "ma_30_close", round(avg("close").over(window_30),2)
    ).withColumn(
        "year", year("date")
    ).withColumn(
        "month", month("date")
    )

    monthly_df = gold_df.groupBy("symbol","year","month").agg(
        round(avg("close"),2).alias("avg_monthly_close"),
        round(max("high"),2).alias("max_monthly_high"),
        round(min("low"),2).alias("min_monthly_low"),
        round(avg("daily_return_pct"),2).alias("avg_monthly_return_pct"),
        sum("volume").alias("total_monthly_volume")
    )

    return gold_df.join(monthly_df, ["symbol","year","month"], "left")