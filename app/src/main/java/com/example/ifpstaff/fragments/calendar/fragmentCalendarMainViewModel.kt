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
import com.google.android.gms.common.internal.Objects
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class fragmentCalendarMainViewModel : ViewModel() {

    //TODO: The following lines using update of spinner through liveData
    private val _returnNewDayForSpinner = MutableLiveData<String>()
    val returnNewDayForSpinner: LiveData<String>
        get() = _returnNewDayForSpinner
    private val _returnMewMonthSpinner = MutableLiveData<String>()
    val returnNewMonthSpinner: LiveData<String>
        get() = _returnMewMonthSpinner
    private val _returnNewYearSpinner = MutableLiveData<String>()
    val returnNewYearSpinner: LiveData<String>
        get() = _returnNewYearSpinner

    //TODO در متغییر زیر مقدار بروز شده ی تقویم های خواسته شده براساس تاریخ درخواستی بصورت جیسون نگهداری شده و اگر متغییر پر شود برگشت داده میشود
    private val _returnAllCalendar_ofSelectedDay = MutableLiveData<List<ModelCalendar>>()
    val returnAllCalendar_ofSelectedDay: LiveData<List<ModelCalendar>>
        get() = _returnAllCalendar_ofSelectedDay

    //TODO مقدار اولیه و یا تغییر یافته تاریخ و زمان را نگه داری میکند
    private val _currentDateTime = MutableLiveData<String>()
    val returnCurrentDateTime: LiveData<String>
        get() = _currentDateTime

    private val ShamsiDate: ShamsiDate = ShamsiDate()
    private var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private val calendarDbHandler: CalendarDbHandler =
        RetrofitClientInstance.retrofitInstance.create(
            CalendarDbHandler::class.java
        )

    fun createProfile(
        Id: Long,
        idUser: Int,
        note: String,
        datetimeY: String,
        datetimeM: String,
        datetimeD: String,
        context: Context
    ) {
        val userInfo = ModelCalendar(
            Id = Id,
            idUser = idUser,
            datetime = validDateOfSpinner(datetimeY, (datetimeM.toInt() + 1).toString(), datetimeD),
            note = note
        )
        compositeDisposable.add(
            calendarDbHandler.createProfile(userInfo)
                .andThen(calendarDbHandler.getProfiles(userInfo.datetime))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { e ->
                        _returnAllCalendar_ofSelectedDay.value =
                            e //وقتی پر میشود یعنی دارد اعلام میکند که چیزی تغییر کرده، بنابراین لیست باید بروز شود
                        Toast.makeText(context, "New task has added.", Toast.LENGTH_SHORT).show()
                    },
                    { throwable ->
                        Log.e("HomeActivity3", throwable.message ?: "onError")
                    }
                )
        )
    }

    fun deleteProfile(
        profile: ModelCalendar,
        Year: String,
        Month: String,
        Day: String,
        context: Context
    ) {
        try {
            //Toast.makeText(context, profile.datetime.toString(), Toast.LENGTH_SHORT).show()
            compositeDisposable.add(
                calendarDbHandler.deleteProfile(profile.Id)
                    .andThen(calendarDbHandler.getProfiles(validDateOfSpinner(Year, Month, Day)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { e ->
                            _returnAllCalendar_ofSelectedDay.value =
                                e //وقتی پر میشود یعنی دارد اعلام میکند که چیزی تغییر کرده، بنابراین لیست باید بروز شود
                            Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT)
                                .show()
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

    fun fetchProfiles(Year: String, Month: String, Day: String) {

        //TODO: *********** the following codes function is management of first and last day of month!
        var New_D: String = Day
        var New_M: String = (Month.toInt() + 1).toString() //chera plus 1? chon dare index mikhone

        if (New_D.toInt() != 0) {  // why "0"? aval check mikonam age index day 0 nabod. key 0 mishe? vaghti ingahad karabar kam kone ta 0 roz berese ke yani mah bayad be aghab bargardad
            when (New_M.toInt()) {
                in 1..6 ->
                    if (New_D.toInt() > 31) {
                        New_D = "1"
                        New_M = (New_M.toInt() + 1).toString()
                    }
                in 7..12 ->
                    if (New_D.toInt() > 30) {
                        New_D = "1"
                        New_M = (New_M.toInt() + 1).toString()
                    }
            }
        } else {
            New_M = (New_M.toInt() - 1).toString()
            when (New_M.toInt()) {
                in 1..6 ->
                    New_D = "31"
                in 7..12 ->
                    New_D = "30"
            }
        }
        //TODO: *********** End

        try {
            compositeDisposable.add(
                calendarDbHandler.getProfiles(
                    validDateOfSpinner(
                        Year,
                        New_M,
                        New_D
                    )
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { e ->
                            //وقتی پر میشود یعنی دارد اعلام میکند که چیزی تغییر کرده، بنابراین لیست باید بروز شود
                            _returnAllCalendar_ofSelectedDay.value = e
                            //Set New Long Timestamp after change the time! maybe go to next or back!
                            getLongTimestampt_WithParticularDate(
                                Year.toInt(),
                                New_M.toInt() ,
                                New_D.toInt()
                            )
                            //Update Spinners
                            _returnNewDayForSpinner.value = (New_D.toInt() - 1).toString()
                            _returnMewMonthSpinner.value = (New_M.toInt() - 1).toString()
                            _returnNewYearSpinner.value = Year
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

    fun validDateOfSpinner(Y: String, M: String, D: String): String {

//TODO: یکی کردن فرمت دودویی ماه تاریخ
        var New_M: String = M
        when (New_M) {
            "1" -> New_M = "01"
            "2" -> New_M = "02"
            "3" -> New_M = "03"
            "4" -> New_M = "04"
            "5" -> New_M = "05"
            "6" -> New_M = "06"
            "7" -> New_M = "07"
            "8" -> New_M = "08"
            "9" -> New_M = "09"
            else -> {
                New_M = New_M
            }
        }
        var New_D: String = D
        when (New_D) {
            "1" -> New_D = "01"
            "2" -> New_D = "02"
            "3" -> New_D = "03"
            "4" -> New_D = "04"
            "5" -> New_D = "05"
            "6" -> New_D = "06"
            "7" -> New_D = "07"
            "8" -> New_D = "08"
            "9" -> New_D = "09"
            else -> {
                New_D = New_D
            }
        }
        return "$Y$New_M$New_D"
    }

    fun getLongTimestampt_WithParticularDate(Y: Int, M: Int, D: Int) {
        _currentDateTime.value =
            ShamsiDate.getDayOfWeekName_WithParticularDate(Y, M, D) + "  " +
                    D.toString() + "  " +
                    ShamsiDate.getMonthName_WithParticularDate(M) + "  " +
                    Y.toString()
    }

    fun getLongTimestampt() {
        _currentDateTime.value = ShamsiDate.getLongTimestampt()
    }

    fun getOnlyNumbericDate(): String {
        return ShamsiDate.getOnlyNumbericDate()
    }

    fun getOnlyNumbericYear(): Int {
        return ShamsiDate.getOnlyNumbericYear().toInt()
    }

    fun getOnlyNumbericMonth(): Int {
        return ShamsiDate.getOnlyNumbericMonth().toInt()
    }

    fun getOnlyNumbericDay(): Int {
        return ShamsiDate.getOnlyNumbericDay().toInt()
    }

    //TODO: خیلی خیلی مهم: متن زیر را بخوان
    //چون init باید تمامی متغییر هایی که قصد دارید در فراخوانی اش استفاده کند، قبلش تعریف شود، بهتر است در انتهای کدها نوشته شود
    // مثل در کامنت زیر:
    //اگر متغییر ShamsiDate بالا، بعد از init تغریف میشد، خطا داشتیم. چرا؟ چون ما در init قصد فراخوانی تابع getOnlyNumberDay را داشتیم که قرار بود از ShamsiDate استفاده کند در حالی که بعد از init نوشته میشد
    init {
        //چرا در کد های زیر ما ماه را یک واحد کم میکنیم؟
        //داستان مفصل است! ما یک تابع داریم بنام fetchProfile که از بانک براساس تاریخ (مثال 14010305) رکورد ها را برمیگرداند
        //در تابع init عین تاریخ روز بصورت فرمت صحیح خوانش شده و مشکلی ندارد
        //اما وقتی کاربر روز را جلو و عقب میکند، براساس ذات کنترل های spinner ما گاهی index و گاهی خود item را میخوانیم
        //بنابراین برای روز خود آیتم که عددی است و برای ماه که رشته ایی است index خوانده میشود و تابع fetchProfile خودبخود یک واحد اضافه میکند
        // پس برای آنکه برای هر دو رخداد یعنی لود اولیه توسط init و افزایش و کاهش روز از یک تابع استفاده کنیم و نه دو تابع
        // محبور شدیم که بعضی مواقع یک واحد کم و یا زیاد کنیم
        fetchProfiles(
            getOnlyNumbericYear().toString(),
            (getOnlyNumbericMonth().toInt() - 1).toString(),
            (getOnlyNumbericDay().toInt()).toString()
        )
        // Set today's date and time
        getLongTimestampt()
        //
        _returnNewDayForSpinner.value = (getOnlyNumbericDay().toInt() - 1).toString()
        _returnMewMonthSpinner.value = (getOnlyNumbericMonth().toInt() - 1).toString()
        _returnNewYearSpinner.value = getOnlyNumbericYear().toString()
    }

}