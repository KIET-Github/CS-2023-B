package com.example.finalprojectv1.activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalprojectv1.R
import com.example.finalprojectv1.databinding.ActivityAddImagesBinding
import com.example.finalprojectv1.databinding.ActivityFetchImagesBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class FetchImages : AppCompatActivity() {
    lateinit var Source:String
    lateinit var destination:String
    private  lateinit var storageReference: StorageReference

    private lateinit var binding:ActivityFetchImagesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFetchImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Source=intent.getStringExtra("source").toString()
        destination=intent.getStringExtra("destination").toString()

        val imageID=Source+destination

        for (i in 1..4){

            storageReference = FirebaseStorage.getInstance().getReference("images/$imageID+$i.png")
            Log.d("fetchImg",imageID+i+".png")

            val localfile= File.createTempFile("temp",".png")
            storageReference.getFile(localfile)

                .addOnSuccessListener {
                    val Bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                    if(i==1)
                    {
                        binding.Fetchimg1.setImageBitmap(Bitmap)

                    }
                    else if(i==2)
                    {
                        binding.Fetchimg2.setImageBitmap(Bitmap)

                    }
                    else if(i==3)
                    {
                        binding.Fetchimg3.setImageBitmap(Bitmap)

                    }
                    else if(i==4)
                    {
                        binding.Fetchimg4.setImageBitmap(Bitmap)

                    }
//                    Toast.makeText(this, "Car image  load ho gyi", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener{
  //                  Toast.makeText(this, "Car image Load nhi hui ", Toast.LENGTH_SHORT).show()



                }


        }




    }
}