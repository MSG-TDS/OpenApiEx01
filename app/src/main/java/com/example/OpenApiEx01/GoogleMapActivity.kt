package com.example.OpenApiEx01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.OpenApiEx01.DataClass.AirCondition

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.main_activity.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var latLng : LatLng
    private lateinit var stationName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        stationName = intent.getStringExtra("StationName") ?: ""
        val dmX : Double = intent.getDoubleExtra("dmX", 0.0)
        val dmY : Double = intent.getDoubleExtra("dmY", 0.0)
        latLng  = LatLng(dmX, dmY)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://openapi.airkorea.or.kr/").client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val t1 = apiService.getMsrstnAcctoRltmMesureDnsty(
            getString(R.string.apiKey),
            "json",
            stationName!!,
            "DAILY",
            "1.3"
        )

        t1.enqueue(object : Callback<AirCondition> {
            override fun onResponse(call: Call<AirCondition>, response: Response<AirCondition>) {
                Log.e("fail", response.toString())
                val resultVal = response.body() as AirCondition
                val item = resultVal.list.firstOrNull()

                val stationLoc = latLng
                val cameraP = CameraPosition.builder().bearing(0f).target(stationLoc).zoom(16f).build()

                var marker = MarkerOptions()
                    .position(stationLoc).title(stationName)

                marker = marker.snippet("PM10: ${item?.pm10Grade}")

                mMap.addMarker(marker)
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraP))

            }

            override fun onFailure(call: Call<AirCondition>, t: Throwable) {
                val list = call
                val tval = t

            }
        })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        // Add a marker in Sydney and move the camera
        val stationLoc = latLng
        mMap.addMarker(MarkerOptions().position(stationLoc).title(stationName))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stationLoc))
    }
}
