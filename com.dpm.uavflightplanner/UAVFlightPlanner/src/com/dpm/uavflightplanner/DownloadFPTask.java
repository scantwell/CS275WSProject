package com.dpm.uavflightplanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import android.os.AsyncTask;
import android.util.Log;

//This class downloads a single flight plan (its waypoints) from the backend
public class DownloadFPTask extends AsyncTask<String, Void, ArrayList<Map<String, String>>> {
	
	String[] dummy;
	String fpName;
	
	public DownloadFPTask(String selectedFP) {
		fpName = selectedFP;
	}
	@Override
	protected void onPreExecute() {
		
	}
	@Override
	protected ArrayList doInBackground(String... params) {
		
		Log.i("InfoTag", "Starting DownloadFPTask.doInBackground");
		
		//Strip quotes
		fpName = fpName.replace("\"", "");

		//URL of backend, hosted on local machine with help of Bottle.py
		String url = "http://10.0.2.2:8080/flights/" + fpName;
		Log.i("InfoTag", "DownloadFPTask.doInBackground, url is " + url);
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

		Log.i("InfoTag", "From DownloadFPtask.doInBackground, response is " + response);
		
		JsonParser jp = new JsonParser();
		jp.parse(response.toString());
		JsonElement root = jp.parse(response.toString());
		JsonObject rootobj = root.getAsJsonObject();
		JsonArray waypoints = rootobj.get("waypoint").getAsJsonArray();
		Log.i("InfoTag", "From DownloadFPtask.doInBackground, waypoints = " + waypoints.toString());	
		JsonElement item;
		JsonObject wp;
		JsonPrimitive name;
		JsonPrimitive seq_num;
		JsonPrimitive latitude;
		JsonPrimitive longitude;
		JsonPrimitive altitude;
		JsonPrimitive delta;
		
		String fpname = rootobj.get("name").toString();
		fpname = fpname.replace("\"", "");
		
		ArrayList<Map<String, String>> fplan = new ArrayList<Map<String, String>>();
		//Log.i("InfoTag", "From doInBackground, before for loop");
		for(int i = 0; i < waypoints.size(); i++){
			item = waypoints.get(i);
			wp = item.getAsJsonObject();
			name = wp.get("name").getAsJsonPrimitive();
			seq_num = wp.get("seq_number").getAsJsonPrimitive();
			latitude = wp.get("latitude").getAsJsonPrimitive();
			longitude = wp.get("longitude").getAsJsonPrimitive(); 
			altitude = wp.get("altitude").getAsJsonPrimitive();
			delta = wp.get("deltat").getAsJsonPrimitive();
			
			//Log.i("InfoTag", "From doInBackground, in for loop");
			Map<String, String> map = new HashMap<String, String>();
			map.put("fpname", fpname);
			map.put("name", name.toString().replace("\"", ""));
			map.put("seq_number", seq_num.toString());
			map.put("latitude", latitude.toString());
			map.put("longitude", longitude.toString());
			map.put("altitude", altitude.toString());
			map.put("deltat", delta.toString());
			fplan.add(map);
		}
		
		//Sort fplan list
		
		//Arrays.sort(fplans, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
		Log.i("InfoTag", "Finishing DownloadFPTask.doInBackground");
		
		
		return fplan;
		
	} //doInBackground
	
	protected void onProgressUpdate() {
		
	}
	
	protected void onPostExecute() {
		
	}	
	
}
