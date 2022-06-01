package com.mechwv.placeeventmap.presentation.events

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class CalendarHandler {
    companion object {
        fun useCalendar(calendar: Calendar,
                        title: String = "",
                        description: String = "",
                        context: Context): Long? {
            val datetime = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, calendar.timeInMillis)
                put(CalendarContract.Events.DTEND,calendar.timeInMillis + 60000*60)
                put(CalendarContract.Events.TITLE, title)
                put(CalendarContract.Events.DESCRIPTION, description)
                put(CalendarContract.Events.CALENDAR_ID, 3)
                put(
                    CalendarContract.Events.EVENT_TIMEZONE,
                    CalendarContract.CalendarCache.TIMEZONE_TYPE_AUTO
                )
            }
            var eventId: Long? = null
            runBlocking {
                val job = GlobalScope.launch {
                    val contentResolver = context.contentResolver
                    val uri: Uri? =
                        contentResolver?.insert(CalendarContract.Events.CONTENT_URI, datetime)
                    eventId = uri?.lastPathSegment?.toLong()
                }
                job.join()
            }
            return eventId
    }
        fun getEvent(eventID: Long, context: Context): String {
//        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
            val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID)
            val contentResolver = context.contentResolver

            val EVENT_PROJECTION: Array<String> = arrayOf(
                CalendarContract.Events.CALENDAR_ID,                     // 0
                CalendarContract.Events.TITLE,            // 1
                CalendarContract.Events.DESCRIPTION,   // 2
                CalendarContract.Calendars.OWNER_ACCOUNT            // 3
            )
            val PROJECTION_CALENDAR_ID: Int = 0
            val PROJECTION_TITLE: Int = 1
            val PROJECTION_DESCRIPTION: Int = 2
            val PROJECTION_OWNER_ACCOUNT_INDEX: Int = 3

//        val selection: String = "_id = ?"
//        val selectionArgs: Array<String> = arrayOf(eventID.toString())
//        val cur: Cursor? = contentResolver?.query(uri, EVENT_PROJECTION, selection, selectionArgs, null)
            val cur: Cursor? = contentResolver?.query(uri, EVENT_PROJECTION, "", arrayOf(), null)
            if (cur != null) {
                while (cur.moveToNext()) {
                    val calID: Long = cur.getLong(PROJECTION_CALENDAR_ID)
                    val displayName: String = cur.getString(PROJECTION_TITLE)
                    val desc: String = cur.getString(PROJECTION_DESCRIPTION)
                    return "$calID  +  $displayName  +  $desc "
                }
            }
            return ""
        }

    }
}