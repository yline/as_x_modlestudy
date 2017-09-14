package com.sqlite.green.test;

import java.io.Serializable;
import java.util.List;

/**
 * 中等复杂度的Model (带有List)
 *
 * @author yline 2017/9/14 -- 14:54
 * @version 1.0.0
 */
public class XMediumModel implements Serializable {
    private String userId;

    private List<String> userList;

    private List<Integer> userIntegerList;

    public XMediumModel(String userId, List<String> userList, List<Integer> userIntegerList) {
        this.userId = userId;
        this.userList = userList;
        this.userIntegerList = userIntegerList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public List<Integer> getUserIntegerList() {
        return userIntegerList;
    }

    public void setUserIntegerList(List<Integer> userIntegerList) {
        this.userIntegerList = userIntegerList;
    }
}
