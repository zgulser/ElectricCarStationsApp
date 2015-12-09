/**
 * 
 * Author: Zeki Gulser
 * 
 * Desc: Helper class to perform Http Requests
 * 
 */

package com.example.electriccarstationsapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import javax.net.ssl.HttpsURLConnection;

import android.util.Log;

public class ConnectionManager {

	/**
	 * 
	 * Returns stations in JSON format
	 * 
	 * @param pUrl
	 * @return
	 */
	public static String getStations(String pUrl)
	{				
		System.setProperty("http.keepAlive", "false");
				
		URL url = null;
		InputStream is = null;
		HttpsURLConnection connection = null;
		StringBuilder jsonData = new StringBuilder();
		
		try {
			
			url = new URL(pUrl);									
			connection = (HttpsURLConnection) url.openConnection();	
						
			is = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));				
			String line;
			while ((line = reader.readLine()) != null) {
				jsonData.append(line);
			}																		
				
			is.close();
			connection.disconnect();											
		} 
		catch (ConnectException e)
		{
			e.printStackTrace();
		}
		catch (MalformedURLException e) {

			e.printStackTrace();
		}
		catch (SocketTimeoutException e)
		{
			e.printStackTrace();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (UnknownServiceException e)
		{
			e.printStackTrace();
		}		
		catch (IOException e) {
			
			e.printStackTrace();
		}
		
		Log.d("ConnMan", "STEP content: " + jsonData.toString());
		return jsonData.toString();	
	}

}
