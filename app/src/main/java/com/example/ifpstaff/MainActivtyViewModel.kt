package com.example.ifpstaff

import android.content.Context
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ifpstaff.model.ModelCalendarOverview
import com.example.ifpstaff.retrofitHandler.CalendarOverviewDbHandler
import com.example.ifpstaff.retrofitService.RetrofitClientInstance
import com.google.android.material.navigation.NavigationView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivtyViewModel : ViewModel() {

    // Return full list of CalendarOverview
    private val _returnCalendarOverview = MutableLiveData<List<ModelCalendarOverview>>()
    val returnCalendarOverview : LiveData<List<ModelCalendarOverview>>
    get() = _returnCalendarOverview

    private var compositeDisposable = CompositeDisposable()

    private val calendarOverviewDbHandler: CalendarOverviewDbHandler =
        RetrofitClientInstance.retrofitInstance.create(
            CalendarOverviewDbHandler::class.java
        )

    init {
        fetchCalendarOverview()
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun ChangeColorSizeMenuItem(context: Context,
                                navV: NavigationView,
                                textItem: String,
                                fontItem : Float,
                                colorItem : Int )
    {
        //Change a specific menu item in Drawer Navigation Menu
        val menu = navV.menu
        for (i in 0 until menu.size()) {
            val menuItem = navV.menu.getItem(i)

            if (menuItem.title == textItem)
            {
                val spanString = SpannableString(menuItem.title.toString())
                //Size
                spanString.setSpan( RelativeSizeSpan(fontItem), 0,spanString.length, 0)
                //Color
                spanString.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            context,
                            colorItem
                        )
                    ), 0, spanString.length, 0
                )
                menuItem.title= spanString
            }

            if (menuItem.hasSubMenu()) {

                for (z in 0 until menuItem.subMenu.size()) {

                    val subMenuItem = menuItem.subMenu.getItem(z)
                    if (subMenuItem.title == textItem)
                    {
                        val spanString = SpannableString(subMenuItem.title.toString())
                        //Size
                        spanString.setSpan( RelativeSizeSpan(fontItem), 0,spanString.length, 0)
                        //Color
                        spanString.setSpan(
                            ForegroundColorSpan(
                                ContextCompat.getColor(
                                    context,
                                    colorItem
                                )
                            ), 0, spanString.length, 0
                        )
                        subMenuItem.title= spanString
                    }
                }
            }
        }


    }

    fun fetchCalendarOverview() {
        try {
            compositeDisposable.add(
                calendarOverviewDbHandler.getCalOverview(
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { e ->
                            //وقتی پر میشود یعنی دارد اعلام میکند که چیزی تغییر کرده، بنابراین لیست باید بروز شود
                            _returnCalendarOverview.value = e
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
}