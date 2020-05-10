# Data Ingesting:
 - Reddit: Run the Python3 code in the env provided. It will save the data to a CSV file given a data in input command prompt. The criteras are: The post must have 100 upvotes and we take the top 20 comments.
 - Twitter:
 - John Hopkins: Run the get_data.sh script to get the data from the John Hopkins repository
 
 # Data Analysis:
 - Reddit: First run curl.sh to move csv files from local env to Dumbo (Please be aware the location). Afterwards run import.scala in a spark shell. It will provide tables of some analysis.
 - Twitter:
 - John Hopkins: Run confirmed_cases.sh to run all the data through map reduce code to calculate stats per country per day. Afterwards, load import.scala in a spark shell. It will provide tables for our data.
