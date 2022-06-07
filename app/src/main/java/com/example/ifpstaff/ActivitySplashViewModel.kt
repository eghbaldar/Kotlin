package com.example.ifpstaff

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ifpstaff.retrofitHandler.ApkDbHandler
import com.example.ifpstaff.retrofitService.RetrofitClientInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ActivitySplashViewModel: ViewModel() {

    // ورژن جدید را نگه داری میکند
    private var _returnNewApkVersion = MutableLiveData<String>()
    val returnNewApkVersion : LiveData<String>
    get() = _returnNewApkVersion

    //اگر در این متغیر true بشیند یعنی ورژن جدید است واگه false یعنی تغییری در ورژن نداشته ایم
    private var _returnDoesNewApkVersion = MutableLiveData<Boolean>()
    val returnDoesNewApkVersion : LiveData<Boolean>
    get() = _returnDoesNewApkVersion

    private var compositeDisposable = CompositeDisposable()
    private val apkDbHandler: ApkDbHandler = RetrofitClientInstance.retrofitInstance.create(
        ApkDbHandler::class.java
    )

    init {
        getApk()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getLastVersionOfApp(): String{
        //this value catch from "versionName" element in "build.gradle(:app)"
       return BuildConfig.VERSION_NAME
    }

    fun CheckNewVersion( newVersion : String,oldVersion : String ) : Boolean{
        val new = newVersion.replace(".apk","").replace("v-","")
        val old = oldVersion.replace(".apk","").replace("v-","")
        _returnNewApkVersion.value = newVersion
        return old == new
    }

    fun getApk() {
        try {
            compositeDisposable.add(
                apkDbHandler.getApk()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { e ->
                            // TODO: چرا بصورت زیر نوشته شده؟ چون مقدار برگشتی جیسن از ای پی آی، بصورت لیست است یعنی آیتم ها در یک براکت کلی هستند.
                            // TODO: هر چند که از طرف کدنویس سرور نیازی به این کار نبود،چون همیشه قرار است فقط یک مقدار برگشت داده شود، بنابراین میتواست از لیست استفاده نکند
                            // TODO: حالا که استفاده کرده است، و فقط یک لیست هم داریم، با ایندکس صفر به اولین و تنها ترین آن دسترسی پیدا کرده و یک آیتم آن را میخوانیم
                            _returnDoesNewApkVersion.value = CheckNewVersion(e[0].apk,BuildConfig.VERSION_NAME) //وقتی پر میشود یعنی دارد اعلام میکند که چیزی تغییر کرده، بنابراین لیست باید بروز شود
                            //You could have written this code instead of above code: [if (oldVersion == newVersion) true else false] but is absolutely redundant!
                        },
                        { e ->
                            Log.e("getApk1", e.message ?: "onError")
                        }
                    )
            )
        } catch (ex: Exception) {
            Log.e("getApk2", ex.message ?: "onError")
        }
    }

}