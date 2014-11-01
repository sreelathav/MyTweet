package org.wit.mytweet.activities;

import java.util.ArrayList;
import org.wit.mytweet.activities.TweetFragmentActivity;
import org.wit.mytweet.activities.TweetPagerActivity;
import org.wit.mytweet.R;
import org.wit.mytweet.app.MyTweetApp;
import org.wit.mytweet.models.Tweetbox;
import org.wit.mytweet.models.Tweet;
import org.wit.mytweet.settings.SettingsActivity;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.MultiChoiceModeListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ActionMode;
import org.wit.android.helpers.IntentHelper;


public class TweetboxFragmentActivity extends ListFragment implements OnItemClickListener, MultiChoiceModeListener
{
  private ArrayList<Tweet> tweets;
  private Tweetbox tweetbox;
  private TweetAdapter adapter;
  private ListView listView;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    getActivity().setTitle(R.string.timeline);
    
    MyTweetApp app = (MyTweetApp) getActivity().getApplication();
    tweetbox = app.tweetbox;
    tweets = tweetbox.tweets;
    
    adapter = new TweetAdapter(getActivity(), tweets);
    setListAdapter(adapter);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
  {
    View v = super.onCreateView(inflater, parent, savedInstanceState);
    listView = (ListView) v.findViewById(android.R.id.list);
    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
    listView.setMultiChoiceModeListener(this);
    return v;
  }
  
  @Override
  public void onListItemClick(ListView l, View v, int position, long id)
  {
    Tweet tweet = ((TweetAdapter) getListAdapter()).getItem(position);
    Intent it = new Intent(getActivity(), TweetPagerActivity.class);
    it.putExtra(TweetFragmentActivity.EXTRA_TWEET_ID, tweet.id);
    startActivityForResult(it,0);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id)
  {
    Tweet tweet = adapter.getItem(position);
    IntentHelper.startActivityWithData(getActivity(), TweetPagerActivity.class, "TWEET_ID", tweet.id);
  }

      
  
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
  {
    super.onCreateOptionsMenu(menu,  inflater);
    inflater.inflate(R.menu.tweetbox, menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.menu_item_new_tweet: 
                                     Tweet tweet = new Tweet();
                                     tweetbox.addTweet(tweet);
        
                                     Intent it = new Intent( getActivity(), TweetPagerActivity.class);
                                     it.putExtra(TweetFragmentActivity.EXTRA_TWEET_ID, tweet.id);
                                     startActivityForResult(it, 0);
                                     return true; 
          
      case R.id.action_clear:
                                     adapter.clear();
                                     return true;
        
      case R.id.action_settings:
                                  Intent i = new Intent(getActivity(), SettingsActivity.class);
                                  startActivityForResult(i, 0);
                                  return true;
        
      default:        return super.onOptionsItemSelected(item);
    }
  }
  

  @Override
  public boolean onActionItemClicked(ActionMode mode, MenuItem item)
  {
    switch (item.getItemId())
    {
       case R.id.menu_item_delete_tweet:  deleteTweet(mode);
                                          return true;
       default:              return false;
    }
  }
    
    private void deleteTweet(ActionMode mode)
    {
      for (int i = adapter.getCount() - 1; i >= 0; i--)
      {
        if (listView.isItemChecked(i))
        {
          tweetbox.deleteTweet(adapter.getItem(i));
        }
      }
      mode.finish();
      adapter.notifyDataSetChanged();
    }
    @Override
    public void onResume()
    {
      try
      {
        for(Tweet tweet: tweets)
        {
           if(tweet.tweetText == null || tweet.tweetText.equals(""))
           {
              tweetbox.deleteTweet(tweet);
              tweetbox.saveTweets();
              adapter.notifyDataSetChanged();
           }
        }
        super.onResume();
        adapter.notifyDataSetChanged();
      }
      catch (Exception e)
      {
        super.onResume();
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.app_name);

        MyTweetApp app = (MyTweetApp) getActivity().getApplication();
        tweetbox = app.tweetbox;
        tweets = tweetbox.tweets;

        adapter = new TweetAdapter(getActivity(), tweets);
        setListAdapter(adapter);
      }
    }


  @Override
  public boolean onCreateActionMode(ActionMode mode, Menu menu)
  {
    MenuInflater inflater = mode.getMenuInflater();
    inflater.inflate(R.menu.tweet_box_item_context, menu);
    return true;
  }

  @Override
  public void onDestroyActionMode(ActionMode mode)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean onPrepareActionMode(ActionMode mode, Menu menu)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
  {
    // TODO Auto-generated method stub
    
  }
  
}

/** TWEET ADAPTER CLASS*/

class TweetAdapter extends ArrayAdapter<Tweet>
{
     private Context context;

     public TweetAdapter(Context context, ArrayList<Tweet> tweets)
     {
         super(context, 0, tweets);
         this.context = context;
     }
     
     @SuppressLint("InflateParams")
     @Override
     public View getView(int position, View convertView, ViewGroup parent)
     {
       LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       if (convertView == null)
       {
         convertView = inflater.inflate(R.layout.tweet_box_item, null);
       }
       Tweet tweet = getItem(position);

       TextView text = (TextView) convertView.findViewById(R.id.tweet_box_item_text);
       text.setText(tweet.tweetText);

       TextView dateTextView = (TextView) convertView.findViewById(R.id.tweet_box_item_dateTextView);
       dateTextView.setText(tweet.getDate());

       return convertView;
     }
   
     public void removeTweet(int position)
     {
         this.removeTweet(position);
     }
}
