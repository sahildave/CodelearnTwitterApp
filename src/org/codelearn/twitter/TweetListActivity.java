package org.codelearn.twitter;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.codelearn.twitter.models.Tweet;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TweetListActivity extends ListActivity {

	private TweetAdapter tweetItemArrayAdapter;
	// private List<Tweet> tweets = new ArrayList<Tweet>();
	private List<Tweet> tweetsRead = new ArrayList<Tweet>();
	private static final String TWEETS_CACHE_FILE = "tweet_cache.ser";
	private static final long serialVersionUID = 1L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_list);

		try {
			FileInputStream fis = openFileInput(TWEETS_CACHE_FILE);
			ObjectInputStream ois = new ObjectInputStream(fis);
			tweetsRead = (List<Tweet>) ois.readObject();
			// TODO: Is this needed?
		} catch (Exception e) {

		}

		// renderTweets(tweetsRead);

		// initialize the adapter with the read tweets
		tweetItemArrayAdapter = new TweetAdapter(this, tweetsRead);
		setListAdapter(tweetItemArrayAdapter);
		refreshlist();

	}

	public void refreshlist() {
		AsyncFetchTweets asnyc = new AsyncFetchTweets(this);
		asnyc.execute();
	}

	// This adds new list to the top of the adapter.
	public void renderTweets(List<Tweet> tweets) {
		tweetsRead.addAll(0, tweets);
		tweetItemArrayAdapter.notifyDataSetChanged();

		AsyncWriteTweets test1 = new AsyncWriteTweets(this);
		test1.execute(tweetsRead);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			// sign in button pressed
			refreshlist();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, TweetDetailActivity.class);
		intent.putExtra("MyClass", (Tweet) getListAdapter().getItem(position));

		startActivity(intent);

	}
}
