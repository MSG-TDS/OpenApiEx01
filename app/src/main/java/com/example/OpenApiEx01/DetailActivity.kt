package com.example.OpenApiEx01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var stationName : String
    private lateinit var addr : String
    private var dmX : Double = 0.0
    private var dmY : Double = 0.0
    private lateinit var item : String
    private lateinit var photo : String

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        mapView.onCreate(savedInstanceState)


        stationName  = intent.getStringExtra("stationName") as String
        addr  = intent.getStringExtra("addr") as String
        dmX  = intent.getDoubleExtra("dmX",0.0) as Double
        dmY  = intent.getDoubleExtra("dmY", 0.0) as Double
        item  = intent.getStringExtra("item") as String
        photo  = intent.getStringExtra("photo") as String

        textAddr.text = addr
        textName.text = stationName
        textLoc.text = "$dmX , $dmY"
        textServiceItem.text = item

        Glide.with(this).load(photo).into(imagePhoto)

        mapView.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {
            mMap = p0
            val latLng = LatLng(dmX, dmY)
            val marker = MarkerOptions().position(latLng).title(stationName)
            val camPosition = CameraPosition.builder().target(latLng).zoom(16f).build()

            mMap.addMarker(marker)?.showInfoWindow()
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPosition))
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}