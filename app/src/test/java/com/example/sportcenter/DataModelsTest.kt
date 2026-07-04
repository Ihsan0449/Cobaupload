package com.example.sportcenter

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit test untuk validasi logika PBO pada DataModels.
 */
class DataModelsTest {

    @Test
    fun member_saldoAwal_benar() {
        val member = Member("M01", "Ihsan", 150000.0)
        assertEquals(150000.0, member.saldo, 0.001)
    }

    @Test
    fun member_topUp_menambahSaldo() {
        val member = Member("M01", "Ihsan", 100000.0)
        member.topUp(50000.0)
        assertEquals(150000.0, member.saldo, 0.001)
    }

    @Test
    fun member_bayar_memotongSaldo() {
        val member = Member("M01", "Ihsan", 150000.0)
        member.bayar(50000.0)
        assertEquals(100000.0, member.saldo, 0.001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun member_bayar_saldoTidakCukup_throwException() {
        val member = Member("M01", "Ihsan", 10000.0)
        member.bayar(50000.0)
    }

    @Test
    fun receptionist_verifikasiBooking_saldoCukup() {
        val member = Member("M01", "Ihsan", 150000.0)
        val lapangan = Lapangan("L01", "Badminton", 50000.0, "08:00-10:00")
        val receptionist = Receptionist("R01", "Admin")

        val result = receptionist.verifikasiBooking(member, lapangan)
        assertTrue(result)
        assertEquals(100000.0, member.saldo, 0.001)
    }

    @Test
    fun receptionist_verifikasiBooking_saldoTidakCukup() {
        val member = Member("M01", "Ihsan", 10000.0)
        val lapangan = Lapangan("L01", "Badminton", 50000.0, "08:00-10:00")
        val receptionist = Receptionist("R01", "Admin")

        val result = receptionist.verifikasiBooking(member, lapangan)
        assertFalse(result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun lapangan_hargaNegatif_throwException() {
        val lapangan = Lapangan("L01", "Test", 50000.0, "08:00")
        lapangan.hargaSewa = -1.0
    }
}
