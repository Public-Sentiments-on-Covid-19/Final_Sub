import tweepy

with open("pass.txt","r") as f:
    consumer_key=f.readline().replace('\n','')
    consumer_secret=f.readline().replace('\n','')
    access_token=f.readline().replace('\n','')
    access_token_secret=f.readline().replace('\n','')

auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)

api = tweepy.API(auth)

hydrated_tweets=api.statuses_lookup([1212360731695427584])
print(len(hydrated_tweets))
for tweet in hydrated_tweets:
    print(tweet.text)
