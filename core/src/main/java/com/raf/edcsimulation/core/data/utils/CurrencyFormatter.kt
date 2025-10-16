package com.raf.edcsimulation.core.data.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    fun Int.toRupiah(): String {
        val localeID = Locale.Builder().setLanguage("in").setRegion("ID").build()
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(this).replace("Rp", "Rp ").replace(",00", "")
    }
}