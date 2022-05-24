package com.example.ifpstaff.model

import com.google.gson.annotations.SerializedName

data class ModelCalendar (
        @SerializedName("Id")
        val Id: Long,
        @SerializedName("idUser")
        val idUser: Int,
        @SerializedName("datetime")
        val datetime: String,
        @SerializedName("note")
        val note: String
    )