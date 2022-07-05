package com.project.kresnahotspot.model;

public class Voucher {
    String nama, kecepatan, durasi, harga, id,idConfig,configUptime,configProfile;

    public Voucher(){

    }

    public Voucher(String nama, String kecepatan, String durasi, String harga,String idConfig) {
        this.nama = nama;
        this.kecepatan = kecepatan;
        this.durasi = durasi;
        this.harga = harga;
        this.idConfig=idConfig;
    }

    public String getConfigUptime() {
        return configUptime;
    }

    public void setConfigUptime(String configUptime) {
        this.configUptime = configUptime;
    }

    public String getConfigProfile() {
        return configProfile;
    }

    public void setConfigProfile(String configProfile) {
        this.configProfile = configProfile;
    }

    public String getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(String idConfig) {
        this.idConfig = idConfig;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKecepatan() {
        return kecepatan;
    }

    public void setKecepatan(String kecepatan) {
        this.kecepatan = kecepatan;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
