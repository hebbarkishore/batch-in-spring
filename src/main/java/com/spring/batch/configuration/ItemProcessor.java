package com.spring.batch.configuration;

import org.springframework.stereotype.Component;

import com.spring.batch.model.CarDetails;

@Component
public class ItemProcessor implements org.springframework.batch.item.ItemProcessor<CarDetails, CarDetails>{
	public CarDetails process(CarDetails carDetails) throws Exception {
		if( Integer.parseInt(carDetails.getYear()) < 2009) {
			carDetails.setGoodCondition("NO");
		} else 
			carDetails.setGoodCondition("YES");
		
		return carDetails;
	}
}