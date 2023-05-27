package com.example.finalprojectv1.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectv1.R
import com.example.finalprojectv1.activities.ChatList
import com.example.finalprojectv1.activities.ChatMessage
import com.example.finalprojectv1.activities.ChatTripListView
import com.example.finalprojectv1.utils.ChatTripDetail

class TripListAdapter(private val context: Context, private val DetList : ArrayList<ChatTripDetail>)
    : RecyclerView.Adapter<TripListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.chat_trip_list,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = DetList[position]

        holder.sourceC.text = currentItem.source.toString()

        holder.destinationC.text = currentItem.destination.toString()

        holder.dateC.text = currentItem.date.toString()

        holder.timeC.text = currentItem.time.toString()

        holder.itemView.setOnClickListener {

            val intent = Intent( context, ChatList::class.java )
            intent.putExtra( "Key", currentItem.getstr() )
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {

        return DetList.size

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sourceC : TextView = itemView.findViewById(R.id.tvSourceC)
        val destinationC : TextView = itemView.findViewById(R.id.tvDestinationC)
        val dateC : TextView = itemView.findViewById(R.id.tvdateC)
        val timeC : TextView = itemView.findViewById(R.id.tvtimeC)

    }


}