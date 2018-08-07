package com.view.wheel.area.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author yline 2018/3/5 -- 15:34
 * @version 1.0.0
 */
public class Province implements Serializable {
	public String name;
	public List<City> city;
	
	public Province() {
	}
	
	public Province(String name, List<City> city) {
		this.name = name;
		this.city = city;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<City> getCity() {
		return city;
	}
	
	public void setCity(List<City> city) {
		this.city = city;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
