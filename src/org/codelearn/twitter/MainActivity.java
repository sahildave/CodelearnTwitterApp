package org.codelearn.twitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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

	String codelearnUrl = "http://app-dev-challenge-endpoint.herokuapp.com/loginstring"; // TODO:
																							// Add
																							// this
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

			/*
			 * HttpUrlConnection
			 * 
			 * 
			 * try { URL url = new URL(codelearnUrl);
			 * 
			 * HttpURLConnection con = (HttpURLConnection) url
			 * .openConnection(); con.setDoOutput(true); con.setDoInput(true);
			 * 
			 * con.setDoOutput(true); DataOutputStream wr = new
			 * DataOutputStream( con.getOutputStream());
			 * wr.writeBytes(usernameString); // Setting parameters as the //
			 * username wr.flush(); wr.close();
			 * 
			 * BufferedReader br = new BufferedReader(new InputStreamReader(
			 * con.getInputStream())); String line; StringBuffer sb = new
			 * StringBuffer();
			 * 
			 * while ((line = br.readLine()) != null) { sb.append(line + "\n");
			 * }
			 * 
			 * responseString = sb.toString(); br.close();
			 * 
			 * return true; // True if no exception occured
			 * 
			 * } catch (MalformedURLException e) { e.printStackTrace(); return
			 * false; } catch (IOException e) { e.printStackTrace(); return
			 * false; }
			 */

			/*
			 * DefaultHttpClient
			 */
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(codelearnUrl);

				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("username",
						usernameString));
				post.setEntity(new UrlEncodedFormEntity(urlParameters));

				HttpResponse response = client.execute(post);

				StringBuilder sb = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				String line = null;

				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				responseString = sb.toString();
				br.close();
				return true; // True if no exception occured

			} catch (ClientProtocolException e) {
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
