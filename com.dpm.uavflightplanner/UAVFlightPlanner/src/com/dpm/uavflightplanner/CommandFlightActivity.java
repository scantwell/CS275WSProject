package com.dpm.uavflightplanner;

import java.util.concurrent.ExecutionException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
import android.widget.TextView;

public class CommandFlightActivity extends Activity {
	
	//ListView weatherList;
	//String[] weatherArray;
	AsyncTask<Void, Void, String> asyncResponse;
	String obs = "none";
	TextView textViewWxMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_command_flight);
		Log.i("InfoTag", "Start onCreate");
		asyncResponse = new DownloadWxTask().execute();
		try {
			obs = asyncResponse.get();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		textViewWxMsg = (TextView)findViewById(R.id.textViewWeatherMessage);
		textViewWxMsg.setText(obs);
		
		Log.i("InfoTag", "From onCreate, obs = " + obs);
		Log.i("InfoTag", "Finishing onCreate");
		
		
	}//onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.command_flight, menu);
		return true;
	}

}
