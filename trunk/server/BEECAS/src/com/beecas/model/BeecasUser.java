package com.beecas.model;

import java.io.Serializable;

import org.joda.time.DateTime;

public class BeecasUser implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private long id;

    private String username;

    private String password;

    private String fullname;

    private DateTime birthday;

    private String email;

    private String country;

    private int gender;

    private int registerService;

    private DateTime registerDate;

    private DateTime updateTime;

    private long money;

    private int vip;

    private int level;

    private String phone;

    private int activateType;

    private int emailIsPrivate;

    private int birthdayIsPrivate;

    private int countryIsPrivate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public DateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(DateTime birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getRegisterService() {
        return registerService;
    }

    public void setRegisterService(int registerService) {
        this.registerService = registerService;
    }

    public DateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(DateTime registerDate) {
        this.registerDate = registerDate;
    }

    public DateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getActivateType() {
        return activateType;
    }

    public void setActivateType(int activateType) {
        this.activateType = activateType;
    }

    public int getEmailIsPrivate() {
        return emailIsPrivate;
    }

    public void setEmailIsPrivate(int emailIsPrivate) {
        this.emailIsPrivate = emailIsPrivate;
    }

    public int getBirthdayIsPrivate() {
        return birthdayIsPrivate;
    }

    public void setBirthdayIsPrivate(int birthdayIsPrivate) {
        this.birthdayIsPrivate = birthdayIsPrivate;
    }

    public int getCountryIsPrivate() {
        return countryIsPrivate;
    }

    public void setCountryIsPrivate(int countryIsPrivate) {
        this.countryIsPrivate = countryIsPrivate;
    }
    
}
