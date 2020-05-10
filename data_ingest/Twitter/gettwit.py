import twitter


with open("pass.txt","r") as f:
    consumer_key=f.readline().replace('\n','')
    consumer_secret=f.readline().replace('\n','')
    access_token=f.readline().replace('\n','')
    access_token_secret=f.readline().replace('\n','')

api = twitter.Api(consumer_key=consumer_key,
                  consumer_secret=consumer_secret,
                  access_token_key=access_token,
                  access_token_secret=access_token_secret)

print(consumer_key)
print(access_token_secret)



results2 = api.GetSearch(
    raw_query="q=twitter%20coronavirus",lang="en")




for x in results2:
    print(x.text)
date=read("date.txt")

while date<current_date:
    results = api.GetSearch(
        raw_query="q=twitter%20coronavirus",lang="en")
    with open(date+".csv","w") as f:
        f.writelines(results.text,date,results.sentiment)
