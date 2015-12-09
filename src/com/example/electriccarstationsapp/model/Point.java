/**
 * 
 * Author: Zeki Gulser
 * 
 * Desc: Represents position of a base
 *  
 */

package com.example.electriccarstationsapp.model;

import com.google.android.gms.maps.model.LatLng;

public class Point {
		
	// stable values
	private String status = null;
	private String name = null;
				
	public Point(String status, String name) {
		this.status = status;
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	

}
