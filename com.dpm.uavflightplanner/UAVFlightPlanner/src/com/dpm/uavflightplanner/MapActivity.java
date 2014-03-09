package com.dpm.uavflightplanner;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class MapActivity extends Activity {
	
	Button btnAll;
	Button btnOne;
	Button btnMap;
	Button btnFly;
	String selectedFP;
	AsyncTask<String, Void, ArrayList<Map<String, String>>> asyncFP;
	ArrayList<Map<String, String>> fplan = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		btnAll = (Button) findViewById(R.id.buttonAll);
		btnOne = (Button) findViewById(R.id.buttonOne);
		btnMap = (Button) findViewById(R.id.buttonMap);
		btnFly = (Button) findViewById(R.id.buttonFly);
		btnAll.setOnClickListener(allHandler);
		btnOne.setOnClickListener(oneHandler);
		btnMap.setOnClickListener(mapHandler);
		btnFly.setOnClickListener(flyHandler);
		
		String bundle = getIntent().getExtras().getString("fp");
		TextView textViewFPName = (TextView)this.findViewById(R.id.textViewFPName);
		if( bundle != null ) {
			Log.i("InfoTag", "onCreate, bundle is not null");
			textViewFPName.setText(bundle.toString());
			//variable for passing to next intent
			selectedFP = textViewFPName.getText().toString();
				
			//Get the flight plan
			asyncFP = new DownloadFPTask(selectedFP).execute();
			try {
				fplan = asyncFP.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//if  
		
		//For use in the map as required
		String wpName = null;
		String wpNumber = null;
		String latitude = null;
		String longitude = null;
		String altitude = null;
		String delta = null;
		
		for(int i = 0; i < fplan.size(); i++){
			//Log.i("InfoTag", "MapActivity.onCreate, fplan = " + fplan.get(i).entrySet().toString());
			wpName = fplan.get(i).get("name").toString();
			wpNumber = fplan.get(i).get("seq_number").toString();
			latitude = fplan.get(i).get("latitude").toString();
			longitude = fplan.get(i).get("longitude").toString();
			altitude = fplan.get(i).get("altitude").toString();
			delta = fplan.get(i).get("deltat").toString();
			Log.i("InfoTag", "MapActivity.onCreate, fplan.name = " + wpName);
			Log.i("InfoTag", "MapActivity.onCreate, fplan.seq_number = " + wpNumber);
			Log.i("InfoTag", "MapActivity.onCreate, fplan.latitude = " + latitude);
			Log.i("InfoTag", "MapActivity.onCreate, fplan.longitude = " + longitude);
			Log.i("InfoTag", "MapActivity.onCreate, fplan.altitude = " + altitude);
			Log.i("InfoTag", "MapActivity.onCreate, fplan.deltat = " + delta);
		}
		//String url = "https://www.google.com/maps/search/lat,lon/@39.9894269,-75.196572,11z/data=!3m1!4b1";
		//String url = "https://www.cs.drexel.edu/~sc3356/googMap/?title=StevesMap,MyMap&lat=40.000,40.5900&lng=-74.358,-75.234";
		//String url = "https://www.cs.drexel.edu/~sc3356/googMap/";
		String url = "http://www.google.com/maps/";
		
		WebView webView = (WebView)this.findViewById(R.id.webViewMap);
		webView.loadUrl(url);	
		
	}//onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flight_plan_map, menu);
		return true;
	}

	//group of four handlers for navigation between activities
	View.OnClickListener allHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MapActivity.this, AllFPsActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				MapActivity.this.startActivity(myIntent);
		   }
		};
	View.OnClickListener oneHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MapActivity.this, OneFPActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				MapActivity.this.startActivity(myIntent);
		   }
		};	
	View.OnClickListener flyHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MapActivity.this, FlyActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				MapActivity.this.startActivity(myIntent);
		   }
		};
	View.OnClickListener mapHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MapActivity.this, MapActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				MapActivity.this.startActivity(myIntent);
		   }
		};	
		
}//class
