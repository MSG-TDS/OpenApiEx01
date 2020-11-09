package com.example.OpenApiEx01

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val PERM_LOCATION: Int = 99
    private lateinit var mMap: GoogleMap
    val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    private lateinit var fusedLocationClient:FusedLocationProviderClient
    private lateinit var locationCallback:LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission()
    }

    fun checkPermission(){
        var permitted_all = true
        for(permission in permissions){
            val result =  ContextCompat.checkSelfPermission(this, permission)
            if(result != PackageManager.PERMISSION_GRANTED){
                permitted_all = false
                requestPermission()
                break
            }
        }
        if(permitted_all){
            startProcess()
        }
    }

    private fun startProcess() {
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,permissions, PERM_LOCATION)
    }

    private fun confirmAgain(){
        AlertDialog.Builder(this)
            .setTitle("권한 승인 확인")
            .setMessage("권한승인 해야 합니다. 하시겠습니까?")
            .setPositiveButton("네"){ _,_ ->
                requestPermission()
            }
            .setNegativeButton("아니오"){ _, _ ->
                finish()
            }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            99 -> {
                var granted_all = true
                for (result in grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        granted_all = false
                        break
                    }
                }
                if(granted_all){
                    startProcess()
                }
                else{
                    confirmAgain()
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val taekwang = LatLng(35.232240, 128.913027)
        val positionTaeKeang = CameraPosition.Builder()
                .target(taekwang)
                .bearing(0f)
                .tilt(60f)
                .zoom(15f)
                .build()

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(positionTaeKeang))
        mMap.addMarker(MarkerOptions().position(positionTaeKeang.target).title("TeaKwang"))

        val louvre = LatLng(48.860692, 2.338333)
        val positionlouvre = CameraPosition.Builder()
            .target(louvre)
            .bearing(0f)
            .tilt(60f)
            .zoom(20f)
            .build()

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(positionlouvre))
        mMap.addMarker(MarkerOptions().position(positionlouvre.target).title("루브르").snippet("추가정보"))


    }

    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.run{
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                p0?.let{
                    for((i, location) in it.locations.withIndex()){
                        Log.d("Location", "$i ${location.latitude}, ${location.longitude}")
                        setLastLocation(location)
                    }
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun setLastLocation(location: Location?) {
        val LATLNG = LatLng(location?.latitude!!, location?.longitude)
        val markerOptions = MarkerOptions()
            .position(LATLNG)
            .title("here!")
        val cameraPosition = CameraPosition.builder()
            .target(LATLNG)
            .zoom(15f)
            .build()
        mMap.clear()
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }


}