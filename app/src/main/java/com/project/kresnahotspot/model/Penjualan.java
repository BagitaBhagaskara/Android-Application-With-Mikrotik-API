package com.project.kresnahotspot.model;

public class Penjualan {
    String idVoucher, usernameHotspot, passwordHotspot, tanggalPembelian, idPenjualan;

    public Penjualan(){


    }

    public Penjualan(String idVoucher, String usernameHotspot, String passwordHotspot, String tanggalPembelian, String idPenjualan) {
        this.idVoucher = idVoucher;
        this.usernameHotspot = usernameHotspot;
        this.passwordHotspot = passwordHotspot;
        this.tanggalPembelian = tanggalPembelian;
        this.idPenjualan = idPenjualan;
    }

    public String getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(String idVoucher) {
        this.idVoucher = idVoucher;
    }

    public String getUsernameHotspot() {
        return usernameHotspot;
    }

    public void setUsernameHotspot(String usernameHotspot) {
        this.usernameHotspot = usernameHotspot;
    }

    public String getPasswordHotspot() {
        return passwordHotspot;
    }

    public void setPasswordHotspot(String passwordHotspot) {
        this.passwordHotspot = passwordHotspot;
    }

    public String getTanggalPembelian() {
        return tanggalPembelian;
    }

    public void setTanggalPembelian(String tanggalPembelian) {
        this.tanggalPembelian = tanggalPembelian;
    }

    public String getIdPenjualan() {
        return idPenjualan;
    }

    public void setIdPenjualan(String idPenjualan) {
        this.idPenjualan = idPenjualan;
    }
}
