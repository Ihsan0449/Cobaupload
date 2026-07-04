package com.example.sportcenter

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * MainActivity yang menghubungkan desain layout XML dengan logika PBO Kotlin.
 * Mengontrol alur Login, validasi, Dashboard, serta simulasi pemesanan lapangan.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Tampilkan layar login saat pertama kali dibuka
        showLoginScreen()
    }

    /**
     * Memuat layout login XML dan menangani aksi klik login beserta validasi PBO.
     */
    private fun showLoginScreen() {
        setContentView(R.layout.activity_login)

        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@MainActivity, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simulasi Login Member
            try {
                // Instansiasi Member dari DataModels.kt dengan Saldo Awal Rp 150.000 (PBO)
                val member = Member(
                    idMember = "MBR-04231049",
                    namaMember = username,
                    saldoAwal = 150000.0
                )

                Toast.makeText(this@MainActivity, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                // Pindah ke halaman Beranda/Home
                showHomeScreen(member)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Memuat layout beranda XML dan menangani simulasi booking lapangan & top up.
     */
    private fun showHomeScreen(member: Member) {
        setContentView(R.layout.activity_home)

        // View Binding untuk Dashboard
        val tvMemberName: TextView = findViewById(R.id.tvMemberName)
        val tvMemberSaldo: TextView = findViewById(R.id.tvMemberSaldo)
        val tvLapanganTitle: TextView = findViewById(R.id.tvLapanganTitle)
        val tvLapanganHarga: TextView = findViewById(R.id.tvLapanganHarga)
        val tvLapanganJadwal: TextView = findViewById(R.id.tvLapanganJadwal)

        val btnCekKetersediaan: Button = findViewById(R.id.btnCekKetersediaan)
        val btnPesanLapangan: Button = findViewById(R.id.btnPesanLapangan)
        val btnTopUp: Button = findViewById(R.id.btnTopUp)
        val btnBatalkanBooking: Button = findViewById(R.id.btnBatalkanBooking)
        val btnLogout: Button = findViewById(R.id.btnLogout)

        // Tampilkan profil member
        tvMemberName.text = "Member: ${member.getNamaMember()}"
        tvMemberSaldo.text = member.cekSaldo()

        // Instansiasi Lapangan dan Receptionist (PBO)
        val lapangan = Lapangan(
            idLapangan = "LPG-A",
            namaLapangan = "Lapangan A - Badminton",
            hargaAwal = 50000.0,
            jadwalBooking = "Jadwal: 08:00 - 10:00 (Tersedia)"
        )
        val receptionist = Receptionist("RCP-01", "Ihsan Receptionist")

        // Tampilkan info lapangan
        tvLapanganTitle.text = lapangan.getNamaLapangan()
        tvLapanganHarga.text = "Harga Sewa: Rp ${lapangan.hargaSewa.toInt()} / Sesi"
        tvLapanganJadwal.text = lapangan.tampilJadwal()

        // 1. Cek Ketersediaan Lapangan
        btnCekKetersediaan.setOnClickListener {
            val tersedia = lapangan.cekKetersediaan()
            if (tersedia) {
                Toast.makeText(this@MainActivity, "${lapangan.getNamaLapangan()} tersedia untuk di-booking!", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Booking Lapangan (Melibatkan Receptionist Verifikasi)
        btnPesanLapangan.setOnClickListener {
            if (tvLapanganJadwal.text.toString().contains("Ter-booking")) {
                Toast.makeText(this@MainActivity, "Lapangan sudah ter-booking!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifikasi booking lewat Receptionist
            val success = receptionist.verifikasiBooking(member, lapangan)
            if (success) {
                // Update UI setelah saldo terpotong
                tvMemberSaldo.text = member.cekSaldo()
                val jadwalBaru = "Jadwal: 08:00 - 10:00 (Ter-booking oleh ${member.getNamaMember()})"
                lapangan.setJadwalBooking(jadwalBaru)
                tvLapanganJadwal.text = lapangan.tampilJadwal()

                Toast.makeText(this@MainActivity, "Booking Sukses! Saldo didebet Rp ${lapangan.hargaSewa.toInt()}", Toast.LENGTH_LONG).show()
            } else {
                receptionist.menolakBooking(member, lapangan)
                Toast.makeText(this@MainActivity, "Booking Ditolak! Saldo tidak mencukupi.", Toast.LENGTH_LONG).show()
            }
        }

        // 3. Top Up Saldo Member
        btnTopUp.setOnClickListener {
            try {
                member.topUp(50000.0)
                tvMemberSaldo.text = member.cekSaldo()
                Toast.makeText(this@MainActivity, "Top Up Rp 50.000 Berhasil!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Top Up Gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Batalkan Booking (Simulasi refund dana)
        btnBatalkanBooking.setOnClickListener {
            if (!tvLapanganJadwal.text.toString().contains("Ter-booking")) {
                Toast.makeText(this@MainActivity, "Anda belum melakukan booking lapangan!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            member.batalkanBooking()
            // Refund dana sewa lapangan
            member.topUp(lapangan.hargaSewa)
            tvMemberSaldo.text = member.cekSaldo()

            // Reset jadwal status
            val jadwalReset = "Jadwal: 08:00 - 10:00 (Tersedia)"
            lapangan.setJadwalBooking(jadwalReset)
            tvLapanganJadwal.text = lapangan.tampilJadwal()

            Toast.makeText(this@MainActivity, "Booking dibatalkan. Dana dikembalikan!", Toast.LENGTH_LONG).show()
        }

        // 5. Logout
        btnLogout.setOnClickListener {
            Toast.makeText(this@MainActivity, "Berhasil Logout", Toast.LENGTH_SHORT).show()
            showLoginScreen()
        }
    }
}
