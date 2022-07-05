package com.project.kresnahotspot.model;

public class ManagementUser {
    String poin,email,namaUser,phone,id;
    public ManagementUser(){

    }

    public ManagementUser(String poin, String email, String namaUser, String phone, String id) {
        this.poin = poin;
        this.email = email;
        this.namaUser = namaUser;
        this.phone = phone;
        this.id = id;
    }

    public String getPoin() {
        return poin;
    }

    public void setPoin(String poin) {
        this.poin = poin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
