class Member(
    private val idMember: String,
    private var namaMember: String,
    saldoAwal: Double
) {

    var saldo = saldoAwal
        private set(value) {
            if (value >= 0) {
                field = value
            } else {
                println("Saldo tidak boleh negatif!")
            }
        }

    fun topUp(jumlah: Double) {
        if (jumlah > 0) saldo += jumlah
    }

    fun bookingLapangan() {
        println("Booking berhasil")
    }

    fun cekSaldo() {
        println("Saldo: Rp$saldo")
    }

    fun batalkanBooking() {
        println("Booking dibatalkan")
    }
}

class Lapangan(
    private val idLapangan: String,
    private var namaLapangan: String,
    hargaAwal: Double,
    private var jadwalBooking: String
) {

    var hargaSewa = hargaAwal
        set(value) {
            if (value >= 0)
                field = value
            else
                println("Harga sewa tidak boleh negatif")
        }

    fun cekKetersediaan() {
        println("Lapangan tersedia")
    }

    fun tambahBooking() {
        println("Booking ditambahkan")
    }

    fun tampilJadwal() {
        println(jadwalBooking)
    }
}