package com.perpustakaan.exception;

// Custom exception: dilempar saat stok buku habis atau tidak ditemukan
public class StokBukuHabisException extends Exception {
    public StokBukuHabisException(String message) {
        super(message);
    }
}
