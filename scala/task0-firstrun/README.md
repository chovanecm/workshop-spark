## Task 0: The First Run of Spark

You will see the main part of the Spark APi and run the first job. 
___

#### 1. Briefly look at the Spark directory
  Go to the main directory of Apache Spark
  ```
  cd /usr/local/spark/
  ```
  Inspect the files in the bin directory. You will soon use ```spark-shell``` to launch your first Spark job. Also note ```spark-submit```, which is used to submit standalone Spark programs to a cluster.

  Inspect the scripts in the sbin directory. These scripts help with setting up a stand-alone Spark cluster, deploying Spark to EC2 virtual machines, and a bunch of additional tasks.

  Finally, take a look at the examples directory. You can find a number of stand-alone demo programs here, covering a variety of Spark APIs.
___

#### 2. Spark REPL
  Play with the spark-shell
  ```
  spark-shell --master spark://spark:7077
  ```
  * Spark context is available as 'sc'
  * Spark session is available as 'spark'
___

#### 3. Run your first job
  ```
  cat /usr/local/spark/examples/src/main/scala/org/apache/spark/examples/SparkPi.scala
  ```
  The source code of SparkPi.scala from the Spark examples
  ```
  package org.apache.spark.examples

  import scala.math.random
  import org.apache.spark.sql.SparkSession

  /** Computes an approximation to pi */
  object SparkPi {
    def main(args: Array[String]) {
      val spark = SparkSession
        .builder
        .appName("Spark Pi")
        .getOrCreate()
        
      val slices = if (args.length > 0) args(0).toInt else 2
      val n = math.min(100000L * slices, Int.MaxValue).toInt // avoid overflow
      
      val count = spark.sparkContext
        .parallelize(1 until n, slices)
        .map { i =>
          val x = random * 2 - 1
          val y = random * 2 - 1
          if (x*x + y*y <= 1) 1 else 0
        }.reduce(_ + _)
        
      println("Pi is roughly " + 4.0 * count / (n - 1))
      spark.stop()
    }
  }
  ```

Execute Spark job for calculating `Pi` Value
  ```
  spark-submit \
  --class org.apache.spark.examples.SparkPi \
  --master spark://spark:7077 \
  /usr/local/spark/examples/jars/spark-examples_2.11-2.2.0.jar \
  100
  Pi is roughly 3.140495114049511
  ```
OR even simpler
  ```
  $SPARK_HOME/bin/run-example SparkPi 100
  Pi is roughly 3.1413855141385514
  ```

Please note the first command above expects Spark Master and Slave to be running. We can even check the Spark Web UI after executing this command. You can write ```stop-master.sh``` and you will see that the first command doesn't work.

