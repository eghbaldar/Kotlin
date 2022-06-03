package com.example.ifpstaff.model

import com.google.gson.annotations.SerializedName

data class ModelCalendarOverview(
    @SerializedName("Day")
    val day: String,
    @SerializedName("Count")
    val count: String
)