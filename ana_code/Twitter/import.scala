import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StringType, IntegerType, DoubleType}

val spark = org.apache.spark.sql.SparkSession.builder.master("local").appName("Spark CSV Reader").getOrCreate;

val df = (spark.read.format("csv")
  .option("header", "true")
  .option("multiline","true")
  .option("mode", "DROPMALFORMED")
  .load("input/tweet_sentiment5.csv")
  )

var splitDF = (df.withColumn("Tweet_VS",col("Tweet_VS").cast(DoubleType))
  .withColumn("Date", to_date($"Date", "yyyy-MM-dd")).drop("go"))

//get the average qualifying post tweet sentiments each day
val averageTweetVSDayDF = (splitDF.groupBy("Date")
  .agg(
    avg("Tweet_VS") as ("avgTweetVS")
  ))
averageTweetVSDayDF.show()
