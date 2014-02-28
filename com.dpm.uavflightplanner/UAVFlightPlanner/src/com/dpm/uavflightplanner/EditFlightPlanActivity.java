package com.dpm.uavflightplanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EditFlightPlanActivity extends Activity {
	
	ListView listViewFP;
	EditText editFPName;
	String fpName = "2014_02_27_Baseball";		//sample data

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_flight_plan);
		
		//Initialize
		listViewFP = (ListView)findViewById(R.id.listViewFlightPlan); 
		editFPName = (EditText)findViewById(R.id.editTextFPName);
		editFPName.setText(fpName);	
		Map<String, String> map = new HashMap<String, String>();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		 // Assign adapter to ListView		 
		SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.layout_row_flightplan, 
					new String[] { "wpname", "wpnumber", "latitude", "longitude", "altitude", "deltatime" },
					new int[] { R.id.editTextWPName, R.id.editTextWPNumber, R.id.editTextLatitude, 
					R.id.editLongitude, R.id.editTextAltitude, R.id.editTextDeltaTime });
		listViewFP.setAdapter(adapter);

		
		//Populate with sample data
		 map.put("wpname", "Above Home Plate");
		 map.put("wpnumber", "1");
		 map.put("latitude", "7435.838");
		 map.put("longitude", "-4001.629");
		 map.put("altitude", "10");
		 map.put("deltatime", "0:15");
		 list.add(map);
		
		 map.put("wpname", "First Base");
		 map.put("wpnumber", "2");
		 map.put("latitude", "7435.726");
		 map.put("longitude", "-4001.567");
		 map.put("altitude", "20");
		 map.put("deltatime", "0:15");
		 list.add(map);		
		 
		 map.put("wpname", "Second Base");
		 map.put("wpnumber", "3");
		 map.put("latitude", "7435.653");
		 map.put("longitude", "-4001.478");
		 map.put("altitude", "30");
		 map.put("deltatime", "0:15");
		 list.add(map);
		 
		 map.put("wpname", "Third Base");
		 map.put("wpnumber", "4");
		 map.put("latitude", "7435.543");
		 map.put("longitude", "-4001.347");
		 map.put("altitude", "20");
		 map.put("deltatime", "0:15");
		 list.add(map);
		 
		 map.put("wpname", "Above Home Plate");
		 map.put("wpnumber", "5");
		 map.put("latitude", "7435.838");
		 map.put("longitude", "-4001.629");
		 map.put("altitude", "10");
		 map.put("deltatime", "0:15");
		 list.add(map);
		 
		 map.put("wpname", "Home Plate");
		 map.put("wpnumber", "5");
		 map.put("latitude", "7435.838");
		 map.put("longitude", "-4001.629");
		 map.put("altitude", "0");
		 map.put("deltatime", "0:15");
		 
		
		//ArrayAdapter<Map<String, String>> arrayAdpt = new ArrayAdapter<Map<String, String>>(this, R.layout.layout_row_flightplan, list);
		//listViewFP.setAdapter(arrayAdpt);
		/*arrayAdpt.notifyDataSetChanged();*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_flight_plan, menu);
		return true;
	}

}
