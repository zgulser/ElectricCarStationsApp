package com.example.electriccarstationsapp.ui;

import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.electriccarstationsapp.R;
import com.example.electriccarstationsapp.model.DataUtils;
import com.example.electriccarstationsapp.model.GPSTracker;
import com.example.electriccarstationsapp.model.UpdateStationsIntentService;
import com.example.electriccarstationsapp.ui.StationsListFragment.OnMyListItemClickListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity implements OnMyListItemClickListener, ConnectionCallbacks, OnConnectionFailedListener{
	
	public static String STATION_LIST_FRAGMENT_TAG = "com.example.electriccarstationsapp.stationListFragment";
	public static String MAP_FRAGMENT_TAG = "com.example.electriccarstationsapp.mapFragment";
	public static String ACTION_ALARM = "com.example.electriccarstationsapp.myAlarm";
	public static LocationManager mLocationManager = null;
	public static GPSTracker gpsTracker = null;
	private GoogleApiClient mGoogleApiClient = null;  
	
	private final LocationListener mLocationListener = new LocationListener() {
	    @Override
	    public void onLocationChanged(final Location location) {
	        
	    	LatLng newOne = new LatLng(location.getLatitude(), location.getLongitude());
	    	DataUtils.myLocation = newOne;
	    }
	};
	
	/**
	 * 
	 * Comment: Unused receiver. Added to be later.
	 */
	private BroadcastReceiver mAlarmReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			
			 Bundle bundle = intent.getExtras();
			 String action = bundle.getString(ACTION_ALARM);
			 if (action.equals(ACTION_ALARM)) {
				 Log.d("MainAct", "STEP alarm received");
				 Intent inService = new Intent(MainActivity.this, UpdateStationsIntentService.class);
				 MainActivity.this.startService(inService);
			 }
			 else
			 {
				  Log.i("Alarm Receiver", "Else loop");
			 }
		}
	};
	
	private BroadcastReceiver mDataUpdatedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String intentAction = intent.getAction();
			if (intentAction.equals(DataUtils.DATASET_UPDATED_ACTION)) {
				StationsListFragment stationFrag = (StationsListFragment)getFragmentManager().findFragmentByTag(STATION_LIST_FRAGMENT_TAG);
				if (stationFrag!= null && 
						stationFrag.isVisible()) {
					stationFrag.updateRecyclerView();					
				} else {
					StationsMapFragment mapFrag = (StationsMapFragment)getFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
					if (mapFrag!= null && 
							mapFrag.isVisible()) {
						// do sth
					}
				}
			}
		}
	};
	
	private void initReceiver() {
		this.registerReceiver(this.mDataUpdatedReceiver, new IntentFilter(
				DataUtils.DATASET_UPDATED_ACTION));
		
		if(mGoogleApiClient.isConnected()){
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, 
				createLocationRequest(), mLocationListener);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initReceiver();		
	}
	
	@Override
	protected void onPause() {	
		super.onPause();
		
		if(mGoogleApiClient.isConnected()){
			LocationServices.FusedLocationApi.removeLocationUpdates(
	            mGoogleApiClient, mLocationListener);
		}
	}
	
	@Override
	protected void onStop() {	
		super.onStop();

		mGoogleApiClient.disconnect();
	}
	
	/**
	 * COMMENT: To understand background update, unregistering performed here instead onPause()
	 */
	@Override
	protected void onDestroy() {	
		super.onDestroy();
		unregisterReceiver(mDataUpdatedReceiver);		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		DataUtils.myLocation = new LatLng(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),
									mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
		/*
		gpsTracker = new GPSTracker(this);
		if (gpsTracker.canGetLocation()){
			DataUtils.myLocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());			
		}*/
		
		
		mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
         .addApi(LocationServices.API)
         .addConnectionCallbacks(this)
         .addOnConnectionFailedListener(this)
         .build();

		
		setupUI();	
		startAlarmManager();	
	}
	
	protected LocationRequest createLocationRequest() {
	    LocationRequest mLocationRequest = new LocationRequest();
	    mLocationRequest.setInterval(10000);
	    mLocationRequest.setFastestInterval(5000);
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
	    
	    return mLocationRequest;
	}
	
	@Override
	protected void onStart() {	
		super.onStart();
		mGoogleApiClient.connect();
	}
	
	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		if (getFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG) != null
				&& getFragmentManager().findFragmentByTag(
						MAP_FRAGMENT_TAG).isVisible()) {
			if (findViewById(R.id.fragment_container) != null
					&& getFragmentManager().findFragmentByTag(
							STATION_LIST_FRAGMENT_TAG) == null) {
				StationsListFragment fragmentStationList = new StationsListFragment();
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(R.anim.left_swipe_one,
						R.anim.right_swipe_one);
				ft.replace(R.id.fragment_container, fragmentStationList,
						STATION_LIST_FRAGMENT_TAG);
				fragmentStationList.setArguments(getIntent().getExtras());
				ft.commitAllowingStateLoss();
			} else {
				StationsListFragment fragmentStationList = (StationsListFragment) getFragmentManager()
						.findFragmentByTag(STATION_LIST_FRAGMENT_TAG);

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(R.anim.left_swipe_one,
						R.anim.right_swipe_one);
				ft.replace(R.id.fragment_container, fragmentStationList,
						STATION_LIST_FRAGMENT_TAG);
				ft.commitAllowingStateLoss();
			}
		} else if (getFragmentManager().findFragmentByTag(
				STATION_LIST_FRAGMENT_TAG) != null
				&& getFragmentManager().findFragmentByTag(
						STATION_LIST_FRAGMENT_TAG).isVisible()) {
			moveTaskToBack(true);
		} else {
		}
	}
	
	private void setupUI(){
		if (findViewById(R.id.fragment_container) != null
				&& getSupportFragmentManager().findFragmentByTag(
						STATION_LIST_FRAGMENT_TAG) == null) {
			StationsListFragment fragmentStationList = new StationsListFragment();
			FragmentTransaction ft = getFragmentManager()
					.beginTransaction();
			ft.add(R.id.fragment_container, fragmentStationList,
					STATION_LIST_FRAGMENT_TAG);
			fragmentStationList.setArguments(getIntent().getExtras());
			ft.commitAllowingStateLoss();
		} else {
			StationsListFragment fragmentStationList = (StationsListFragment) getFragmentManager()
					.findFragmentByTag(STATION_LIST_FRAGMENT_TAG);

			FragmentTransaction ft = getFragmentManager()
					.beginTransaction();
			ft.replace(R.id.fragment_container, fragmentStationList,
					STATION_LIST_FRAGMENT_TAG);
			ft.commitAllowingStateLoss();
		}			
	}
	
	private void startAlarmManager(){
		
	    AlarmManager alarmManager = (AlarmManager) this
			     .getSystemService(Context.ALARM_SERVICE);
			 
	    Intent alarmIntent = new Intent(this, UpdateStationsIntentService.class);	   	        
	    PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);
	 
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
			   System.currentTimeMillis(), 20000, pending);	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMyListItemClickListener(int pos) {
		Bundle data = new Bundle();
		data.putInt("position", pos);
		if (findViewById(R.id.fragment_container) != null
				&& getSupportFragmentManager().findFragmentByTag(
						MAP_FRAGMENT_TAG) == null) {
			StationsMapFragment fragmentStationMap = new StationsMapFragment();
			android.app.FragmentTransaction ft = getFragmentManager()
					.beginTransaction();
			ft.setCustomAnimations(R.anim.right_swipe_two,
					R.anim.left_swipe_two);
			ft.replace(R.id.fragment_container, fragmentStationMap,
					MAP_FRAGMENT_TAG);
			fragmentStationMap.setArguments(data);
			ft.commitAllowingStateLoss();
		} else {
			StationsMapFragment fragmentStationMap = (StationsMapFragment) getFragmentManager()
					.findFragmentByTag(MAP_FRAGMENT_TAG);
			android.app.FragmentTransaction ft = getFragmentManager()
					.beginTransaction();
			ft.setCustomAnimations(R.anim.right_swipe_two,
					R.anim.left_swipe_two);
			fragmentStationMap.setArguments(data);
			ft.replace(R.id.fragment_container, fragmentStationMap,
					MAP_FRAGMENT_TAG);
			ft.commit();
		}	
	}

	@Override
	public void onConnected(Bundle connectionHint) {		
		// get & update last loc
		Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);		
		DataUtils.myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());	
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

}
