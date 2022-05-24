package com.example.ifpstaff.fragments.calendar

import android.content.Context
import android.graphics.Typeface
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.text.set
import androidx.core.text.toSpannable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ifpstaff.general.ShamsiDate
import com.example.ifpstaff.model.ModelCalendar
import com.example.ifpstaff.retrofitHandler.CalendarDbHandler
import com.example.ifpstaff.retrofitService.RetrofitClientInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class fragmentCalendarMainViewModel : ViewModel() {

    //TODO در متغییر زیر مقدار بروز شده ی تقویم های خواسته شده براساس تاریخ درخواستی بصورت جیسون نگهداری شده و اگر متغییر پر شود برگشت داده میشود
    private val _returnAllCalendar_ofSelectedDay = MutableLiveData<List<ModelCalendar>>()
    val returnAllCalendar_ofSelectedDay : LiveData<List<ModelCalendar>>
        get() = _returnAllCalendar_ofSelectedDay

    //TODO مقدار اولیه و یا تغییر یافته تاریخ و زمان را نگه داری میکند
    private val _currentDateTime = MutableLiveData<String>()
    val returnCurrentDateTime : LiveData<String>
        get() = _currentDateTime

    init {
    }

    private val ShamsiDate : ShamsiDate = ShamsiDate()
    private var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private val calendarDbHandler: CalendarDbHandler = RetrofitClientInstance.retrofitInstance.create(
        CalendarDbHandler::class.java
    )

    fun createProfile(
        Id : Long,
        idUser : Int,
        note: String,
        datetimeY : String,
        datetimeM : String,
        datetimeD : String,
        context: Context
    ) {
        //Toast.makeText(context,"$Y/$M/$D",Toast.LENGTH_SHORT).show()
        val userInfo = ModelCalendar(
            Id = Id,
            idUser = idUser,
            datetime = validDateOfSpinner(datetimeY,datetimeM,datetimeD),
            note = note
        )
        compositeDisposable.add(
            calendarDbHandler.createProfile(userInfo)
                .andThen(calendarDbHandler.getProfiles(userInfo.datetime))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { e ->
                        _returnAllCalendar_ofSelectedDay.value = e //وقتی پر میشود یعنی دارد اعلام میکند که چیزی تغییر کرده، بنابراین لیست باید بروز شود
                        Toast.makeText(context,"New task has added.",Toast.LENGTH_SHORT).show()
                    },
                    { throwable ->
                        Log.e("HomeActivity3", throwable.message ?: "onError")
                    }
                )
        )
    }

    fun deleteProfile(profile: ModelCalendar,Year : String,Month : String,Day : String,context: Context) {
        try {
            //Toast.makeText(context, profile.datetime.toString(), Toast.LENGTH_SHORT).show()
            compositeDisposable.add(
                calendarDbHandler.deleteProfile(profile.Id)
                    .andThen(calendarDbHandler.getProfiles(validDateOfSpinner(Year,Month,Day)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { e ->
                            _returnAllCalendar_ofSelectedDay.value = e //وقتی پر میشود یعنی دارد اعلام میکند که چیزی تغییر کرده، بنابراین لیست باید بروز شود
                            Toast.makeText(context,"Deleted Successfully!",Toast.LENGTH_SHORT).show()
                        },
                        { throwable ->
                            Log.e("HomeActivity4", throwable.message ?: "onError")
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("HomeActivity4-1", e.message ?: "onError")
        }
    }

    fun fetchProfiles(Year : String,Month : String,Day : String) {
        try {
            compositeDisposable.add(
                calendarDbHandler.getProfiles(validDateOfSpinner(Year,Month,Day))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { e ->
                            _returnAllCalendar_ofSelectedDay.value = e //وقتی پر میشود یعنی دارد اعلام میکند که چیزی تغییر کرده، بنابراین لیست باید بروز شود
                        },
                        { e ->
                            Log.e("fetchProfiles1", e.message ?: "onError")
                        }
                    )
            )
        } catch (ex: Exception) {
            Log.e("fetchProfiles2", ex.message ?: "onError")
        }
    }

    fun validDateOfSpinner(Y : String,M : String,D : String): String {
        var New_M: String = M
        when (New_M) {
            "1" -> New_M ="01"
            "2" -> New_M ="02"
            "3" -> New_M ="03"
            "4" -> New_M ="04"
            "5" -> New_M ="05"
            "6" -> New_M ="06"
            "7" -> New_M ="07"
            "8" -> New_M ="08"
            "9" -> New_M ="09"
            else -> {
                New_M = New_M
            }
        }
        return "$Y$New_M$D"
    }

    fun getLongTimestampt_WithParticularDate(Y: Int, M: Int , D: Int){
        _currentDateTime.value =
                ShamsiDate.getDayOfWeekName_WithParticularDate(Y,M,D) + "  " +
                D.toString() + "  " +
                ShamsiDate.getMonthName_WithParticularDate(M) + "  " +
                Y.toString()
    }
    fun getLongTimestampt(){
        _currentDateTime.value = ShamsiDate.getLongTimestampt()
    }
    fun getOnlyNumbericDate():String{ return ShamsiDate.getOnlyNumbericDate() }
    fun getOnlyNumbericYear():Int{ return ShamsiDate.getOnlyNumbericYear().toInt() }
    fun getOnlyNumbericMonth():Int{ return ShamsiDate.getOnlyNumbericMonth().toInt() }
    fun getOnlyNumbericDay():Int{ return ShamsiDate.getOnlyNumbericDay().toInt() }

}