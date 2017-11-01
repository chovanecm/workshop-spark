package org.workshop

import org.apache.spark.sql.SparkSession

object Flights {
  def main(arg: Array[String]) {
    val spark = SparkSession.builder
      .appName(Flights)
      .getOrCreate()

    
  }
}

