import csv
tsv_file = open("tweet_ids.txt","r")
read_tsv = csv.reader(tsv_file, delimiter="\t")
file1=open("tweet_idss.txt","a")
count=0
try:
    for row in read_tsv:
        if count%100000==0:
            print(count)
        count+=1
        file1.write(row[0]+'\n')
except:
    print(read_tsv.line_num)
