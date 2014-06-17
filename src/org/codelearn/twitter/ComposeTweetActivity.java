package org.codelearn.twitter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeTweetActivity extends Activity {

	EditText compose;
	Button btn_submit;
	SharedPreferences prefs;
	String token;
	String tweet;

	final static String sendTweetUrl = ""; // TODO: Add this

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);

		prefs = getSharedPreferences("codelearn_twitter", MODE_PRIVATE);
		token = prefs.getString("token", "-1");

		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				tweet = compose.getText().toString();
				if (tweet.trim().length() > 0) {
					btn_submit.setEnabled(false);
					new updateTwitterStatus().execute(tweet);

				} else {
					Toast.makeText(ComposeTweetActivity.this,
							"Please write something!", Toast.LENGTH_SHORT)
							.show();

				}

			}
		});

	}

	class updateTwitterStatus extends AsyncTask<String, Void, String> {
		String responseString;

		@Override
		protected String doInBackground(String... params) {
			String sendThisTweet = params[0];

			try {
				URL url = new URL(sendTweetUrl);

				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setDoOutput(true);
				con.setDoInput(true);

				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(
						con.getOutputStream());
				wr.writeBytes(sendThisTweet); // Setting parameters as the
												// tweet
				wr.flush();
				wr.close();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String line;
				StringBuffer sb = new StringBuffer();

				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}

				responseString = sb.toString();
				br.close();

				return responseString; // True if no exception occured

			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result != null) { // if no exception occured

				Toast.makeText(ComposeTweetActivity.this, result,
						Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(ComposeTweetActivity.this,
						"Uh oh!, Something went wrong", Toast.LENGTH_LONG)
						.show();

			}
		}
	}
}
