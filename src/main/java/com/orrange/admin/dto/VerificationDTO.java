package com.orrange.admin.dto;

public class VerificationDTO {
    private String phone;
    private String scene; // register | login
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getScene() { return scene; }
    public void setScene(String scene) { this.scene = scene; }
} 