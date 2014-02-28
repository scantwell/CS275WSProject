package com.dpm.uavflightplanner;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FlightPlanMapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flight_plan_map);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flight_plan_map, menu);
		return true;
	}

}
