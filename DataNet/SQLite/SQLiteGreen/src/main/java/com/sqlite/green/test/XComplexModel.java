package com.sqlite.green.test;

import java.io.Serializable;
import java.util.List;

/**
 * 高等复杂度的Model (带有其它Model，其它Model中含有List)
 * @author yline 2017/9/14 -- 14:54
 * @version 1.0.0
 */
public class XComplexModel implements Serializable{
    private String userName;

    private int id;

    private long time;

    private List<String> strList;

    private List<SimpleModel> simpleModelList;

    private List<XMediumModel> mediumModelList;

    public XComplexModel(String userName, int id, long time, List<String> strList, List<SimpleModel> simpleModelList, List<XMediumModel> mediumModelList) {
        this.userName = userName;
        this.id = id;
        this.time = time;
        this.strList = strList;
        this.simpleModelList = simpleModelList;
        this.mediumModelList = mediumModelList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<String> getStrList() {
        return strList;
    }

    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    public List<SimpleModel> getSimpleModelList() {
        return simpleModelList;
    }

    public void setSimpleModelList(List<SimpleModel> simpleModelList) {
        this.simpleModelList = simpleModelList;
    }

    public List<XMediumModel> getMediumModelList() {
        return mediumModelList;
    }

    public void setMediumModelList(List<XMediumModel> mediumModelList) {
        this.mediumModelList = mediumModelList;
    }
}
