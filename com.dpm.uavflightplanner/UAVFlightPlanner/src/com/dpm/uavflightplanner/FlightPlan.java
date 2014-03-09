package com.dpm.uavflightplanner;

import java.util.ArrayList;

class FlightPlan{
	
	class Waypoint {
		private String name;
		private int seq_number;
	    private float latitude;
	    private float longitude;
	    private float altitude;
	    private int deltat;

	    Waypoint(String n, int seq, float lat, float lng, float alt, int delta){
	    	
	    	System.out.println( " THIS IS THE: " + n );
	    	this.name = n;
	    	this.seq_number = seq;
	    	this.latitude = lat;
	    	this.longitude = lng;
	    	this.altitude = alt;
	    	this.deltat = delta;
	    }
	}
	
	private String name;
	private ArrayList<Waypoint> waypoint = new ArrayList<Waypoint>();
	
	FlightPlan(String n){
		this.name=n;
	}
	
	public void addWaypoint(String n, int seq, float lat, float lng, float alt, int delta){
		
		Waypoint wp = new Waypoint( n, seq, lat, lng, alt, delta);
		this.waypoint.add(wp);
		
	}
}

