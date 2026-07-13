package com.perpustakaan;

import com.perpustakaan.exception.AnggotaTidakDitemukanException;
import com.perpustakaan.exception.StokBukuHabisException;
import com.perpustakaan.model.Anggota;
import com.perpustakaan.model.Buku;
import com.perpustakaan.service.AnggotaService;
import com.perpustakaan.service.BukuService;
import com.perpustakaan.service.TransaksiService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static AnggotaService anggotaService = new AnggotaService();
    static BukuService bukuService = new BukuService();
    static TransaksiService transaksiService = new TransaksiService();

    public static void main(String[] args) {
        int pilihan;
        do {
            System.out.println("\n=== SISTEM PERPUSTAKAAN SEDERHANA ===");
            System.out.println("1. Menu Manajemen Anggota");
            System.out.println("2. Menu Manajemen Buku");
            System.out.println("3. Menu Transaksi Peminjaman");
            System.out.println("4. Keluar");
            System.out.print("Pilih menu: ");
            pilihan = bacaInt();

            switch (pilihan) {
                case 1:
                    menuAnggota();
                    break;
                case 2:
                    menuBuku();
                    break;
                case 3:
                    menuTransaksi();
                    break;
                case 4:
                    System.out.println("Terima kasih, program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid");
            }
        } while (pilihan != 4);
    }

    static void menuAnggota() {
        int pilihan;
        do {
            System.out.println("\n--- Menu Manajemen Anggota ---");
            System.out.println("1. Tambah Anggota");
            System.out.println("2. Lihat Daftar Anggota");
            System.out.println("3. Kembali ke Menu Utama");
            System.out.print("Pilih sub menu: ");
            pilihan = bacaInt();

            try {
                switch (pilihan) {
                    case 1:
                        System.out.print("Nama: ");
                        String nama = scanner.nextLine();
                        System.out.print("Alamat: ");
                        String alamat = scanner.nextLine();
                        System.out.print("No HP: ");
                        String noHp = scanner.nextLine();
                        anggotaService.tambahAnggota(nama, alamat, noHp);
                        System.out.println("Anggota berhasil ditambahkan");
                        break;
                    case 2:
                        List<Anggota> daftarAnggota = anggotaService.lihatSemuaAnggota();
                        for (Anggota a : daftarAnggota) {
                            System.out.println(a.tampilkanInfo());
                        }
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Pilihan tidak valid");
                }
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan database: " + e.getMessage());
            }
        } while (pilihan != 3);
    }

    static void menuBuku() {
        int pilihan;
        do {
            System.out.println("\n--- Menu Manajemen Buku ---");
            System.out.println("1. Tambah Buku");
            System.out.println("2. Lihat Daftar Buku");
            System.out.println("3. Kembali ke Menu Utama");
            System.out.print("Pilih sub menu: ");
            pilihan = bacaInt();

            try {
                switch (pilihan) {
                    case 1:
                        System.out.print("Judul: ");
                        String judul = scanner.nextLine();
                        System.out.print("Penulis: ");
                        String penulis = scanner.nextLine();
                        System.out.print("Jenis (FIKSI/NONFIKSI): ");
                        String jenis = scanner.nextLine();
                        System.out.print("Stok: ");
                        int stok = bacaInt();
                        bukuService.tambahBuku(judul, penulis, jenis, stok);
                        System.out.println("Buku berhasil ditambahkan");
                        break;
                    case 2:
                        List<Buku> daftarBuku = bukuService.lihatSemuaBuku();
                        for (Buku b : daftarBuku) {
                            System.out.println(b.tampilkanInfo());
                        }
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Pilihan tidak valid");
                }
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan database: " + e.getMessage());
            }
        } while (pilihan != 3);
    }

    static void menuTransaksi() {
        int pilihan;
        do {
            System.out.println("\n--- Menu Transaksi Peminjaman ---");
            System.out.println("1. Pinjam Buku");
            System.out.println("2. Kembalikan Buku");
            System.out.println("3. Kembali ke Menu Utama");
            System.out.print("Pilih sub menu: ");
            pilihan = bacaInt();

            try {
                switch (pilihan) {
                    case 1:
                        System.out.print("ID Anggota: ");
                        int idAnggota = bacaInt();
                        System.out.print("ID Buku: ");
                        int idBuku = bacaInt();
                        try {
                            transaksiService.pinjamBuku(idAnggota, idBuku);
                            System.out.println("Buku berhasil dipinjam");
                        } catch (AnggotaTidakDitemukanException | StokBukuHabisException e) {
                            System.out.println("Gagal: " + e.getMessage());
                        }
                        break;
                    case 2:
                        System.out.print("ID Transaksi: ");
                        int idTransaksi = bacaInt();
                        transaksiService.kembalikanBuku(idTransaksi);
                        System.out.println("Buku berhasil dikembalikan");
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Pilihan tidak valid");
                }
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan database: " + e.getMessage());
            }
        } while (pilihan != 3);
    }

    // helper untuk membaca angka dengan aman
    static int bacaInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
