package com.example.busschedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.busschedule.database.schedule.Schedule
import com.example.busschedule.databinding.BusStopItemBinding
import java.text.SimpleDateFormat
import java.util.*

class BusStopAdapter(private val onItemClicked: (Schedule) -> Unit) :
    ListAdapter<Schedule, BusStopAdapter.BusStopViewHolder>(DiffCallback) {

    class BusStopViewHolder(private var binding: BusStopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: Schedule) {
            binding.stopNameTextView.text = schedule.stopName
            binding.arrivalTimeTextView.text = SimpleDateFormat(
                "h:mm a", Locale.getDefault()
            ).format(Date(schedule.arrivalTime.toLong() * 1000))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopViewHolder {
        return BusStopViewHolder(
            BusStopItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
        val schedule = getItem(position)
        holder.bind(schedule)
        holder.itemView.setOnClickListener {
            onItemClicked(schedule)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }

    }

}