package com.perpustakaan.dao;

import com.perpustakaan.model.Buku;
import com.perpustakaan.model.BukuFiksi;
import com.perpustakaan.model.BukuNonFiksi;
import com.perpustakaan.util.KoneksiDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {

    public void tambahBuku(String judul, String penulis, String jenis, int stok) throws SQLException {
        String sql = "INSERT INTO buku (judul, penulis, jenis, stok) VALUES (?, ?, ?, ?)";
        try (Connection conn = KoneksiDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, judul);
            stmt.setString(2, penulis);
            stmt.setString(3, jenis);
            stmt.setInt(4, stok);
            stmt.executeUpdate();
        }
    }

    public List<Buku> getSemuaBuku() throws SQLException {
        List<Buku> daftar = new ArrayList<>();
        String sql = "SELECT * FROM buku";
        try (Connection conn = KoneksiDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id_buku");
                String judul = rs.getString("judul");
                String penulis = rs.getString("penulis");
                String jenis = rs.getString("jenis");
                int stok = rs.getInt("stok");

                // objek dibuat sesuai jenisnya (polimorfisme saat pemakaian)
                Buku buku;
                if (jenis.equalsIgnoreCase("FIKSI")) {
                    buku = new BukuFiksi(id, judul, penulis, stok);
                } else {
                    buku = new BukuNonFiksi(id, judul, penulis, stok);
                }
                daftar.add(buku);
            }
        }
        return daftar;
    }

    public boolean cekBukuAda(int idBuku) throws SQLException {
        String sql = "SELECT id_buku FROM buku WHERE id_buku = ?";
        try (Connection conn = KoneksiDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBuku);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
