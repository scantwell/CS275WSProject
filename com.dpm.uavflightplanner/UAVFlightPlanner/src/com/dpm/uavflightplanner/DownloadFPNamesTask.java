package com.dpm.uavflightplanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import android.os.AsyncTask;
import android.util.Log;

//This class downloads all of the flight plan names (no waypoints) from the backend
public class DownloadFPNamesTask extends AsyncTask<Void, Void, String[]> {
	
	@Override
	protected void onPreExecute() {
		
	}
	@Override
	protected String[] doInBackground(Void... params) {
		
		Log.i("InfoTag", "Starting DownloadFPNamesTask.doInBackground");

		//URL of backend, hosted on local machine with help of Bottle.py
		String url = "http://10.0.2.2:8080/flights/";
		
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

		//Log.i("InfoTag", "From doInBackground, response is " + response);
		
		JsonParser jp = new JsonParser();
		jp.parse(response.toString());
		JsonElement root = jp.parse(response.toString());
		JsonObject rootobj = root.getAsJsonObject();
		JsonArray flights = rootobj.get("flights").getAsJsonArray();
		JsonElement item;
		JsonObject fp;
		JsonPrimitive name;
		String strName;
		String[] fpnames = new String[flights.size()];
		
		for(int i = 0; i < flights.size(); i++){
			item = flights.get(i);
			fp = item.getAsJsonObject();
			name = fp.get("name").getAsJsonPrimitive();
			strName = name.toString();
			strName = strName.replace("\"", "");
			fpnames[i] = strName;
			//Log.i("InfoTag", "From doInBackground, name = " + fpnames[i]);	
		}
		Arrays.sort(fpnames, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
		//Log.i("InfoTag", "Finishing doInBackground");
		return fpnames;
		
	} //doInBackground
	
	protected void onProgressUpdate() {
		
	}
	
	protected void onPostExecute() {
		
	}	
	
}
