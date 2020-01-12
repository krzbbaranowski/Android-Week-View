package com.alamkanak.weekview

import com.alamkanak.weekview.Constants.DAY_IN_MILLIS
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

internal interface Duration {
    val inMillis: Int
}

internal inline class Days(val days: Int) : Duration {
    override val inMillis: Int
        get() = days * (24 * 60 * 60 * 1_000)
}

internal inline class Hours(val hours: Int) : Duration {
    override val inMillis: Int
        get() = hours * (60 * 60 * 1_000)
}

internal inline class Millis(val millis: Int) : Duration {
    override val inMillis: Int
        get() = millis
}

internal operator fun Calendar.plus(days: Days): Calendar {
    return copy().apply {
        add(Calendar.DATE, days.days)
    }
}

internal operator fun Calendar.plus(hours: Hours): Calendar {
    return copy().apply {
        add(Calendar.HOUR_OF_DAY, hours.hours)
    }
}

internal operator fun Calendar.plus(millis: Millis): Calendar {
    return copy().apply {
        add(Calendar.MILLISECOND, millis.millis)
    }
}

internal operator fun Calendar.minus(days: Days): Calendar {
    return copy().apply {
        add(Calendar.DATE, days.days * (-1))
    }
}

internal operator fun Calendar.minus(hours: Hours): Calendar {
    return copy().apply {
        add(Calendar.HOUR_OF_DAY, hours.hours * (-1))
    }
}

internal operator fun Calendar.minus(millis: Millis): Calendar {
    return copy().apply {
        add(Calendar.MILLISECOND, millis.millis * (-1))
    }
}

internal operator fun Calendar.plusAssign(days: Days) {
    add(Calendar.DATE, days.days)
}

internal operator fun Calendar.plusAssign(hours: Hours) {
    add(Calendar.HOUR_OF_DAY, hours.hours)
}

internal operator fun Calendar.plusAssign(millis: Millis) {
    add(Calendar.MILLISECOND, millis.millis)
}

internal operator fun Calendar.minusAssign(days: Days) {
    add(Calendar.DATE, days.days * (-1))
}

internal operator fun Calendar.minusAssign(hours: Hours) {
    add(Calendar.HOUR_OF_DAY, hours.hours * (-1))
}

internal operator fun Calendar.minusAssign(millis: Millis) {
    add(Calendar.MILLISECOND, millis.millis * (-1))
}

internal fun Calendar.atStartOfDay(): Calendar {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    return this
}

internal fun Calendar.atEndOfDay(): Calendar {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    return this
}

internal fun Calendar.atEndOfMonth(): Calendar {
    set(Calendar.DAY_OF_MONTH, lengthOfMonth)
    atEndOfDay()
    return this
}

internal val Calendar.hour: Int
    get() = get(Calendar.HOUR_OF_DAY)

internal val Calendar.minute: Int
    get() = get(Calendar.MINUTE)

internal val Calendar.second: Int
    get() = get(Calendar.SECOND)

internal val Calendar.month: Int
    get() = get(Calendar.MONTH)

internal val Calendar.year: Int
    get() = get(Calendar.YEAR)

internal val Calendar.dayOfWeek: Int
    get() = get(Calendar.DAY_OF_WEEK)

internal val Calendar.dayOfMonth: Int
    get() = get(Calendar.DAY_OF_MONTH)

internal val Calendar.isBeforeToday: Boolean
    get() = this < today()

internal val Calendar.isToday: Boolean
    get() = isSameDate(today())

internal fun Calendar.toEpochDays(): Int = (timeInMillis / (24 * 60 * 60 * 1_000)).toInt()

internal val Calendar.lengthOfMonth: Int
    get() = getActualMaximum(Calendar.DAY_OF_MONTH)

internal fun Calendar.withTimeAtStartOfPeriod(hour: Int): Calendar {
    return copy().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

internal fun Calendar.withTimeAtEndOfPeriod(hour: Int): Calendar {
    return copy().apply {
        set(Calendar.HOUR_OF_DAY, hour - 1)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
}

internal val Calendar.millisAtStartOfDay: Long
    get() {
        val currentMillis = timeInMillis
        val result = atStartOfDay().timeInMillis
        timeInMillis = currentMillis // Restore the current Calendar instance
        return result
    }

internal val Calendar.millisAtEndOfDay: Long
    get() = millisAtStartOfDay + DAY_IN_MILLIS - 1

internal val Calendar.daysFromToday: Int
    get() {
        val diff = (millisAtStartOfDay - today().timeInMillis).toFloat()
        return (diff / DAY_IN_MILLIS).roundToInt()
    }

internal fun today() = now().apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

internal fun now() = Calendar.getInstance()

internal inline class Timestamp(val value: Long) {
    fun toCalendar(): Calendar = newCalendar(millis = value)
}

private fun newCalendar(
    millis: Long = System.currentTimeMillis()
) = Calendar.getInstance().apply {
    timeInMillis = millis
}

internal fun newDate(year: Int, month: Int, dayOfMonth: Int): Calendar {
    return today().apply {
        set(Calendar.DAY_OF_MONTH, dayOfMonth)
        set(Calendar.MONTH, month)
        set(Calendar.YEAR, year)
    }
}

internal fun Calendar.isSameDate(other: Calendar): Boolean = toEpochDays() == other.toEpochDays()

internal fun Calendar.isNotSameDate(other: Calendar): Boolean = isSameDate(other).not()

internal fun firstDayOfYear(): Calendar {
    return today().apply {
        set(Calendar.MONTH, Calendar.JANUARY)
        set(Calendar.DAY_OF_MONTH, 1)
    }
}

internal fun createDateRange(start: Int, end: Int): List<Calendar> {
    return (start..end).map {
        today().apply { this += Days(it - 1) }
    }
}

internal val Calendar.isWeekend: Boolean
    get() = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY

internal fun Calendar.setTime(hour: Int, minutes: Int): Calendar {
    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minutes)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    return this
}

internal fun Calendar.copy(): Calendar {
    return clone() as Calendar
}

internal fun Calendar.copy(
    block: Calendar.() -> Unit
): Calendar = copy().apply { block() }

/**
 * Checks if this date is at the start of the next day after startTime.
 * For example, if the start date was January the 1st and startDate was January the 2nd at 00:00,
 * this method would return true.
 * @param startDate The start date of the event
 * @return Whether or not this date is at the start of the day after startDate
 */
internal fun Calendar.isAtStartOfNextDay(startDate: Calendar): Boolean {
    val isAtStartOfDay = timeInMillis == millisAtStartOfDay
    return if (isAtStartOfDay) {
        val lastMilliOfPreviousDay = timeInMillis - 1
        lastMilliOfPreviousDay == startDate.millisAtEndOfDay
    } else {
        false
    }
}

internal fun getDefaultDateFormat(
    numberOfDays: Int
): SimpleDateFormat = when (numberOfDays) {
    1 -> SimpleDateFormat("EEEE M/dd", Locale.getDefault()) // full weekday
    in 2..6 -> SimpleDateFormat("EEE M/dd", Locale.getDefault()) // first three characters
    else -> SimpleDateFormat("EEEEE M/dd", Locale.getDefault()) // first character
}

internal fun getDefaultTimeFormat(is24HourFormat: Boolean): SimpleDateFormat {
    val format = if (is24HourFormat) "HH:mm" else "hh a"
    return SimpleDateFormat(format, Locale.getDefault())
}

internal fun Calendar.format(
    format: Int = java.text.DateFormat.MEDIUM
): String = SimpleDateFormat.getDateInstance(format).format(time)
