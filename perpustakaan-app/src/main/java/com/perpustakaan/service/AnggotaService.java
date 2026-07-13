package com.perpustakaan.service;

import com.perpustakaan.dao.AnggotaDAO;
import com.perpustakaan.model.Anggota;

import java.sql.SQLException;
import java.util.List;

public class AnggotaService {

    private AnggotaDAO anggotaDAO = new AnggotaDAO();

    public void tambahAnggota(String nama, String alamat, String noHp) throws SQLException {
        Anggota anggota = new Anggota(0, nama, alamat, noHp);
        anggotaDAO.tambahAnggota(anggota);
    }

    public List<Anggota> lihatSemuaAnggota() throws SQLException {
        return anggotaDAO.getSemuaAnggota();
    }
}
