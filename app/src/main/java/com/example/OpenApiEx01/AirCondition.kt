package com.example.OpenApiEx01

data class AirCondition(
    val ArpltnInforInqireSvcVo: AirConditionDetail,
    val list: List<AirConditionDetail>,
    val parm: AirConditionDetail,
    val totalCount: Int
)