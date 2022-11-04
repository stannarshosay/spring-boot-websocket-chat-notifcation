package com.sockets.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="location_tb")
public class LocationModel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int location_id;
	private String location;
	
	public LocationModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LocationModel(int location_id, String location) {
		super();
		this.location_id = location_id;
		this.location = location;
	}
	public int getLocation_id() {
		return location_id;
	}
	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
}
