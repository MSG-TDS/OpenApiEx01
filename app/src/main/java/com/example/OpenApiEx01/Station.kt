package com.example.OpenApiEx01

data class Station(
    val MsrstnInfoInqireSvrVo: StationDetail,
    val list: List<StationDetail>,
    val parm: StationDetail,
    val totalCount: Int
)