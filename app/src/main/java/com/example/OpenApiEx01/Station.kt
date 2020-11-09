package com.example.OpenApiEx01

data class Station(
    val MsrstnInfoInqireSvrVo: MsrstnInfoInqireSvrVo,
    val list: List<Parm>,
    val parm: Parm,
    val totalCount: Int
)