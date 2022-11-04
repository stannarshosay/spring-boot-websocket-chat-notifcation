package com.sockets.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "slots_tb")
public class SlotsModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int slot_id;
	private String slot_status;
	private String start_date;
	private String end_date;
	private int ads_id;
	private String seasonal_price;
	
	public SlotsModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SlotsModel(int slot_id, String slot_status, String start_date, String end_date, int ads_id, String seasonal_price) {
		super();
		this.slot_id = slot_id;
		this.slot_status = slot_status;
		this.start_date = start_date;
		this.end_date = end_date;
		this.ads_id = ads_id;
		this.seasonal_price = seasonal_price;
	}
	public int getSlot_id() {
		return slot_id;
	}
	public void setSlot_id(int slot_id) {
		this.slot_id = slot_id;
	}
	public String getSlot_status() {
		return slot_status;
	}
	public void setSlot_status(String slot_status) {
		this.slot_status = slot_status;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public int getAds_id() {
		return ads_id;
	}
	public void setAds_id(int ads_id) {
		this.ads_id = ads_id;
	}
	public String getSeasonalPrice() {
		return seasonal_price;
	}
	public void setSeasonalPrice(String seasonal_price) {
		this.seasonal_price = seasonal_price;
	}
	
}
