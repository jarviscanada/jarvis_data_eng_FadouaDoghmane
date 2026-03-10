# Databricks notebook source
# -----------------------------
# Bronze Layer
# -----------------------------
import dlt
import requests
import time
from pyspark.sql import SparkSession

API_KEY = "HGJ4JU5Y7HMAM0BO"
symbols = ["AAPL","MSFT","TSLA","GOOGL"]

@dlt.table(
    name="bronze_stock",
    comment="Raw stock data from Alpha Vantage API"
)
def bronze_stock():
    """
    Ingest raw stock data from API.
    """
    data_list = []

    base_url = "https://www.alphavantage.co/query"

    for symbol in symbols:
        url = f"{base_url}?function=TIME_SERIES_DAILY&symbol={symbol}&apikey={API_KEY}"
        response = requests.get(url)
        data = response.json()

        if "Time Series (Daily)" in data:
            time_series = data["Time Series (Daily)"]
            for date, values in time_series.items():
                record = {
                    "symbol": symbol,
                    "date": date,
                    "open": float(values["1. open"]),
                    "high": float(values["2. high"]),
                    "low": float(values["3. low"]),
                    "close": float(values["4. close"]),
                    "volume": int(values["5. volume"])
                }
                data_list.append(record)

        # Respect API rate limit
        time.sleep(12)

    return spark.createDataFrame(data_list)