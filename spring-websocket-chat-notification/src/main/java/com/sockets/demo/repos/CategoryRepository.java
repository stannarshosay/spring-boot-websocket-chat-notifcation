package com.sockets.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sockets.demo.models.CategoryModel;

public interface CategoryRepository extends JpaRepository<CategoryModel, Integer> {

}
