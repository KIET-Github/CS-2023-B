package com.example.finalprojectv1.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectv1.R
import com.example.finalprojectv1.activities.ChatMessage
import com.example.finalprojectv1.activities.tripAdapter
import com.example.finalprojectv1.utils.ChatTripDetail
import com.example.finalprojectv1.utils.UserNameLocation
import com.example.finalprojectv1.utils.gMapIntent
import androidx.core.content.ContextCompat.startActivity

import com.example.finalprojectv1.MainActivity




class TripDetailAdapter(private val context: Context, private val DetList : ArrayList<UserNameLocation>)
    : RecyclerView.Adapter<TripDetailAdapter.MyViewHolder>(), gMapIntent {


    private lateinit var userArrayList: ArrayList<UserNameLocation>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        userArrayList = arrayListOf<UserNameLocation>()
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.chat_name,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = DetList[position]

//        holder.sourceC.text = currentitem.source
//        holder.destinationC.text = currentitem.destination
//        holder.dateC.text = currentitem.date
//        holder.timeC.text = currentitem.time
        holder.name.text = currentItem.name

        holder.name.setOnClickListener {

            val intent = Intent( context, ChatMessage::class.java )
            intent.putExtra( "Number", currentItem.name )
            context.startActivity(intent)

        }

        holder.check_box_chat.setOnClickListener {

            if(holder.check_box_chat.isChecked){
                userArrayList.add(currentItem)
            }
            else if(userArrayList.contains(currentItem)){
                userArrayList.remove(currentItem)
            }
            Toast.makeText( context,userArrayList.size.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return DetList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

//        val sourceC : TextView = itemView.findViewById(R.id.tvSourceC)
//        val destinationC : TextView = itemView.findViewById(R.id.tvDestinationC)
//        val dateC : TextView = itemView.findViewById(R.id.tvdateC)
//        val timeC : TextView = itemView.findViewById(R.id.tvtimeC)
          val name : TextView = itemView.findViewById(R.id.tv_chatNames)
        val check_box_chat: CheckBox = itemView.findViewById(R.id.check_box_chat)


    }

    override fun onIntent(start: String, end: String) {
        startIntent(start, end);
    }


    fun startIntent( src: String, dest: String ){
//        val uri =
//            "http://maps.google.com/maps?daddr=12.972442,77.580643" + "+to:12.9747,77.6094+to:12.9365,77.5447+to:12.9275,77.5906+to:12.9103,77.645"
//

        var str = ""
        for(  args in userArrayList ){

            val tempUser = ( args.loc )?.split("$")?.toTypedArray()

            if(tempUser != null && args.loc != "N/A") {
                var temp = tempUser.get(1);
                temp = temp.substring( 0, temp.length-1 );
                str = str + "|" + tempUser.get(0) + "," + temp
            }
        }

//        val gmmIntentUri = Uri.parse(
//            "http://maps.google.com/maps?saddr="+ src + str +  "&daddr=" + dest
//        )

        val gmmIntentUri = Uri.parse(
            "https://www.google.com/maps/dir/?api=1&origin=" + src + "5&destination=" +
                    dest + "&waypoints=" + str + "&travelmode=driving"
        )

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")
        Log.d("tag", "https://www.google.com/maps/dir/?api=1&origin=" + src + "5&destination=" +
                dest + "&waypoints=" + str + "&travelmode=driving" );
        // Attempt to start an activity that can handle the Intent
        context.startActivity(mapIntent)

    }

}

