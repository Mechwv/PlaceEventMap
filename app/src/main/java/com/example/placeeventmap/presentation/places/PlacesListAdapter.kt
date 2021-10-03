package com.example.placeeventmap.presentation.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.placeeventmap.domain.model.Place
import com.example.placeeventmap.databinding.PlaceListItemBinding

class PlacesListAdapter(var data : List<Place>, var mlistener: onItemClickListener) :
    RecyclerView.Adapter<PlacesListAdapter.PlacesViewHolder>() {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlacesViewHolder {
        val binding = PlaceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlacesViewHolder(binding, mlistener)
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        val places = data[position]
        holder.binding.place = places
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: onItemClickListener) {
        mlistener = listener
    }


    class PlacesViewHolder(val binding: PlaceListItemBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.touch.setOnClickListener {
                val id: Int = adapterPosition
                listener.onItemClick(id)
            }
        }
    }
}