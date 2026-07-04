package com.example.sportcenter

/**
 * Representasi data Member (Pengguna Layanan Sport Center).
 * Menerapkan prinsip enkapsulasi dengan validasi saldo non-negatif.
 */
class Member(
    private val idMember: String,
    private var namaMember: String,
    saldoAwal: Double
) {
    // Saldo diproteksi dengan private set agar tidak bisa dimanipulasi langsung
    var saldo: Double = saldoAwal
        private set(value) {
            if (value >= 0) {
                field = value
            } else {
                throw IllegalArgumentException("Saldo tidak boleh negatif!")
            }
        }

    // Method untuk menambah saldo dengan validasi
    fun topUp(jumlah: Double) {
        if (jumlah > 0) {
            saldo += jumlah
        } else {
            throw IllegalArgumentException("Jumlah top up harus bernilai positif!")
        }
    }

    // Method untuk melakukan transaksi pembayaran reservasi
    fun bayar(jumlah: Double) {
        if (jumlah < 0) {
            throw IllegalArgumentException("Jumlah pembayaran tidak boleh negatif!")
        }
        if (saldo < jumlah) {
            throw IllegalArgumentException("Saldo tidak mencukupi!")
        }
        saldo -= jumlah
    }

    fun bookingLapangan() {
        println("Booking berhasil")
    }

    fun cekSaldo(): String {
        return "Saldo: Rp$saldo"
    }

    fun batalkanBooking() {
        println("Booking dibatalkan")
    }

    // Getter untuk properti private
    fun getIdMember() = idMember
    fun getNamaMember() = namaMember
    fun setNamaMember(nama: String) {
        namaMember = nama
    }
}

/**
 * Representasi data Lapangan Olahraga.
 * Menerapkan enkapsulasi dengan validasi harga sewa non-negatif.
 */
class Lapangan(
    private val idLapangan: String,
    private var namaLapangan: String,
    hargaAwal: Double,
    private var jadwalBooking: String
) {
    // Harga sewa dilindungi oleh custom setter dengan logika validasi
    var hargaSewa: Double = hargaAwal
        set(value) {
            if (value >= 0) {
                field = value
            } else {
                throw IllegalArgumentException("Harga sewa tidak boleh negatif!")
            }
        }

    fun cekKetersediaan(): Boolean {
        println("Lapangan tersedia")
        return true
    }

    fun tambahBooking() {
        println("Booking ditambahkan")
    }

    fun tampilJadwal(): String {
        return jadwalBooking
    }

    // Getter & Setter untuk properti private
    fun getIdLapangan() = idLapangan
    fun getNamaLapangan() = namaLapangan
    fun setNamaLapangan(nama: String) {
        namaLapangan = nama
    }
    fun getJadwalBooking() = jadwalBooking
    fun setJadwalBooking(jadwal: String) {
        jadwalBooking = jadwal
    }
}

/**
 * Representasi Aktor Receptionist yang bertugas memvalidasi transaksi booking.
 */
class Receptionist(
    private val idReceptionist: String,
    private var namaReceptionist: String
) {
    // Menjalankan verifikasi booking lapangan berdasarkan saldo Member
    fun verifikasiBooking(member: Member, lapangan: Lapangan): Boolean {
        if (member.saldo >= lapangan.hargaSewa) {
            member.bayar(lapangan.hargaSewa)
            lapangan.tambahBooking()
            return true
        }
        return false
    }

    fun menolakBooking(member: Member, lapangan: Lapangan) {
        println("Booking lapangan ${lapangan.getNamaLapangan()} ditolak untuk member ${member.getNamaMember()}")
    }

    // Getter untuk properti private
    fun getIdReceptionist() = idReceptionist
    fun getNamaReceptionist() = namaReceptionist
}
