package com.example.finalprojectv1.activities

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Image
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectv1.ProfileActivity
import com.example.finalprojectv1.R
import com.example.finalprojectv1.utils.OnIntentReceived
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.trip_item.view.*
import kotlinx.coroutines.NonDisposableHandle.parent
import java.io.File

class tripAdapter(private val context: Context, private val TripList : ArrayList<FetchTrips>) : RecyclerView.Adapter<tripAdapter.MyViewHolder>(), OnIntentReceived {

    private  lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    val id = 1


//    private val secondActivityWithResult = mIntentInterface.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.isResultOk()){
//            if (result.data?.hasExtra(RESULT_TEXT)!!){
////                resultTextView.text = result.data!!.extras?.getString(RESULT_TEXT) ?: "No Result Provided"
//            }
//        }
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.trip_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = TripList[position]



        holder.source.text = currentitem.source
        holder.ph.text = currentitem.phone
        holder.destination.text = currentitem.destination
        holder.date.text = currentitem.date
        holder.time.text = currentitem.time
        holder.car.text = currentitem.car
        holder.seat.text = currentitem.seat
        holder.rating.text = "0"

        //....yahan par change krna h mujhe .

        if(holder.car.text=="NULL")
        {
            holder.car.visibility=View.GONE
            holder.seat.visibility=View.GONE
            holder.car_tv.visibility=View.GONE
            holder.seattv.visibility=View.GONE
            holder.c_image.visibility=View.GONE
            holder.linear_view.setBackgroundColor(Color.parseColor("#FFFF8D"))

            holder.btn.text="Chat"

        }

        val imageID = currentitem.phone.toString()

        storageReference = FirebaseStorage.getInstance().getReference("image/$imageID.png")


        val localfile= File.createTempFile("temp",".png")
        storageReference.getFile(localfile)

            .addOnSuccessListener {
                val Bitmap = BitmapFactory.decodeFile(localfile.absolutePath)

                holder.p_image.setImageBitmap(Bitmap)
//                Toast.makeText(context, "ho gyi image load", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
  //              Toast.makeText(context, "image load nhi hui ", Toast.LENGTH_SHORT)
    //                .show()

            }


        holder.p_image.setOnClickListener {

          //  val isprofile = true

            val phone = currentitem.phone
            Log.d("Acitivity",phone.toString())
            Toast.makeText( context,  phone,Toast.LENGTH_LONG).show()

            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("ExtraPhone",phone)
            //intent.putExtra("ExtraProfile",phone)
            context.startActivity(intent)

        }

        holder.myButton.setOnClickListener {

            if(context is AllTrips) {
                (context as AllTrips).openYourActivity(holder)

            }
        }


        holder.call_icon.setOnClickListener {
            val phone=currentitem.phone
            Toast.makeText(context,phone,Toast.LENGTH_LONG).show()

            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "$phone"))
            context.startActivity(intent)
        }

        holder.c_image.setOnClickListener {
//        val dest = holder.destination.text.toString()
            val sour = holder.source.toString()

            val source = currentitem.source
            val destination= currentitem.destination
            val intent = Intent(context, FetchImages::class.java)
            intent.putExtra("source",source)
            intent.putExtra("destination",destination)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return TripList.size
    }


    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

        var call_icon = itemView.call_id

        val source : TextView = itemView.findViewById(R.id.tvSource)
        val destination : TextView = itemView.findViewById(R.id.tvDestination)
        val date : TextView = itemView.findViewById(R.id.tvdate)
        val time : TextView = itemView.findViewById(R.id.tvtime)
        val ph : TextView = itemView.findViewById(R.id.tv_phone_number)
        val car : TextView = itemView.findViewById(R.id.tvCar)
        val seat : TextView = itemView.findViewById(R.id.tvSeat)
        val rating : TextView = itemView.findViewById(R.id.tvRating)
        val myButton = itemView.findViewById<Button>(R.id.BookBtnAllTrip)
        val p_image= itemView.profile_image_trips
        val c_image=itemView.CarImage
        val car_tv=itemView.car_tv
        val seattv=itemView.seat_tv
        val linear_view=itemView.card_lv
        val btn=itemView.BookBtnAllTrip

    }

    fun putData(userLocation: String, holder: MyViewHolder){

        var database = FirebaseDatabase.getInstance().getReference("List")


        val ph = holder.ph.text.toString()
        val dest = holder.destination.text.toString()
        val sour = holder.source.text.toString()
        val time = holder.time.text.toString()
        val date = holder.date.text.toString()


        val firebaseAuth = FirebaseAuth.getInstance()
        var phone = (firebaseAuth.currentUser?.phoneNumber).toString()

        if( !ph.equals(phone, true) ) {

            database.child(ph).child( dest+"_"+sour+"_"+date+"_"+time ).child(phone).setValue( userLocation ).addOnSuccessListener {
                Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context,"Failure",Toast.LENGTH_SHORT).show()
            }

            database.child(phone).child( dest+"_"+sour+"_"+date+"_"+time ).child(ph).setValue( userLocation ).addOnSuccessListener {
                Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context,"Failure",Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText( context,"Yours own trip, Booking not possible !!"
                ,Toast.LENGTH_LONG).show()
        }

        val context = context
        val intent = Intent( context, ChatTripListView::class.java)
        context.startActivity(intent)

    }

    override fun onIntent(lang: String, long: String, holder: MyViewHolder) {
        val loc = lang+"$"+long
        Toast.makeText( context, loc, Toast.LENGTH_LONG).show()
        putData( loc , holder)
    }


}