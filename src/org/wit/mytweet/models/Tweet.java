package org.wit.mytweet.models;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.wit.mytweet.R;

import android.content.Context;

public class Tweet
{

  public String tweetText;
  public Date date;
  public UUID id;
  public String recipientName;
  public String recipientEmail;

  private static final String JSON_ID = "id";
  private static final String JSON_DATE = "date";
  private static final String JSON_TWEETTEXT = "tweetText";
  private static final String JSON_RECIPIENT_NAME = "recipientName";
  private static final String JSON_RECIPIENT_EMAIL = "recipientEmail";

  public Tweet()
  {
    id = UUID.randomUUID();
    this.date = new Date();
    tweetText = "";
    recipientName = "yet to select";
    recipientEmail = "yet to find";
  }

  public Tweet(JSONObject json) throws JSONException
  {
    id = UUID.fromString(json.getString(JSON_ID));
    date = new Date(json.getLong(JSON_DATE));
    tweetText = json.getString(JSON_TWEETTEXT);
    recipientName = json.getString(JSON_RECIPIENT_NAME);
    recipientEmail = json.getString(JSON_RECIPIENT_EMAIL);
  }

  public JSONObject toJSON() throws JSONException
  {
    JSONObject json = new JSONObject();
    json.put(JSON_ID, id.toString());
    json.put(JSON_DATE, date.getTime());
    json.put(JSON_TWEETTEXT, tweetText);
    json.put(JSON_RECIPIENT_NAME, recipientName);
    json.put(JSON_RECIPIENT_EMAIL, recipientEmail);
    return json;
  }

  public String getDate()
  {
    return DateFormat.getDateTimeInstance().format(date);
  }

  public String getRecipientEmail(Context context)
  {
    String recEmail = recipientEmail;
    recEmail = context.getString(R.string.tweet_email_prospective_recipient, recipientEmail);
    String ret = recEmail;
    return ret;
  }

  public String getTweetReport(Context context)
  {
    String dateFormat = "EEE, MMM dd";
    String dateString = android.text.format.DateFormat.format(dateFormat, date).toString();
    String prospectiveRecipient = recipientName;
    if (recipientName == null)
    {
      prospectiveRecipient = context.getString(R.string.tweet_report_no_contact);
    }
    else
    {
      prospectiveRecipient = context.getString(R.string.tweet_report_prospective_recipient, recipientName);
    }
    String report = "Tweet: " + tweetText + "\n Date: " + dateString + "\n " + prospectiveRecipient;
    return report;
  }
}
