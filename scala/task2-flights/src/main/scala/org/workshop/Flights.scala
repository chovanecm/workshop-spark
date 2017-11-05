package org.workshop

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Column
import org.apache.spark.sql.types.DoubleType

object Flights {
  def main(arg: Array[String]) {
    val spark = SparkSession.builder
      .appName("Flights")
      .getOrCreate()

    import spark.implicits._

    val flightsDS = spark.read
      .format("csv")
      .option("header", "true")
      .option("mode", "DROPMALFORMED")
      .load(arg(0))

    val rdd = flightsDS.rdd
    rdd.cache()

    // RDD: airline with the most flights from Boston
    val airlineWithMostFlights = rdd.filter(f => f.getAs("OriginCityName") == "Boston, MA")
      .map(f => (f.getAs[String]("Carrier"), 1))
      .reduceByKey(_ + _)
    val sortedFlights = airlineWithMostFlights.sortBy(_._2, false).coalesce(1)
    sortedFlights.saveAsTextFile(arg(1) + "/airline-with-most-flight/")

    // RDD: airline with the worst average delay
    val airlineWithWorstDelay = rdd.filter(r => !r.isNullAt(r.fieldIndex("ArrDelay"))
        && r.getAs[String]("ArrDelay").toDouble > 0)
      .map(row => (row.getAs[String]("Carrier"), row.getAs[String]("ArrDelay").toDouble))
      .mapValues(value => (value, 1))
      .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))
      .mapValues(v => v._1/v._2)
    val sortedDelays = airlineWithWorstDelay.sortBy(_._2, false).coalesce(1)
    sortedDelays.saveAsTextFile(arg(1) + "/airline-with-worst-delay/")

    // Dataset: airline with the least delay
    val airlineWithLeastDelay = flightsDS.filter((flightsDS.col("OriginCityName") === "New York, NY")
        && (flightsDS.col("DestCityName") === "San Francisco, CA")
        && (flightsDS.col("ArrDelay") > 0))
      .map(f => (f.getAs[String]("Carrier"), f.getAs[String]("ArrDelay").toDouble))
      .groupByKey(_._1)
      .reduceGroups((a, b) => (a._1, a._2 + b._2))
      .map(_._2)
    val sortedLeastDelay = airlineWithLeastDelay.orderBy("_2").coalesce(1)
    sortedLeastDelay.write.format("csv").save(arg(1) + "/airline-with-least-delay/")

    // SQL: the farthest 10 destination from Chicago
    flightsDS.withColumn("Distance", new Column("Distance").cast(DoubleType))
      .createOrReplaceTempView("flights")
    val farthesDestination = spark.sql("""SELECT DISTINCT OriginCityName, DestCityName, Distance
      FROM flights
      WHERE OriginCityName == 'Chicago, IL'
      ORDER BY Distance
      DESC LIMIT 10""")
    farthesDestination.write.format("csv").save(arg(1) + "/farthest-destination/")

    spark.stop()
  }
}

