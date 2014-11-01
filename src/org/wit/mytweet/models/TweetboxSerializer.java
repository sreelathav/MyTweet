package org.wit.mytweet.models;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class TweetboxSerializer
{
  private Context mContext;
  private String mFilename;

  public TweetboxSerializer(Context c, String file)
  {
    mContext = c;
    mFilename = file;
  }

  public void saveTweets(ArrayList<Tweet> tweets) throws JSONException, IOException
  {
    JSONArray array = new JSONArray();
    for (Tweet t : tweets)
      array.put(t.toJSON());

    Writer writer = null;
    try
    {
      OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
      writer = new OutputStreamWriter(out);
      writer.write(array.toString());
    }
    finally
    {
      if (writer != null)
        writer.close();
    }
  }

  public ArrayList<Tweet> loadTweets() throws IOException, JSONException
  {
    ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    BufferedReader reader = null;
    try
    {
      InputStream in = mContext.openFileInput(mFilename);
      reader = new BufferedReader(new InputStreamReader(in));
      StringBuilder jsonString = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null)
      {
        jsonString.append(line);
      }
      JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
      for (int i = 0; i < array.length(); i++)
      {
        tweets.add(new Tweet(array.getJSONObject(i)));
      }
    }
    catch (FileNotFoundException e)
    {
    }
    finally
    {
      if (reader != null)
        reader.close();
    }
    return tweets;
  }
}
