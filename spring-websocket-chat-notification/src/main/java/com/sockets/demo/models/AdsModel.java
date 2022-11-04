package com.sockets.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ads_tb")
public class AdsModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ads_id;
	private String price;
	private int category_id;
	private int location_id;
	
	public AdsModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AdsModel(int ads_id, String price, int category_id, int location_id) {
		super();
		this.ads_id = ads_id;
		this.price = price;
		this.category_id = category_id;
		this.location_id = location_id;
	}
	public int getAds_id() {
		return ads_id;
	}
	public void setAds_id(int ads_id) {
		this.ads_id = ads_id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public int getLocation_id() {
		return location_id;
	}
	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
	
}
