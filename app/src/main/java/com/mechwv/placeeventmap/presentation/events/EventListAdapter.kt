package com.mechwv.placeeventmap.presentation.events

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mechwv.placeeventmap.databinding.EventListItemBinding
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.databinding.PlaceListItemBinding
import com.mechwv.placeeventmap.domain.model.Event

class EventListAdapter(var data : List<Event>, var mlistener: onItemClickListener) :
    RecyclerView.Adapter<EventListAdapter.EventsViewHolder>() {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventsViewHolder {
        val binding = EventListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventsViewHolder(binding, mlistener)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val event = data[position]
        Log.d("EVENT", event.toString())
        holder.binding.event = event
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: onItemClickListener) {
        mlistener = listener
    }


    class EventsViewHolder(val binding: EventListItemBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.touch.setOnClickListener {
                val id: Int = adapterPosition
                listener.onItemClick(id)
            }
        }
    }
}