package com.example.ifpstaff.retrofitHandler

import com.example.ifpstaff.model.ModelApk
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface ApkDbHandler {

    @GET("api/Staffapk")
    fun getApk(): Observable<List<ModelApk>>

}