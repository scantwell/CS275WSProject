package com.dpm.uavflightplanner;

import java.util.concurrent.ExecutionException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
import android.widget.TextView;

public class FlyActivity extends Activity {
	
	Button btnAll;
	Button btnOne;
	Button btnMap;
	Button btnFly;
	String selectedFP;
	
	AsyncTask<Void, Void, String> asyncResponse;
	String obs = "none";
	TextView textViewWxMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fly);
		//Log.i("InfoTag", "Start onCreate");
		
		String bundle = getIntent().getExtras().getString("fp");
		TextView textViewFPName = (TextView)this.findViewById(R.id.textViewFPNameDisplay);
		if( bundle != null ) {
			//Log.i("InfoTag", "onCreate, bundle is not null");
			textViewFPName.setText(bundle.toString());
			selectedFP = textViewFPName.getText().toString();
		}
		
		btnAll = (Button) findViewById(R.id.buttonAll);
		btnOne = (Button) findViewById(R.id.buttonOne);
		btnMap = (Button) findViewById(R.id.buttonMap);
		btnFly = (Button) findViewById(R.id.buttonFly);
		btnAll.setOnClickListener(allHandler);
		btnOne.setOnClickListener(oneHandler);
		btnMap.setOnClickListener(mapHandler);
		btnFly.setOnClickListener(flyHandler);
		
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
		
		//Log.i("InfoTag", "From onCreate, obs = " + obs);
		//Log.i("InfoTag", "Finishing onCreate");
		
	}//onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.command_flight, menu);
		return true;
	}

	//group of four handlers for navigation between activities
	View.OnClickListener allHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(FlyActivity.this, AllFPsActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				FlyActivity.this.startActivity(myIntent);
		   }
		};
	View.OnClickListener oneHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(FlyActivity.this, OneFPActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				FlyActivity.this.startActivity(myIntent);
		   }
		};	
	View.OnClickListener flyHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(FlyActivity.this, FlyActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				FlyActivity.this.startActivity(myIntent);
		   }
		};
	View.OnClickListener mapHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(FlyActivity.this, MapActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				FlyActivity.this.startActivity(myIntent);
		   }
		};	
	
}//class
