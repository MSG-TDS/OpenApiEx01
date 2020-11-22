package com.example.OpenApiEx01

import android.graphics.Bitmap
import android.graphics.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var stations = listOf<StationDetail>()
    private var airConditions = listOf<AirConditionDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val client = OkHttpClient.Builder()
            .callTimeout(10000, TimeUnit.MILLISECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://openapi.airkorea.or.kr")
            .client(client)
            .build()

        val service = retrofit.create(OpenApiService::class.java)

        service.getCtprvnRltmMesureDnsty(
            getString(R.string.api_key),
            "json",
            40,
            "경남",
            "1.3"
        ).enqueue(object : Callback<AirCondition> {
            override fun onResponse(call: Call<AirCondition>, response: Response<AirCondition>) {
                val result = response.body() as AirCondition
                airConditions = result.list

                drawOnMap()
            }

            override fun onFailure(call: Call<AirCondition>, t: Throwable) {
                Log.e("xx", t.toString())
            }
        })

        service.getMsrstnList(
            getString(R.string.api_key),
            "json", "경남"
        ).enqueue(object : Callback<Station> {
            override fun onResponse(call: Call<Station>, response: Response<Station>) {
                val result = response.body() as Station
                stations = result.list

                drawOnMap()
            }

            override fun onFailure(call: Call<Station>, t: Throwable) {
                Log.e("xx", t.toString())
            }
        })
    }

    private fun drawOnMap() {

        if (stations.size > 0
            && airConditions.size > 0
        ) {

            for (sta in stations) {
                for (air in airConditions) {
                    if (air.stationName == sta.stationName) {
                        // Add a marker in Sydney and move the camera
                        val loc = LatLng(sta.dmX.toDouble(), sta.dmY.toDouble())
                        val hue: Float =
                            when (air.pm10Grade) {
                                "1" -> BitmapDescriptorFactory.HUE_BLUE
                                "2" -> BitmapDescriptorFactory.HUE_GREEN
                                "3" -> BitmapDescriptorFactory.HUE_YELLOW
                                else
                                -> BitmapDescriptorFactory.HUE_ORANGE
                            }

                        val markerOptions = MarkerOptions()
                            .position(loc)
                            .title(sta.stationName)
                            .snippet("${air.pm10Value}")
                            .icon(BitmapDescriptorFactory.defaultMarker(hue))

                        val marker = mMap.addMarker(markerOptions)
                        marker.showInfoWindow()
                    }
                }
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(stations[0].dmX.toDouble(), stations[0].dmY.toDouble())))
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                .target(LatLng( 35.03097235790337,128.30152831971645))
                .zoom(8.7f)
                .build()
            ))
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

        mMap.setOnCameraMoveListener({
            Log.e("xx", mMap.cameraPosition.toString())
        })


        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val marker = MarkerOptions()
            .position(sydney)
            .title("Marker in Sydney")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))

        val sydney1 = LatLng(-32.0, 151.0)
        val marker1 = MarkerOptions()
            .position(sydney1)
            .title("Marker in Sydney")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))

        val sydney2 = LatLng(-30.0, 151.0)
        val marker3 = MarkerOptions()
            .position(sydney2)
            .title("Marker in Sydney")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        val sydney4 = LatLng(-15.0, 151.0)
        val marker4 = MarkerOptions()
            .position(sydney4)
            .title("Marker in Sydney")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))

        mMap.addMarker(marker)
        mMap.addMarker(marker1)
        mMap.addMarker(marker3)
        mMap.addMarker(marker4)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}