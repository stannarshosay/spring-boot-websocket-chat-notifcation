package com.sockets.demo.repos;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.sockets.demo.models.AdsModel;

public interface AdsRepository extends JpaRepository<AdsModel, Integer>{
	
	@Modifying
	@Transactional
	@Query(value="INSERT INTO ads_tb ( category_id, location_id, price) SELECT DISTINCT ?1 AS category_id, l.location_id,'500' FROM category_tb c,location_tb l",nativeQuery = true)
	int addCombinationsByCategoryId(int categoryId);
	
	@Query(value="SELECT * FROM ads_tb WHERE category_id = ?1",nativeQuery = true)
	List<AdsModel> findByCategoryId(int categoryId);
}
