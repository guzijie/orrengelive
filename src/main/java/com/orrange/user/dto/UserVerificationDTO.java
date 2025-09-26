package com.orrange.user.dto;

public class UserVerificationDTO {
    private String phone;
    // register | login
    private String scene;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }
}


