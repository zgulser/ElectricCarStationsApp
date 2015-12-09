package com.example.electriccarstationsapp.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.electriccarstationsapp.R;
import com.example.electriccarstationsapp.model.DataUtils;
import com.example.electriccarstationsapp.model.Station;

public class StationsListAdapter extends RecyclerView.Adapter<StationsListAdapter.MyViewHolder> {

	private LinkedHashMap<String, Station> mDataset;
	WeakReference<StationsListFragment> mParentReferenceFragment = null;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case
    	private ClickListener clickListener;
        public TextView mStationName;
        public TextView mStationShortAdd;
        public TextView mPointCount;
        public TextView mTextViewSpec;
        public ImageView mStatusImageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mStationName = (TextView) itemView.findViewById(R.id.stationName);
            mStationShortAdd = (TextView) itemView.findViewById(R.id.stationShortAddress);
            mPointCount = (TextView) itemView.findViewById(R.id.textViewPointNumber);
            mTextViewSpec = (TextView) itemView.findViewById(R.id.textViewSpec);
            mStatusImageView = (ImageView) itemView.findViewById(R.id.imageViewStatus);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        
		@Override		
		public boolean onLongClick(View v) {
			clickListener.onClick(v, getPosition(), true);
			return true;
		}
		@Override
		public void onClick(View v) {
			clickListener.onClick(v, getPosition(), false);
		}
		
		/* Setter for listener. */
	    public void setClickListener(ClickListener clickListener) {
	        this.clickListener = clickListener;
	    }
	   	
		public interface ClickListener {

	        /**
	         * Called when the view is clicked.
	         *
	         * @param v view that is clicked
	         * @param position of the clicked item
	         * @param isLongClick true if long click, false otherwise
	         */
	        public void onClick(View v, int position, boolean isLongClick);

	    }
    }

    public StationsListAdapter(LinkedHashMap<String, Station> stationLinkedHashMap, StationsListFragment parentFragment) {
        mDataset = stationLinkedHashMap;
        mParentReferenceFragment = new WeakReference<StationsListFragment>(parentFragment);
    }

    @Override
    public StationsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.station_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
    	
    	List<Station> l = new ArrayList<Station>(mDataset.values());
    	Station station = l.get(position);
    	if(station == null){
    		return;
    	}
    	
    	// set name
    	String name = station.getName();
    	String owner = station.getOwner();
    	String operator = station.getOperator();
    	
    	if(operator.length() > 0){
    		owner += " " + operator;
    	}
    	
    	if(name.length() > 0){
    		owner += " " + name;
    	}    	
        holder.mStationName.setText(owner);
        
        // set address
        String address = station.getAddress();
        String city = station.getCity();
        int end = address.length();
        if(end > 20){
        	end = 17;
        }
        address = address.substring(0, end) + "...";
        if(city.length() > 0){
        	address += " - " + city;        		
        }
        holder.mStationShortAdd.setText(address);
        
        // set spec
        String spec = "Volt.: " + station.getVoltage() + "  ";
        spec += "Cur.: " + station.getCurrent() + "  ";        
        holder.mTextViewSpec.setText(spec);
        
        // set active status
        if(!station.isActive()){
        	holder.mStatusImageView.setImageResource(R.drawable.status_inactive);
        	holder.mPointCount.setVisibility(View.INVISIBLE);
        } else {
        	if(station.getStatus().equals(DataUtils.STATION_STATUS_AVAILABLE)){
        		holder.mStatusImageView.setImageResource(R.drawable.status_conv);
        		holder.mPointCount.setText(String.valueOf(station.getFreePointCount()));
        		holder.mPointCount.setVisibility(View.VISIBLE);
        	} else if(station.getStatus().equals(DataUtils.STATION_STATUS_OCCUPIED)){
        		holder.mStatusImageView.setImageResource(R.drawable.status_occupied);
        		holder.mPointCount.setVisibility(View.INVISIBLE);
        	} 
        }                
        
        holder.setClickListener(new MyViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {
                } else {
                	
                	// send the action to the activiy
                	mParentReferenceFragment.get().itemClicked(pos);
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }    
}
