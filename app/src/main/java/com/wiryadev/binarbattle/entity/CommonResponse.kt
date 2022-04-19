package com.wiryadev.binarbattle.entity


import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName("data")
    val commonData: CommonData,
    @SerializedName("success")
    val success: Boolean
)