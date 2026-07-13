package com.perpustakaan.model;

public class BukuFiksi extends Buku {

    public BukuFiksi(int idBuku, String judul, String penulis, int stok) {
        super(idBuku, judul, penulis, stok);
    }

    @Override
    public String getJenis() {
        return "FIKSI";
    }

    @Override
    public String tampilkanInfo() {
        // polimorfisme: perilaku berbeda dari class induk
        return super.tampilkanInfo() + " - Masa pinjam maksimal 7 hari";
    }
}
