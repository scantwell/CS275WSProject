package com.dpm.uavflightplanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddWPActivity extends Activity {
	
	Button btnSave;
	Button btnCancel;
	String selectedFP;
	EditText editTextFPName;
	EditText editTextWPNum;
	EditText editTextWPName;
	EditText editTextLatitude;
	EditText editTextLongitude;
	EditText editTextAltitude;
	EditText editTextDelta;
	AsyncTask<String, Void, ArrayList<Map<String, String>>> asyncDownloadFP;
	AsyncTask<ArrayList<Map<String,String>>, Void, String> asyncUploadWP;
	ArrayList<Map<String, String>> fplan = null;
	String response = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_wp);
		
		Log.i("InfoTag", "AddWPActivity.onCreate, start");
		
		editTextFPName = (EditText)this.findViewById(R.id.editTextFPName);
		editTextWPNum = (EditText)this.findViewById(R.id.editTextWPNumber);
		editTextWPName = (EditText)this.findViewById(R.id.editTextWPName);
		editTextLatitude = (EditText)this.findViewById(R.id.editTextLatitude);
		editTextLongitude = (EditText)this.findViewById(R.id.editTextLongitude);
		editTextAltitude = (EditText)this.findViewById(R.id.editTextAltitude);
		editTextDelta = (EditText)this.findViewById(R.id.editTextDelta);
		
		btnSave = (Button) findViewById(R.id.buttonSubmit);
		btnCancel = (Button) findViewById(R.id.buttonCancel);
		btnSave.setOnClickListener(saveHandler);
		btnCancel.setOnClickListener(cancelHandler);
		
		//Get the likely WP number and FPName passed from the edit activity
		String bundle = getIntent().getExtras().getString("nextWPandFPName");
		Log.i("InfoTag", "AddWPActivity.onCreate, bundle is " + bundle);
			
		String[] parts = bundle.split("%");
		editTextWPNum.setText(parts[0]);
		if (parts.length == 2) {
			Log.i("InfoTag", "AddWPActivity.onCreate, parts = " + parts[0] + " " + parts[1]);
			editTextFPName.setText(parts[1]);
			selectedFP = parts[1];			//to pass back if cancel activity
		}
		Log.i("InfoTag", "AddWPActivity.onCreate, parts[0] is " + parts[0]);
		Log.i("InfoTag", "AddWPActivity.onCreate, selectedFP is " + selectedFP);
		//Get the flight plan 
		if(selectedFP != null){
			asyncDownloadFP = new DownloadFPTask(selectedFP).execute();
			try {
				fplan = asyncDownloadFP.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(fplan != null){
			Log.i("InfoTag", "AddWPActivity.onCreate, fplan.size() = " + fplan.size());
			for(int i = 0; i < fplan.size();i++){
				Log.i("InfoTag", "AddWPActivity.onCreate.fplaninfo" + fplan.get(i).entrySet().toString());
				//Log.i("InfoTag", "AddWPActivity.onCreate.fplan1info" + fplan.get(i).get("fpname").toString());
				//Log.i("InfoTag", "AddWPActivity.onCreate.fplan1info" + fplan.get(i).get("seq_number").toString());
				//Log.i("InfoTag", "AddWPActivity.onCreate.fplan1info" + fplan.get(i).get("name").toString());
				//Log.i("InfoTag", "AddWPActivity.onCreate.fplan1info" + fplan.get(i).get("latitude").toString());
				//Log.i("InfoTag", "AddWPActivity.onCreate.fplan1info" + fplan.get(i).get("longitude").toString());
				//Log.i("InfoTag", "AddWPActivity.onCreate.fplan1info" + fplan.get(i).get("altitude").toString());
				//Log.i("InfoTag", "AddWPActivity.onCreate.fplan1info" + fplan.get(i).get("deltat").toString());
			}
		}
	}//onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_w, menu);
		return true;
	}

	View.OnClickListener saveHandler = new View.OnClickListener() {
		@SuppressWarnings("unchecked")
		public void onClick(View v) {
			//Pass the new waypoint to the async task
			Log.i("InfoTag", "AddWPActivity.onClick");
			selectedFP = editTextFPName.getText().toString();
			Map<String,String> map = new HashMap<String,String>();
			map.put("fpname", selectedFP);
			map.put("seq_number", editTextWPNum.getText().toString());
			map.put("name", editTextWPName.getText().toString());
			map.put("latitude", editTextLatitude.getText().toString());
			map.put("longitude", editTextLongitude.getText().toString());
			map.put("altitude", editTextAltitude.getText().toString());
			map.put("deltat", editTextDelta.getText().toString());
			Log.i("InfoTag", "AddWPActivity.onClick, finished map putting");
			Log.i("InfoTag", "AddWPActivity.onClick, map.fpname = " + map.get("fpname").toString());
			Log.i("InfoTag", "AddWPActivity.onClick, map.seq_number = " + map.get("seq_number").toString());
			Log.i("InfoTag", "AddWPActivity.onClick, map.name = " + map.get("name").toString());
			Log.i("InfoTag", "AddWPActivity.onClick, map.latitude = " + map.get("latitude").toString());
			Log.i("InfoTag", "AddWPActivity.onClick, map.longitude = " + map.get("longitude").toString());
			Log.i("InfoTag", "AddWPActivity.onClick, map.altitude = " + map.get("altitude").toString());
			Log.i("InfoTag", "AddWPActivity.onClick, map.delta = " + map.get("deltat").toString());
			
			//Make a new list consisting of either the old fp + wp, or just wp
			ArrayList<Map<String, String>> fplan1 = new ArrayList<Map<String, String>>();
			if(fplan != null) {
				fplan1.addAll(fplan);
			}
			//Add the waypoint
			fplan1.add(map);
			
			Log.i("InfoTag", "AddWPActivity.onClick, fplan1.size() = " + fplan1.size());
			for(int i = 0; i < fplan1.size();i++){
				Log.i("InfoTag", "AddWPActivity.onClick.fplan1info" + fplan1.get(i).entrySet().toString());
				Log.i("InfoTag", "AddWPActivity.onClick.fplan1info" + fplan1.get(i).get("fpname").toString());
				Log.i("InfoTag", "AddWPActivity.onClick.fplan1info" + fplan1.get(i).get("seq_number").toString());
				Log.i("InfoTag", "AddWPActivity.onClick.fplan1info" + fplan1.get(i).get("name").toString());
				Log.i("InfoTag", "AddWPActivity.onClick.fplan1info" + fplan1.get(i).get("latitude").toString());
				Log.i("InfoTag", "AddWPActivity.onClick.fplan1info" + fplan1.get(i).get("longitude").toString());
				Log.i("InfoTag", "AddWPActivity.onClick.fplan1info" + fplan1.get(i).get("altitude").toString());
				Log.i("InfoTag", "AddWPActivity.onClick.fplan1info" + fplan1.get(i).get("deltat").toString());
			}
			
			//Call the upload async task to submit the new waypoint
			 try {
				 asyncUploadWP = new UploadFPTask(fplan1).execute();
				 Log.i("InfoTag", "Called asyncUploadWP");
				 response = asyncUploadWP.get(1000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 Log.i("InfoTag", "AddWPActivity.response = " + response);
			if(response != null) {	
				Intent myIntent = new Intent(AddWPActivity.this, OneFPActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				AddWPActivity.this.startActivity(myIntent);
			}
	   }
	};	
	
	View.OnClickListener cancelHandler = new View.OnClickListener() {
		public void onClick(View v) {
			//Just go back one activity
			Intent myIntent = new Intent(AddWPActivity.this, OneFPActivity.class);
			myIntent.putExtra("fp", selectedFP); //Optional parameters
			AddWPActivity.this.startActivity(myIntent);
	   }
	};		
	
}//class
