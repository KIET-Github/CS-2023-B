package com.example.finalprojectv1.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectv1.R
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.finalprojectv1.MainActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapTriggerActivity  : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private var mMap: GoogleMap? = null
    internal lateinit var mLastLocation: Location
    internal var mCurrLocationMarker: Marker? = null
    var currentLocation: Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    private val REQUEST_CODE = 101
    var lat = ""
    var long = ""
    internal lateinit var mLocationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_trigger)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation()

        val currLoc = findViewById<Button>(R.id.currLoc)
        val submitLoc = findViewById<Button>(R.id.submitLoc)

        submitLoc.setOnClickListener {
            if( !long.isEmpty() && !lat.isEmpty()) {

                val intent = Intent()
                intent.putExtra("Lat", lat)
                intent.putExtra("Long", long)
                setResult(Activity.RESULT_OK,intent)
                finish()

            }else{
                Toast.makeText( this, "Select a location or Open your Location from Setting",
                    Toast.LENGTH_LONG ).show()
            }
        }

        currLoc.setOnClickListener {
            fetchLocation()
        }

    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }


        val task = fusedLocationProviderClient!!.lastLocation

        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Toast.makeText(applicationContext, currentLocation!!.latitude.toString() + "" + currentLocation!!.longitude,
                    Toast.LENGTH_SHORT).show()
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
                lat = currentLocation!!.latitude.toString()
                long = currentLocation!!.longitude.toString()
            }
        }



    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ){
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        }else{
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }

        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
//        lat = currentLocation!!.latitude.toString()
//        long = currentLocation!!.longitude.toString()
        moveMarket(latLng)

        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                Log.d("====", "latitude : " + marker.position.latitude)

                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker?.remove()
                }
                val newlatLng = LatLng(marker.position.latitude, marker.position.longitude)
//                lat = marker.position.latitude.toString()
//                long = marker.position.longitude.toString()
                moveMarket(newlatLng)
            }

            override fun onMarkerDrag(marker: Marker) {}
        })

    }

    private fun moveMarket(latLng: LatLng) {
        if (mCurrLocationMarker != null){
            mCurrLocationMarker!!.remove()
        }
        val markerOptions = MarkerOptions().position(latLng).title("I am here")
            .snippet(getTheAddress(latLng!!.latitude, latLng!!.longitude)).draggable(true)
        lat = latLng.latitude.toString()
        long = latLng.longitude.toString()
        mMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        mCurrLocationMarker = mMap?.addMarker(markerOptions)
        mCurrLocationMarker?.showInfoWindow()
    }

    private fun getTheAddress(latitude: Double, longitude: Double): String? {
        var retVal = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            retVal = addresses[0].getAddressLine(0)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return retVal
    }

    protected fun buildGoogleApiClient(){
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null){
            mCurrLocationMarker!!.remove()
        }

        val latLng = LatLng(location.latitude, location.longitude)
//        val markerOptions = MarkerOptions()
//        markerOptions.position(latLng)
//        markerOptions.title("Current Position")
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//        mCurrLocationMarker = mMap!!.addMarker(markerOptions)
//
//        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(11f))
//
//        if (mGoogleApiClient != null){
//            LocationServices.getFusedLocationProviderClient(this)
//        }

        moveMarket(latLng)

    }

    override fun onConnected(p0: Bundle?) {

        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    fun searchLocation(view: View){
        val locationSearch: EditText = findViewById(R.id.et_search)
        var location: String
        location = locationSearch.text.toString().trim()
        var addressList: List<Address>? = null

        if (location == null || location == ""){
            Toast.makeText(this, "provide location", Toast.LENGTH_SHORT).show()
        }else{
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location, 1)
            }catch (e: IOException){
                e.printStackTrace()
            }

            val address = addressList!![0]
            val latLng = LatLng(address.latitude, address.longitude)
//            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
//            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            moveMarket(latLng)


        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

}