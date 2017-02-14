package com.caragiz_studios.talks.schmooze.helper.db.firebase;

/**
 * Created by caragiz on 13/2/17.
 */

public class UserDataModelFB {
    String username;
    String userid;
    boolean requested;
    String requestFrom;
    boolean connected;
    String registerNumber;

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public UserDataModelFB() {
    }

    public UserDataModelFB(String userid, String username, String registerNumber) {
        this.userid = userid;
        this.username = username;
        this.requestFrom = "";
        this.registerNumber = registerNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public String getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
