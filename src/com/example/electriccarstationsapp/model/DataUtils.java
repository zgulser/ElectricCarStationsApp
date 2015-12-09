/**
 * 
 * Author: Zeki Gulser
 * 
 * Desc: Utility class for containers, final variables & some utility functions
 * 
 */

package com.example.electriccarstationsapp.model;

import java.util.LinkedHashMap;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class DataUtils {
	
	public static final String stationsUrl = "https://distribute.labela.nl/labela-test/stations.json";
	public static LinkedHashMap<String, Station> stationList = new LinkedHashMap<String, Station>();
	public static LinkedHashMap<String, Station> stationListAval = new LinkedHashMap<String, Station>();
	public static LinkedHashMap<String, Station> stationListOcc = new LinkedHashMap<String, Station>();
	public static LinkedHashMap<String, Station> stationListNear1km = new LinkedHashMap<String, Station>();
	public static LinkedHashMap<String, Station> stationListNear5km = new LinkedHashMap<String, Station>();
	public static final String DATASET_UPDATED_ACTION = "com.example.eclectriccarstationsapp"+ "_DATASET_UPDATED_ACTION";
	public static final String STATION_STATUS_AVAILABLE = "AVAILABLE";
	public static final String STATION_STATUS_OCCUPIED = "OCCUPIED";
	public static final String POINT_STATUS_FREE = "FREE";
	public static final String POINT_STATUS_INUSE = "IN_USE";
	public static LatLng myLocation = new LatLng(0, 0);
	
	public enum FilterType{
		
		FILTER_ALL,
		FILTER_1km,
		FILTER_5km,
		FILTER_AVAILABLE,
		FILTER_OCCUPIED
	}
	
	public static FilterType currentFilter = FilterType.FILTER_ALL;
	
	public static void sendBroadcast(String action, Context context){
		Intent intent = new Intent(action);
		context.sendBroadcast(intent);
	}

	public static FilterType getCurrentFilter() {
		return currentFilter;
	}

	public static void setCurrentFilter(FilterType currentFilter) {
		DataUtils.currentFilter = currentFilter;
	}		

	/**
	 * 
	 * Returns distance bw two points
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static int findBareDistanceBetweenTwoPoints(LatLng me, LatLng target){
		
		double distance = 0;
		Location A = new Location("Me");
		A.setLatitude(me.latitude);
		A.setLongitude(me.longitude);
		Location B = new Location("Trgt");
		B.setLatitude(target.latitude);
		B.setLongitude(target.longitude);
		distance = A.distanceTo(B); // in meters
		return (int)distance;
	}
	
	public static boolean is1kmClose(int distance){
		return distance <= 1000;
	}
	
	public static boolean is5kmClose(int distance){
		return distance > 1000 && distance <= 5000;
	}
	
}
