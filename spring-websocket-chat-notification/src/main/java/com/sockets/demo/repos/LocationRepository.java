package com.sockets.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sockets.demo.models.LocationModel;

public interface LocationRepository extends JpaRepository<LocationModel, Integer> {

}
