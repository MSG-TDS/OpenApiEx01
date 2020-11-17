package com.example.OpenApiEx01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        val adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val client = OkHttpClient.Builder()
            .callTimeout(10000, TimeUnit.MILLISECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://openapi.airkorea.or.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OpenApiService::class.java)

        btnGet.setOnClickListener{
            service.getMsrstnList(
                getString(R.string.apiKey),
                "JSON",0.0,0.0
            ).enqueue(object : Callback<Station> {
                override fun onResponse(call: Call<Station>, response: Response<Station>) {
                    val result = response.body() as Station
                    adapter.dataList.clear()
                    adapter.dataList.addAll(result.list)
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<Station>, t: Throwable) {
                    Log.e("xX" , t.toString())
                }
            })
        }
    }
}

interface OpenApiService {
    @GET("openapi/services/rest/MsrstnInfoInqireSvc/getMsrstnList")
    fun getMsrstnList(
        @Query("ServiceKey", encoded = true)
        serviceKey : String,
        @Query("_returnType")
        returnType : String,
        @Query("tmX")
        tmX : Double,
        @Query("tmY")
        tmY : Double
    ):Call<Station>
}