import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    // Konfigurasi koneksi database ke XAMPP
    private static final String URL = "jdbc:mysql://localhost:3306/toko_retail";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connectDB() {
        try {
            // Memastikan driver MySQL terdeteksi
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL tidak ditemukan: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("Gagal terhubung ke database: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String pilihan;

        while (true) {
            System.out.println("==========================");
            System.out.println("    MENU TOKO RETAIL      ");
            System.out.println("==========================");
            System.out.println(" 1. Tampil Semua Data     ");
            System.out.println(" 2. Tambah Data           ");
            System.out.println(" 3. Cari Data             ");
            System.out.println(" 4. Ubah Data             ");
            System.out.println(" 5. Hapus Data            ");
            System.out.println(" 0. Keluar                ");
            System.out.println("==========================");
            System.out.print("Pilihan : ");
            pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1":
                    tampilSemuaData();
                    break;
                case "2":
                    tambahData(scanner);
                    break;
                case "3":
                    cariData(scanner);
                    break;
                case "4":
                    ubahData(scanner);
                    break;
                case "5":
                    hapusData(scanner);
                    break;
                case "0":
                    System.out.println("Terima kasih telah menggunakan aplikasi ini.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Pilihan tidak valid! Silakan coba lagi.\n");
            }
        }
    }

    // 1. TAMPIL DATA
    private static void tampilSemuaData() {
        String query = "SELECT kode, nama_barang, harga, stok FROM barang";
        int total = 0;

        try (Connection conn = connectDB()) {
            if (conn == null) return;
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                
                System.out.println("\n========================================================");
                System.out.println("               DAFTAR BARANG TOKO RETAIL                ");
                System.out.println("========================================================");
                System.out.printf("%-3s | %-6s | %-20s | %-8s | %-5s\n", "#", "Kode", "Nama Barang", "Harga", "Stok");
                System.out.println("--------------------------------------------------------");

                while (rs.next()) {
                    total++;
                    System.out.printf("%-3d | %-6s | %-20s | %-8d | %-5d\n", 
                        total, rs.getString("kode"), rs.getString("nama_barang"), rs.getInt("harga"), rs.getInt("stok"));
                }
                
                System.out.println("--------------------------------------------------------");
                System.out.println("Total: " + total + " barang");
                System.out.println("========================================================\n");
            }
        } catch (SQLException e) {
            System.out.println("Error query data: " + e.getMessage());
        }
    }

    // 2. TAMBAH DATA
    private static void tambahData(Scanner scanner) {
        System.out.println("\n--- TAMBAH DATA BARANG ---");
        System.out.print("Masukkan Kode Barang: ");
        String kode = scanner.nextLine();
        System.out.print("Masukkan Nama Barang: ");
        String nama = scanner.nextLine();
        
        int harga, stok;
        try {
            System.out.print("Masukkan Harga: ");
            harga = Integer.parseInt(scanner.nextLine());
            System.out.print("Masukkan Stok: ");
            stok = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Harga dan Stok harus berupa angka!");
            return;
        }

        String query = "INSERT INTO barang (kode, nama_barang, harga, stok) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectDB()) {
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, kode);
                pstmt.setString(2, nama);
                pstmt.setInt(3, harga);
                pstmt.setInt(4, stok);
                
                pstmt.executeUpdate();
                System.out.println("Data berhasil ditambahkan!");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan data: " + e.getMessage());
        }
    }

    // 3. CARI DATA
    private static void cariData(Scanner scanner) {
        System.out.println("\n--- CARI DATA BARANG ---");
        System.out.print("Masukkan Nama atau Kode Barang yang dicari: ");
        String keyword = scanner.nextLine();

        String query = "SELECT kode, nama_barang, harga, stok FROM barang WHERE nama_barang LIKE ? OR kode LIKE ?";
        try (Connection conn = connectDB()) {
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    System.out.println("\nHasil Pencarian:");
                    int idx = 1;
                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        System.out.printf("%d. Kode: %s | Nama: %s | Harga: %d | Stok: %d\n",
                                idx++, rs.getString("kode"), rs.getString("nama_barang"), rs.getInt("harga"), rs.getInt("stok"));
                    }
                    if (!found) {
                        System.out.println("Data tidak ditemukan.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error mencari data: " + e.getMessage());
        }
    }

    // 4. UBAH DATA
    private static void ubahData(Scanner scanner) {
        System.out.println("\n--- UBAH DATA BARANG ---");
        System.out.print("Masukkan Kode Barang yang ingin diubah: ");
        String kode = scanner.nextLine();

        String checkQuery = "SELECT nama_barang, harga, stok FROM barang WHERE kode = ?";
        try (Connection conn = connectDB()) {
            if (conn == null) return;
            
            String namaLama = "";
            int hargaLama = 0, stokLama = 0;
            boolean exists = false;

            try (PreparedStatement checkPstmt = conn.prepareStatement(checkQuery)) {
                checkPstmt.setString(1, kode);
                try (ResultSet rs = checkPstmt.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                        namaLama = rs.getString("nama_barang");
                        hargaLama = rs.getInt("harga");
                        stokLama = rs.getInt("stok");
                    }
                }
            }

            if (!exists) {
                System.out.println("Kode barang tidak ditemukan.");
                return;
            }

            System.out.printf("Data lama -> Nama: %s, Harga: %d, Stok: %d\n", namaLama, hargaLama, stokLama);
            System.out.print("Nama Baru (kosongkan jika tidak diubah): ");
            String namaBaru = scanner.nextLine();
            if (namaBaru.trim().isEmpty()) namaBaru = namaLama;

            System.out.print("Harga Baru (kosongkan jika tidak diubah): ");
            String hargaInput = scanner.nextLine();
            int hargaBaru = hargaInput.trim().isEmpty() ? hargaLama : Integer.parseInt(hargaInput);

            System.out.print("Stok Baru (kosongkan jika tidak diubah): ");
            String stokInput = scanner.nextLine();
            int stokBaru = stokInput.trim().isEmpty() ? stokLama : Integer.parseInt(stokInput);

            String updateQuery = "UPDATE barang SET nama_barang = ?, harga = ?, stok = ? WHERE kode = ?";
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateQuery)) {
                updatePstmt.setString(1, namaBaru);
                updatePstmt.setInt(2, hargaBaru);
                updatePstmt.setInt(3, stokBaru);
                updatePstmt.setString(4, kode);
                
                updatePstmt.executeUpdate();
                System.out.println("Data berhasil diperbarui!");
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error memperbarui data: " + e.getMessage());
        }
    }

    // 5. HAPUS DATA
    private static void hapusData(Scanner scanner) {
        System.out.println("\n--- HAPUS DATA BARANG ---");
        System.out.print("Masukkan Kode Barang yang ingin dihapus: ");
        String kode = scanner.nextLine();

        String query = "DELETE FROM barang WHERE kode = ?";
        try (Connection conn = connectDB()) {
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, kode);
                int rowsDeleted = pstmt.executeUpdate();
                
                if (rowsDeleted > 0) {
                    System.out.println("Data berhasil dihapus!");
                } else {
                    System.out.println("Kode barang tidak ditemukan / tidak ada data yang dihapus.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error menghapus data: " + e.getMessage());
        }
    }
}