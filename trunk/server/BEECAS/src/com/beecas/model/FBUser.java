package com.beecas.model;

import java.io.Serializable;

import org.joda.time.DateTime;

public class FBUser implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private long id;

    private long beecasId;

    private String fbUsername;

    private String fbPassword;

    private String fullname;

    private int gender;

    private DateTime birthday;

    private String locale;

    private String email;

    private DateTime updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBeecasId() {
        return beecasId;
    }

    public void setBeecasId(long beecasId) {
        this.beecasId = beecasId;
    }

    public String getFbUsername() {
        return fbUsername;
    }

    public void setFbUsername(String fbUsername) {
        this.fbUsername = fbUsername;
    }

    public String getFbPassword() {
        return fbPassword;
    }

    public void setFbPassword(String fbPassword) {
        this.fbPassword = fbPassword;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public DateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(DateTime birthday) {
        this.birthday = birthday;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }

}
