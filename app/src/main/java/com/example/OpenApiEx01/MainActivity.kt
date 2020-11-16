package com.example.OpenApiEx01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.OpenApiEx01.DataClass.AirCondition
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

        btnGet.setOnClickListener {
            val apiService = retrofit.create(ApiService::class.java)
            val t1 = apiService.getMsrstnList(
                getString(R.string.apiKey),
                "json",
                35.232084, 128.915356
            )

            t1.enqueue(object : Callback<Station> {
                override fun onResponse(call: Call<Station>, response: Response<Station>) {

                    val resultVal = response.body() as Station

                    val list = resultVal.list as List<Parm>

                    adapter.dataList.addAll(list)
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<Station>, t: Throwable) {
                    Log.e("fail", t.toString())
                    val list = call
                    val tval = t

                }
            })
        }
    }
}

interface ApiService {
    @GET("openapi/services/rest/MsrstnInfoInqireSvc/getMsrstnList")
    fun getMsrstnList(
        @Query("ServiceKey", encoded = true) serviceKey: String,
        @Query("_returnType") returnType: String,
        @Query("tmX") tmX: Double,
        @Query("tmY") tmY: Double
    ): Call<Station>

    @GET("openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty")
    fun getMsrstnAcctoRltmMesureDnsty(
        @Query("ServiceKey", encoded = true) serviceKey: String,
        @Query("_returnType") returnType: String,
        @Query("stationName", encoded = true) stationName: String,
        @Query("dataTerm") dataTerm: String,
        @Query("ver") ver: String
    ): Call<AirCondition>
}