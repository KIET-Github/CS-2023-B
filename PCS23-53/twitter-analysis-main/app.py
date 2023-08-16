from flask import Flask, render_template, request, json
import tweepy
from textblob import TextBlob
from wordcloud import WordCloud
import pandas as pd
import numpy as np
import re
import matplotlib.pyplot as plt
plt.style.use('fivethirtyeight')
lis = ['non hate tweet', 'hate tweet']

ACCESS_TOKEN = "1504889196539834368-pNnsxjfSebxUE2raPXcNLENvewmte0"
ACCESS_TOKEN_SECRET = "P1NIV9kpBFT2GVEHpT6n7r0A6mKmms1i8vah6LQPitNfu"
CONSUMER_KEY = "xazSVEcvwzMuqzPh1GAE2oydh"
CONSUMER_SECRET = "WR0jPEWtTZSaoeJknB18YqoBfn6GZ2UbIQsX5qR6BnEqqtm4Zz"


# Create auth obj
authenticate = tweepy.OAuthHandler(CONSUMER_KEY, CONSUMER_SECRET)
# Set the access token
authenticate.set_access_token(ACCESS_TOKEN, ACCESS_TOKEN_SECRET)

# Create the API
api = tweepy.API(authenticate, wait_on_rate_limit=True)


app = Flask(__name__)


def cleanTxt(text):
    text = re.sub(r'@[A-Za-z0-9]+', '', text)  # Removed  @mentions
    text = re.sub(r'#', '', text)
    text = re.sub(r'RT[\s]+', '', text)
    text = re.sub(r'https?:\/\/S+', '', text)
    text = re.sub(r'https:\/\/[A-Za-z0-9]+', '', text)

    return text


def getSubjectivity(text):
    return TextBlob(text).sentiment.subjectivity

# to get Polarity


def getPolarity(text):
    return TextBlob(text).sentiment.polarity


def getAnalysis(score):
    if score < 0:
        return 'Negative'
    elif score == 0:
        return 'Neutral'
    else:
        return 'Positive'


def run_simulation(sortedDF):
    count1 = 0
    count2 = 0
    count3 = 0
    j = 1

    for i in range(0, sortedDF.shape[0]):
        if (sortedDF['Analysis'][i] == 'Positive'):
            count1 = count1+1
      #print(str(j)+') '+sortedDF['Tweets'][i]+'\n')
            j += 1
        if (sortedDF['Analysis'][i] == 'Neutral'):
            count2 = count2+1
      #print(str(j)+') '+sortedDF['Tweets'][i]+'\n')
            j += 1
        if (sortedDF['Analysis'][i] == 'Negative'):
            count3 = count3+1
      #print(str(j)+') '+sortedDF['Tweets'][i]+'\n')
            j += 1
    return [count1, count2, count3]


@app.route('/')
def home():
    return render_template('index.html')


@app.route('/predict1', methods=['POST'])
def predict():
    user_handle = request.form['tweet']
    posts = api.user_timeline(screen_name=user_handle,
                              count=200, lang="en", tweet_mode="extended")

# print some tweets
    print("Showing some tweets\n")
    i = 1
    for tweet in posts:
        #print(str(i)+') '+tweet.full_text+'\n')
        i += 1

    df = pd.DataFrame([tweet.full_text for tweet in posts], columns=['Tweets'])

# Showing some of the data

    df['Tweets'] = df['Tweets'].apply(cleanTxt)

    df['Subjectivity'] = df['Tweets'].apply(getSubjectivity)
    df['Polarity'] = df['Tweets'].apply(getPolarity)

    df['Analysis'] = df['Polarity'].apply(getAnalysis)

    sortedDF = df.sort_values(by=['Polarity'])

    result = run_simulation(sortedDF)

    # result = json.dumps(res)

    print(result)
    return render_template('index.html', result1=result[0], result2=result[1], result3=result[2])


app.run(host='0.0.0.0', port=5001, debug=True)
