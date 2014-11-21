package com.example.androidhttpsandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONInputStream;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	String line = "";
	HashMap aMap;
	Handler aHandler = new Handler();
	double temp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Executor anExecutor = Executors.newCachedThreadPool();
		
		anExecutor.execute(new Runnable() {
			@Override
			public void run() {
		try {
			  URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Boise,Idaho");
			  HttpURLConnection con = (HttpURLConnection) url
			    .openConnection();
			  readStream(con.getInputStream(), (TextView)findViewById(R.id.responseView));
			  } catch (Exception e) {
			  e.printStackTrace();
			}
			}
		}
		);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void readStream(InputStream in, final TextView responseView) {
		  JSONInputStream inFromClient = null;
		  try {
		    inFromClient = new JSONInputStream(in);
		    aMap = (HashMap) inFromClient.readObject();
		    HashMap map = (HashMap) aMap.get("main");
  		  	temp = (double) map.get("temp");
		    temp = (temp - 273.15) * 1.8 + 32;
		    aHandler.post(new Runnable() {
		    	  @Override
		    	  public void run() {
		    	  responseView.setText("City: " + aMap.get("name").toString() + "\n" +
		    	  					   "Temperature: " + (double)Math.round(temp));
		    	  }
		      });
		  } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
		  }
		
}
