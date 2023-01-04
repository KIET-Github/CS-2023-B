package com.example.finalprojectv1.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectv1.R
import com.example.finalprojectv1.utils.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context: Context, private val DetList : ArrayList<Message>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val item_sent = 2
    val item_received = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if( viewType == 1 ){

            val view: View = LayoutInflater.from(parent.context).inflate( R.layout.message_received,
                parent,false)

            return ReceiveViewHolder(view)

        }else{

            val view: View = LayoutInflater.from(parent.context).inflate( R.layout.message_sent,
                parent,false)

            return SentViewHolder(view)

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = DetList[position]

        if( holder.javaClass == SentViewHolder::class.java ){
            //do stuff for sent holder
            val viewHolder = holder as SentViewHolder
            viewHolder.sendMessage.text = currentMessage.message

        }else{
            //do stuff for sent holder
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text = currentMessage.message

        }

    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = DetList[position]

        if( FirebaseAuth.getInstance().currentUser?.phoneNumber.equals( currentMessage.userId ) )
            return item_sent

        return item_received

    }

    override fun getItemCount(): Int {
        return DetList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sendMessage : TextView = itemView.findViewById(R.id.txt_sent_message)

    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val receiveMessage : TextView = itemView.findViewById(R.id.txt_receive_message)

    }

}