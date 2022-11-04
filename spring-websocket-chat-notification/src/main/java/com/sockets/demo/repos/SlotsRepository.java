package com.sockets.demo.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.sockets.demo.models.SlotsModel;

public interface SlotsRepository extends JpaRepository<SlotsModel, Integer> {

   @Query(value="SELECT * FROM slots_tb WHERE slot_status = 'EXPIRED'",nativeQuery = true)
   List<SlotsModel> fetchAvailableExpiredSlots();
   
   @Modifying
   @Transactional
   @Query(value="UPDATE slots_tb SET slot_status = 'EXPIRED' WHERE CURDATE() + INTERVAL 7 DAY >= STR_TO_DATE(start_date, '%Y-%m-%d') AND slot_status = 'AVAILABLE'",nativeQuery = true)
   int updateExpiryStatusOfAvailableSlots();
   
   @Modifying
   @Transactional
   @Query(value="UPDATE slots_tb SET slot_status = 'EXPIRED' WHERE CURDATE() > STR_TO_DATE(end_date, '%Y-%m-%d') AND slot_status != 'AVAILABLE'",nativeQuery = true)
   int updateExpiryStatusOfNotAvailableSlots();
      
   @Modifying
   @Transactional
   @Query(value="UPDATE slots_tb SET slot_status = 'ACTIVE' WHERE CURDATE() >= STR_TO_DATE(start_date, '%Y-%m-%d') AND CURDATE() <= STR_TO_DATE(end_date, '%Y-%m-%d') AND slot_status = 'APPROVED'",nativeQuery = true)
   int activateApprovedSlots();
   
   @Query(value="SELECT  DATE_FORMAT(MAX(end_date),'%Y-%m-%d') FROM slots_tb WHERE ads_id = ?1",nativeQuery = true)
   String fetchMaxDateForAdId(Integer adsId);
   
   @Query(value="SELECT * FROM slots_tb WHERE ads_id = ?1",nativeQuery = true)
   List<SlotsModel> findByAdsId(int ads_id);
   
   @Query(value="SELECT * FROM slots_tb s JOIN ads_tb a ON(s.ads_id = a.ads_id) WHERE a.location_id = ?1 AND a.category_id = ?2 AND s.slot_status = 'ACTIVE'",nativeQuery = true)
   SlotsModel fetchActiveSlotByCategoryAndLocation(int location_id,int category_id);
}
