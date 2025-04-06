// Kelas untuk menangani data akun pelanggan
class AkunPelanggan {
    // Atribut private (enkapsulasi) untuk menyembunyikan informasi akun
    private String nomorPelanggan;
    private String nama;
    private double saldo;
    private String pin;
    private int percobaan;
    private boolean aktif;
    
    // Konstanta
    private final double SALDO_MINIMAL = 10000;
    
    // Constructor
    public AkunPelanggan(String nomorPelanggan, String nama, double saldoAwal, String pin) {
        // Validasi nomor pelanggan harus 10 digit
        if (nomorPelanggan.length() != 10) {
            System.out.println("Error: Nomor pelanggan harus 10 digit!");
            return;
        }
        
        this.nomorPelanggan = nomorPelanggan;
        this.nama = nama;
        this.saldo = saldoAwal;
        this.pin = pin;
        this.percobaan = 0;
        this.aktif = true;
    }
    
    // Getter methods (accessor)
    public String getNomorPelanggan() {
        return this.nomorPelanggan;
    }
    
    public String getNama() {
        return this.nama;
    }
    
    public double getSaldo() {
        return this.saldo;
    }
    
    public boolean isAktif() {
        return this.aktif;
    }
    
    // Method untuk memvalidasi PIN
    public boolean validasiPin(String inputPin) {
        if (!this.aktif) {
            System.out.println("Akun telah diblokir!");
            return false;
        }
        
        if (this.pin.equals(inputPin)) {
            // Reset percobaan jika berhasil
            this.percobaan = 0;
            return true;
        } else {
            this.percobaan++;
            System.out.println("PIN salah! Percobaan ke-" + this.percobaan);
            
            // Blokir akun jika sudah 3x salah
            if (this.percobaan >= 3) {
                this.aktif = false;
                System.out.println("Akun diblokir karena 3x kesalahan autentifikasi!");
            }
            
            return false;
        }
    }
    
    // Method untuk top up saldo
    public boolean topUp(double jumlah, String inputPin) {
        if (!validasiPin(inputPin)) {
            return false;
        }
        
        if (jumlah <= 0) {
            System.out.println("Jumlah top up harus lebih dari 0!");
            return false;
        }
        
        this.saldo += jumlah;
        System.out.println("Top up berhasil. Saldo saat ini: Rp" + this.saldo);
        return true;
    }
    
    // Method untuk transaksi pembelian
    public boolean pembelian(double jumlah, String inputPin) {
        if (!validasiPin(inputPin)) {
            return false;
        }
        
        if (jumlah <= 0) {
            System.out.println("Jumlah pembelian harus lebih dari 0!");
            return false;
        }
        
        // Hitung cashback berdasarkan jenis pelanggan
        double cashback = hitungCashback(jumlah);
        
        // Cek apakah saldo cukup
        if ((this.saldo - jumlah + cashback) < SALDO_MINIMAL) {
            System.out.println("Transaksi gagal! Saldo akhir akan kurang dari saldo minimal (Rp" + SALDO_MINIMAL + ")");
            return false;
        }
        
        // Proses transaksi
        this.saldo = this.saldo - jumlah + cashback;
        System.out.println("Pembelian berhasil sebesar Rp" + jumlah);
        System.out.println("Anda mendapat cashback sebesar Rp" + cashback);
        System.out.println("Saldo saat ini: Rp" + this.saldo);
        return true;
    }
    
    // Method untuk menghitung cashback berdasarkan jenis pelanggan
    private double hitungCashback(double jumlah) {
        // Ambil 2 digit awal dari nomor pelanggan
        String jenisPelanggan = this.nomorPelanggan.substring(0, 2);
        double cashback = 0;
        
        switch (jenisPelanggan) {
            case "38": // Silver
                if (jumlah > 1000000) {
                    cashback = jumlah * 0.05; // 5% cashback
                }
                break;
            case "56": // Gold
                if (jumlah > 1000000) {
                    cashback = jumlah * 0.07; // 7% cashback
                } else {
                    cashback = jumlah * 0.02; // 2% cashback
                }
                break;
            case "74": // Platinum
                if (jumlah > 1000000) {
                    cashback = jumlah * 0.10; // 10% cashback
                } else {
                    cashback = jumlah * 0.05; // 5% cashback
                }
                break;
            default:
                // Tidak ada cashback untuk jenis lain
                break;
        }
        
        return cashback;
    }
}

// Kelas untuk pengujian sistem
public class TugasPraktikum_4_HasanHuzaifi {
    public static void main(String[] args) {
        // Membuat beberapa contoh akun
        AkunPelanggan pelanggan1 = new AkunPelanggan("3812345678", "Budi Santoso", 100000, "1234");
        AkunPelanggan pelanggan2 = new AkunPelanggan("5698765432", "Siti Rahma", 2000000, "5678");
        AkunPelanggan pelanggan3 = new AkunPelanggan("7412341234", "Joko Widodo", 5000000, "9012");
        
        System.out.println("=== Testing Pelanggan Silver ===");
        System.out.println("Nama: " + pelanggan1.getNama());
        System.out.println("Nomor: " + pelanggan1.getNomorPelanggan());
        System.out.println("Saldo Awal: Rp" + pelanggan1.getSaldo());
        
        // Testing topup
        pelanggan1.topUp(500000, "1234");
        
        // Testing pembelian kecil (tidak dapat cashback)
        pelanggan1.pembelian(100000, "1234");
        
        // Testing pembelian besar (dapat cashback 5%)
        pelanggan1.pembelian(1200000, "1234");
        
        System.out.println("\n=== Testing Pelanggan Gold ===");
        System.out.println("Nama: " + pelanggan2.getNama());
        System.out.println("Nomor: " + pelanggan2.getNomorPelanggan());
        System.out.println("Saldo Awal: Rp" + pelanggan2.getSaldo());
        
        // Testing pembelian kecil (dapat cashback 2%)
        pelanggan2.pembelian(500000, "5678");
        
        // Testing pembelian besar (dapat cashback 7%)
        pelanggan2.pembelian(1500000, "5678");
        
        System.out.println("\n=== Testing Pelanggan Platinum ===");
        System.out.println("Nama: " + pelanggan3.getNama());
        System.out.println("Nomor: " + pelanggan3.getNomorPelanggan());
        System.out.println("Saldo Awal: Rp" + pelanggan3.getSaldo());
        
        // Testing pembelian kecil (dapat cashback 5%)
        pelanggan3.pembelian(800000, "9012");
        
        // Testing pembelian besar (dapat cashback 10%)
        pelanggan3.pembelian(2000000, "9012");
        
        System.out.println("\n=== Testing Validasi PIN ===");
        // Test PIN salah
        System.out.println("Mencoba PIN salah:");
        pelanggan1.pembelian(10000, "4321");
        pelanggan1.pembelian(10000, "4321");
        pelanggan1.pembelian(10000, "4321");
        
        // Coba transaksi setelah akun diblokir
        System.out.println("Mencoba transaksi setelah akun diblokir:");
        pelanggan1.pembelian(10000, "1234");
        
        System.out.println("\n=== Testing Validasi Saldo Minimal ===");
        // Test saldo tidak cukup
        System.out.println("Mencoba pembelian dengan saldo akhir kurang dari minimal:");
        AkunPelanggan pelanggan4 = new AkunPelanggan("3812345679", "Ahmad Dahlan", 20000, "1111");
        pelanggan4.pembelian(15000, "1111");
    }
}