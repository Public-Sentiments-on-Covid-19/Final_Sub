import csv
tsv_file = open("full_dataset-clean.tsv")
read_tsv = csv.reader(tsv_file, delimiter="\t")
count=0
file1=open("data/tweet_idno.txt","a")
day="2020-01-01"
try:
    for row in read_tsv:
        if row[0]!="tweet_id":
            if row[1]!=day:
                print(row[1])
                day=row[1]
                count=0
            if count<10000:
                file1.write(row[0]+'\n')
            count+=1
except:
    print(read_tsv.line_num)
file1.close()
