package com.perpustakaan.model;

import java.sql.Date;

public class Transaksi {

    private int idTransaksi;
    private int idAnggota;
    private int idBuku;
    private Date tanggalPinjam;
    private Date tanggalJatuhTempo;
    private Date tanggalKembali;
    private String status;
    private int denda;

    public Transaksi(int idTransaksi, int idAnggota, int idBuku, Date tanggalPinjam,
                      Date tanggalJatuhTempo, Date tanggalKembali, String status, int denda) {
        this.idTransaksi = idTransaksi;
        this.idAnggota = idAnggota;
        this.idBuku = idBuku;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
        this.tanggalKembali = tanggalKembali;
        this.status = status;
        this.denda = denda;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public int getIdAnggota() {
        return idAnggota;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public Date getTanggalPinjam() {
        return tanggalPinjam;
    }

    public Date getTanggalJatuhTempo() {
        return tanggalJatuhTempo;
    }

    public Date getTanggalKembali() {
        return tanggalKembali;
    }

    public String getStatus() {
        return status;
    }

    public int getDenda() {
        return denda;
    }
}
