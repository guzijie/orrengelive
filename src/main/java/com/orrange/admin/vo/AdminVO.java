package com.orrange.admin.vo;

import java.util.Date;

public class AdminVO {
    private String phone;
    private String name;
    private String avatar;
    private String gender;
    private Date birthday;
    private String education;
    private String politicalStatus;
    private String address;
    private Date validFrom;
    private Date validTo;
    private String organization;
    private String position;
    // getter/setter
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getPoliticalStatus() { return politicalStatus; }
    public void setPoliticalStatus(String politicalStatus) { this.politicalStatus = politicalStatus; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Date getValidFrom() { return validFrom; }
    public void setValidFrom(Date validFrom) { this.validFrom = validFrom; }
    public Date getValidTo() { return validTo; }
    public void setValidTo(Date validTo) { this.validTo = validTo; }
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
} 