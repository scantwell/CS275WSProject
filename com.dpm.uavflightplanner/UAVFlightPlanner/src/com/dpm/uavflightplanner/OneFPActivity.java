package com.dpm.uavflightplanner;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class OneFPActivity extends Activity {
	
	Button btnAll;
	Button btnOne;
	Button btnMap;
	Button btnFly;
	Button btnAdd;
	Button btnDelete;
	String selectedFP = null;
	AsyncTask<String, Void, ArrayList<Map<String, String>>> asyncFP;
	ArrayList<Map<String, String>> fplan;
	ListView listViewFP; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.i("InfoTag", "Starting OneFPActivity.onCreate");
		setContentView(R.layout.activity_one_fp);
		
		btnAll = (Button) findViewById(R.id.buttonAll);
		btnOne = (Button) findViewById(R.id.buttonOne);
		btnMap = (Button) findViewById(R.id.buttonMap);
		btnFly = (Button) findViewById(R.id.buttonFly);
		btnAdd = (Button) findViewById(R.id.buttonAdd);
		btnDelete = (Button) findViewById(R.id.buttonDelete);
		
		btnAll.setOnClickListener(allHandler);
		btnOne.setOnClickListener(oneHandler);
		btnMap.setOnClickListener(mapHandler);
		btnFly.setOnClickListener(flyHandler);
		btnAdd.setOnClickListener(addHandler);
		btnDelete.setOnClickListener(deleteHandler);		
		listViewFP = (ListView) findViewById(R.id.listViewFlightPlan1);
		
		//Get the selected flight plan passed between activities
		String bundle = getIntent().getExtras().getString("fp");
		TextView textViewFPName = (TextView)this.findViewById(R.id.textViewFPNameDisplay);
		if( bundle != null ) {
			//Log.i("InfoTag", "onCreate, bundle is not null");
			textViewFPName.setText(bundle.toString());
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
		
			// Assign adapter to ListView		 
			SimpleAdapter adapter = new SimpleAdapter(this, fplan,
				R.layout.layout_listview_row_view_flightplan, 
				new String[] { "name", "seq_number", "latitude", "longitude", "altitude", "deltat" },
				new int[] { R.id.textViewWPNameDisplay, R.id.textViewWPNumberDisplay, R.id.textViewLatitudeDisplay, 
				R.id.textViewLongitudeDisplay, R.id.textViewAltitudeDisplay, R.id.textViewDeltaDisplay });
		
			listViewFP.setAdapter(adapter);
		}//if	
	}//onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_flight_plan, menu);
		return true;
	}

	//group of four handlers for navigation between activities
		View.OnClickListener allHandler = new View.OnClickListener() {
				public void onClick(View v) {
					Intent myIntent = new Intent(OneFPActivity.this, AllFPsActivity.class);
					myIntent.putExtra("fp", selectedFP); //Optional parameters
					OneFPActivity.this.startActivity(myIntent);
			   }
			};
		View.OnClickListener oneHandler = new View.OnClickListener() {
				public void onClick(View v) {
					Intent myIntent = new Intent(OneFPActivity.this, OneFPActivity.class);
					myIntent.putExtra("fp", selectedFP); //Optional parameters
					OneFPActivity.this.startActivity(myIntent);
			   }
			};	  

		View.OnClickListener mapHandler = new View.OnClickListener() {
				public void onClick(View v) {
					Intent myIntent = new Intent(OneFPActivity.this, MapActivity.class);
					myIntent.putExtra("fp", selectedFP); //Optional parameters
					OneFPActivity.this.startActivity(myIntent);
			   }
			};	
		View.OnClickListener flyHandler = new View.OnClickListener() {
				public void onClick(View v) {
					Intent myIntent = new Intent(OneFPActivity.this, FlyActivity.class);
					myIntent.putExtra("fp", selectedFP); //Optional parameters
					OneFPActivity.this.startActivity(myIntent);
			   }
			};
		View.OnClickListener addHandler = new View.OnClickListener() {
				public void onClick(View v) {
					int nextWPNumber = fplan.size() + 1;
					String wpNum = String.valueOf(nextWPNumber);
					String both = wpNum + "%" + selectedFP;
					Intent myIntent = new Intent(OneFPActivity.this, AddWPActivity.class);
					myIntent.putExtra("nextWPandFPName", both); //Optional parameters
					Log.i("InfoTag", "From OneFPActivity.onClick.addWPHandler, both = " + both.toString());
					OneFPActivity.this.startActivity(myIntent);
					Log.i("InfoTag", "From OneFPActivity.onClick.addWPHandler, post myIntent");
			   }
			};	
			
			
	View.OnClickListener deleteHandler = new View.OnClickListener() {
			    public void onClick(View v) {
			    	msgbox(R.string.activity_viewflightplan_button_delete_waypoint, R.string.activity_viewflightplan_delete_confirmation);
			    }
			};
			  
	//Adapted from http://stackoverflow.com/questions/7456509/android-messagebox	
	public void msgbox(int activityMainDeleteButton,int activityMainDeleteConfirmation)
		{
		    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
		    dlgAlert.setTitle(activityMainDeleteButton); 
		    dlgAlert.setMessage(activityMainDeleteConfirmation); 
		    dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		             finish();  
		        }
		   });
		    dlgAlert.setCancelable(true);
		    dlgAlert.create().show();
		}	
				
}//class
