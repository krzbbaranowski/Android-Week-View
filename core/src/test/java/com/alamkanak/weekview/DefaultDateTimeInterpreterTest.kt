package com.alamkanak.weekview

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class DefaultDateTimeInterpreterTest {

    private val date = Calendar.getInstance().apply {
        set(Calendar.MONTH, Calendar.JANUARY)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.YEAR, 2020)
        set(Calendar.HOUR_OF_DAY, 1)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    @Test
    fun `correct date when in day view`() {
        val dateFormatProvider = FakeDateFormatProvider()
        val underTest = DefaultDateTimeInterpreter(dateFormatProvider, numberOfDays = 1)

        val expected = "WEDNESDAY 1/01"
        val observed = underTest.interpretDate(date)

        assertEquals(expected, observed)
    }

    @Test
    fun `correct date when in 3-day view`() {
        val dateFormatProvider = FakeDateFormatProvider()
        val underTest = DefaultDateTimeInterpreter(dateFormatProvider, numberOfDays = 3)

        val expected = "WED 1/01"
        val observed = underTest.interpretDate(date)

        assertEquals(expected, observed)
    }

    // SimpleDateFormat does not accept EEEEE in the testing environment, even though it
    // works in the app ¯\_(ツ)_/¯
    /*
    @Test
    fun `correct date when in week view`() {
        val dateFormatProvider = FakeDateFormatProvider()
        val underTest = DefaultDateTimeInterpreter(dateFormatProvider, numberOfDays = 7)

        val expected = "T 1/01"
        val observed = underTest.interpretDate(date)

        assertEquals(expected, observed)
    }
    */

    @Test
    fun `correct time in 12 hour format`() {
        val dateFormatProvider = FakeDateFormatProvider()
        val underTest = DefaultDateTimeInterpreter(dateFormatProvider, numberOfDays = 1)

        val expected = "01 AM"
        val observed = underTest.interpretTime(date.hour)

        assertEquals(expected, observed)
    }

    @Test
    fun `correct time in 24 hour format`() {
        val dateFormatProvider = FakeDateFormatProvider(is24HourFormat = true)
        val underTest = DefaultDateTimeInterpreter(dateFormatProvider, numberOfDays = 1)

        val expected = "01:00"
        val observed = underTest.interpretTime(date.hour)

        assertEquals(expected, observed)
    }
}

internal class FakeDateFormatProvider(
    override val is24HourFormat: Boolean = false
) : DateFormatProvider
