package org.workshop;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import scala.Tuple2;

import static org.apache.spark.sql.types.DataTypes.DoubleType;

public final class Flights {

  public static void main(String[] args) throws Exception {

    SparkSession spark = SparkSession
      .builder()
	  .appName("Word Count")
	  .getOrCreate();

	Dataset<Row> flightsDS = spark.read()
      .format("csv")
	  .option("header", "true")
	  .option("mode", "DROPMALFORMED")
	  .load(args[0]);

    JavaRDD<Row> rdd = flightsDS.javaRDD();
    rdd.cache();


    // RDD: airline with the most flights from Boston
    JavaPairRDD<String, Integer> airlineWithMostFlights = rdd
      .filter((Row row) -> row.getAs("OriginCityName").equals("Boston, MA"))
      .mapToPair((Row row) -> new Tuple2<>((String)row.getAs("Carrier"), 1))
      .reduceByKey((i1, i2) -> i1 + i2);
    // next three lines represents sortByValue
    JavaPairRDD<String, Integer> sortedFlights = airlineWithMostFlights.mapToPair(Tuple2::swap)
      .sortByKey(false)
      .mapToPair(Tuple2::swap).coalesce(1);
    sortedFlights.saveAsTextFile(args[1] + "/airline-with-most-flight/");


    // RDD: airline with the worst average delay
    JavaPairRDD<String, Double> airlineWithWorstDelay = rdd
      .filter((Row row) -> !row.isNullAt(row.fieldIndex("ArrDelay"))
          && Double.parseDouble(row.getString(row.fieldIndex("ArrDelay"))) > 0)
      .mapToPair(row -> new Tuple2<>((String)row.getAs("Carrier"),
          Double.parseDouble(row.getAs("ArrDelay"))))
      .mapValues(value -> new Tuple2<>(value, 1))
      .reduceByKey((a, b) -> new Tuple2<>(a._1() + b._1(), a._2 + b._2))
      .mapValues(value -> value._1()/value._2());
    // next three lines represents sortByValue
    JavaPairRDD<String, Double> sortedDelays = airlineWithWorstDelay.mapToPair(Tuple2::swap)
      .sortByKey(false)
      .mapToPair(Tuple2::swap).coalesce(1);
    sortedDelays.saveAsTextFile(args[1] + "/airline-with-worst-delay/");


//    // Dataset: airline with the least delay
//    flightsDS.filter(flightsDS.col("OriginCityName").equalTo("New York, NY")
//        .and(flightsDS.col("DestCityName").equalTo("San Francisco, CA"))
//        .and(flightsDS.col("ArrDelay").gt(0)))


    // SQL: the farthest 10 destination from Chicago
    flightsDS.withColumn("Distance", new Column("Distance").cast(DoubleType))
       .createOrReplaceTempView("flights");
    Dataset<Row> farthesDestination = spark.sql("SELECT DISTINCT OriginCityName, DestCityName, Distance " +
          "FROM flights " +
          "WHERE OriginCityName == 'Chicago, IL' " +
          "ORDER BY Distance " +
          "DESC LIMIT 10");
    farthesDestination.write().format("csv").save(args[1] + "/farthest-destination/");


    spark.stop();
  }

}
