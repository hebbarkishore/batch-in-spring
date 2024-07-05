package com.spring.batch.configuration;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.batch.model.CarDetails;
import com.spring.batch.repo.CarDetailsRepo;

@Component
public class ItemWriter implements org.springframework.batch.item.ItemWriter<CarDetails>{

	@Autowired
	CarDetailsRepo shareTransactionRepo;
	
	@Override
	public void write(List<? extends CarDetails> items) throws Exception {
		System.out.println("write");
		shareTransactionRepo.saveAll(items);
		System.out.println("List of size:"+items.size());
	}

}
