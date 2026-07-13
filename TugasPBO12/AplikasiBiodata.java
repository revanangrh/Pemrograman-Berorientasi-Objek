import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AplikasiBiodata extends JFrame {
    // Komponen Input
    private JTextField txtNim, txtNama, txtProdi;
    // Komponen Output
    private JTextArea txtOutput;
    // Tombol
    private JButton btnTampilkan, btnReset;

    public AplikasiBiodata() {
        // Pengaturan Jendela Utama (Window)
        setTitle("Aplikasi Biodata Mahasiswa");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. PANEL INPUT DATA ---
        JPanel panelInputOuter = new JPanel(new BorderLayout());
        panelInputOuter.setBorder(BorderFactory.createTitledBorder("Input Data"));
        
        JPanel panelInputGrid = new JPanel(new GridLayout(3, 2, 5, 10));
        
        panelInputGrid.add(new JLabel("NIM"));
        txtNim = new JTextField();
        panelInputGrid.add(txtNim);
        
        panelInputGrid.add(new JLabel("Nama"));
        txtNama = new JTextField();
        panelInputGrid.add(txtNama);
        
        panelInputGrid.add(new JLabel("Program Studi"));
        txtProdi = new JTextField();
        panelInputGrid.add(txtProdi);
        
        panelInputOuter.add(panelInputGrid, BorderLayout.CENTER);

        // --- 2. PANEL TOMBOL ---
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnTampilkan = new JButton("Tampilkan");
        btnReset = new JButton("Reset");
        panelTombol.add(btnTampilkan);
        panelTombol.add(btnReset);

        // --- 3. PANEL OUTPUT ---
        JPanel panelOutput = new JPanel(new BorderLayout());
        panelOutput.setBorder(BorderFactory.createTitledBorder("Output"));
        
        txtOutput = new JTextArea();
        txtOutput.setFont(new Font("Courier New", Font.PLAIN, 13));
        txtOutput.setEditable(false); // Mengunci text area agar tidak bisa diketik manual
        
        JScrollPane scrollPane = new JScrollPane(txtOutput);
        panelOutput.add(scrollPane, BorderLayout.CENTER);

        // --- GABUNGKAN SEMUA PANEL ---
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelInputOuter, BorderLayout.NORTH);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);

        add(panelAtas, BorderLayout.NORTH);
        add(panelOutput, BorderLayout.CENTER);

        // --- LOGIKA EVENT LISTENER (JAVASCRIPT / ACTION) ---
        
        // Aksi Tombol Tampilkan
        btnTampilkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim = txtNim.getText();
                String nama = txtNama.getText();
                String prodi = txtProdi.getText();

                String hasil = "========== BIODATA MAHASISWA ==========\n\n" +
                               "NIM           : " + nim + "\n" +
                               "Nama          : " + nama + "\n" +
                               "Program Studi : " + prodi;
                
                txtOutput.setText(hasil);
            }
        });

        // Aksi Tombol Reset
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtNim.setText("");
                txtNama.setText("");
                txtProdi.setText("");
                txtOutput.setText("");
            }
        });
    }

    public static void main(String[] args) {
        // Menjalankan GUI di thread yang aman
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AplikasiBiodata().setVisible(true);
            }
        });
    }
}