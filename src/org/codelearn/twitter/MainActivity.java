package org.codelearn.twitter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * Get Token from server by sending the username password
 * Save it in sharedprefs.
 * This token would be used by AsyncFetchTweets
 */
public class MainActivity extends Activity {

	String codelearnUrl = ""; // TODO: New Url which should give a token
	Button _loginBtn;
	String passwordString;
	String usernameString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_loginBtn = (Button) findViewById(R.id.btn_login);
		SharedPreferences prefs = getSharedPreferences("codelearn_twitter",
				MODE_PRIVATE);

		String s = prefs.getString("user", null);
		String s1 = prefs.getString("pass", null);
		if (s != null && s1 != null) { // TODO: Check this
			Intent intent = new Intent(MainActivity.this,
					TweetListActivity.class);
			startActivity(intent);
		}

		_loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				EditText username = (EditText) findViewById(R.id.fld_username);
				EditText password = (EditText) findViewById(R.id.fld_pwd);

				Log.d("User", username.getText().toString());
				Log.d("Pass", password.getText().toString());

				usernameString = username.getText().toString();
				passwordString = username.getText().toString();

				MyAsyncTask async = new MyAsyncTask();
				async.execute();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class MyAsyncTask extends AsyncTask<Void, Void, Boolean> {

		String responseString;

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				URL url = new URL(codelearnUrl);

				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setRequestMethod("POST"); // TODO: Add in Lesson52 too

				// We are now sending and receiving JSON
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
				con.setDoInput(true);

				JSONObject cred = new JSONObject();
				cred.put("username", usernameString);
				cred.put("password", passwordString);

				DataOutputStream wr = new DataOutputStream(
						con.getOutputStream());

				// Now sending the JSON object as parameters
				wr.writeBytes(cred.toString());
				wr.flush();
				wr.close();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String line;
				StringBuffer sb = new StringBuffer();

				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}

				br.close();

				JSONObject json = new JSONObject(sb.toString());
				responseString = (String) json.get("token");
				// Get token but from JSON

				return true; // True if no exception occured

			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result) { // if no exception occured

				Toast.makeText(MainActivity.this, responseString,
						Toast.LENGTH_LONG).show();
				SharedPreferences aPrefs = getSharedPreferences(
						"codelearn_twitter", MODE_PRIVATE);
				Editor edit = aPrefs.edit();
				edit.putString("token", responseString);
				edit.commit();

			} else {
				Toast.makeText(MainActivity.this,
						"Uh oh!, Something went wrong", Toast.LENGTH_LONG)
						.show();

			}
		}

	}

}
