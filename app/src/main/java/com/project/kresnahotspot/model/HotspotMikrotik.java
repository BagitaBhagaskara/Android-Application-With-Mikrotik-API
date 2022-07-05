package com.project.kresnahotspot.model;

public class HotspotMikrotik {
    String jumlahUserActive, jumlahHotspotUsers;
    public HotspotMikrotik(){

    }

    public HotspotMikrotik(String jumlahUserActive, String jumlahHotspotUsers) {
        this.jumlahUserActive = jumlahUserActive;
        this.jumlahHotspotUsers = jumlahHotspotUsers;
    }

    public String getJumlahUserActive() {
        return jumlahUserActive;
    }

    public void setJumlahUserActive(String jumlahUserActive) {
        this.jumlahUserActive = jumlahUserActive;
    }

    public String getJumlahHotspotUsers() {
        return jumlahHotspotUsers;
    }

    public void setJumlahHotspotUsers(String jumlahHotspotUsers) {
        this.jumlahHotspotUsers = jumlahHotspotUsers;
    }
}
