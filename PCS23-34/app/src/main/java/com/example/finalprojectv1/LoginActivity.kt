package com.example.finalprojectv1

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.finalprojectv1.activities.AllTrips
import com.example.finalprojectv1.activities.ProfileData
import com.example.finalprojectv1.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    // view binding
    private lateinit var binding: ActivityLoginBinding

    // if code sending failed, will use this
    private lateinit var forceResendingToken : PhoneAuthProvider.ForceResendingToken

    private lateinit var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mVerificationId: String
    private lateinit var firebaseAuth: FirebaseAuth

    private val TAG = "LOGIN_TAG"

    // process dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE)
        val bool = sharedPreferences.getBoolean("LoggedIn", false)

        if( bool ){
            Toast.makeText(this, "Welcome Back", Toast.LENGTH_LONG).show()

            startActivity(Intent( this, AllTrips::class.java ))
            finish()
        }

        binding.phoneL1.visibility = View.VISIBLE
        binding.codeL1.visibility = View.GONE

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait!!!!!")
        progressDialog.setCanceledOnTouchOutside(false)

        mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential)

            }

            override fun onVerificationFailed(e: FirebaseException) {

                progressDialog.dismiss()
                Toast.makeText(this@LoginActivity, "${e.message}", Toast.LENGTH_SHORT).show()

            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

                Log.d(TAG, "OnCodeSent: $verificationId")
                mVerificationId = verificationId
                forceResendingToken = token
                progressDialog.dismiss()

                //Hide phone layout, and show code layout
                binding.phoneL1.visibility = View.GONE
                binding.codeL1.visibility = View.VISIBLE
                Toast.makeText(this@LoginActivity, "Verification Code Sent", Toast.LENGTH_SHORT).show()
                binding.codeSentDescriptionTv.text =
                    "Please enter code sent to ${binding.phoneEt.text.toString().trim()}"


            }

        }

        binding.phoneContinueBtn.setOnClickListener{
            val phone = binding.phoneEt.text.toString().trim()
            //validate phone no.
            if( TextUtils.isEmpty( phone ) )
                Toast.makeText(this@LoginActivity, "Please enter Phone no.", Toast.LENGTH_SHORT).show()
            else
                startPhoneNumberVerification(phone)
        }

        binding.resendCodeTv.setOnClickListener {
            val phone = binding.phoneEt.text.toString().trim()
            //validate phone no.
            if( TextUtils.isEmpty( phone ) )
                Toast.makeText(this@LoginActivity, "Please enter Phone no.", Toast.LENGTH_SHORT).show()
            else
                resendVerificationCode(phone, forceResendingToken)
        }

        binding.codeSubmitBtn.setOnClickListener {
            val code = binding.codeEt.text.toString().trim()
            if( TextUtils.isEmpty( code ) )
                Toast.makeText(this@LoginActivity, "Please enter verification code", Toast.LENGTH_SHORT).show()
            else
                verifyPhoneNumberWithCode(mVerificationId, code)

        }

    }

    private fun startPhoneNumberVerification(Phone: String){
        progressDialog.setMessage( " Verifying Phone Number..... " )
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(Phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallBacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendVerificationCode( Phone:String, token: PhoneAuthProvider.ForceResendingToken ){
        progressDialog.setMessage( " Resending Code..... " )
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(Phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallBacks)
            .setForceResendingToken(token)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode( verificationId: String, code:String ){
        progressDialog.setMessage(" Verifying Code.... ")
        progressDialog.show()

        val credentials = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential( credentials )
    }

    private fun signInWithPhoneAuthCredential( credential: PhoneAuthCredential ){
        progressDialog.setMessage(" Logging in... ")

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val phone = firebaseAuth.currentUser?.phoneNumber
                Toast.makeText(this, "Logged in as $phone", Toast.LENGTH_SHORT).show()

                saveData()
                finish()
                startActivity(Intent( this, ProfileData::class.java ))


            }

            .addOnFailureListener{ e->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun saveData(){

        val sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply{

            val phone = binding.phoneEt.text.toString().trim()
            putString("Id",phone)
            putBoolean("LoggedIn",true)

        }.apply()

    }

}


