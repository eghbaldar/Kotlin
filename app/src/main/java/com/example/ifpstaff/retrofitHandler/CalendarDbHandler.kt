package com.example.ifpstaff.retrofitHandler

import com.example.ifpstaff.model.ModelCalendar
import com.example.ifpstaff.model.ModelCalendarUpdate
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*
import retrofit2.http.POST

interface CalendarDbHandler {

    //Selected Based Id (Each of record)
    @GET("api/StaffCalendarGeneral/{selectedDate}")
    fun getProfiles(
        @Path("selectedDate") selectedDate: String
    ): Observable<List<ModelCalendar>>

    //Selected Based Id (Each of record)
    @GET("api/StaffCalendarGeneral/each/{id}")
    fun getOneProfile(
        @Path("id") id: Long
    ): Observable<List<ModelCalendar>>

    //Insert (Create)
    @POST("api/StaffCalendarGeneral")
    fun createProfile(
        @Body profile: ModelCalendar
    ): Completable

    //Delete
    @DELETE("api/StaffCalendarGeneral/each/{id}")
    fun deleteProfile(
        @Path("id") Id: Long
    ): Completable

    //Update
    @PUT("api/StaffCalendarGeneral/each/{id}")
    fun updateProfile(
        @Path("id") Id: Long,
        @Body profile: ModelCalendarUpdate
    ): Observable<List<ModelCalendar>>
}