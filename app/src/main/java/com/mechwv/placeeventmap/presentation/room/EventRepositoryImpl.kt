package com.mechwv.placeeventmap.presentation.room

import androidx.lifecycle.LiveData
import com.mechwv.placeeventmap.data.repository.EventRepository
import com.mechwv.placeeventmap.domain.model.Event
import com.mechwv.placeeventmap.presentation.room.dao.EventDao
import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO
import java.util.concurrent.*

class EventRepositoryImpl
constructor(
    private val eventDao: EventDao
)  : EventRepository {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private var events: LiveData<List<DBEventDTO>> = eventDao.getAll()

    override fun addEvent(event: Event) {
        executorService.execute {
            eventDao.insert(DBEventDTO(event))
        }
    }

    override fun updateEvent(event: Event) {
        executorService.execute {
            eventDao.updateEvent(
                id = event.id,
                desc = event.description.toString(),
                name = event.name,
                startTime = event.eventStartTime
            )
        }
    }

    override fun addEventWithReturn(event: Event): Long {
        val insertCallable = Callable { eventDao.insertWithReturn(DBEventDTO(event)) }
        var rowId: Long = 0

        val future: Future<Long> = executorService.submit(insertCallable)
        try {
            rowId = future.get()
        } catch (e1: InterruptedException) {
            e1.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return rowId
    }

    override fun getEvent(id: Long): LiveData<Event> {
        return eventDao.getOne(id) as LiveData<Event>
    }

    override fun updatePlaceEvent(id: Long, place_id: Int?) {
        executorService.execute {
            eventDao.updatePlaceEvent(id, place_id)
        }
    }


    override fun getEvents(): LiveData<List<Event>> {
        return events as LiveData<List<Event>>
    }

    override fun deleteEvent(event: Event) {
        executorService.execute {
            eventDao.delete(event as DBEventDTO)
        }
    }


}