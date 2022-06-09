package com.example.ifpstaff

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ifpstaff.model.ModelLogin
import com.example.ifpstaff.retrofitHandler.LoginDbHelper
import com.example.ifpstaff.retrofitService.RetrofitClientInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import android.content.SharedPreferences

class ActivityLoginViewModel(): ViewModel() {

    //TODO: Once the USER will be valid, the CHECK update with TRUE value!
    private var _returnCheck = MutableLiveData<String>()
    val returnCheck : LiveData<String>
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

    fun checkValidationUsers(user:String,pass:String,context: Context)
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
                                    _returnCheck.value = "NoValidated_WithClick"
                                else -> {
                                    savePreferences(context) // If user has validated, does not necessary login again!
                                    _returnCheck.value = "Validated"
                                }
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

    //region SharedPreference (for storing the last LOGIN)
    private val PREFS_NAME = "Preference_Login"
    private var PREF_CHECK : String = ""

    private fun savePreferences(context: Context) {

        val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Edit, Fill and commit the SharedPreferences
        editor.putString(PREF_CHECK, "Checked")
        editor.commit()
    }

    fun loadPreferences(context: Context) {
        val settings = context.getSharedPreferences(
            PREFS_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        // Checking the validation of USER
        _returnCheck.value = if (settings.getString(PREF_CHECK, "") === "Checked") "Validated" else "NoValidated_CheckedSharedPreference"
    }
    //endregion

}