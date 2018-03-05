package com.view.wheel.area.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author yline 2018/3/5 -- 15:34
 * @version 1.0.0
 */
public class City implements Serializable {
    public String name;
    public List<String> area;

    public City() {
    }

    public City(String name, List<String> area) {
        this.name = name;
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }
}
