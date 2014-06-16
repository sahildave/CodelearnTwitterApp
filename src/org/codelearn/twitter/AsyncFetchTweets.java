package org.codelearn.twitter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codelearn.twitter.models.Tweet;
import org.json.JSONArray;
import org.json.JSONTokener;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncFetchTweets extends AsyncTask<Void, Void, List<Tweet>> {
	private static final String TWEETS_CACHE_FILE = "tweet_cache.ser";

	private List<Tweet> tweets = new ArrayList<Tweet>();
	private TweetListActivity listActivity = null;
	private JSONArray result;
	private static final String tweetURL = "http://app-dev-challenge-endpoint.herokuapp.com/tweets";

	public AsyncFetchTweets(TweetListActivity act) {
		listActivity = act;
	}

	@Override
	protected List<Tweet> doInBackground(Void... params) {

		InputStream stream = null;
		try {

			SharedPreferences prefs = listActivity.getSharedPreferences(
					"codelearn_twitter", listActivity.MODE_PRIVATE);
			String token = prefs.getString("token", "-1");

			URL url = new URL(tweetURL);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);

			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			// Now sending the JSON object as parameters
			wr.writeBytes(token);
			wr.flush();
			wr.close();

			stream = con.getInputStream();
			int HttpResult = con.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						stream));
				StringBuilder content = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null) {
					content.append(line + "\n");
				}
				JSONTokener tokener = new JSONTokener(content.toString());
				result = new JSONArray(tokener);
				br.close();
				for (int i = 0; i < result.length(); i++) {
					Tweet tweet = new Tweet();

					tweet.setTitle(result.getJSONObject(i).getString("title"));
					tweet.setBody(result.getJSONObject(i).getString("body"));
					tweets.add(tweet);
				}
			}

		} catch (Exception e) {
			Log.d("Error in Async Task", "" + e.toString());
		}
		return tweets;

	}

	protected void onPostExecute(List<Tweet> tweets) {
		Log.d("Call From on Prefetch", "Check");
		listActivity.renderTweets(tweets);
	}

}
