package com.example.finalprojectv1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectv1.Adapters.TripDetailAdapter
import com.example.finalprojectv1.ProfileActivity
import com.example.finalprojectv1.R
import com.example.finalprojectv1.databinding.ActivityChatListBinding
import com.example.finalprojectv1.utils.UserNameLocation
import com.example.finalprojectv1.utils.gMapIntent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import kotlin.collections.ArrayList

class ChatList : AppCompatActivity(){

    private lateinit var binding : ActivityChatListBinding
    private lateinit var database : DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var mIntentReceived : gMapIntent
    private lateinit var userArrayList: ArrayList<UserNameLocation>
    private lateinit var tripStr : String
    private lateinit var src : String
    private lateinit var dest : String

    private val TAG = "CHAT_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // navbar
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_Add_Trip-> startActivity(Intent( this, Trips::class.java ))
                R.id.ic_profile->startActivity(Intent( this, ProfileActivity::class.java ))
                R.id.ic_All_Trip-> startActivity(Intent(this,AllTrips::class.java))
//                R.id.ic_chat->startActivity(Intent( this, ChatList::class.java ))
            }
            true
        }
        //navbar end

        tripStr = intent.getStringExtra("Key").toString()
        val tempLoc = ( tripStr ).split("_").toTypedArray()
        try{
            src = tempLoc.get(0)
            dest = tempLoc.get(1)
        }catch(e: Exception){
            src = "N/A"
            dest = "N/A"
        }


        firebaseAuth = FirebaseAuth.getInstance()

        userRecyclerview = findViewById(R.id.recyclerChatTrips)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<UserNameLocation>()
        tripData()

        floating_intent_button.setOnClickListener {
            mIntentReceived.onIntent( src, dest );
        }

    }


    private fun tripData() {

        var database = FirebaseDatabase.getInstance().getReference("List")

        database.child( (firebaseAuth.currentUser?.phoneNumber).toString() ).addValueEventListener(
            object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    userArrayList.clear()

                    for (userSnapshot in snapshot.children) {

                        if( userSnapshot.key.toString() != tripStr)
                            continue;

                        val tempUser = userSnapshot.getValue().toString()
                        Log.d(TAG, "Received Data: $tempUser\n")

                        userArrayList.add( UserNameLocation( tempUser.substring(1,14),
                            tempUser.substring(15) ) )

//                        user = tempUser.split("_").toTypedArray()
//                        userArrayList.add( ChatTripDetail( user[0], user[1], user[2], user[3]) )
//                        Toast.makeText(this@ChatList, tempUser, Toast.LENGTH_LONG).show()

                    }

                    userRecyclerview.adapter = TripDetailAdapter(this@ChatList,userArrayList)
                    mIntentReceived = userRecyclerview.adapter as TripDetailAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}