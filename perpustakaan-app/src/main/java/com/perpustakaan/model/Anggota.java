package com.perpustakaan.model;

public class Anggota {

    private int idAnggota;
    private String nama;
    private String alamat;
    private String noHp;

    public Anggota(int idAnggota, String nama, String alamat, String noHp) {
        this.idAnggota = idAnggota;
        this.nama = nama;
        this.alamat = alamat;
        this.noHp = noHp;
    }

    public int getIdAnggota() {
        return idAnggota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String tampilkanInfo() {
        return idAnggota + " - " + nama + " - " + alamat + " - " + noHp;
    }
}
