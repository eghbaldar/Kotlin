package com.example.ifpstaff.retrofitHandler

import com.example.ifpstaff.model.ModelCalendarOverview
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface CalendarOverviewDbHandler {

    @GET("api/calOverview/staffcalOverview")
    fun getCalOverview(
    ): Observable<List<ModelCalendarOverview>>

}