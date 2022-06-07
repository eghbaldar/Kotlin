package com.example.ifpstaff

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ifpstaff.model.ModelLogin
import com.example.ifpstaff.retrofitHandler.LoginDbHelper
import com.example.ifpstaff.retrofitService.RetrofitClientInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ActivityLoginViewModel: ViewModel() {

    //TODO: Once the USER will be valid, the CHECK update with TRUE value!
    private var _returnCheck = MutableLiveData<Boolean>()
    val returnCheck : LiveData<Boolean>
    get() = _returnCheck

    private var compositeDisposable = CompositeDisposable()
    private val LoginDbHandler: LoginDbHelper = RetrofitClientInstance.retrofitInstance.create(
        LoginDbHelper::class.java
    )

    init {

    }

    override fun onCleared() {
        super.onCleared()
    }

    fun checkValidationUsers(user:String,pass:String)
    {
        try {
            compositeDisposable.add(
                LoginDbHandler.getAuth(user,pass)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { e ->
                            val auth: ModelLogin = e
                            when(auth.auth.toInt())
                            {
                                0 ->
                                    _returnCheck.value = false
                                else ->
                                    _returnCheck.value = true
                            }
                        },
                        { e ->
                            Log.e("login1", e.message ?: "onError")
                        }
                    )
            )
        } catch (ex: Exception) {
            Log.e("login2", ex.message ?: "onError")
        }

    }

}