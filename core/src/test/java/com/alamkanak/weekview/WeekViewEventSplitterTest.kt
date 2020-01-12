package com.alamkanak.weekview

import com.alamkanak.weekview.model.Event
import com.alamkanak.weekview.util.setHour
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class WeekViewEventSplitterTest {

    private val config = mock(WeekViewConfigWrapper::class.java)
    private val underTest = WeekViewEventSplitter<Event>(config)

    init {
        MockitoAnnotations.initMocks(this)
        `when`(config.minHour).thenReturn(0)
        `when`(config.maxHour).thenReturn(24)
    }

    @Test
    fun `single-day event is not split`() {
        val startTime = today().setHour(11)
        val endTime = startTime + Hours(2)
        val event = Event(startTime, endTime).toWeekViewEvent()

        val results = underTest.split(event)
        val expected = listOf(event)

        assertEquals(expected, results)
    }

    @Test
    fun `two-day event is split correctly`() {
        val startTime = today().setHour(11)
        val endTime = (startTime + Days(1)).setHour(2)

        val event = Event(startTime, endTime).toWeekViewEvent()
        val results = underTest.split(event)

        val expected = listOf(
            Event(startTime, startTime.atEndOfDay()),
            Event(endTime.atStartOfDay(), endTime)
        )

        val expectedTimes = expected.map { it.startTime.timeInMillis to it.endTime.timeInMillis }
        val resultTimes = results.map { it.startTime.timeInMillis to it.endTime.timeInMillis }

        assertEquals(expectedTimes, resultTimes)
    }

    @Test
    fun `three-day event is split correctly`() {
        val startTime = today().setHour(11)
        val endTime = (startTime + Days(2)).setHour(2)

        val event = Event(startTime, endTime).toWeekViewEvent()
        val results = underTest.split(event)

        val intermediateDate = startTime + Days(1)
        val expected = listOf(
            Event(startTime, startTime.atEndOfDay()),
            Event(intermediateDate.atStartOfDay(), intermediateDate.atEndOfDay()),
            Event(endTime.atStartOfDay(), endTime)
        )

        val expectedTimes = expected.map { it.startTime.timeInMillis to it.endTime.timeInMillis }
        val resultTimes = results.map { it.startTime.timeInMillis to it.endTime.timeInMillis }

        assertEquals(expectedTimes, resultTimes)
    }
}
