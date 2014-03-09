package com.dpm.uavflightplanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EditFPActivity extends Activity {
	
	ListView listViewFP;
	String selectedFP;
	EditText editTextFPName;
	AsyncTask<String, Void, ArrayList<Map<String, String>>> asyncDownloadFP; 
	AsyncTask<ArrayList<Map<String, String>>, Void, String> asyncUploadFP;
	ArrayList<Map<String, String>> fplan;
	Button btnAddWP;
	Button btnSubmit;
	Button btnCancel;
	String response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_fp);
		Log.i("InfoTag", "Start EditFPActivity, onCreate");
				
		//Initialize controls
		editTextFPName = (EditText)this.findViewById(R.id.editTextFPName);
		listViewFP = (ListView) findViewById(R.id.listViewEditFP);
		btnAddWP = (Button) findViewById(R.id.buttonAddWP);
		btnCancel = (Button) findViewById(R.id.buttonCancel);
		btnSubmit = (Button) findViewById(R.id.buttonSubmit);
		
		// Assign onClick handlers
		btnAddWP.setOnClickListener(addWPHandler);
		btnSubmit.setOnClickListener(submitHandler);
		btnCancel.setOnClickListener(cancelHandler);
		
		//Get the selected fpname that is passed between activities
		String fpName = getIntent().getExtras().getString("fp");
		
		if( fpName != null ) {
			Log.i("InfoTag", "EditFPActivity.onCreate, fpName is not null");
			editTextFPName.setText(fpName.toString());
			selectedFP = editTextFPName.getText().toString();
		}
		
		//Get the flight plan 
		if(fplan == null){
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
		
		Log.i("InfoTag", "onCreate, fplan.size() = " + fplan.size());
		
		 // Assign adapter to ListView		 
		SimpleAdapter adapter = new SimpleAdapter(this, fplan,
				R.layout.layout_listview_row_edit_flightplan, 
				new String[] { "name", "seq_number", "latitude", "longitude", "altitude", "deltat" },
				new int[] { R.id.editTextWPName, R.id.editTextWPNumber, R.id.editTextLatitude, 
				R.id.editLongitude, R.id.editTextAltitude, R.id.editTextDeltaTime });
		
		listViewFP.setAdapter(adapter);
		
	}//onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_flight_plan, menu);
		return true;
	}
	
	View.OnClickListener submitHandler = new View.OnClickListener() {
	    @SuppressWarnings("unchecked")
		public void onClick(View v) {
	    	Log.i("InfoTag", "Start EditFPActivity.submitHandler");
	    	asyncUploadFP = new UploadFPTask(fplan).execute();					//WORK HERE fplan has to be 
	    	try {																//modified in form controls
	    		Log.i("InfoTag", "From onClick try");							//Try making new fplan1
	    		asyncUploadFP.get(1000, TimeUnit.MILLISECONDS);					//then make fplan null
	    																		//This should force a requery
			} catch (InterruptedException e) {									//to the backend
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
			//Now go back one activity
			Intent myIntent = new Intent(EditFPActivity.this, OneFPActivity.class);
			myIntent.putExtra("fp", selectedFP); //Optional parameters
			EditFPActivity.this.startActivity(myIntent);
	    	
	    }
	  };
	View.OnClickListener addWPHandler = new View.OnClickListener() {
			public void onClick(View v) {
				//Pass the next (likely) wp number plus the flight plan name
				int nextWPNumber = fplan.size() + 1;
				String wpNum = String.valueOf(nextWPNumber);
				String both = wpNum + "%" + selectedFP;
				Intent myIntent = new Intent(EditFPActivity.this, AddWPActivity.class);
				myIntent.putExtra("nextWPandFPName", both); //Optional parameters
				Log.i("InfoTag", "From EditFPActivity.onClick.addWPHandler, both = " + both.toString());
				EditFPActivity.this.startActivity(myIntent);
				Log.i("InfoTag", "From EditFPActivity.onClick.addWPHandler, post myIntent");
				
		   }
		};		  
	View.OnClickListener cancelHandler = new View.OnClickListener() {
			public void onClick(View v) {
				//Just go back one activity
				Intent myIntent = new Intent(EditFPActivity.this, OneFPActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				EditFPActivity.this.startActivity(myIntent);
		   }
		};		
}//class
