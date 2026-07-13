package com.perpustakaan.dao;

import com.perpustakaan.util.KoneksiDatabase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransaksiDAO {

    public void pinjamBuku(int idAnggota, int idBuku) throws SQLException {
        String sql = "{CALL sp_pinjam_buku(?, ?)}";
        try (Connection conn = KoneksiDatabase.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAnggota);
            stmt.setInt(2, idBuku);
            stmt.execute();
        }
    }

    public void kembalikanBuku(int idTransaksi) throws SQLException {
        String sql = "{CALL sp_kembalikan_buku(?)}";
        try (Connection conn = KoneksiDatabase.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idTransaksi);
            stmt.execute();
        }
    }

    public List<String> getPeminjamanAktif() throws SQLException {
        List<String> daftar = new ArrayList<>();
        String sql = "SELECT * FROM view_peminjaman_aktif";
        try (Connection conn = KoneksiDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String baris = "ID Transaksi: " + rs.getInt("id_transaksi")
                        + " | Anggota: " + rs.getString("nama")
                        + " | Buku: " + rs.getString("judul")
                        + " | Pinjam: " + rs.getDate("tanggal_pinjam")
                        + " | Jatuh Tempo: " + rs.getDate("tanggal_jatuh_tempo");
                daftar.add(baris);
            }
        }
        return daftar;
    }
}
