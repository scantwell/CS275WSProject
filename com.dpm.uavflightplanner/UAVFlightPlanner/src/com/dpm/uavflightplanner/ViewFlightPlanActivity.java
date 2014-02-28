package com.dpm.uavflightplanner;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ViewFlightPlanActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_flight_plan);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_flight_plan, menu);
		return true;
	}

}
