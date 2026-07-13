package com.perpustakaan.model;

// Class abstrak sebagai induk (inheritance) untuk BukuFiksi dan BukuNonFiksi
public abstract class Buku {

    // enkapsulasi: atribut private, akses lewat getter/setter
    private int idBuku;
    private String judul;
    private String penulis;
    private int stok;

    public Buku(int idBuku, String judul, String penulis, int stok) {
        this.idBuku = idBuku;
        this.judul = judul;
        this.penulis = penulis;
        setStok(stok);
    }

    public int getIdBuku() {
        return idBuku;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        // validasi sederhana, contoh penerapan enkapsulasi
        this.stok = Math.max(stok, 0);
    }

    // method abstrak, wajib di-override oleh class turunan (polimorfisme)
    public abstract String getJenis();

    public String tampilkanInfo() {
        return "[" + getJenis() + "] " + judul + " - " + penulis + " (Stok: " + stok + ")";
    }
}
