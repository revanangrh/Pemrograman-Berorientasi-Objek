package com.perpustakaan.dao;

import com.perpustakaan.model.Anggota;
import com.perpustakaan.util.KoneksiDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AnggotaDAO {

    public void tambahAnggota(Anggota anggota) throws SQLException {
        String sql = "INSERT INTO anggota (nama, alamat, no_hp) VALUES (?, ?, ?)";
        try (Connection conn = KoneksiDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anggota.getNama());
            stmt.setString(2, anggota.getAlamat());
            stmt.setString(3, anggota.getNoHp());
            stmt.executeUpdate();
        }
    }

    public List<Anggota> getSemuaAnggota() throws SQLException {
        List<Anggota> daftar = new ArrayList<>();
        String sql = "SELECT * FROM anggota";
        try (Connection conn = KoneksiDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Anggota a = new Anggota(
                        rs.getInt("id_anggota"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getString("no_hp")
                );
                daftar.add(a);
            }
        }
        return daftar;
    }

    public boolean cekAnggotaAda(int idAnggota) throws SQLException {
        String sql = "SELECT id_anggota FROM anggota WHERE id_anggota = ?";
        try (Connection conn = KoneksiDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAnggota);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
