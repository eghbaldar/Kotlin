package com.example.ifpstaff.general

import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat

//TODO this class works based of {samanzamani} that implemented in gradle!
class ShamsiDate {

    var pdate = PersianDate()
    var pdformater = PersianDateFormat()

    fun getLongTimestampt(): String {
        pdformater = PersianDateFormat("j F y")
        return pdate.dayName() + "  " + pdformater.format(pdate)
    }

    fun getOnlyNumbericDate(): String {
        pdformater = PersianDateFormat("Y/m/d")
        return pdformater.format(pdate)
    }

    fun getOnlyNumbericYear(): String {
        pdformater = PersianDateFormat("Y")
        return pdformater.format(pdate)
    }

    fun getOnlyNumbericMonth(): String {
        pdformater = PersianDateFormat("m")
        return pdformater.format(pdate)
    }

    fun getOnlyNumbericDay(): String {
        pdformater = PersianDateFormat("d")
        return pdformater.format(pdate)
    }

    fun getDayOfWeekName_WithParticularDate(Y: Int, M: Int, D: Int): String {
        pdate.setShYear(Y)
        pdate.setShMonth(M)
        pdate.setShDay(D)
        return pdate.dayName()
    }

    fun getMonthName_WithParticularDate(M: Int): String {
        pdate.setShMonth(M)
        return pdate.monthName()
    }


}