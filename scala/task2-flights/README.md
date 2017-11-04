## task 2: Analyzing Flight Delays

You will get a taste of RDD and SQL API during an analyzing a real-world dataset that represents information about US flight delays in January 2016. You can [download](https://www.transtats.bts.gov/DL_SelectFields.asp?Table_ID=236&DB_Short_Name=On-Time) additional datasets.
___

#### 1. Look at a data
  Dataset has two files in ```/root/workshop-spark/data/task2``` directory. First HTML file with description of data and ```airline-delays.csv``` file that represents a comma-separated collection of flight records, one record per line.

  Let's count the number of the record.
  ```
  wc -l airline-delays.csv
  ```
  and display first 5 lines of file.
  ```
  head -n 5 data/task2/airline-delays.csv
  "Year","Quarter","Month","DayofMonth","DayOfWeek","FlightDate","UniqueCarrier","AirlineID","Carrier","TailNum","FlightNum","OriginAirportID","OriginAirportSeqID","OriginCityMarketID","Origin","OriginCityName","OriginState","OriginStateFips","OriginStateName","OriginWac","DestAirportID","DestAirportSeqID","DestCityMarketID","Dest","DestCityName","DestState","DestStateFips","DestStateName","DestWac","CRSDepTime","DepTime","DepDelay","DepDelayMinutes","DepDel15","DepartureDelayGroups","DepTimeBlk","TaxiOut","WheelsOff","WheelsOn","TaxiIn","CRSArrTime","ArrTime","ArrDelay","ArrDelayMinutes","ArrDel15","ArrivalDelayGroups","ArrTimeBlk","Cancelled","CancellationCode","Diverted","CRSElapsedTime","ActualElapsedTime","AirTime","Flights","Distance","DistanceGroup","CarrierDelay","WeatherDelay","NASDelay","SecurityDelay","LateAircraftDelay","FirstDepTime","TotalAddGTime","LongestAddGTime","DivAirportLandings","DivReachedDest","DivActualElapsedTime","DivArrDelay","DivDistance","Div1Airport","Div1AirportID","Div1AirportSeqID","Div1WheelsOn","Div1TotalGTime","Div1LongestGTime","Div1WheelsOff","Div1TailNum","Div2Airport","Div2AirportID","Div2AirportSeqID","Div2WheelsOn","Div2TotalGTime","Div2LongestGTime","Div2WheelsOff","Div2TailNum","Div3Airport","Div3AirportID","Div3AirportSeqID","Div3WheelsOn","Div3TotalGTime","Div3LongestGTime","Div3WheelsOff","Div3TailNum","Div4Airport","Div4AirportID","Div4AirportSeqID","Div4WheelsOn","Div4TotalGTime","Div4LongestGTime","Div4WheelsOff","Div4TailNum","Div5Airport","Div5AirportID","Div5AirportSeqID","Div5WheelsOn","Div5TotalGTime","Div5LongestGTime","Div5WheelsOff","Div5TailNum",
  2016,1,1,6,3,2016-01-06,"AA",19805,"AA","N4YBAA","43",11298,1129804,30194,"DFW","Dallas/Fort Worth, TX","TX","48","Texas",74,11433,1143302,31295,"DTW","Detroit, MI","MI","26","Michigan",43,"1100","1057",-3.00,0.00,0.00,-1,"1100-1159",15.00,"1112","1424",8.00,"1438","1432",-6.00,0.00,0.00,-1,"1400-1459",0.00,"",0.00,158.00,155.00,132.00,1.00,986.00,4,,,,,,"",,,0,,,,,"",,,"",,,"","","",,,"",,,"","","",,,"",,,"","","",,,"",,,"","","",,,"",,,"","",
  2016,1,1,7,4,2016-01-07,"AA",19805,"AA","N434AA","43",11298,1129804,30194,"DFW","Dallas/Fort Worth, TX","TX","48","Texas",74,11433,1143302,31295,"DTW","Detroit, MI","MI","26","Michigan",43,"1100","1056",-4.00,0.00,0.00,-1,"1100-1159",14.00,"1110","1416",10.00,"1438","1426",-12.00,0.00,0.00,-1,"1400-1459",0.00,"",0.00,158.00,150.00,126.00,1.00,986.00,4,,,,,,"",,,0,,,,,"",,,"",,,"","","",,,"",,,"","","",,,"",,,"","","",,,"",,,"","","",,,"",,,"","",
  2016,1,1,8,5,2016-01-08,"AA",19805,"AA","N541AA","43",11298,1129804,30194,"DFW","Dallas/Fort Worth, TX","TX","48","Texas",74,11433,1143302,31295,"DTW","Detroit, MI","MI","26","Michigan",43,"1100","1055",-5.00,0.00,0.00,-1,"1100-1159",21.00,"1116","1431",14.00,"1438","1445",7.00,7.00,0.00,0,"1400-1459",0.00,"",0.00,158.00,170.00,135.00,1.00,986.00,4,,,,,,"",,,0,,,,,"",,,"",,,"","","",,,"",,,"","","",,,"",,,"","","",,,"",,,"","","",,,"",,,"","",
  2016,1,1,9,6,2016-01-09,"AA",19805,"AA","N489AA","43",11298,1129804,30194,"DFW","Dallas/Fort Worth, TX","TX","48","Texas",74,11433,1143302,31295,"DTW","Detroit, MI","MI","26","Michigan",43,"1100","1102",2.00,2.00,0.00,0,"1100-1159",13.00,"1115","1424",9.00,"1438","1433",-5.00,0.00,0.00,-1,"1400-1459",0.00,"",0.00,158.00,151.00,129.00,1.00,986.00,4,,,,,,"",,,0,,,,,"",,,"",,,"","","",,,"",,,"","","",,,"",,,"","","",,,"",,,"","","",,,"",,,"","",
  ```
  The first line represents header. This is typical example of structured data that we have to parse first.
 ___
 
#### 2. Parsing the CSV
  Next, create an DataSet based on the ```airline-delays.csv``` file.
  ```
  val flightsDS = spark.read
    .format("csv")
    .option("header", "true")
    .option("mode", "DROPMALFORMED")
    .load("file:///root/workshop-spark/data/task2/airline-delays.csv")
  ```
  You can check the schema by printing it:
  ```
  flightsDS.printSchema
  ```
___
 
#### 3. Querying with RDD
  Create the [RDD](http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.RDD) with [Row](https://spark.apache.org/docs/2.2.0/api/scala/index.html#org.apache.spark.sql.Row)s.
  ```
  val rdd = flightsDS.rdd
  ```
  
  Question 1: Suppose you're in Boston, MA. Which airline has the most flights departing from Boston? 
  ```
  val onlyBoston = rdd.filter(f => f.getAs("OriginCityName") == "Boston, MA")
  val airlinesFromBoston = onlyBoston.map(f => (f.getAs[String]("Carrier"), 1))
  val airlineWithMostFlights = airlinesFromBoston.reduceByKey(_ + _)
  val airlineWithMostFlight = airlineWithMostFlights.sortBy(_._2, false).take(1)
  ```
  
  Question 2: Overall, which airline has the worst average delay? How bad was that delay?
  ```
  val filteredDelays = rdd.filter(r => !r.isNullAt(r.fieldIndex("ArrDelay")) && r.getAs[String]("ArrDelay").toDouble > 0)
  val airlines = filteredDelays.map(r => (r.getAs[String]("Carrier"), r.getAs[String]("ArrDelay").toDouble))
  val worstAirlines = airlines.mapValues(v => (v, 1))
    .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))
    .mapValues(v => v._1/v._2)
  val worstAirline = worstAirlines.sortBy(_._2, false).take(1)
  ```
___
 
#### 4. Querying with DataSet
  We are going to work with [DataSet](https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.Dataset).

  Question 1: Suppose you're in New York, NY and are contemplating direct flights to San Francisco, CA. In terms of arrival delay, which airline has the best record on that route?
  ```
  val filteredFlights = flightsDS.filter((col("OriginCityName") === "New York, NY") && (col("DestCityName") === "San Francisco, CA") && (col("ArrDelay") > 0))
  val airlines = filteredFlights.map(f => (f.getAs[String]("Carrier"), f.getAs[String]("ArrDelay").toDouble))
  val countedDelay = airlines.groupByKey(_._1)
    .reduceGroups((a, b) => (a._1, a._2 + b._2))
    .map(_._2)
  val airlineWithSmallestDelay = countedDelay.orderBy("_2").take(1)
  ```
 ___
 
#### 5. Querying with ordinary SQL
  Question 1: Living in Chicago, IL, what are the farthest 10 destinations that you could fly to? (Note that our dataset contains only US domestic flights.)
  ```
  flightsDS.withColumn("Distance", $"Distance".cast(org.apache.spark.sql.types.DoubleType))
    .createTempView("flights")
  ```
  ```
  spark.sql("""SELECT DISTINCT OriginCityName, DestCityName, Distance
    FROM flights
    WHERE OriginCityName == 'Chicago, IL'
    ORDER BY Distance
    DESC LIMIT 1""").collect()
  ```
