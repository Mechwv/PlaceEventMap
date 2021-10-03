package com.example.placeeventmap.presentation.events

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class CalendarHandler {
    fun useCalendar(calendar: Calendar, context: Context): Long? {
        val datetime = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, calendar.timeInMillis)
            put(CalendarContract.Events.DTEND,calendar.timeInMillis + 60000*60)
            put(CalendarContract.Events.TITLE, "Dance club")
            put(CalendarContract.Events.DESCRIPTION, "Group workout")
            put(CalendarContract.Events.CALENDAR_ID, 3)
            put(
                CalendarContract.Events.EVENT_TIMEZONE,
                CalendarContract.CalendarCache.TIMEZONE_TYPE_AUTO
            )
        }
        var eventId: Long? = null
        runBlocking {
            launch {
                val contentResolver = context.contentResolver
                val uri: Uri? = contentResolver?.insert(CalendarContract.Events.CONTENT_URI, datetime)
                eventId = uri?.lastPathSegment?.toLong()
            }
        }.isCompleted
        return eventId
    }
}