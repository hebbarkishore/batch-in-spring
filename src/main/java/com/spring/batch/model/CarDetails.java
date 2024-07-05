package com.spring.batch.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="car_details")
@Entity
public class CarDetails {
	@Id
	private Integer carId;
	private String make;
	private String model;
	private String year;
	private String goodCondition;
	public Integer getCarId() {
		return carId;
	}
	public void setCarId(Integer carId) {
		this.carId = carId;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getGoodCondition() {
		return goodCondition;
	}
	public void setGoodCondition(String goodCondition) {
		this.goodCondition = goodCondition;
	}
	
	
	
	
}