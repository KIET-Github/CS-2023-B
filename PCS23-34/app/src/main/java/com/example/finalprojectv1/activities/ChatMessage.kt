package com.example.finalprojectv1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectv1.Adapters.MessageAdapter
import com.example.finalprojectv1.R
import com.example.finalprojectv1.databinding.ActivityAllTripsBinding
import com.example.finalprojectv1.databinding.ActivityChatMessageBinding
import com.example.finalprojectv1.utils.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatMessage : AppCompatActivity() {

    private lateinit var binding: ActivityChatMessageBinding
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var database: DatabaseReference

    var senderRoom : String?=null
    var receiverRoom : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityChatMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference()

        val receiverUid = intent.getStringExtra("Number")
        val senderUid = FirebaseAuth.getInstance().currentUser?.phoneNumber

        receiverRoom = senderUid + receiverUid
        senderRoom = receiverUid + senderUid

        supportActionBar?.title = receiverUid

        messageBox = binding.etMessageBox
        sendButton = binding.sendButtom

        messageRecyclerView = binding.messageRecyclerView
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        //adding message to recycler view
        database.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for( postSnapshot in snapshot.children ){

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)

                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })




        // adding message to database
        sendButton.setOnClickListener{

            val message = messageBox.text.toString()
            val messageObject = Message( message, senderUid!! )

            database.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {

                    database.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)

                }
            messageBox.setText("")
        }

    }
}

