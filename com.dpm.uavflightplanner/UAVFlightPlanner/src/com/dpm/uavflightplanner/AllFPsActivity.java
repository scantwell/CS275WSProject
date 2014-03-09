package com.dpm.uavflightplanner;

import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AllFPsActivity extends Activity {
	
	Button btnAll;
	Button btnOne;
	Button btnMap;
	Button btnFly;
	Button btnNew;
	Button btnDelete;
	String[] fpnames;
	String selectedFP = null;
	TextView textViewFPName;
	AsyncTask<Void, Void, String[]> asyncFPs;
	ListView listViewFPs;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_fps);
		
		//initialize controls
		textViewFPName = (TextView)this.findViewById(R.id.textViewSelectedFPName);
		listViewFPs = (ListView) findViewById(R.id.listViewFlightPlans);
		
		btnAll = (Button) findViewById(R.id.buttonAll);
		btnOne = (Button) findViewById(R.id.buttonOne);
		btnMap = (Button) findViewById(R.id.buttonMap);
		btnFly = (Button) findViewById(R.id.buttonFly);
		btnNew = (Button) findViewById(R.id.buttonNew);
		btnDelete = (Button) findViewById(R.id.buttonDelete);
		
		btnAll.setOnClickListener(allHandler);
		btnOne.setOnClickListener(oneHandler);
		btnMap.setOnClickListener(mapHandler);
		btnFly.setOnClickListener(flyHandler);
		btnNew.setOnClickListener(newHandler);
		btnDelete.setOnClickListener(deleteHandler);
		
		//Get the flight plan names
		asyncFPs = new DownloadFPNamesTask().execute();
		try {
			fpnames = asyncFPs.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, fpnames); 

        listViewFPs.setAdapter(adapter); 
        
        listViewFPs.setOnItemClickListener(new OnItemClickListener() {

       @Override
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {         
           //Get name to pass to other activities
           selectedFP = (String) listViewFPs.getItemAtPosition(position);
           //Display selection in text view control
           textViewFPName.setText(selectedFP);
              }
         }); 
    }//onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}//onCreateOptionsMenu
		  
	//group of four handlers for navigation between activities
	View.OnClickListener allHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(AllFPsActivity.this, AllFPsActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				AllFPsActivity.this.startActivity(myIntent);
		   }
		};
	View.OnClickListener oneHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(AllFPsActivity.this, OneFPActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				AllFPsActivity.this.startActivity(myIntent);
		   }
		};	
	View.OnClickListener mapHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(AllFPsActivity.this, MapActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				AllFPsActivity.this.startActivity(myIntent);
		   }
		};	
	View.OnClickListener flyHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(AllFPsActivity.this, FlyActivity.class);
				myIntent.putExtra("fp", selectedFP); //Optional parameters
				AllFPsActivity.this.startActivity(myIntent);
		   }
		};
		
	View.OnClickListener newHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(AllFPsActivity.this, AddWPActivity.class);
				//Open it blank -- fp = null, next wp = 1
				String nextWPandFPName = String.valueOf(1);
				myIntent.putExtra("nextWPandFPName", nextWPandFPName); //Optional parameters
				AllFPsActivity.this.startActivity(myIntent);
		   }
		};
		
	View.OnClickListener deleteHandler = new View.OnClickListener() {
			    public void onClick(View v) {
			    	msgbox(R.string.activity_main_delete_button, R.string.activity_main_delete_confirmation);
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
    


	


