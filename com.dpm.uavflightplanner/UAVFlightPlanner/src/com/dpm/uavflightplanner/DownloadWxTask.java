package com.dpm.uavflightplanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class DownloadWxTask extends AsyncTask<Void, Void, String> {
	
	@Override
	protected void onPreExecute() {
		
	}
	@Override
	protected String doInBackground(Void... params) {
		String zipCode = "08054";  //initial value is overwritten
		
		Log.i("InfoTag", "Starting doInBackground");

		String url = "http://freegeoip.net/json";
		URL obj = null;
		try {
			obj = new URL(url);
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) obj.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	 
		BufferedReader in = null;
		try {
			in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String inputLine;
		StringBuffer response = new StringBuffer();
	 
		try {
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Log.i("InfoTag", "From doInBackground, freegeoip response is " + response);
		
		JsonParser jp = new JsonParser();
		jp.parse(response.toString());
		JsonElement root = jp.parse(response.toString());
		JsonObject rootobj = root.getAsJsonObject();
		JsonPrimitive zip = rootobj.get("zipcode").getAsJsonPrimitive();
		
		//trap null
		if(zip.toString() == ""){
			;//keep default
		}
		else{
			;//zipCode = zip.toString();			
		}
		
		Log.i("InfoTag", "From doInBackground, zipCode = " + zipCode);
		
		url = "http://api.wunderground.com/api/148f2b4012594267/conditions/q/" + zipCode + ".json";

		try {
			obj = new URL(url);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			con = (HttpURLConnection) obj.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		try {
			in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response = new StringBuffer();
	 
		try {
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("InfoTag", "From doInBackground, got wx response");
		//Log.i("InfoTag", "From doInBackground, response " + response.toString());
		
		//get forecast
		String wxobs = "None";
		jp = new JsonParser();
		root = jp.parse(response.toString());
		rootobj = root.getAsJsonObject();
		JsonObject currentobs = rootobj.get("current_observation").getAsJsonObject();
		JsonObject loc = currentobs.get("display_location").getAsJsonObject();
		JsonPrimitive full = loc.get("full").getAsJsonPrimitive();
		JsonPrimitive obstime = currentobs.get("observation_time").getAsJsonPrimitive();
		JsonPrimitive winddegrees = currentobs.get("wind_degrees").getAsJsonPrimitive();
		JsonPrimitive windmph = currentobs.get("wind_mph").getAsJsonPrimitive();
		JsonPrimitive windgust = currentobs.get("wind_gust_mph").getAsJsonPrimitive();			
		JsonPrimitive temperature = currentobs.get("temperature_string").getAsJsonPrimitive();
		JsonPrimitive pressure = currentobs.get("pressure_in").getAsJsonPrimitive();
		JsonPrimitive pressuretrend = currentobs.get("pressure_trend").getAsJsonPrimitive();
		
		//JsonArray forecastday = txtforecast.get("forecastday").getAsJsonArray();
		//JsonObject period0 = forecastday.get(0).getAsJsonObject();
		
		//JsonObject period;
		//JsonPrimitive title = null;
		//String[] forecastarray = new String[forecastday.size()];
		
		wxobs = "Current observation for " + full.toString() + ". " + obstime.toString() + ". " + "Winds " 
		+ winddegrees.toString() + " at " + windmph.toString() + " gusting " + windgust.toString() + " mph. "
		+ "Temperature " + temperature.toString() + ". " 
		+ "Pressure " + pressure.toString() + " trending " + pressuretrend.toString();
		

		
		Log.i("InfoTag", "Finishing doInBackground");
		//Log.i("InfoTag", "From doInBackground, wxobs = " + wxobs);			//for debugging 
		
		//return forecastarray;
		return wxobs;
	} 
	
	protected void onProgressUpdate() {
		
	}
	
	protected void onPostExecute() {
		
	}	
	
}
