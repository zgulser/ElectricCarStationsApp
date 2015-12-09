/**
 * 
 * Author: Zeki Gulser
 * 
 * Desc: Utility class to parse JSON string/file
 * 
 *       To prevent multiple service threads to interleave it's synchoronized (just in case) 
 * 
 */

package com.example.electriccarstationsapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;

public class JSONParser {
	
	// For JSON parsing
	public static final String TAG_RESPONSE = "response";
	public static final String TAG_STATIONS = "chargingstations";
	public static final String TAG_POSTALCODE = "postalcode";
	public static final String TAG_CITY = "city";
	public static final String TAG_NAME = "name";
	public static final String TAG_ADDRESS = "address";
	public static final String TAG_OWNER = "owner";
	public static final String TAG_OPERATOR = "operator";
	public static final String TAG_STATUS = "status";
	public static final String TAG_OPENDATE = "created";
	public static final String TAG_MODIFIEDDATE = "modified";
	public static final String TAG_VOLTAGE = "voltage";
	public static final String TAG_CURRENT = "current";
	public static final String TAG_POWER = "power";
	public static final String TAG_POINTS = "points";
	public static final String TAG_POINT_STATUS = "status";
	public static final String TAG_POINT_NAME = "name";
	public static final String TAG_ISACTIVE = "isactive";
	public static final String TAG_LOC_ARRAY = "loc";
	
	public synchronized static void formJSONDocAndParse(String pJSONInString)
	{		
		String postalCode = null;
		String city = null;
		String openDate = null;
		String modifiedDate = null;
		String name = null;
		String address = null;
		String owner = null;
		String operator = null;
		String status = null;
		int voltage = 0;
		int current = 0;
		int power = 0;
		boolean isActive = true;		
		SparseArray<Point> points = new SparseArray<Point>();		
		LatLng latLong = null;		
	    	    
		JSONObject rootjObj = null;
		JSONObject responsejObj = null;
		JSONArray stations = null;
		try {
						
			rootjObj = new JSONObject(pJSONInString);
			responsejObj = rootjObj.getJSONObject(TAG_RESPONSE);
			stations = responsejObj.getJSONArray(TAG_STATIONS);
			for(int i=0; i < stations.length(); i++)
			{
				// get regarding json object which is a contact
				JSONObject stationObj = stations.getJSONObject(i);
				if(stationObj != JSONObject.NULL)
				{
					// parse it's name fields first
					postalCode = stationObj.getString(TAG_POSTALCODE);
					city = stationObj.getString(TAG_CITY);
					openDate = stationObj.getString(TAG_OPENDATE);
					modifiedDate = stationObj.getString(TAG_MODIFIEDDATE);
					name = stationObj.getString(TAG_NAME);
					address = stationObj.getString(TAG_ADDRESS);
					owner = stationObj.getString(TAG_OWNER);
					operator = stationObj.getString(TAG_OPERATOR);
					status = stationObj.getString(TAG_STATUS);
					voltage = stationObj.getInt(TAG_VOLTAGE);
					current = stationObj.getInt(TAG_CURRENT);
					power = stationObj.getInt(TAG_POWER);
					Log.d("Jsonpars", "STEP is active: " + stationObj.getString(TAG_ISACTIVE));
					isActive = (stationObj.getString(TAG_ISACTIVE)==null || stationObj.getString(TAG_ISACTIVE).equals("0")) ? false : true;
					
					SparseArray<Point> pointsJSON = new SparseArray<Point>();
					JSONArray pointsArray = stationObj.getJSONArray(TAG_POINTS);
					for(int j=0; j < pointsArray.length(); j++)
					{
						JSONObject pointObj = pointsArray.getJSONObject(j);
						String pointStatus = pointObj.getString(TAG_POINT_STATUS);
						String pointName = pointObj.getString(TAG_POINT_NAME);
						
						Point p = new Point(pointStatus, pointName);
						pointsJSON.append(j, p);
					}
					
					points = pointsJSON;
					
					JSONArray latLongArray = stationObj.getJSONArray(TAG_LOC_ARRAY);
					double lat = latLongArray.getDouble(0);
					double lon = latLongArray.getDouble(1);
					LatLng target = new LatLng(lat, lon);
										
					Station station = new Station(postalCode, city, openDate, modifiedDate, name, address, owner,
											operator, status, points, voltage, current, power , target, isActive);
					
					DataUtils.stationList.put(name+owner+operator+address,station);
					
					if(status.equals(DataUtils.STATION_STATUS_AVAILABLE)){
						DataUtils.stationListAval.put(name+owner+operator+address,station);
					} else {
						DataUtils.stationListOcc.put(name+owner+operator+address,station);
					}
					
					int distance = DataUtils.findBareDistanceBetweenTwoPoints(DataUtils.myLocation, target);
					if(DataUtils.is1kmClose(distance)){
						DataUtils.stationListNear1km.put(name+owner+operator+address,station);
					} else if(DataUtils.is5kmClose(distance)){
						DataUtils.stationListNear5km.put(name+owner+operator+address,station);
					}
				}
			}
			
	    } catch (JSONException e) {
	           e.printStackTrace();
	    }		
					
	}	
}
