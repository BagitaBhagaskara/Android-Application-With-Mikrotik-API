package com.project.kresnahotspot.model;

public class ProfileHotspotAdmin_model {
    String namaProfile, rateLimit, server, shareUser,uptime,idProfile;
    public ProfileHotspotAdmin_model(){

    }

    public ProfileHotspotAdmin_model(String namaProfile, String rateLimit, String server, String shareUser, String uptime, String idProfile) {
        this.namaProfile = namaProfile;
        this.rateLimit = rateLimit;
        this.server = server;
        this.shareUser = shareUser;
        this.uptime = uptime;
        this.idProfile = idProfile;
    }

    public String getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(String idProfile) {
        this.idProfile = idProfile;
    }

    public String getNamaProfile() {
        return namaProfile;
    }

    public void setNamaProfile(String namaProfile) {
        this.namaProfile = namaProfile;
    }

    public String getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(String rateLimit) {
        this.rateLimit = rateLimit;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getShareUser() {
        return shareUser;
    }

    public void setShareUser(String shareUser) {
        this.shareUser = shareUser;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }
}
