package com.sockets.demo.controllers;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sockets.demo.models.CategoryModel;
import com.sockets.demo.models.SlotsModel;
import com.sockets.demo.repos.AdsRepository;
import com.sockets.demo.repos.CategoryRepository;
import com.sockets.demo.repos.SlotsRepository;

@RestController
@RequestMapping("/myapi")
public class SlotController {
	@Autowired
	private AdsRepository adsRepo;
	@Autowired
	private SlotsRepository slotsRepo;
	@Autowired
	private CategoryRepository categoryRepo; 
	
//	print or if needed insert all date slots in empty slots table referenced by ads_id
//	only use when slot_tb is empty 
	@GetMapping("/generate-slots")
	public List<SlotsModel> getDateSlots() {
       
		String slotStart;
		String slotEnd;
		
		List<Integer> adsList = adsRepo.findAll().stream().map(ads->ads.getAds_id()).collect(Collectors.toList());
		List<SlotsModel> slots = new ArrayList<SlotsModel>();

		
		LocalDate startDate = LocalDate.now().plusDays(10);	
		
		for(int i = 0;i<9;i++) {
			slotStart = startDate.toString();
			slotEnd = startDate.plusDays(10).toString();
			startDate = startDate.plusDays(11);		
			SlotsModel slot = new SlotsModel();
			slot.setStart_date(slotStart);
			slot.setEnd_date(slotEnd);
			slot.setSeasonalPrice("0");
			slot.setSlot_status("AVAILABLE");
			slots.add(slot);
		}
		
				
		return generatePermutations(adsList,slots);
	}
	
    //add slot combinations upon adding categories
	@PostMapping("/add-category")
	public List<SlotsModel> addCategory(@RequestBody CategoryModel categoryModel) {		
		categoryModel = categoryRepo.save(categoryModel);
		int insertedRows = adsRepo.addCombinationsByCategoryId(categoryModel.getCategory_id());
		List<Integer> adsList  = adsRepo.findByCategoryId(categoryModel.getCategory_id()).stream().map(ads->ads.getAds_id()).collect(Collectors.toList()); 
		
		String slotStart;
		String slotEnd;
		
		List<SlotsModel> slots = new ArrayList<SlotsModel>();
		
		LocalDate startDate = LocalDate.now().plusDays(10);		
		
		for(int i = 0;i<9;i++) {
			slotStart = startDate.toString();
			slotEnd = startDate.plusDays(10).toString();
			startDate = startDate.plusDays(11);		
			SlotsModel slot = new SlotsModel();
			slot.setStart_date(slotStart);
			slot.setEnd_date(slotEnd);
			slot.setSeasonalPrice("0");
			slot.setSlot_status("AVAILABLE");
			slots.add(slot);
		}
		
		return generatePermutations(adsList,slots);		
	}
	
	
//	Updating recieved slots status as BOOKED
//	recieves slot ids in current api
//	contains insert into vendor_ad_xref and order_tb
	@PostMapping("/book-slots")
	public List<SlotsModel> bookSlots(@RequestBody List<SlotsModel> bookedSlots) {		
		bookedSlots  = bookedSlots.stream().peek(obj->obj.setSlot_status("BOOKED")).collect(Collectors.toList());		
		slotsRepo.saveAll(bookedSlots);		
		return bookedSlots;
		
//		for getting single adsid objects for reference
//		return slotsRepo.findByAdsId(2);
	}
	
//	get ads image from vendor_ad_xref[not created] from ads_id(location_id,category_id) join slot_tb where slot_status "ACTIVE"[join vendor_ad_xref by slot_id and get image-> not done here]    
	@GetMapping("/get-ads/{location_id}/{category_id}")
	public SlotsModel getAdsByCategoryIdAndLocationId(@PathVariable("location_id") Integer location_id,@PathVariable("category_id") Integer category_id) {
		//should be vendor_ad_xref
		return slotsRepo.fetchActiveSlotByCategoryAndLocation(location_id,category_id);
	}
	
	
	
//	***********************************Scheduler [Every Month at 12:05 PM]*********************************************************
//	remove slots that has status expired and insert corresponding slots with slot_status AVAILABLE where number of deleted rows = number of inserted slots
	@GetMapping("/clean-slots")
	public List<SlotsModel> removeAndInsertNewSlots() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<SlotsModel> newSlots = new ArrayList<>(); 
		List<SlotsModel> slotsToRemove = slotsRepo.fetchAvailableExpiredSlots();
		for(SlotsModel slotToRemove : slotsToRemove){
			String maxDateAsString = slotsRepo.fetchMaxDateForAdId(slotToRemove.getAds_id());
			LocalDate startDate = LocalDate.parse(maxDateAsString,formatter).plusDays(1);
			LocalDate endDate = startDate.plusDays(10);
			SlotsModel slot = new SlotsModel(0, "AVAILABLE",startDate.toString(), endDate.toString(), slotToRemove.getAds_id(), "0");
			newSlots.add(slot);
		}
		slotsRepo.deleteAll(slotsToRemove);
		slotsRepo.saveAll(newSlots);
		return newSlots;
	}
//	*********************************Scheduler Ends [Monthly]*******************************************************************	

	
//	**********************************Scheduler [Everyday at 12:05 PM]**********************************************************
//	update AVAILABLE slots to EXPIRY if start_date comes within 7 days from today [7 days interval for admin approval]
	@GetMapping("/update-expiry-status-avialable")
	public int updateExpiryStatusOfAvailableSlots() {
		int updatedRowCount = slotsRepo.updateExpiryStatusOfAvailableSlots();
		return updatedRowCount;
	}
	
//	update NOT AVAILABLE slots to EXPIRY if end_date exceeds today [ACTIVE,APPROVED,BOOKED status are updated here] 
	@GetMapping("/update-expiry-status-notavailable")
	public int updateExpiryStatusOfNotAvailableSlots(){
		int updatedRowCount = slotsRepo.updateExpiryStatusOfNotAvailableSlots();
		return updatedRowCount;
	}
	
//	update APPROVED slots to ACTIVE if today comes between start_date and end_date [inclusive]
	@GetMapping("/activate-slots")
	public int activateApprovedSlots() {
		int activatedSlotsCount = slotsRepo.activateApprovedSlots();		
		return activatedSlotsCount;
	}
//	*********************************Scheduler Ends [Everyday]*******************************************************************
	
	
	public List<SlotsModel> generatePermutations(List<Integer> adsList, List<SlotsModel> slots) {
		List<SlotsModel> result = new ArrayList<>(); 
		for(SlotsModel slot:slots) {
			for(int adId : adsList) {
				SlotsModel finalSlot = new SlotsModel(0,slot.getSlot_status(),slot.getStart_date(),slot.getEnd_date(),adId, slot.getSeasonalPrice());
				result.add(finalSlot);
			}			
		}
		
//		insert all slot combinations to table 
//		slotsRepo.saveAll(result);
		
		System.out.println("AdsListSize :" + adsList.size());
		System.out.println("SlotsSize :" + slots.size());
		System.out.println("Slots*AdsList Size :" + adsList.size()*slots.size());
		System.out.println("ResultSize :" + result.size());

		return result;
	}
}
