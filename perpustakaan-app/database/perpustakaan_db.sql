-- =========================================================
-- DATABASE: perpustakaan_db
-- Sistem Manajemen Peminjaman Buku Perpustakaan Sederhana
-- =========================================================

CREATE DATABASE IF NOT EXISTS perpustakaan_db;
USE perpustakaan_db;

-- =========================================================
-- 1. TABEL DASAR
-- =========================================================

CREATE TABLE anggota (
    id_anggota INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    alamat VARCHAR(200),
    no_hp VARCHAR(20)
);

CREATE TABLE buku (
    id_buku INT AUTO_INCREMENT PRIMARY KEY,
    judul VARCHAR(150) NOT NULL,
    penulis VARCHAR(100),
    jenis ENUM('FIKSI', 'NONFIKSI') NOT NULL,
    stok INT NOT NULL DEFAULT 0
);

CREATE TABLE transaksi_peminjaman (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    id_anggota INT NOT NULL,
    id_buku INT NOT NULL,
    tanggal_pinjam DATE NOT NULL,
    tanggal_jatuh_tempo DATE NOT NULL,
    tanggal_kembali DATE NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DIPINJAM',
    denda INT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_anggota) REFERENCES anggota(id_anggota),
    FOREIGN KEY (id_buku) REFERENCES buku(id_buku)
);

-- =========================================================
-- 2. TRIGGER
-- Trigger 1: otomatis mengurangi stok saat buku dipinjam,
--            dan menolak peminjaman jika stok habis
-- Trigger 2: otomatis menambah stok saat buku dikembalikan
-- =========================================================

DELIMITER $$

CREATE TRIGGER trg_before_pinjam
BEFORE INSERT ON transaksi_peminjaman
FOR EACH ROW
BEGIN
    DECLARE stok_tersedia INT;

    SELECT stok INTO stok_tersedia
    FROM buku
    WHERE id_buku = NEW.id_buku;

    IF stok_tersedia IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Buku tidak ditemukan';
    ELSEIF stok_tersedia <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stok buku habis';
    ELSE
        UPDATE buku SET stok = stok - 1 WHERE id_buku = NEW.id_buku;
    END IF;
END$$

CREATE TRIGGER trg_after_kembali
AFTER UPDATE ON transaksi_peminjaman
FOR EACH ROW
BEGIN
    IF NEW.status = 'DIKEMBALIKAN' AND OLD.status = 'DIPINJAM' THEN
        UPDATE buku SET stok = stok + 1 WHERE id_buku = NEW.id_buku;
    END IF;
END$$

DELIMITER ;

-- =========================================================
-- 3. FUNCTION
-- Menghitung denda keterlambatan (Rp 1.000 per hari terlambat)
-- =========================================================

DELIMITER $$

CREATE FUNCTION fn_hitung_denda(p_id_transaksi INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE v_jatuh_tempo DATE;
    DECLARE v_tanggal_kembali DATE;
    DECLARE v_selisih INT;
    DECLARE v_denda INT;

    SELECT tanggal_jatuh_tempo, IFNULL(tanggal_kembali, CURDATE())
    INTO v_jatuh_tempo, v_tanggal_kembali
    FROM transaksi_peminjaman
    WHERE id_transaksi = p_id_transaksi;

    SET v_selisih = DATEDIFF(v_tanggal_kembali, v_jatuh_tempo);

    IF v_selisih > 0 THEN
        SET v_denda = v_selisih * 1000;
    ELSE
        SET v_denda = 0;
    END IF;

    RETURN v_denda;
END$$

DELIMITER ;

-- =========================================================
-- 4. STORED PROCEDURE
-- sp_pinjam_buku     : mencatat transaksi peminjaman baru
-- sp_kembalikan_buku : mencatat pengembalian dan hitung denda
-- =========================================================

DELIMITER $$

CREATE PROCEDURE sp_pinjam_buku(IN p_id_anggota INT, IN p_id_buku INT)
BEGIN
    INSERT INTO transaksi_peminjaman
        (id_anggota, id_buku, tanggal_pinjam, tanggal_jatuh_tempo, status, denda)
    VALUES
        (p_id_anggota, p_id_buku, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'DIPINJAM', 0);
END$$

CREATE PROCEDURE sp_kembalikan_buku(IN p_id_transaksi INT)
BEGIN
    DECLARE v_denda INT;
    SET v_denda = fn_hitung_denda(p_id_transaksi);

    UPDATE transaksi_peminjaman
    SET tanggal_kembali = CURDATE(),
        status = 'DIKEMBALIKAN',
        denda = v_denda
    WHERE id_transaksi = p_id_transaksi;
END$$

DELIMITER ;

-- =========================================================
-- 5. VIEW
-- Menampilkan daftar peminjaman yang masih aktif (belum kembali)
-- =========================================================

CREATE VIEW view_peminjaman_aktif AS
SELECT
    t.id_transaksi,
    a.nama,
    b.judul,
    t.tanggal_pinjam,
    t.tanggal_jatuh_tempo
FROM transaksi_peminjaman t
JOIN anggota a ON t.id_anggota = a.id_anggota
JOIN buku b ON t.id_buku = b.id_buku
WHERE t.status = 'DIPINJAM';

-- =========================================================
-- 6. DATA CONTOH (opsional, untuk uji coba)
-- =========================================================

INSERT INTO anggota (nama, alamat, no_hp) VALUES
('Andi Saputra', 'Jl. Melati No.1', '081234567890'),
('Budi Santoso', 'Jl. Mawar No.2', '081298765432');

INSERT INTO buku (judul, penulis, jenis, stok) VALUES
('Laskar Pelangi', 'Andrea Hirata', 'FIKSI', 3),
('Sejarah Indonesia', 'M.C. Ricklefs', 'NONFIKSI', 2);
