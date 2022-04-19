package com.wiryadev.binarbattle.entity


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("data")
    val dataRegister: DataRegister,
    @SerializedName("success")
    val success: Boolean
)