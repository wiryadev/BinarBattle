package com.wiryadev.binarbattle.entity


import com.google.gson.annotations.SerializedName

data class DataLogin(
    @SerializedName("email")
    val email: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("username")
    val username: String
)