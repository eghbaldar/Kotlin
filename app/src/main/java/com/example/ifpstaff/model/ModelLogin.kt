package com.example.ifpstaff.model

import com.google.gson.annotations.SerializedName

data class ModelLogin(
    @SerializedName("auth")
    var auth: String
)