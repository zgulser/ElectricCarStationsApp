package com.example.electriccarstationsapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.electriccarstationsapp.R;
import com.example.electriccarstationsapp.model.DataUtils;
import com.example.electriccarstationsapp.model.Station;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class StationsMapFragment extends Fragment implements OnInfoWindowClickListener{

    private MapView stationsMapView;
    private GoogleMap stationsMap;
	Marker stationMarker = null;
	Station mapStation = null;
    private Bundle mBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.maps_fragment, container, false);

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            // TODO handle this situation
        }

        stationsMapView = (MapView) inflatedView.findViewById(R.id.map);
        stationsMapView.onCreate(mBundle);
        
        setUpMapIfNeeded(inflatedView);

        return inflatedView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
        getActivity().getActionBar().hide();
    }

    private void setUpMapIfNeeded(View inflatedView) {
        if (stationsMap == null) {
        	stationsMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
        	stationsMap.setMyLocationEnabled(true);
            if (stationsMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
    	//stationsMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    	int pos = getArguments().getInt("position");
		mapStation = (Station) DataUtils.stationList.values().toArray()[pos];
		if(mapStation == null ){
			return;
		}
		
		LatLng coordinate = mapStation.getLatLong();
    	String name = mapStation.getName();
    	String owner = mapStation.getOwner();
    	String operator = mapStation.getOperator();
    	
    	if(operator.length() > 0){
    		owner += " " + operator;
    	}
    	
    	if(name.length() > 0){
    		owner += " " + name;
    	}    	
    	
        String spec = "Volt.: " + mapStation.getVoltage() + "  ";
        spec += "Cur.: " + mapStation.getCurrent() + ", ";        
        spec += mapStation.getFreePointCount() + " free station";
    	
    	int drawableId = 0;
        if(!mapStation.isActive()){
        	drawableId = R.drawable.marker_station_inactive;       	
        } else {
        	if(mapStation.getStatus().equals(DataUtils.STATION_STATUS_AVAILABLE)){
        		drawableId = R.drawable.marker_station_conv;
        	} else if(mapStation.getStatus().equals(DataUtils.STATION_STATUS_OCCUPIED)){
        		drawableId = R.drawable.marker_station_busy;
        	} 
        } 			    	    	
    			
		stationMarker = stationsMap.addMarker(new MarkerOptions()
        	.title(owner + "\n\n")                                                  
        	.position(coordinate)                                                       
        	.snippet(spec)                          
        	.draggable(false)
        	.visible(true)
        	.icon(BitmapDescriptorFactory.fromResource(drawableId)));
        			
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
		stationsMap.animateCamera(yourLocation);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().hide();
        stationsMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        stationsMapView.onPause();
    }

    @Override
    public void onDestroy() {
    	stationsMapView.onDestroy();
        super.onDestroy();
    }

	@Override
	public void onInfoWindowClick(Marker marker) {
		
	}
}