# Workshop: An Intorduction to Apache Spark - 101    
![](http://spark.apache.org/docs/latest/img/spark-logo-hd.png)
  
  This workshop was initially created for the [DevFest 2017](https://2017.devfest.cz/) in Prague. When you will go thought all tasks and [intro presentation](slides.pdf) you should know of the basics architecture in the [Apache Spark](https://spark.apache.org/). You will know differences between [MapReduce](https://en.wikipedia.org/wiki/MapReduce) and Spark approaches, between batch and stream data processing. You will be able to start Spark job in the standalone cluster and work with basic Spark API.
___

## Set the environment
  As the first step you have to set our Spark environment to get everything work. [Here](environment.md) are prepared few instruction to do that. It includes docker installation and description how to run docker container with Apache Spark.
___

## Task 0: The First Run of Spark
  Get to know the Spark, Spark REPL and run our first job.
  * scala: [link](scala/task0-firstrun/README.md)
  * java: [link](java/task0-firstrun/README.md)
___

## Task 1: Word-count
  You will write our first Spark application. The word-count is the "hello world" in the distribution computation.
  * scala: [link](scala/task1-wordcount/README.md)
  * java: [link](java/task1-wordcount/README.md)
___

## Task 2: Analyzing Flight Delays
  You will analyze real data with help RDD and Dataset.
  * scala: [link](scala/task2-flights/README.md)
  * java: [link](java/task2-flights/README.md)
___

## Optional: Run all spark jobs in the cluster
  You can submit and run all app in cluster deploy mode on standalone cluster. 
  * scala: [link](scala/task3/README.md)
  * java: [link](java/task3/README.md)

Recommendation for further reading: [Spark: The Definitive Guide](http://shop.oreilly.com/product/0636920034957.do)
