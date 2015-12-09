/**
 * 
 * Author: Zeki Gulser
 * 
 * Desc: Represents a station
 *  
 */

package com.example.electriccarstationsapp.model;

import java.util.Iterator;

import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;

public class Station {
		
	private String postalCode = null;
	private String city = null;
	private String openDate = null;
	private String modifiedDate = null;
	private String name = null;
	private String address = null;
	private String owner = null;
	private String operator = null;
	private String status = null;
	private int voltage = 0;
	private int current = 0;
	private int power = 0;
	SparseArray<Point> points = new SparseArray<Point>();
	private LatLng latLong = null;	
	private boolean isActive = true;
	
	public Station(String postalCode, String city, String openDate, String modifiedDate,
			String name, String address, String owner, String operator, String status, SparseArray<Point> points, int voltage,
			int current, int power, LatLng latLong, boolean isActive) {
		super();
		this.postalCode = postalCode;
		this.city = city;
		this.openDate = openDate;
		this.modifiedDate = modifiedDate;
		this.name = name;
		this.address = address;
		this.owner = owner;
		this.operator = operator;
		this.status = status;
		this.points = points;
		this.voltage = voltage;
		this.current = current;
		this.power = power;
		this.latLong = latLong;
		this.isActive = isActive;
	}	
	
	public String getStatus() {
		
		if(status == null){
			return "";
		}
		
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getModifiedDate() {
		
		if(modifiedDate == null){
			return "";
		}
		
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {				
		this.modifiedDate = modifiedDate;
	}

	public SparseArray<Point> getPoints() {
		return points;
	}

	public void setPoints(SparseArray<Point> points) {
		this.points = points;
	}
			
	public String getOperator() {
		if(operator == null){
			return "";
		}
		
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Station() {
		
	}

	public String getPostalCode() {
		if(postalCode == null){
			return "";
		}
		
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		if(city == null){
			return "";
		}
		
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOpenDate() {
		if(openDate == null){
			return "";
		}
		
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getName() {
		if(name == null){
			return "";
		}
		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		if(address == null){
			return "";
		}
		
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOwner() {
		if(owner == null){
			return "";
		}
		
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getVoltage() {
		return voltage;
	}

	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public LatLng getLatLong() {
		return latLong;
	}

	public void setLatLong(LatLng latLong) {
		this.latLong = latLong;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public int getFreePointCount(){
		int count = 0;
		for(int i=0; i < getPoints().size();i++){			
			if(getPoints().get(i) != null &&
					getPoints().get(i).getStatus().equals(DataUtils.POINT_STATUS_FREE)){
				count++;				
			}
		}
		
		return count;
	}

}
