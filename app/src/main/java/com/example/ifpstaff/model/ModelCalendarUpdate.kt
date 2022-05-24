package com.example.ifpstaff.model

import com.google.gson.annotations.SerializedName

data class ModelCalendarUpdate (
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("note")
    val note: String
)