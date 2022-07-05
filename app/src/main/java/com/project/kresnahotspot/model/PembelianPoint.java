package com.project.kresnahotspot.model;

public class PembelianPoint {
    String idPembelianPoint,jumlahPoint, metodePembayaran,tanggal,status,idUser,namaUser,url,alasanTolak;
    public PembelianPoint(){

    }

    public PembelianPoint(String idPembelianPoint, String jumlahPoint, String metodePembayaran, String tanggal, String status, String url,String alasanTolak) {
        this.idPembelianPoint = idPembelianPoint;
        this.jumlahPoint = jumlahPoint;
        this.metodePembayaran = metodePembayaran;
        this.tanggal = tanggal;
        this.status = status;
        this.url = url;
        this.alasanTolak=alasanTolak;
    }

    public String getAlasanTolak() {
        return alasanTolak;
    }

    public void setAlasanTolak(String alasanTolak) {
        this.alasanTolak = alasanTolak;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdPembelianPoint() {
        return idPembelianPoint;
    }

    public void setIdPembelianPoint(String idPembelianPoint) {
        this.idPembelianPoint = idPembelianPoint;
    }

    public String getJumlahPoint() {
        return jumlahPoint;
    }

    public void setJumlahPoint(String jumlahPoint) {
        this.jumlahPoint = jumlahPoint;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }
}
