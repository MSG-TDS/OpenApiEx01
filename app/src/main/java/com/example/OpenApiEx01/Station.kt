package com.example.OpenApiEx01

data class Station(
    val msrstnInfoInqireSvrVo: StationDetail,
    val list: List<StationDetail>,
    val stationDetail: StationDetail,
    val totalCount: Int
)