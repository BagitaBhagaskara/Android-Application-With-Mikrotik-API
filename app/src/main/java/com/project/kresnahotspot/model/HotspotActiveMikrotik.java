package com.project.kresnahotspot.model;

public class HotspotActiveMikrotik {
    String server, user, uptime, idleTime,sessionTime,macAddress;
    public HotspotActiveMikrotik(){

    }

    public HotspotActiveMikrotik(String server, String user, String uptime, String idleTime, String sessionTime, String macAddress) {
        this.server = server;
        this.user = user;
        this.uptime = uptime;
        this.idleTime = idleTime;
        this.sessionTime = sessionTime;
        this.macAddress = macAddress;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(String idleTime) {
        this.idleTime = idleTime;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
