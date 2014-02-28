package com.dpm.uavflightplanner;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

//Adapted from http://androidexample.com/Create_A_Simple_Listview_-_Android_Example/index.php?view=article_discription&aid=65&aaid=90
public class MainActivity extends Activity {
	
	ListView listViewFlightPlans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 // Get ListView object from xml
        listViewFlightPlans = (ListView) findViewById(R.id.listViewFlightPlans);

        // Define/get array values to show in ListView
        String[] values = new String[] { "2014_02_27_LaurelAcres", 
                                         "2014_02_25_WhartonForest",
                                         "2014_02_23_RancocasWoods",
                                         "2014_02_21_LongBeachIsland", 
                                         "2014_02_15_FrontYardHoverTest", 
                                         "2014_02_12_BaseBallField", 
                                         "2014_02_09_Bahamas", 
                                         "2014_02_07_Rt38_HartfordRdtoI295", 
                                         "2014_02_03_Rt38_HartfordRdtoArcRd",
                                         "2014_01_27_LarchmontBlvdSchooltoWawa",
                                         "2014_01_15_LaurelAcresBaseBallField1"
                                        };
        
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listViewFlightPlans.setAdapter(adapter); 
        
        // ListView Item Click Listener
        listViewFlightPlans.setOnItemClickListener(new OnItemClickListener() {

       @Override
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	   // ListView Clicked item index
           int itemPosition = position;

           // ListView Clicked item value
           String  itemValue    = (String) listViewFlightPlans.getItemAtPosition(position);

                // Show Alert 
                Toast.makeText(getApplicationContext(),
                  "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                  .show();
              }
         }); 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
   /*final Button buttonView = (Button) findViewById(R.id.buttonView);
    buttonView.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            sendMessage(buttonView);// Perform action on click
        }});
    }*/

	
	/** Called when the user clicks the View button */
	public void sendMessage(View viewButton) {
	    // Do something in response to button
		//Intent intent = new Intent(this, EditFlightPlanActivity.class);
		//startActivity(intent);
	}
}
