package com.sockets.demo.schedulers;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import com.sockets.demo.models.SlotsModel;
import com.sockets.demo.repos.AdsRepository;
import com.sockets.demo.repos.CategoryRepository;
import com.sockets.demo.repos.SlotsRepository;

@Component
public class ManageSlotsScheduler {
	@Autowired
	private SlotsRepository slotsRepo;
	
	//Everday at 12:05 pm	
	@Scheduled(cron = "0 5 0 * * *")
	public void cronManageSlotEverydayJobSch() {
		  
        System.out.println("Its 12:05, Starting everday slot management....");
        
//	  	update AVAILABLE slots to EXPIRY if start_date comes within 7 days from today [7 days interval for admin approval]
	  	int updatedAvailableRowCount = slotsRepo.updateExpiryStatusOfAvailableSlots();
	  	System.out.println("updatedAvailableRowCount :"+updatedAvailableRowCount);
	  	
//	  	update NOT AVAILABLE slots to EXPIRY if end_date exceeds today [ACTIVE,APPROVED,BOOKED status are updated here] 	  	
	  	int updatedNotAvailableRowCount = slotsRepo.updateExpiryStatusOfNotAvailableSlots();
	  	System.out.println("updatedNotAvailableRowCount :"+updatedNotAvailableRowCount);

//	  	update APPROVED slots to ACTIVE if today comes between start_date and end_date [inclusive]
	  	int activatedSlotsCount = slotsRepo.activateApprovedSlots();
	  	System.out.println("activatedSlotsCount :"+activatedSlotsCount);     
      	
	      
	 }
	
	//1st day of every month at 12:05 pm 	
	@Scheduled(cron = "0 5 0 1 1/1 *")
	public void cronManageSlotMonthlyJobSch() {	  		
	      
	    System.out.println("Its 12:05, Starting monthly slot management....");
	      
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

	}
	
	
}
