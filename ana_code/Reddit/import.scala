import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StringType, IntegerType, DoubleType}

val spark = org.apache.spark.sql.SparkSession.builder.master("local").appName("Spark CSV Reader").getOrCreate;

val df = (spark.read.format("csv")
        .option("header", "true")
        .option("multiline","true")
        .option("mode", "DROPMALFORMED")
        //loads ALL data
        // .load("reddit_Data_H/reddit_Data/*.csv")
        //loads smaller sample data (1-20, 1-21, 3-18)
        .load("reddit_Data_H/*.csv")
        )

var splitDF = (df.withColumn("Day",split(col("Publish Date")," ").getItem(0))
            .withColumn("Time",split(col("Publish Date")," ").getItem(1))
            .withColumn("Title_VS",col("Title_VS").cast(DoubleType))
            .withColumn("Comment_VS",col("Comment_VS").cast(DoubleType))
            .drop("Publish Date"))
// splitDF.show()

val titleDF = splitDF.select("Title_VS", "Title","Day","Time").dropDuplicates()
// titleDF.show(100)

// //Get how many (qualifying - 100 upvotes) posts are posted each day
// val postCountsDayDF = (titleDF.groupBy("Day")
//                     .count())
// postCountsDayDF.show()

//get the average qualifying post title sentiments each day
val averagePostVSDayDF = (titleDF.groupBy("Day")
                        .avg("Title_VS"))
averagePostVSDayDF.show()

//get the average qualifying post comments sentiments each day
val averageCommentVSDayDF = (splitDF.groupBy("Day")
                          .avg("Comment_VS"))
averageCommentVSDayDF.show()

//Get the difference in daily sentiments between post and comments (Postive number represents comment is more postive than title)
val averagePostCommentVSDayDF = averageCommentVSDayDF.join(averagePostVSDayDF,"Day")
val differencePostCommentDF = averagePostCommentVSDayDF.withColumn("Difference", averagePostCommentVSDayDF("avg(Title_VS)")-averagePostCommentVSDayDF("avg(Comment_VS)"))
differencePostCommentDF.show()
