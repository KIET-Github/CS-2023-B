package com.example.finalprojectv1.activities
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.finalprojectv1.MainActivity
import com.example.finalprojectv1.R
import com.example.finalprojectv1.databinding.ActivityProfileDataBinding
import com.example.finalprojectv1.databinding.EditProfileBinding
import com.example.finalprojectv1.utils.UserDetail
import com.example.finalprojectv1.utils.UserDetail2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.edit_profile.*
import kotlinx.android.synthetic.main.profile.*

class ProfileData : AppCompatActivity() {

    private lateinit var binding: EditProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private  lateinit var storageReference: StorageReference
    private lateinit var imageUri:Uri


    companion object {
        val IMAGE_REQUEST_CODE = 100;
    }
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = EditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth =  FirebaseAuth.getInstance()
        readData()

        binding.profileImage.setOnClickListener {

            pickImageFromGallery()
        }

        binding.btnSubmit.setOnClickListener {

            val fullName = binding.etName.text.toString()
            val email=binding.etEmailId.text.toString()
            val emergencyNumber=binding.etEmergencyNumber.text.toString()
            val PanCard=binding.etPanCard.text.toString()
            val aadharCard=binding.etAdhaarCard.text.toString()
            val profession= binding.etProfession.text.toString()
            val address=binding.etAddress.text.toString()

            val phone = (firebaseAuth.currentUser?.phoneNumber).toString()

            database = FirebaseDatabase.getInstance().getReference("Users")
            val user = UserDetail2(fullName,aadharCard,PanCard,email,phone,emergencyNumber,address,profession  )

            database.child( phone ).setValue(user).addOnSuccessListener {

                binding.etAddress.text.clear()
                binding.etAdhaarCard.text.clear()
                binding.etEmailId.text.clear()
                binding.etPanCard.text.clear()
                binding.etName.text.clear()
                binding.etEmergencyNumber.text.clear()
                binding.etProfession.text.clear()

                upload_Profile_pic()

                binding.etPhoneNumber.text.clear()
                //Toast.makeText(this@ProfileData, "Profile Data Set Successfully", Toast.LENGTH_SHORT).show()

                finish()

                startActivity(Intent( this, AllTrips::class.java ))

            }
                .addOnFailureListener{

                //Toast.makeText(this@ProfileData, "Profile Data Set Unsuccessful", Toast.LENGTH_SHORT).show()

            }

        }

    }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode==100 && resultCode== RESULT_OK){
            imageUri=data?.data!!
            binding.profileImage.setImageURI(data?.data)

        }

    }

    //yeh functoin bnaaya h mene
    private fun upload_Profile_pic() {

       // imageUri = Uri.parse("android.resource://$packageName/${R.drawable.pancard }")
        storageReference=FirebaseStorage.getInstance().getReference("image/"+firebaseAuth.currentUser?.phoneNumber+".png")
        storageReference.putFile(imageUri).addOnSuccessListener {

         //   Toast.makeText(this@ProfileData, "Profile image uploaded", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{

           //     Toast.makeText(this@ProfileData, "Profile image not uploaded", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readData(){

        val phone = (firebaseAuth.currentUser?.phoneNumber).toString()
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(phone).get().addOnSuccessListener {


            if( it.exists() ){


                val fName = it.child("name").value.toString()
                val AadharCard = it.child("Aadhaar_card").value.toString()
                val PanCard = it.child("Pan_card").value.toString()
                val EmailID = it.child("Email_id").value.toString()
                val PhoneNumber = it.child("Phone_number").value.toString()
                val EmergencyNumber = it.child("Emergency_contact_number").value.toString()
                val Address= it.child("Address").value.toString()
                val Profession = it.child("Profession").value.toString()


                val editName: EditText = findViewById(R.id.et_name)
                editName.setText(fName)

                val Aadhar: EditText = findViewById(R.id.et_Adhaar_card)
                Aadhar.setText(AadharCard)

                val EmailAddress: EditText = findViewById(R.id.et_email_id)
                EmailAddress.setText(EmailID)

                val editPanCard: EditText = findViewById(R.id.et_Pan_card)
                editPanCard.setText(PanCard)

                val editPhone: EditText = findViewById(R.id.et_Phone_Number)
                editPhone.setText(PhoneNumber)

                val editAddress: EditText = findViewById(R.id.et_Address)
                editAddress.setText(Address)

                val editprofession: EditText = findViewById(R.id.et_profession)
                editprofession.setText(Profession)

                val editEmergency: EditText = findViewById(R.id.et_emergency_number)
                editEmergency.setText(EmergencyNumber)

            }

        }.addOnFailureListener {
        }

    }

}