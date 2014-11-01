package org.wit.mytweet.activities;


import java.util.UUID;

import org.wit.mytweet.R;
import org.wit.mytweet.app.MyTweetApp;
import org.wit.mytweet.models.Tweet;
import org.wit.mytweet.models.Tweetbox;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import static org.wit.android.helpers.IntentHelper.navigateUp;
import static org.wit.android.helpers.ContactHelper.getContact;
import static org.wit.android.helpers.ContactHelper.getEmail;
import static org.wit.android.helpers.IntentHelper.sendEmail;


public class TweetFragmentActivity extends Fragment implements TextWatcher, OnCheckedChangeListener, OnClickListener
{
  public static final String EXTRA_TWEET_ID = "mytweet.TWEET_ID";

  private static final int REQUEST_CONTACT = 1;

  
  private EditText tweetText;
  private Tweet tweet;
  private TextView letterCounter;
  private int defaultTextColor;
  private TextView tweetDate;
  private Tweetbox tweetbox;
  private Button tweetButton;
  private Button recipientButton;
  private Button emailTweetButton;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    UUID tweetId = (UUID) getArguments().getSerializable(EXTRA_TWEET_ID);

    MyTweetApp app = (MyTweetApp) getActivity().getApplication();
    tweetbox = app.tweetbox;
    tweet = tweetbox.getTweet(tweetId);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
  {
    View v = inflater.inflate(R.layout.tweet_fragment_activity, parent, false);

    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    addListeners(v);
    updateControls(tweet);
    return v;
  }

  private void addListeners(View v)
  {
    tweetDate = (TextView) v.findViewById(R.id.tweetDate);
    tweetButton = (Button) v.findViewById(R.id.tweetButton);
    tweetText = (EditText) v.findViewById(R.id.tweetText);
   
    letterCounter = (TextView) v.findViewById(R.id.letterCounter);
    letterCounter.setText(R.string.letterCounter);
    defaultTextColor = letterCounter.getTextColors().getDefaultColor();
    recipientButton = (Button) v.findViewById(R.id.select_contact_button);
    emailTweetButton = (Button) v.findViewById(R.id.email_tweet_button);
     
    tweetDate.setText(tweet.getDate());
    tweetText.addTextChangedListener(this);
    tweetButton.setOnClickListener(this);
    recipientButton.setOnClickListener(this);
    emailTweetButton.setOnClickListener(this);   
  }

  public void updateControls(Tweet tweet)
  {
    tweetText.setText(tweet.tweetText);
    tweetDate.setText(tweet.getDate());
  }

  @Override
  public void afterTextChanged(Editable s)
  {
    int count = 140 - tweetText.length();
    letterCounter.setText(Integer.toString(count));
    letterCounter.setTextColor(Color.GREEN);
    if (count < 9)
      letterCounter.setTextColor(Color.RED);
    else
      letterCounter.setTextColor(defaultTextColor);

    if (count == 0)
    {
      Toast toast = Toast.makeText(getActivity(), "Text Length Exceeded", Toast.LENGTH_SHORT);
      toast.show();
    }
    tweet.tweetText = s.toString();
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
    case android.R.id.home:  navigateUp(getActivity());
                             return true;

    default:                 return super.onOptionsItemSelected(item);
    }
  }

  public void tweetButtonPressed(View view)
  {
    if (tweetText.getText().toString().equals(""))
    {
      Toast.makeText(getActivity(), "Tweet Empty", Toast.LENGTH_SHORT).show();
    }
    else
    {
      Toast.makeText(getActivity(), "Tweet Noted", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onClick(View view)
  {
    switch (view.getId())
    {
    case R.id.select_contact_button:
                           Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                           startActivityForResult(i, REQUEST_CONTACT);
                           if (tweet.recipientName != null)
                           {
                             recipientButton.setText("Recipient: " + tweet.recipientName);
                            }
                            break;

    case R.id.email_tweet_button: 
    	                   sendEmail(getActivity(), tweet.getRecipientEmail(getActivity()), getString(R.string.tweet_subject),
                            tweet.getTweetReport(getActivity()));
                            break;

    case R.id.tweetButton:
                            tweetButtonPressed(tweetButton);
                            navigateUp(getActivity());
                            break;
    }
  }

  public void onPause()
  {
    super.onPause();
    tweetbox.saveTweets();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if (resultCode != Activity.RESULT_OK)
    {
      return;
    }
    else
      if (requestCode == REQUEST_CONTACT)
      {
        String name = getContact(getActivity(), data);
        String email = getEmail(getActivity(), data);
        tweet.recipientName = name;
        tweet.recipientEmail = email;
        recipientButton.setText(email);
      }
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
  {
    // TODO Auto-generated method stub

  }
}