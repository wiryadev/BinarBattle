package com.wiryadev.binarbattle.entity


import com.google.gson.annotations.SerializedName

data class UpdateData(
    @SerializedName("email")
    val email: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("photo")
    val photo: String?,
)