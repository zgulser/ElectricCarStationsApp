package com.example.electriccarstationsapp.ui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.AsyncQueryHandler;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.electriccarstationsapp.R;
import com.example.electriccarstationsapp.model.DataUtils;
import com.example.electriccarstationsapp.model.DataUtils.FilterType;

public class StationsListFragment extends Fragment implements OnNavigationListener
{
	OnMyListItemClickListener mClickListener;
	private RecyclerView stationsRecyclerView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.Adapter stationsAdapter;
    private RecyclerView.LayoutManager stationsLayoutManager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {    
    	super.onCreate(savedInstanceState);
    	setHasOptionsMenu(true);
    	getActivity().getActionBar().setTitle(getResources().getString(R.string.text_list_Fragment_title));
    	getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    	
    	ArrayList<String> itemList = new ArrayList<String>();
    	itemList.add(getResources().getString(R.string.text_list_Fragment_filter_all));
    	itemList.add(getResources().getString(R.string.text_list_Fragment_filter_around_1));
    	itemList.add(getResources().getString(R.string.text_list_Fragment_filter_around_5));
    	itemList.add(getResources().getString(R.string.text_list_Fragment_filter_aval));
    	itemList.add(getResources().getString(R.string.text_list_Fragment_filter_occ));
    	ArrayAdapter<String> aAdpt = new ArrayAdapter<String>(getActivity(), R.layout.station_filter_item_layout, R.id.filterName, itemList);
    	
    	getActivity().getActionBar().setListNavigationCallbacks(aAdpt, this);	

    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{		
		View rowView =  inflater.inflate(R.layout.stationlist_fragment, container, false);		
		return rowView;
	}
	
	@Override
	public void onResume() {	
		super.onResume();
		getActivity().getActionBar().show();
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {	
		super.onViewCreated(view, savedInstanceState);
		
		stationsRecyclerView = (RecyclerView) view.findViewById(R.id.stations_recycler_view);
		stationsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
		
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				
				new AsyncTask<Void, Void, Void>(){

					@Override
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
					
					@Override
					protected void onPostExecute(Void result) {
						mSwipeRefreshLayout.setRefreshing(false);
					};
					
				}.execute();
			}
		});
		
		stationsLayoutManager = new LinearLayoutManager(getActivity());
		stationsRecyclerView.setLayoutManager(stationsLayoutManager);
					
		stationsAdapter = new StationsListAdapter(DataUtils.stationList, this);
		stationsRecyclerView.setAdapter(stationsAdapter);		
	}
	
	public void updateRecyclerView(){
		switch (DataUtils.getCurrentFilter()) {
		case FILTER_ALL:
			stationsAdapter = new StationsListAdapter(DataUtils.stationList, this);
			break;
		case FILTER_1km:
			stationsAdapter = new StationsListAdapter(DataUtils.stationListNear1km, this);
			break;						
		case FILTER_5km:
			stationsAdapter = new StationsListAdapter(DataUtils.stationListNear5km, this);
			break;				
		case FILTER_AVAILABLE:
			stationsAdapter = new StationsListAdapter(DataUtils.stationListAval, this);
			break;						
		case FILTER_OCCUPIED:
			stationsAdapter = new StationsListAdapter(DataUtils.stationListOcc, this);
			break;						
		default:
			break;
		}
		
		stationsRecyclerView.swapAdapter(stationsAdapter, false);
	}
	
	private RecyclerView findViewById(int stationsRecyclerView2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public interface OnMyListItemClickListener
	{
		public void onMyListItemClickListener(int pos);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		super.onCreateOptionsMenu(menu, inflater);
		getActivity().getMenuInflater().inflate(R.menu.station_list_frag_menu, menu);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//mListener.onMyListContextMenuItemClicked("");
		return super.onContextItemSelected(item);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
    	
    	if (activity instanceof OnMyListItemClickListener)
    	{
    		mClickListener = (OnMyListItemClickListener) activity;
    	}
    	else 
    	{
    	      throw new ClassCastException(activity.toString()
    	          + " must implement OnMyListItemClickListener");
        }
	}
	
	public void itemClicked(int pos){
		mClickListener.onMyListItemClickListener(pos);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		switch(itemPosition){
		case 0: // all
			DataUtils.setCurrentFilter(FilterType.FILTER_ALL);
			stationsAdapter = new StationsListAdapter(DataUtils.stationList, this);
			break;
		case 1: // available
			DataUtils.setCurrentFilter(FilterType.FILTER_1km);
			stationsAdapter = new StationsListAdapter(DataUtils.stationListNear1km, this);			
			break;
		case 2: // occupied
			DataUtils.setCurrentFilter(FilterType.FILTER_5km);
			stationsAdapter = new StationsListAdapter(DataUtils.stationListNear5km, this);			
			break;			
		case 3: // available
			DataUtils.setCurrentFilter(FilterType.FILTER_AVAILABLE);
			stationsAdapter = new StationsListAdapter(DataUtils.stationListAval, this);			
			break;
		case 4: // occupied
			DataUtils.setCurrentFilter(FilterType.FILTER_OCCUPIED);
			stationsAdapter = new StationsListAdapter(DataUtils.stationListOcc, this);			
			break;
		default:
			break;
		}		
		
		stationsRecyclerView.swapAdapter(stationsAdapter, false);
		return false;
	}
	
}
