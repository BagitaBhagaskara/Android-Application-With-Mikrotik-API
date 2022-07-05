package com.project.kresnahotspot.model;

public class HotspotUserMikrotik {
    String server,name,profile,uptime,password;
    public HotspotUserMikrotik(){

    }

    public HotspotUserMikrotik(String server, String name, String profile, String uptime,String password) {
        this.server = server;
        this.name = name;
        this.profile = profile;
        this.uptime = uptime;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }
}
