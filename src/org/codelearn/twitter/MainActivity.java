package org.codelearn.twitter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * 
 * This lesson deals with basic HTTP POST and GET.
 * Used - HttpUrlConnection, Async, no Json.
 * When Login button is clicked the username is saved as @usernameString
 * A post request is generated and the usernameString is sent as the parameter
 * The user gets back "Hello <usernameString>" as the response, which is 
 * shown as a Toast. If any exception is caught, different Toast message appears
 * 
 */
public class MainActivity extends Activity {

	String codelearnUrl = ""; // TODO: Add this
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
				con.setDoOutput(true);
				con.setDoInput(true);

				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(
						con.getOutputStream());
				wr.writeBytes(usernameString); // Setting parameters as the
												// username
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

				return true; // True if no exception occured

			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
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

			} else {
				Toast.makeText(MainActivity.this,
						"Uh oh!, Something went wrong", Toast.LENGTH_LONG)
						.show();

			}
		}

	}

}
