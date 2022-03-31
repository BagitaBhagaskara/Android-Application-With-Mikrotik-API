package com.project.kresnahotspot.model;

public class ReadResourceMikrotik {
    String cpuLoad, uptime, memory, harddisk;
    public  ReadResourceMikrotik(){

    }

    public ReadResourceMikrotik(String cpuLoad, String uptime, String memory, String harddisk) {
        this.cpuLoad = cpuLoad;
        this.uptime = uptime;
        this.memory = memory;
        this.harddisk = harddisk;
    }

    public String getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(String cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getHarddisk() {
        return harddisk;
    }

    public void setHarddisk(String harddisk) {
        this.harddisk = harddisk;
    }
}
