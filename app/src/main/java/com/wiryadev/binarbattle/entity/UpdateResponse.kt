package com.wiryadev.binarbattle.entity


import com.google.gson.annotations.SerializedName

data class UpdateResponse(
    @SerializedName("data")
    val updateData: UpdateData,
    @SerializedName("success")
    val success: Boolean
)