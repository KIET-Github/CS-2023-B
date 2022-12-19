package com.example.finalprojectv1
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalprojectv1.databinding.ActivityAddImagesBinding
import com.example.finalprojectv1.databinding.EditProfileBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_images.*
import java.text.SimpleDateFormat
import java.util.*

class AddImages : AppCompatActivity() {

    private  lateinit var binding:ActivityAddImagesBinding
    private lateinit var ImageUri : Uri
    lateinit var Source:String
    lateinit var destination:String
     var ID:String = ""
    companion object {
        val IMAGE_REQUEST_CODE = 100;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddImagesBinding.inflate(layoutInflater)

        setContentView(binding.root)

        Source=intent.getStringExtra("Source").toString()
        destination=intent.getStringExtra("destination").toString()

        binding.img1.setOnClickListener {

            selectimages("1")
            //uploadimages()

        }
        binding.img2.setOnClickListener {
            selectimages("2")
        }


        binding.img3.setOnClickListener {
            selectimages("3")

        }
        binding.img4.setOnClickListener {
            selectimages("4")

        }

    }


    private fun uploadimages() {

        val progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Uploading File....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val imageName=Source+destination

        val storageReference= FirebaseStorage.getInstance().getReference("images/$imageName+$ID.png")

        storageReference.putFile(ImageUri).
               addOnSuccessListener {
                    Toast.makeText(this,"Successfully uploaded", Toast.LENGTH_LONG).show()
                    if(progressDialog.isShowing) progressDialog.dismiss()

                }.addOnFailureListener{
            if(progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this,"Failed ", Toast.LENGTH_LONG).show()

        }
    }

    private fun selectimages(id:String) {

        ID=id

        val intent= Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100 && resultCode==RESULT_OK)
        {
            ImageUri=data?.data!!

            if(ID=="1")
            {
                binding.img1.setImageURI(ImageUri)

            }
            else if(ID=="2")
            {
                binding.img2.setImageURI(ImageUri)

            }
            else if(ID=="3")
            {
                binding.img3.setImageURI(ImageUri)

            }
            else if(ID=="4")
            {
                binding.img4.setImageURI(ImageUri)

            }

            uploadimages()

        }
    }
}
