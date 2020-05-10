# Data Ingesting:
 - Reddit: Run the Python3 code in the env provided. It will save the data to a CSV file given a data in input command prompt. The criteras are: The post must have 100 upvotes and we take the top 20 comments.
 - Twitter: clean full_dataset-clean.tsv with parser.py. Use hydrator on the result tweet_id.txt which will output tweet_id.jsonl and tweet_id.csv. Run getsentiment.py to get sentimental values and filter null tweets.
 - John Hopkins: Run the get_data.sh script to get the data from the John Hopkins repository
 
 # Data Analysis:
 - Reddit: First run curl.sh to move csv files from local env to Dumbo (Please be aware the location). Afterwards run import.scala in a spark shell. It will provide tables of some analysis.
 - Twitter: Push sentimental5.csv to HDFS. Run import.scala to get average values. Use DF.sort(asc("Date")) to sort by Date. Export to Tableau the results.
 - John Hopkins: Run confirmed_cases.sh to run all the data through map reduce code to calculate stats per country per day. The data will be found on HDFS under cleaning. Afterwards, load import.scala in a spark shell. It will provide tables for our data. 
