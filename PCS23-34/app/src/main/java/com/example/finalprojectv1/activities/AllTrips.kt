

package com.example.finalprojectv1.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectv1.ProfileActivity
import com.example.finalprojectv1.R
import com.example.finalprojectv1.databinding.ActivityAllTripsBinding
import com.example.finalprojectv1.utils.OnIntentReceived
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.trip_item.*
import java.util.*
import kotlin.collections.ArrayList

class AllTrips : AppCompatActivity() {

    private lateinit var binding: ActivityAllTripsBinding

    private lateinit var database : DatabaseReference
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userArrayList: ArrayList<FetchTrips>
    private lateinit var tempArrayList:ArrayList<FetchTrips>
    private lateinit var mIntentReceived : OnIntentReceived
    private lateinit var holder: tripAdapter.MyViewHolder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllTripsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // navbar
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_Add_Trip-> startActivity(Intent( this, Trips::class.java ))
                R.id.ic_profile->startActivity(Intent( this, ProfileActivity::class.java ))
                R.id.ic_chat->startActivity(Intent( this, ChatTripListView::class.java ))
            }
            true
        }
        //navbar end

        userRecyclerview = findViewById(R.id.recyclerTrips)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<FetchTrips>()
        //initialize comment
        tempArrayList= arrayListOf<FetchTrips>()

        getUserData()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item,menu)
        val item=menu?.findItem(R.id.search_action)
        val searchView=item?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")

            }

            override fun onQueryTextChange(newText: String?): Boolean {

                tempArrayList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()){
                    userArrayList.forEach {
                        if ( it.destination?.toLowerCase(Locale.getDefault())!!.contains(searchText)){
                            tempArrayList.add(it)
                        }
                    }
                    userRecyclerview.adapter!!.notifyDataSetChanged()
                }
                else{
                    tempArrayList.clear()
                    tempArrayList.addAll(userArrayList)
                    userRecyclerview.adapter!!.notifyDataSetChanged()

                }


                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun getUserData() {

        dbref = FirebaseDatabase.getInstance().getReference("Form")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val firebaseAuth = FirebaseAuth.getInstance()
                    var phone = (firebaseAuth.currentUser?.phoneNumber).toString()

                    for (userSnapshot in snapshot.children) {

                        val user = userSnapshot.getValue(FetchTrips::class.java)

                        if (user != null) {
                            if( !user.phone.equals(phone, true) ) {
                                userArrayList.add(user)
                            }
                        }
                    }
                    //.........cmmnt2..........
                    tempArrayList.addAll(userArrayList)
                    //.....................

                   // userRecyclerview.adapter = tripAdapter(this@AllTrips,userArrayList)

                    userRecyclerview.adapter = tripAdapter(this@AllTrips,tempArrayList)
                    mIntentReceived = userRecyclerview.adapter as tripAdapter


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    var launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            var lat = "0.0"
            var long = "0.0"
            lat = result.data?.extras?.getString("Lat").toString()
            long = result.data?.extras?.getString("Long").toString()
            mIntentReceived.onIntent(lat, long, holder)
        }
    }

    fun openYourActivity(holder: tripAdapter.MyViewHolder) {
        this.holder = holder
        val intent = Intent(this, MapTriggerActivity::class.java)
        launchSomeActivity.launch(intent)
    }


}


