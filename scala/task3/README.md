## Optional: Run all spark jobs in the cluster
  You can submit and run all app in cluster deploy mode on standalone cluster.
___

#### 1. Set environment
  Install docker-compose
  * https://docs.docker.com/compose/install/#install-compose
  
  Those who has installed 'docker for windows' or 'docker for mac' already have docker-compose. Check that:
  ```
  docker-compose -version
  ```
  
  Stop and remove all your docker containers
  > **WARNING** It will DELETE ALL your docker CONTAINERS. If you don’t want to remove them all, please remove all docker containers that we created during a workshop.
  ```
  docker stop $(docker ps -a -q)
  docker rm $(docker ps -a -q)
  ```
  
  Run a instances of one master and two workers
  ```
  docker-compose up -d
  ```
  Go into master container
  ```
  docker exec -it workshopspark_master_1 /bin/bash
  ```
___

#### 2. Submit the word-count job
  * '--deploy-mode cluster' the driver program is launched on one of the worker machines inside the cluster
  * '--supervise' restarts the driver on failure
  ```
  spark-submit \
  --class org.workshop.WordCount \
  --master spark://master:7077 \
  --deploy-mode cluster \
  --supervise \
  --executor-memory 1G \
  --total-executor-cores 4 \
  /root/workshop-spark/scala/task1-wordcount/target/scala-2.11/word-count_2.11-1.0.jar \
  "/root/workshop-spark/data/task1/*.txt"
  ```
___

#### 3. Submit the flights job
  ```
  spark-submit \
  --class org.workshop.Flights \
  --master spark://spark:7077 \
  --deploy-mode cluster \
  --supervise \
  --executor-memory 1G \
  --total-executor-cores 4 \
  /root/workshop-spark/scala/task1-wordcount/target/scala-2.11/flights_2.11-1.0.jar \
  "/root/workshop-spark/data/task2/airline-delays.csv" \
  "/root/workshop-spark/data/task2/output"
  ```
