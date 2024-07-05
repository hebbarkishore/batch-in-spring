package com.spring.batch.repo;

import org.springframework.data.repository.CrudRepository;

import com.spring.batch.model.CarDetails;

public interface CarDetailsRepo extends CrudRepository<CarDetails, Integer> {

}
