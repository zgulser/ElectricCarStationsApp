/**
 * 
 * Author: Zeki Gulser
 * 
 * Desc: Service performing JSON data retreival, parsing, creating lists & broadcasting  
 *  
 */

package com.example.electriccarstationsapp.model;

import com.example.electriccarstationsapp.ui.MainActivity;
import com.google.android.gms.maps.model.LatLng;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdateStationsIntentService extends IntentService {

	public UpdateStationsIntentService() {
		super("MyIntentServiceForUpdateStations");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("Service", "STEP service destroyed!");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		if (MainActivity.gpsTracker != null && 
				MainActivity.gpsTracker.canGetLocation()){
			DataUtils.myLocation = new LatLng(MainActivity.gpsTracker.getLatitude(), MainActivity.gpsTracker.getLongitude());			
		}

		String JSONcontent = ConnectionManager.getStations(DataUtils.stationsUrl);
		JSONParser.formJSONDocAndParse(JSONcontent);
		DataUtils.sendBroadcast(DataUtils.DATASET_UPDATED_ACTION, getApplicationContext());
		Log.i("UpdIntServ", "STEP fok: " + JSONcontent);		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
