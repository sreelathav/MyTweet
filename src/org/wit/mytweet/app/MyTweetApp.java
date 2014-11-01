package org.wit.mytweet.app;

import org.wit.mytweet.models.Tweetbox;
import org.wit.mytweet.models.TweetboxSerializer;

import android.app.Application;
import static org.wit.android.helpers.LogHelpers.info;

public class MyTweetApp extends Application
{
  public Tweetbox tweetbox;
  private static final String FILENAME = "tweettbox.json";

  @Override
  public void onCreate()
  {
    super.onCreate();
    TweetboxSerializer serializer = new TweetboxSerializer(this, FILENAME);
    tweetbox = new Tweetbox(serializer);

    info(this, "Tweeting app launched");
  }
}
