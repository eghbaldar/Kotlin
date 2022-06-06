package com.example.ifpstaff.retrofitHandler

import com.example.ifpstaff.model.ModelLogin
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface LoginDbHelper {

    @GET("api/auth/StaffAuthentication/{username}/{password}")
    fun getAuth(
        @Path("username") user: String,
        @Path("password") pass: String
    ): Observable<ModelLogin>

}