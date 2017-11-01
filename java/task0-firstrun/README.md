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
  cat /usr/local/spark/examples/src/main/java/org/apache/spark/examples/JavaSparkPi.java
  ```
  The source code of JavaSparkPi.java from the Spark examples
  ```
  package org.apache.spark.examples;

  import org.apache.spark.api.java.JavaRDD;
  import org.apache.spark.api.java.JavaSparkContext;
  import org.apache.spark.sql.SparkSession;
  import java.util.ArrayList;
  import java.util.List;

  /**
   * Computes an approximation to pi Usage: JavaSparkPi [partitions]
   */
  public final class JavaSparkPi {

    public static void main(String[] args) throws Exception {
      SparkSession spark = SparkSession
        .builder()
        .appName("JavaSparkPi")
        .getOrCreate();

      JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

      int slices = (args.length == 1) ? Integer.parseInt(args[0]) : 2;
      int n = 100000 * slices;
      List<Integer> l = new ArrayList<>(n);
      for (int i = 0; i < n; i++) {
        l.add(i);
      }

      JavaRDD<Integer> dataSet = jsc.parallelize(l, slices);

      int count = dataSet
        .map(integer -> {
          double x = Math.random() * 2 - 1;
          double y = Math.random() * 2 - 1;
          return (x * x + y * y <= 1) ? 1 : 0;
        }).reduce((integer, integer2) -> integer + integer2);

      System.out.println("Pi is roughly " + 4.0 * count / n);

      spark.stop();
    }
  }
  ```

Execute Spark job for calculating `Pi` Value
  ```
  spark-submit \
  --class org.apache.spark.examples.JavaSparkPi \
  --master spark://spark:7077 \
  /usr/local/spark/examples/jars/spark-examples_2.11-2.2.0.jar \
  100
  Pi is roughly 3.140495114049511
  ```
OR even simpler
  ```
  $SPARK_HOME/bin/run-example JavaSparkPi 100
  Pi is roughly 3.1413855141385514
  ```

Please note the first command above expects Spark Master and Slave to be running. We can even check the Spark Web UI after executing this command. You can try to write ```stop-master.sh``` and you will see that your command doesn't work.
