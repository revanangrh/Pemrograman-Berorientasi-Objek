package com.perpustakaan.model;

public class BukuNonFiksi extends Buku {

    public BukuNonFiksi(int idBuku, String judul, String penulis, int stok) {
        super(idBuku, judul, penulis, stok);
    }

    @Override
    public String getJenis() {
        return "NONFIKSI";
    }

    @Override
    public String tampilkanInfo() {
        return super.tampilkanInfo() + " - Masa pinjam maksimal 14 hari";
    }
}
