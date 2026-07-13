package com.perpustakaan.service;

import com.perpustakaan.dao.BukuDAO;
import com.perpustakaan.model.Buku;

import java.sql.SQLException;
import java.util.List;

public class BukuService {

    private BukuDAO bukuDAO = new BukuDAO();

    public void tambahBuku(String judul, String penulis, String jenis, int stok) throws SQLException {
        bukuDAO.tambahBuku(judul, penulis, jenis, stok);
    }

    public List<Buku> lihatSemuaBuku() throws SQLException {
        return bukuDAO.getSemuaBuku();
    }
}
