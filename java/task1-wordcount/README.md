## Task 1: Word Count

You will get familiar with Spark and run your first Spark job.
> **NOTE** Solution is already in WordCount.java file, please don't look at it :-). Try to come up with your solution first. We can check it later.
___

#### 1. Check data
  You will process texts of books that are freely avaliable at [Gutenberg project](http://www.gutenberg.org/). Take a look at the text files that are in the resources directory of ```task1-wordcount```
  ```
  head -n 50 /root/workshop-spark/data/task1/*.txt | less
  ```
  This shows the first 50 lines of each file.
___

#### 2. How to load data
   You can use the [SparkContext.textFile](https://spark.apache.org/docs/2.2.0/api/scala/index.html#org.apache.spark.SparkContext) method to load all data. The textFile method can work with a directory path or a wildcard filter such as /*.txt.
  Your first task is to print out the number of lines in all the text files, combined. The solution should be quite short so you could write in only in the spark-shell.
  ```
  spark.read.textFile("file:///root/workshop-spark/data/task1/*.txt").count()
  ```
___

#### 3. Implementing the Word Count
  Your task is to implement the actual word-count program. Print the top 10 most frequent words in the provided books. Create solution to the spark-shell. When it will work you can try to copy-paste it to ```java/task1-worldcount/src/main/java/org/workshop/WordCount.java``` and then build the jar by the command
  ```
  mvn package
  ```
  Maven wil create the jar in ```target/word-count-1.0.jar```. When you have your jar you then you can [submit](https://spark.apache.org/docs/latest/submitting-applications.html#launching-applications-with-spark-submit) the job to spark cluster.
  ```
  spark-submit \
  --class org.workshop.WordCount \
  --master spark://spark:7077 \
  --executor-memory 1G \
  --total-executor-cores 2 \
  target/word-count-1.0.jar \
  "/root/workshop-spark/data/task1/*.txt"
  ```
___

#### 4. Discussion
You could use ```reduceByKey``` but also the method ```countByValue```. Read [its documentation](https://spark.apache.org/docs/2.0.1/api/scala/index.html#org.apache.spark.rdd.RDD), and try to understand how it works. Which method is better to use and why?


#### 5. Examples from spark shell
```
  scala> val x = spark.read.textFile("file:///root/workshop-spark/data/task1/*.txt")
    x: org.apache.spark.sql.Dataset[String] = [value: string]
    
    scala> x
    res1: org.apache.spark.sql.Dataset[String] = [value: string]
    
    scala> x.count()
    res2: Long = 157417
    
    scala> x.flatMap(line => line.
    *                     concat             grouped               minBy                 segmentLength   toFloat         
    +                     contains           hasDefiniteSize       mkString              self            toIndexedSeq    
    ++                    containsSlice      hashCode              nonEmpty              seq             toInt           
    ++:                   contentEquals      head                  offsetByCodePoints    size            toIterable      
    +:                    copyToArray        headOption            orElse                slice           toIterator      
    /:                    copyToBuffer       indexOf               padTo                 sliding         toList          
    :+                    corresponds        indexOfSlice          par                   sortBy          toLong          
    :\                    count              indexWhere            partition             sortWith        toLowerCase     
    <                     diff               indices               patch                 sorted          toMap           
    <=                    distinct           init                  permutations          span            toSeq           
    >                     drop               inits                 prefixLength          split           toSet           
    >=                    dropRight          intern                product               splitAt         toShort         
    addString             dropWhile          intersect             r                     startsWith      toStream        
    aggregate             endsWith           isDefinedAt           reduce                stringPrefix    toString        
    andThen               equals             isEmpty               reduceLeft            stripLineEnd    toTraversable   
    apply                 equalsIgnoreCase   isTraversableAgain    reduceLeftOption      stripMargin     toUpperCase     
    applyOrElse           exists             iterator              reduceOption          stripPrefix     toVector        
    canEqual              filter             last                  reduceRight           stripSuffix     transpose       
    capitalize            filterNot          lastIndexOf           reduceRightOption     subSequence     trim            
    charAt                find               lastIndexOfSlice      regionMatches         substring       union           
    chars                 flatMap            lastIndexWhere        replace               sum             unzip           
    codePointAt           flatten            lastOption            replaceAll            tail            unzip3          
    codePointBefore       fold               length                replaceAllLiterally   tails           updated         
    codePointCount        foldLeft           lengthCompare         replaceFirst          take            view            
    codePoints            foldRight          lift                  repr                  takeRight       withFilter      
    collect               forall             lines                 reverse               takeWhile       zip             
    collectFirst          foreach            linesIterator         reverseIterator       to              zipAll          
    combinations          format             linesWithSeparators   reverseMap            toArray         zipWithIndex    
    companion             formatLocal        map                   runWith               toBoolean                       
    compare               genericBuilder     matches               sameElements          toBuffer                        
    compareTo             getBytes           max                   scan                  toByte                          
    compareToIgnoreCase   getChars           maxBy                 scanLeft              toCharArray                     
    compose               groupBy            min                   scanRight             toDouble                        
    
    scala> x.flatMap(line => line.split(" "))
    res3: org.apache.spark.sql.Dataset[String] = [value: string]
    
    scala> x.flatMap(line => line.split(" "))x.show(10)
    <console>:1: error: ';' expected but '.' found.
    x.flatMap(line => line.split(" "))x.show(10)
                                       ^
    
    scala> x.show(10)
    +--------------------+
    |               value|
    +--------------------+
    |The Project Guten...|
    |                    |
    |This eBook is for...|
    |no restrictions w...|
    |under the terms o...|
    |eBook or online a...|
    |                    |
    |                    |
    |Title: War and Peace|
    |                    |
    +--------------------+
    only showing top 10 rows
    
    
    scala> x.flatMap(line => line.split(" ")).show(10)
    +---------+
    |    value|
    +---------+
    |      The|
    |  Project|
    |Gutenberg|
    |    EBook|
    |       of|
    |      War|
    |      and|
    |   Peace,|
    |       by|
    |      Leo|
    +---------+
    only showing top 10 rows
    
    
    scala> x.map(line => line.split(" ")).show(10)
    +--------------------+
    |               value|
    +--------------------+
    |[The, Project, Gu...|
    |                  []|
    |[This, eBook, is,...|
    |[no, restrictions...|
    |[under, the, term...|
    |[eBook, or, onlin...|
    |                  []|
    |                  []|
    |[Title:, War, and...|
    |                  []|
    +--------------------+
    only showing top 10 rows
    
    
    scala> val words = x.flatMap(line => line.split(" ")).show(10)
    +---------+
    |    value|
    +---------+
    |      The|
    |  Project|
    |Gutenberg|
    |    EBook|
    |       of|
    |      War|
    |      and|
    |   Peace,|
    |       by|
    |      Leo|
    +---------+
    only showing top 10 rows
    
    words: Unit = ()
    
    scala> val words = x.flatMap(line => line.split(" "))
    words: org.apache.spark.sql.Dataset[String] = [value: string]
    
    scala> words.show(10)
    +---------+
    |    value|
    +---------+
    |      The|
    |  Project|
    |Gutenberg|
    |    EBook|
    |       of|
    |      War|
    |      and|
    |   Peace,|
    |       by|
    |      Leo|
    +---------+
    only showing top 10 rows
    
    
    scala> words.count()
    res8: Long = 1406242
    
    scala> words.map(word => (word, 1)).show(10)
    +---------+---+
    |       _1| _2|
    +---------+---+
    |      The|  1|
    |  Project|  1|
    |Gutenberg|  1|
    |    EBook|  1|
    |       of|  1|
    |      War|  1|
    |      and|  1|
    |   Peace,|  1|
    |       by|  1|
    |      Leo|  1|
    +---------+---+
    only showing top 10 rows
    
    
    scala> val keyValue = words.map(word => (word, 1))
    keyValue: org.apache.spark.sql.Dataset[(String, Int)] = [_1: string, _2: int]
    
    scala> val mapStage = words.map(word => (word, 1))
    mapStage: org.apache.spark.sql.Dataset[(String, Int)] = [_1: string, _2: int]
    
    scala> mapStage.reduce
                                                                                                        
    def reduce(func: org.apache.spark.api.java.function.ReduceFunction[(String, Int)]): (String, Int)   
    def reduce(func: ((String, Int), (String, Int)) => (String, Int)): (String, Int)                    
    
    scala> mapStage.reduce
                                                                                                        
    def reduce(func: org.apache.spark.api.java.function.ReduceFunction[(String, Int)]): (String, Int)   
    def reduce(func: ((String, Int), (String, Int)) => (String, Int)): (String, Int)                    
    
    scala> mapStage.reduceByKey
    <console>:30: error: value reduceByKey is not a member of org.apache.spark.sql.Dataset[(String, Int)]
           mapStage.reduceByKey
                    ^
    
    scala> mapStage.reduceByKey()
    <console>:30: error: value reduceByKey is not a member of org.apache.spark.sql.Dataset[(String, Int)]
           mapStage.reduceByKey()
                    ^
    
    scala> mapStage.reduceByKey((number1, number2) => number1 + number2)
    <console>:30: error: value reduceByKey is not a member of org.apache.spark.sql.Dataset[(String, Int)]
           mapStage.reduceByKey((number1, number2) => number1 + number2)
                    ^
    
    scala> val rddMapStage = mapStage.rdd
    rddMapStage: org.apache.spark.rdd.RDD[(String, Int)] = MapPartitionsRDD[50] at rdd at <console>:29
    
    scala> rddMapStage.reduce
    reduce   reduceByKey   reduceByKeyLocally
    
    scala> rddMapStage.reduceByKey
    reduceByKey   reduceByKeyLocally
    
    scala> rddMapStage.reduceByKey(Display all 660 possibilities? (y or n)
    
    scala> rddMapStage.reduceByKey
    reduceByKey   reduceByKeyLocally
    
    scala> rddMapStage.reduceByKey
    
    def reduceByKey(func: (Int, Int) => Int): org.apache.spark.rdd.RDD[(String, Int)]
    def reduceByKey(func: (Int, Int) => Int,numPartitions: Int): org.apache.spark.rdd.RDD[(String, Int)]
    def reduceByKey(partitioner: org.apache.spark.Partitioner,func: (Int, Int) => Int): org.apache.spark.rdd.RDD[(String, Int)]
    
    scala> rddMapStage.reduceByKey((number1, number2) => number1 + number2)
    res13: org.apache.spark.rdd.RDD[(String, Int)] = ShuffledRDD[51] at reduceByKey at <console>:32
    
    scala> val reduced = rddMapStage.reduceByKey((number1, number2) => number1 + number2)
    reduced: org.apache.spark.rdd.RDD[(String, Int)] = ShuffledRDD[52] at reduceByKey at <console>:31
    
    scala> reduced
       val reduced: org.apache.spark.rdd.RDD[(String, Int)]
    
    scala> reduced.show(10)
    <console>:34: error: value show is not a member of org.apache.spark.rdd.RDD[(String, Int)]
           reduced.show(10)
                   ^
    
    scala> reduced
    res15: org.apache.spark.rdd.RDD[(String, Int)] = ShuffledRDD[52] at reduceByKey at <console>:31
    
    scala> reduced.to
    toDF   toDS   toDebugString   toJavaRDD   toLocalIterator   toString   top
    
    scala> reduced.to
    toDF   toDS   toDebugString   toJavaRDD   toLocalIterator   toString   top
    
    scala> reduced.toDS().show(10)
    +------------------+---+                                                        
    |                _1| _2|
    +------------------+---+
    |               Ah!| 10|
    |           reunion|  2|
    |              bone|  3|
    |          galleys!|  1|
    |           blandly|  5|
    |imprisoned?'--'No,|  1|
    |           wobbers|  1|
    |           lords,"|  1|
    |        me?'--'All|  1|
    |            Epsom,|  1|
    +------------------+---+
    only showing top 10 rows
    
    
    scala> reduced.toDS.show(10)
    +------------------+---+
    |                _1| _2|
    +------------------+---+
    |               Ah!| 10|
    |           reunion|  2|
    |              bone|  3|
    |          galleys!|  1|
    |           blandly|  5|
    |imprisoned?'--'No,|  1|
    |           wobbers|  1|
    |           lords,"|  1|
    |        me?'--'All|  1|
    |            Epsom,|  1|
    +------------------+---+
    only showing top 10 rows
    
    
    scala> reducedDS = reduced.toDS
    <console>:35: error: not found: value reducedDS
    val $ires6 = reducedDS
                 ^
    <console>:33: error: not found: value reducedDS
           reducedDS = reduced.toDS
           ^
    
    scala> reducedDS = reduced.toDS()
    <console>:35: error: not found: value reducedDS
    val $ires7 = reducedDS
                 ^
    <console>:33: error: not found: value reducedDS
           reducedDS = reduced.toDS()
           ^
    
    scala> val reducedDS = reduced.toDS
    reducedDS: org.apache.spark.sql.Dataset[(String, Int)] = [_1: string, _2: int]
    
    scala> reduced.s
    sample                saveAsHadoopFile            saveAsSequenceFile   sortByKey       synchronized   
    sampleByKey           saveAsNewAPIHadoopDataset   saveAsTextFile       sparkContext                   
    sampleByKeyExact      saveAsNewAPIHadoopFile      setName              subtract                       
    saveAsHadoopDataset   saveAsObjectFile            sortBy               subtractByKey                  
    
    scala> reduced.s
    sample                saveAsHadoopFile            saveAsSequenceFile   sortByKey       synchronized   
    sampleByKey           saveAsNewAPIHadoopDataset   saveAsTextFile       sparkContext                   
    sampleByKeyExact      saveAsNewAPIHadoopFile      setName              subtract                       
    saveAsHadoopDataset   saveAsObjectFile            sortBy               subtractByKey                  
    
    scala> reduced.sortBy
    sortBy   sortByKey
    
    scala> reduced.sortBy
    
    def sortBy[K](f: ((String, Int)) => K,ascending: Boolean,numPartitions: Int)(implicit ord: Ordering[K],implicit ctag: scala.reflect.ClassTag[K]): org.apache.spark.rdd.RDD[(String, Int)]
    
    scala> reduced.sortBy
    
    def sortBy[K](f: ((String, Int)) => K,ascending: Boolean,numPartitions: Int)(implicit ord: Ordering[K],implicit ctag: scala.reflect.ClassTag[K]): org.apache.spark.rdd.RDD[(String, Int)]
    
    scala> reducedDS.sort
    sort   sortWithinPartitions
    
    scala> reducedDS.sort
                                                                                                     
    def sort(sortExprs: org.apache.spark.sql.Column*): org.apache.spark.sql.Dataset[(String, Int)]   
    def sort(sortCol: String,sortCols: String*): org.apache.spark.sql.Dataset[(String, Int)]         
    
    scala> reduced.sortBy
    sortBy   sortByKey
    
    scala> reduced.sortBy
    
    def sortBy[K](f: ((String, Int)) => K,ascending: Boolean,numPartitions: Int)(implicit ord: Ordering[K],implicit ctag: scala.reflect.ClassTag[K]): org.apache.spark.rdd.RDD[(String, Int)]
    
    scala> val sorted = reduced.sortBy(tuple => tuple._2, false)
    sorted: org.apache.spark.rdd.RDD[(String, Int)] = MapPartitionsRDD[63] at sortBy at <console>:33
    
    scala> sorted.take(10)
    res18: Array[(String, Int)] = Array((the,73259), (and,41588), (to,38685), ("",38189), (of,37283), (a,25560), (in,19293), (I,17002), (was,16254), (his,16241))
   
   // this will create several CSV files (not merging them to save resources)
   scala> sorted.toDS.write.csv("/root/workshop-spark/data/task1/output")
    
```

