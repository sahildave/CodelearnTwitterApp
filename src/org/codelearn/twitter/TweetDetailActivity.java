package org.codelearn.twitter;

import org.codelearn.twitter.models.Tweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TweetDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_detail);
		Tweet value = (Tweet) getIntent().getSerializableExtra("MyClass");
		TextView u1 = (TextView) findViewById(R.id.tweetTitle);

		TextView u2 = (TextView) findViewById(R.id.tweetBody);
		u1.setText(value.getTitle().toString());
		u2.setText(value.getBody().toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_compose) {
			Intent composeIntent = new Intent(this, ComposeTweetActivity.class);
			startActivity(composeIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

}
