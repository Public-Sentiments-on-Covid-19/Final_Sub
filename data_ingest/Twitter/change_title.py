import csv

writer=csv.writer(open("tweet_sentiment1.csv", 'w', encoding='utf-8', newline=''))
writer.writerow(["Tweet_VS","tweet","Date Published"])