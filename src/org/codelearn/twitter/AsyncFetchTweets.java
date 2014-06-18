package org.codelearn.twitter;

import java.io.BufferedReader;
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
	private static final String tweetURL = "http://for-sahil.herokuapp.com/tweets";

	public AsyncFetchTweets(TweetListActivity act) {
		listActivity = act;
	}

	@Override
	protected List<Tweet> doInBackground(Void... params) {

		try {

			SharedPreferences prefs = listActivity.getSharedPreferences(
					"codelearn_twitter", listActivity.MODE_PRIVATE);
			String token = prefs.getString("token", "-1");

			URL url = new URL(tweetURL);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");

			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder content = new StringBuilder();
			String line;
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

		} catch (Exception e) {
			Log.d("Error in Async Task", "" + e.toString());
		}
		return tweets;

		/*
		 * DefaultHttpClient
		 */

		// try {
		// HttpClient client = new DefaultHttpClient();
		// HttpGet httpGet = new HttpGet(tweetURL);
		//
		// HttpResponse response = client.execute(httpGet);
		//
		// BufferedReader br = new BufferedReader(new InputStreamReader(
		// response.getEntity().getContent()));
		// StringBuilder content = new StringBuilder();
		// String line = null;
		//
		// while ((line = br.readLine()) != null) {
		// content.append(line + "\n");
		// }
		//
		// Log.d(TAG, content.toString());
		//
		// JSONTokener tokener = new JSONTokener(content.toString());
		// result = new JSONArray(tokener);
		//
		// Log.d(TAG, result.toString());
		//
		// br.close();
		// client.getConnectionManager().shutdown();
		//
		// for (int i = 0; i < result.length(); i++) {
		// Tweet tweet = new Tweet();
		// Log.d(TAG, result.getJSONObject(i).getString("title"));
		// tweet.setTitle(result.getJSONObject(i).getString("title"));
		// tweet.setBody(result.getJSONObject(i).getString("body"));
		// tweets.add(tweet);
		// }
		//
		// } catch (ClientProtocolException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// return tweets;
	}

	protected void onPostExecute(List<Tweet> tweets) {
		Log.d("Call From on Prefetch", "Check");
		listActivity.renderTweets(tweets);
	}

}
