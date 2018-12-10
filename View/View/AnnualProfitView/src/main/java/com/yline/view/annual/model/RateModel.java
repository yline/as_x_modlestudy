package com.yline.view.annual.model;

import java.io.Serializable;

public class RateModel implements Serializable {
	private static final long serialVersionUID = -7379478080225881129L;
	
	private String benefitDate;
	private float ratePerWeek;
	
	public RateModel(String benefitDate, float ratePerWeek) {
		this.benefitDate = benefitDate;
		this.ratePerWeek = ratePerWeek;
	}
	
	public String getBenefitDate() {
		return benefitDate;
	}
	
	public void setBenefitDate(String benefitDate) {
		this.benefitDate = benefitDate;
	}
	
	public float getRatePerWeek() {
		return ratePerWeek;
	}
	
	public void setRatePerWeek(float ratePerWeek) {
		this.ratePerWeek = ratePerWeek;
	}
}
