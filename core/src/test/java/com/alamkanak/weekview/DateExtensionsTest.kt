package com.alamkanak.weekview

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class DateExtensionsTest {

    @Test
    fun `returns correct day of week`() {
        val date = firstDayOfYear().setYear(2019)
        val expected = Calendar.TUESDAY
        val result = date.dayOfWeek
        assertEquals(expected, result)
    }

    @Test
    fun `does correct equality check`() {
        val first = firstDayOfYear().setYear(2019)
        val second = firstDayOfYear().setYear(2019)
        assert(first == second)

        val newSecond = second - Millis(1)
        assert(first != newSecond)
    }

    @Test
    fun `adds days correctly`() {
        val date = firstDayOfYear().setYear(2019)
        val result = date + Days(2)
        assertEquals(3, result.dayOfMonth)

        val secondResult = date + Days(31)
        assertEquals(1, secondResult.dayOfMonth)
        assertEquals(Calendar.FEBRUARY, secondResult.month)
    }
}

private fun Calendar.setYear(year: Int): Calendar {
    set(Calendar.YEAR, year)
    return this
}
