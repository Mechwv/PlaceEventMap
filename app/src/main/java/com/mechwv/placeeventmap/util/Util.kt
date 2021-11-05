package com.mechwv.placeeventmap.util

import android.provider.CalendarContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Util {

    companion object {

        fun<T: Any> ioThenMain(work: suspend (() -> T?), callback: ((T?)->Unit)) =
            CoroutineScope(Dispatchers.Main).launch {
                val data = CoroutineScope(Dispatchers.IO).async  rt@{
                    return@rt work()
                }.await()
                callback(data)
            }

        // Permissions constants
        const val CALENDAR_PERMISSIONS: Int = 0

        // Calendar Projection Array
        val CALENDAR_PROJECTION: Array<String> = arrayOf(
            CalendarContract.Calendars._ID,                         // 0
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME        // 1
        )

        // Event Projection Array
        val EVENT_PROJECTION: Array<String> = arrayOf(
            CalendarContract.Events._ID,                            // 0
            CalendarContract.Events.CALENDAR_ID,                    // 1
            CalendarContract.Events.DISPLAY_COLOR,                  // 2
            CalendarContract.Events.TITLE,                          // 3
            CalendarContract.Events.DESCRIPTION,                    // 4
            CalendarContract.Events.DTSTART,                        // 5
            CalendarContract.Events.DTEND,                          // 6
            CalendarContract.Events.EVENT_LOCATION,                 // 7
            CalendarContract.Events.AVAILABILITY                    // 8
        )

//        // Event Projection Array
//        val ATTENDEE_PROJECTION: Array<String> = arrayOf(
//            CalendarContract.Attendees.ATTENDEE_NAME,               // 0
//            CalendarContract.Attendees.ATTENDEE_EMAIL               // 1
//        )

        // Projection arrays indices
        const val PROJECTION_CALENDAR_ID_INDEX: Int = 0
        const val PROJECTION_CALENDAR_DISPLAY_NAME_INDEX: Int = 1
        const val PROJECTION_EVENT_ID_INDEX: Int = 0
        const val PROJECTION_EVENT_CALENDAR_ID_INDEX: Int = 1
        const val PROJECTION_EVENT_DISPLAY_COLOR_INDEX: Int = 2
        const val PROJECTION_EVENT_TITLE_INDEX: Int = 3
        const val PROJECTION_EVENT_DESCRIPTION_INDEX: Int = 4
        const val PROJECTION_EVENT_DATE_START_INDEX: Int = 5
        const val PROJECTION_EVENT_DATE_END_INDEX: Int = 6
        const val PROJECTION_EVENT_LOCATION_INDEX: Int = 7
        const val PROJECTION_EVENT_AVAILABILITY_INDEX: Int = 8
        const val PROJECTION_ATTENDEE_NAME_INDEX: Int = 0
        const val PROJECTION_ATTENDEE_EMAIL_INDEX: Int = 1
    }
}