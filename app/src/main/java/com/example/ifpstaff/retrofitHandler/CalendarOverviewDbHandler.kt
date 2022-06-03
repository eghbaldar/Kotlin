package com.example.ifpstaff.retrofitHandler

import com.example.ifpstaff.model.ModelCalendar
import com.example.ifpstaff.model.ModelCalendarOverview
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface CalendarOverviewDbHandler {

    @GET("api/calOverview/staffcalOverview")
    fun getCalOverview(
    ): Observable<List<ModelCalendarOverview>>

}