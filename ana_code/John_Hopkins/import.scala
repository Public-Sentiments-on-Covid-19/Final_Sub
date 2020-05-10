import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions._
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.types.{StringType, IntegerType, DoubleType}

val spark = org.apache.spark.sql.SparkSession.builder.master("local").appName("Spark CSV Reader").getOrCreate;

val csv = (spark.read.option("inferSchema","true").csv("confirmed_cases.csv")
    .toDF("Date", "Country", "Confirmed_Cases", "Confirmed_Deaths")
    )

val df = (csv.withColumn("Date", to_date(col("date"), "MM-dd-yyyy").as("date"))
    .withColumn("Confirmed_Cases", col("Confirmed_Cases").cast(IntegerType))
    .withColumn("Confirmed_Deaths", col("Confirmed_Deaths").cast(IntegerType))
    )


// get the change in deaths and cases per day
val windowSpec = Window.partitionBy("Country").orderBy("Date")
val newdf = (df.withColumn("New_Cases", $"Confirmed_Cases" - when((lag("Confirmed_Cases", 1).over(windowSpec)).isNull, 0).otherwise(lag("Confirmed_Cases", 1).over(windowSpec)))
    .withColumn("New_Deaths", $"Confirmed_Deaths" - when((lag("Confirmed_Deaths", 1).over(windowSpec)).isNull, 0).otherwise(lag("Confirmed_Deaths", 1).over(windowSpec)))
    )

// save as a table
newdf.write.mode("overwrite").saveAsTable("dec415.covidStats")
