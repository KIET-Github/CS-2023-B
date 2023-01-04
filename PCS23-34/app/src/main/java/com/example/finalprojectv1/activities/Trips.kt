package com.example.finalprojectv1.activities

import android.app.*
import java.util.Date;
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectv1.AddImages
import com.example.finalprojectv1.ProfileActivity
import com.example.finalprojectv1.R
import com.example.finalprojectv1.databinding.ActivityTripsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_trips.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class Trips : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar: Calendar
    lateinit var dateSetListner: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    private lateinit var binding: ActivityTripsBinding
    private lateinit var database: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var firebaseAuth: FirebaseAuth
    var userArrayList = arrayListOf<FetchTrips>()
    private lateinit var notification: Notification

    var CHANNEL_ID = "Channel_id"
    val CHANNEL_NAME = "Channel_name"

    val Notification_Id = 0

    var flag=0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)
        binding = ActivityTripsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

        flag=0

        binding.addImages.setOnClickListener {
            val source = binding.sourceEt.text.toString()
            val destination = binding.destinationEt.text.toString()

            val intent = Intent(this, AddImages::class.java)
            intent.putExtra("Source", source)
            intent.putExtra("destination", destination)
            startActivity(intent)

        }


        date_et.setOnClickListener(this)
        time_et.setOnClickListener(this)


        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                //R.id.ic_Add_Trip-> startActivity(Intent( this, Trips::class.java ))
                R.id.ic_profile -> startActivity(Intent(this, ProfileActivity::class.java))
                R.id.ic_All_Trip -> startActivity(Intent(this, AllTrips::class.java))
                R.id.ic_chat -> startActivity(Intent(this, ChatList::class.java))
            }
            true
        }
        //navbar end


        firebaseAuth = FirebaseAuth.getInstance()
        userRecyclerview = findViewById(R.id.user_Trips)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)
        getUserData()


        var checkBox:CheckBox = CheckBox(this)
        binding.haveCar.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
            {


                binding.carET.visibility=View.GONE
                binding.carTV.visibility=View.GONE
                binding.seatET.visibility=View.GONE
                binding.seatTv.visibility=View.GONE
                binding.addImages.visibility=View.GONE
            }
            else{
                binding.carET.visibility=View.VISIBLE
                binding.carTV.visibility=View.VISIBLE
                binding.seatET.visibility=View.VISIBLE
                binding.seatTv.visibility=View.VISIBLE
                binding.addImages.visibility=View.VISIBLE

            }
        }


        // userArrayList = arrayListOf<FetchTrips>()

        binding.submitBtn.setOnClickListener {
            val source = binding.sourceEt.text.toString()
            val destination = binding.destinationEt.text.toString()
            val date = binding.dateEt.text.toString()
            val time = binding.timeEt.text.toString()
            val phone = (firebaseAuth.currentUser?.phoneNumber).toString()
            var car = binding.carET.text.toString()
            var seat = binding.seatET.text.toString()



            if(binding.haveCar.isChecked)
            {

                    car="NULL"
                    seat="NULL"

//                binding.carET.visibility=View.GONE
//                binding.carTV.visibility=View.GONE
//                binding.seatET.visibility=View.GONE
//                binding.seatTv.visibility=View.GONE
//                binding.addImages.visibility=View.GONE




            }

            val form = Form(source, destination, date, time, phone, car, seat, "0")

            database = FirebaseDatabase.getInstance().getReference("Form")

            database.child(destination).setValue(form).addOnSuccessListener {
                binding.sourceEt.text.clear()
                binding.destinationEt.text.clear()
                binding.dateEt.text?.clear()
                binding.timeEt.text?.clear()
                binding.carET.text.clear()
                binding.seatET.text.clear()
                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
            }

            userArrayList.clear()
            //getUserData()
            userRecyclerview?.adapter?.notifyDataSetChanged()
            //getUserData()
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.RED
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

        }
    }

    private fun refresh_List() {

        if (userArrayList.size > 0) {
            sort(userArrayList)
            userRecyclerview.adapter = tripAdapter(this@Trips, userArrayList)
        }
    }

    private fun getUserData() {
        database = FirebaseDatabase.getInstance().getReference("Form")

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (userSnapshot in snapshot.children) {


                        val user = userSnapshot.getValue(FetchTrips::class.java)
                        if (user!!.phone == (firebaseAuth.currentUser?.phoneNumber).toString()
                            && user.phone != null
                        )
                            userArrayList.add(user)
                    }
                }

                //...............yha se kra h new change...........
                val sdf = SimpleDateFormat("EEE,dd MMM yyyy")

                val currentDate = sdf.format(Date())
                var Cd: String = sdf.format(Date())

                Log.d("hey", "today is $currentDate")

                var cd = (currentDate[4].toString() + currentDate[5].toString())


                Log.d("hey", userArrayList.size.toString())

                if(flag==0) {

                    for (item in userArrayList) {

                        Log.d("hey", item.date.toString())

                        var n = item.date.toString()

                        var a=n.compareTo(currentDate)
                       // var b= Cd?.compareTo(n2)
                        Log.d("taga","hello mr lose r:  "+a.toString());

                        var od = (n[5].toString() + n[6].toString())




                        if (n.compareTo(currentDate) < 0) {

                            var info = item.source + " to " + item.destination
                            sendNotification(info)
                            val notification_manager = NotificationManagerCompat.from(applicationContext)

                            var m = (Date().time / 1000L % Int.MAX_VALUE).toInt()

                            notification_manager.notify(m, notification)
                            Thread.sleep(500)
                        }

//                    Log.d("hey", "today is $cd and $od")

                    }
                    flag=1
                }

                refresh_List()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun sort(users: ArrayList<FetchTrips>) {
        val comparator = Comparator { o1: FetchTrips, o2: FetchTrips ->
            return@Comparator dateValidator(o1.date.toString(), o2.date.toString())
        }
        val copy = arrayListOf<FetchTrips>().apply { addAll(users) }
        copy.sortWith(comparator)
        userArrayList = copy
    }

    fun dateValidator(s1: String, s2: String): Int {

        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date1 = formatter.parse(s1)
            val date2 = formatter.parse(s2)
            if (date1.before(date2))
                return 1
            return 0
        } catch (ignored: java.text.ParseException) {
            return 0
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.date_et -> {
                setListner()
            }
            R.id.time_et -> {
                timeSetListner()
            }
        }
    }

    private fun timeSetListner() {
        myCalendar = Calendar.getInstance()

        timeSetListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, min: Int ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalendar.set(Calendar.MINUTE, min)
                updateTime()
            }
        val timePickerDialog = TimePickerDialog(
            this, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()
    }

    private fun updateTime() {
        val myformat = "h:mm a"
        val sdf = SimpleDateFormat(myformat)
        time_et.setText(sdf.format(myCalendar.time))


    }

    private fun setListner() {
        myCalendar = Calendar.getInstance()

        dateSetListner =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate()
            }
        val datePickerDialog = DatePickerDialog(
            this, dateSetListner, myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {

        val myformat = "EEE,dd MMM yyyy"
        val sdf = SimpleDateFormat(myformat)
        date_et.setText(sdf.format(myCalendar.time))

        time_et.visibility = View.VISIBLE
        time_tv.visibility = View.VISIBLE
    }

    private fun sendNotification(msg: String) {

        val intent = Intent(this, Trips::class.java)

        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Travelling Buddy")
            .setContentText("please confirm members for $msg trip as soon as possible")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(false)
            .build()

    }
}


