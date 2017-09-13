package com.sqlite.green.test;

public class Msg {
    private String userId;

    private String userName;

    private String userNickName;

    private String phoneNumber;

    private String phoneTag;

    public Msg(String userId, String userName, String userNickName, String phoneNumber, String phoneTag) {
        this.userId = userId;
        this.userName = userName;
        this.userNickName = userNickName;
        this.phoneNumber = phoneNumber;
        this.phoneTag = phoneTag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneTag() {
        return phoneTag;
    }

    public void setPhoneTag(String phoneTag) {
        this.phoneTag = phoneTag;
    }
}
