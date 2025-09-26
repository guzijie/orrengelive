package com.orrange.admin.dto;

public class AdminLoginDTO {
    private String loginType; // password | sms
    private String phone;
    private String password; // when loginType=password
    private String code;     // when loginType=sms

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
