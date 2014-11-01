package org.wit.mytweet.models;

import java.util.ArrayList;
import java.util.UUID;
import android.util.Log;
import static org.wit.android.helpers.LogHelpers.info;

public class Tweetbox
{
  public ArrayList<Tweet> tweets;
  private TweetboxSerializer serializer;

  public Tweetbox(TweetboxSerializer serializer)
  {
    this.serializer = serializer;
    try
    {
      tweets = serializer.loadTweets();
    }
    catch (Exception e)
    {
      info(this, "Error loading tweets: " + e.getMessage());
      tweets = new ArrayList<Tweet>();
    }
  }

  public void addTweet(Tweet tweet)
  {
    tweets.add(tweet);
  }
  
  public Tweet getTweet(UUID id)
  {
    Log.i(this.getClass().getSimpleName(), "UUID parameter id: " + id);

    for (Tweet tweet : tweets)
    {
      if (id.equals(tweet.id))
      {
        return tweet;
      }
    }
    return null;
  }
  public boolean saveTweets()
  {
    try
    {
      serializer.saveTweets(tweets);
      info(this, "Tweets saved to file");
      return true;
    }
    catch (Exception e)
    {
      info(this, "Error saving tweets: " + e.getMessage());
      return false;
    }
  }
  
  public void deleteTweet(Tweet tweet)
  {
    tweets.remove(tweet);
  }
}
