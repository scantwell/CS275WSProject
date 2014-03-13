package com.dpm.uavflightplanner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
 
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


//This class uploads a single flight plan to the backend
public class UploadFPTask extends AsyncTask<ArrayList<Map<String, String>>, Void, String> {
	
	ArrayList<Map<String, String>> fwplan = new ArrayList<Map<String, String>>();
	
	public UploadFPTask(ArrayList<Map<String, String>> fw) {
		// TODO Auto-generated constructor stub 
		fwplan = fw;			//fwplan could be either one waypoint or a complete flight plan
	}
	
	@Override
	protected String doInBackground(ArrayList<Map<String, String>>... params) {
		
		String fpName = null;
		Log.i("InfoTag", "Starting UploadFPTask.doInBackground");
		
		Log.i("InfoTag", "UploadFPTask.doInBackground, fwplan.size() = " + fwplan.size());
		for(int i = 0; i < fwplan.size();i++){
			Log.i("InfoTag", "UploadFPTask.doInBackground.fplan1info" + fwplan.get(i).entrySet().toString());
			fpName = fwplan.get(i).get("fpname").toString();
			Log.i("InfoTag", "UploadFPTask.doInBackground.fwplan.fpname = " + fpName);
			//Log.i("InfoTag", "UploadFPTask.doInBackground.fplan1info" + fwplan.get(i).get("fpname").toString());
			//Log.i("InfoTag", "UploadFPTask.doInBackground.fplan1info" + fwplan.get(i).get("seq_number").toString());
			//Log.i("InfoTag", "UploadFPTask.doInBackground.fplan1info" + fwplan.get(i).get("name").toString());
			//Log.i("InfoTag", "UploadFPTask.doInBackground.fplan1info" + fwplan.get(i).get("latitude").toString());
			//Log.i("InfoTag", "UploadFPTask.doInBackground.fplan1info" + fwplan.get(i).get("longitude").toString());
			//Log.i("InfoTag", "UploadFPTask.doInBackground.fplan1info" + fwplan.get(i).get("altitude").toString());
			//Log.i("InfoTag", "UploadFPTask.doInBackground.fplan1info" + fwplan.get(i).get("deltat").toString());
		}
		
		String response = null;
		/*
		//Model of data structure
		//String sURL = "http://localhost:8080/flights/";	
		String sURL = "http://10.0.2.2:8080/flights/";
		Gson gson = new Gson();
		FlightPlan fp = new FlightPlan("2014_03_01SanFran");
		fp.addWaypoint("First", 1, (float)40.03,(float) 50.223,(float) 100.00, 3);
		fp.addWaypoint("Second", 2, (float)40.03,(float) 50.223,(float) 100.00, 5);
		fp.addWaypoint("Three", 1, (float)40.03,(float) 50.223,(float) 100.00, 3);
		*/

		String sURL = "http://10.0.2.2:8080/flights/";
		Gson gson = new Gson();
		Log.i("InfoTag", "Starting UploadFPTask.doInBackground.makes_it_to_here");
		FlightPlan fp = new FlightPlan(fpName);
		String wpName = null;
		String wpNumber = null;
		String latitude = null;
		String longitude = null;
		String altitude = null;
		String delta = null;
		Map<String,String> wp = null; 
		
		Log.i("InfoTag", "UploadFPTask.doInBackground2, fwplan.size() = " + fwplan.size());
		for(int i = 0; i < fwplan.size(); i++) {
			//TODO -- parse fwplan into Steve's datastructure
			wp = fwplan.get(i);
			wpName = wp.get("name").toString();	
			wpNumber = wp.get("seq_number");
			latitude = wp.get("latitude");
			longitude = wp.get("longitude");
			altitude = wp.get("altitude");
			delta = wp.get("deltat");
			Log.i("InfoTag", "UploadFPTask.doInBackground.makes_it_to_here 1.5");
			Log.i("InfoTag", "UploadFPTask.doInBackground.fpName = " + fpName);			//not part of wp
			Log.i("InfoTag", "UploadFPTask.doInBackground.wpName = " + wpName);
			Log.i("InfoTag", "UploadFPTask.doInBackground.wpNumber = " + wpNumber);
			Log.i("InfoTag", "UploadFPTask.doInBackground.latitude = " + latitude);
			Log.i("InfoTag", "UploadFPTask.doInBackground.longitude = " + longitude);
			altitude = altitude.replace(".0", "");
			Log.i("InfoTag", "UploadFPTask.doInBackground.altitude = " + altitude);
			delta = delta.replace(".0", "");
			Log.i("InfoTag", "UploadFPTask.doInBackground.delta = " + delta);
			fp.addWaypoint(wpName, Integer.valueOf(wpNumber), 
							Float.valueOf(latitude), Float.valueOf(longitude), 
							//Integer.valueOf(altitude), Integer.valueOf(delta));
							Integer.valueOf(altitude), Integer.valueOf(delta));
		}
		Log.i("InfoTag", "Starting UploadFPTask.doInBackground.makes_it_to_here 2");
		String json = gson.toJson(fp);
		response = executePost(sURL, json );
		Log.i("InfoTag", "Starting UploadFPTask.doInBackground.makes_it_to_here 3");
		return response;
		
	} //doInBackground
	
	protected void onProgressUpdate() {
		
	}//onProgressUpdate
	
	
    protected void onPostExecute(final Boolean success) {

}

	
	
	
	
	//This function executes the http post to upload a flight plan
	//Implemented by Steve
	public static String executePost(String targetURL, String urlParameters) 
    {
		
		Log.i("InfoTag", "Start executePost");
      URL url;
      HttpURLConnection connection = null;  
      try {
        //Create connection
        url = new URL(targetURL);
        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", 
             //"application/x-www-form-urlencoded");
        		"application/json");
           
        connection.setRequestProperty("Content-Length", "" + 
                 Integer.toString(urlParameters.getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");  
           
        connection.setUseCaches (false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        
        Log.i("InfoTag", "executePost #1");
        
        //Send request
        DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
        Log.i("InfoTag", "executePost #1.1");
        wr.writeBytes (urlParameters);
        Log.i("InfoTag", "executePost #1.2");
        wr.flush ();
        wr.close ();

        Log.i("InfoTag", "executePost #2");
        //Get Response   
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer(); 
        while((line = rd.readLine()) != null) {
          response.append(line);
          response.append('\r');
        }
        rd.close();
        Log.i("InfoTag", "Finish executePost");
        return response.toString();
      } catch (Exception e) {


          e.printStackTrace();
          return null;


        } finally {


          if(connection != null) {
            connection.disconnect(); 
          }
        }
      }//executePost



}//class
