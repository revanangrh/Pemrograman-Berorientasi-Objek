package com.perpustakaan.exception;

// Custom exception: dilempar saat id anggota tidak ditemukan di database
public class AnggotaTidakDitemukanException extends Exception {
    public AnggotaTidakDitemukanException(String message) {
        super(message);
    }
}
