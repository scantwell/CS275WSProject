package com.dpm.uavflightplanner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
 
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import android.os.AsyncTask;
import android.util.Log;


//This class uploads a single flight plan to the backend
public class UploadFPTask extends AsyncTask<Void, Void, String> {
	
	@Override
	protected void onPreExecute() {
		
	}
	
	@Override
	protected String doInBackground(Void... params) {
		
		Log.i("InfoTag", "Start doInBackground");
		
		String response;
		
		String sURL = "http://localhost:8080/flights/";	
		Gson gson = new Gson();
		FlightPlan fp = new FlightPlan("2014_03_01SanFran");
		fp.addWaypoint("First", 1, (float)40.03,(float) 50.223,(float) 100.00, 3);
		fp.addWaypoint("Second", 2, (float)40.03,(float) 50.223,(float) 100.00, 5);
		fp.addWaypoint("Three", 1, (float)40.03,(float) 50.223,(float) 100.00, 3);
		String json = gson.toJson(fp);
		response = executePost(sURL, json );
		
		return response;
		
	} //doInBackground
	
	protected void onProgressUpdate() {
		
	}//onProgressUpdate
	
	protected void onPostExecute() {
		
	}//onPostExecute	
	
	//This function executes the http post to upload a flight plan
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
             "application/x-www-form-urlencoded");
           
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
