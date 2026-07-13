package com.perpustakaan.service;

import com.perpustakaan.dao.AnggotaDAO;
import com.perpustakaan.dao.BukuDAO;
import com.perpustakaan.dao.TransaksiDAO;
import com.perpustakaan.exception.AnggotaTidakDitemukanException;
import com.perpustakaan.exception.StokBukuHabisException;

import java.sql.SQLException;
import java.util.List;

public class TransaksiService {

    private TransaksiDAO transaksiDAO = new TransaksiDAO();
    private AnggotaDAO anggotaDAO = new AnggotaDAO();
    private BukuDAO bukuDAO = new BukuDAO();

    public void pinjamBuku(int idAnggota, int idBuku) throws SQLException,
            AnggotaTidakDitemukanException, StokBukuHabisException {

        if (!anggotaDAO.cekAnggotaAda(idAnggota)) {
            throw new AnggotaTidakDitemukanException("Anggota dengan ID " + idAnggota + " tidak ditemukan");
        }
        if (!bukuDAO.cekBukuAda(idBuku)) {
            throw new StokBukuHabisException("Buku dengan ID " + idBuku + " tidak ditemukan");
        }

        try {
            transaksiDAO.pinjamBuku(idAnggota, idBuku);
        } catch (SQLException e) {
            // trigger di database melempar error saat stok habis,
            // di sini diterjemahkan menjadi custom exception
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("stok")) {
                throw new StokBukuHabisException("Stok buku habis, tidak bisa dipinjam");
            }
            throw e;
        }
    }

    public void kembalikanBuku(int idTransaksi) throws SQLException {
        transaksiDAO.kembalikanBuku(idTransaksi);
    }

    public List<String> lihatPeminjamanAktif() throws SQLException {
        return transaksiDAO.getPeminjamanAktif();
    }
}
