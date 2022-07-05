package com.project.kresnahotspot.model;

public class alatPembayaranModel {
    String mediaPembayaran, nomorPembayaran, atasNama, idAlatPembayaran;
    public alatPembayaranModel(){

    }

    public alatPembayaranModel(String mediaPembayaran, String nomorPembayaran, String atasNama, String idAlatPembayaran) {
        this.mediaPembayaran = mediaPembayaran;
        this.nomorPembayaran = nomorPembayaran;
        this.atasNama = atasNama;
        this.idAlatPembayaran = idAlatPembayaran;
    }

    public String getMediaPembayaran() {
        return mediaPembayaran;
    }

    public void setMediaPembayaran(String mediaPembayaran) {
        this.mediaPembayaran = mediaPembayaran;
    }

    public String getNomorPembayaran() {
        return nomorPembayaran;
    }

    public void setNomorPembayaran(String nomorPembayaran) {
        this.nomorPembayaran = nomorPembayaran;
    }

    public String getAtasNama() {
        return atasNama;
    }

    public void setAtasNama(String atasNama) {
        this.atasNama = atasNama;
    }

    public String getIdAlatPembayaran() {
        return idAlatPembayaran;
    }

    public void setIdAlatPembayaran(String idAlatPembayaran) {
        this.idAlatPembayaran = idAlatPembayaran;
    }
}
