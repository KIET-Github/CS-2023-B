
package com.example.finalprojectv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalprojectv1.activities.AllTrips
import com.example.finalprojectv1.activities.ChatList
import com.example.finalprojectv1.activities.Trips
import com.example.finalprojectv1.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // view binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_Add_Trip-> startActivity(Intent( this, Trips::class.java ))
                R.id.ic_profile->startActivity(Intent( this, ProfileActivity::class.java ))
                R.id.ic_chat->startActivity(Intent( this, ChatList::class.java ))
                R.id.ic_All_Trip-> startActivity(Intent(this,AllTrips::class.java))
            }
            true
        }
//
//        binding.profileBtn.setOnClickListener {
//            startActivity(Intent( this, ProfileActivity::class.java ))
//        }
//
//        binding.addTripsBtn.setOnClickListener {
//            startActivity(Intent(this, Trips::class.java))
//        }
//
//        binding.allTripsBtn.setOnClickListener {
//            startActivity(Intent(this,AllTrips::class.java))
//        }

    }
}
