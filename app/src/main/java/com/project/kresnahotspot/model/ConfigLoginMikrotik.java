package com.project.kresnahotspot.model;

public class ConfigLoginMikrotik {
    String connect, username, password;
    public ConfigLoginMikrotik(){

    }

    public ConfigLoginMikrotik(String connect, String username, String password) {
        this.connect = connect;
        this.username = username;
        this.password = password;
    }

    public String getConnect() {
        return connect;
    }

    public void setConnect(String connect) {
        this.connect = connect;
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
}
