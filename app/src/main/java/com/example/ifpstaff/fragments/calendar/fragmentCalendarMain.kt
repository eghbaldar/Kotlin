package com.example.ifpstaff.fragments.calendar

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.text.set
import androidx.core.text.toSpannable
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ifpstaff.R
import com.example.ifpstaff.databinding.FragmentCalendarMainBinding
import com.example.ifpstaff.general.ShamsiDate
import com.example.ifpstaff.model.ModelCalendar
import com.example.ifpstaff.model.ModelCalendarUpdate
import com.example.ifpstaff.retrofitHandler.CalendarDbHandler
import com.example.ifpstaff.retrofitService.RetrofitClientInstance.retrofitInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat


class fragmentCalendarMain : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentCalendarMainBinding
    private lateinit var viewmodel: fragmentCalendarMainViewModel

    var Y: String = ""  //SelectedYear
    var M: String = ""  //SelectedMonth
    var D: String = ""  //SelectedDay

    private var adapter = calendarAdapter(
        onDeleteClickListener = { profile ->
            context?.let {
                val Y = binding.spinnerYears.selectedItem.toString()
                val M = (binding.spinnerMonth.selectedItemPosition + 1).toString()
                val D = binding.spinnerDays.selectedItem.toString()
                viewmodel.deleteProfile(profile, Y, M, D, it)
            }
        },
        onUpdateClickListener = { profile -> updateProfile(profile) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        InitializeDate()
        getUpdateList()

    }

    private fun InitializeDate() {

        //Show today's date!
        viewmodel.getLongTimestampt() // چرا از ویومدل استفاده کردیم؟ چون براساس قابلیت لایف سایکل، متغییر ویومدل مقدار اولیه و یا تغییر کرده ی خود را حفظ میکند، در صورت یکه اگه از مقدار دهی معمولی استفاده میکردیم، هر بار خارج میشدیم و با بارگشت دوباره، تاریخ لود نمی شد

        /***************** Set Spinners' date *****************/
        //TODO: با استفاده از کدهای زیر، ابتدا آیتم رشته ای در آرایه پیدا کرده و سپس ایندکس آن را برای اسپاینر ست میکنیم
        var Y: Int = 0
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.arrCalYears,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                Y = adapter.getPosition(viewmodel.getOnlyNumbericYear().toString()).toInt()
            }
        }
        binding.spinnerYears.setSelection(Y)
        binding.spinnerMonth.setSelection(viewmodel.getOnlyNumbericMonth() - 1) //TODO چون انتظار ایندکس ماه را دارد و ما همیشه خود ماه را میخوانیم پس باید یک واحد کم شود
        binding.spinnerDays.setSelection(viewmodel.getOnlyNumbericDay() - 1) //TODO چون انتظار ایندکس روز را دارد و ما همیشه خود روز را میخوانیم پس باید یک واحد کم شود
        //TODO: سوال؟ چرا برای اسپاینرهای ماه و روز از مدل اسپاینر سال استفاده نکردیم؟ چون اسپاینرهای ماه و روز خودشان بصورت عدد قابلیت تحلیل دارند
        /***************** End Spinners' date *****************/
    }

    private fun getUpdateList() {
        //First load the data from database
        //کدهای زیر در این متد نوشته شده است بجای آنکرئیت ویو چون در آن متد هنوز اسپینرها ساخته نمی شد و باعث خطا میشد
        Y = binding.spinnerYears.selectedItem.toString()
        M = (binding.spinnerMonth.selectedItemPosition + 1).toString()
        D = binding.spinnerDays.selectedItem.toString()
        //Update RecyclerView
        viewmodel.fetchProfiles(Y, M, D)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalendarMainBinding.inflate(inflater, container, false)
        viewmodel = ViewModelProvider(this).get(fragmentCalendarMainViewModel::class.java)

        //ADD TASK [Button] : Insert into Database
        binding.btnAddTask.setOnClickListener {
            viewmodel.createProfile(
                5,
                10,
                binding.etTask.text.toString(),
                Y,
                M,
                D,
                container!!.context
            )
            binding.etTask.text.clear()
        }

        binding.CalendarRecyclerView.adapter = adapter

        /*************** Initialized Spinner ********************/
        // Create an ArrayAdapter using the string array and a default spinner layout
        val spinnerDays: Spinner = binding.spinnerDays
        if (container != null) {
            ArrayAdapter.createFromResource(
                container.context,
                R.array.arrCalDays,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                spinnerDays.adapter = adapter
            }
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        val spinnerMonth: Spinner = binding.spinnerMonth
        if (container != null) {
            ArrayAdapter.createFromResource(
                container.context,
                R.array.arrCalMonths,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                spinnerMonth.adapter = adapter
            }
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        val spinnerYears: Spinner = binding.spinnerYears
        if (container != null) {
            ArrayAdapter.createFromResource(
                container.context,
                R.array.arrCalYears,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                spinnerYears.adapter = adapter
            }
        }
        /*************** End Initialized Spinners ********************/

        /********** Get Handler of Spinners **********/
        binding.spinnerDays.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                getUpdateList()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                getUpdateList()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.spinnerYears.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                getUpdateList()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        /********** End Get Handler of Spinners **********/
        viewmodel.returnCurrentDateTime.observe( this, Observer { newValue ->
            //TODO: چرا کدهای زیر را در ویومدل قرار ندادم؟ چون تغییر استایلی از طریق مقدار برگشتی استرینگ رشته ای منتقل نمی شد متاسفانه
            val _result = "تاریخ:    $newValue"
            val spannable = SpannableString(_result)
            spannable.setSpan(ForegroundColorSpan(Color.RED),6, spannable.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(StyleSpan(Typeface.BOLD),6, spannable.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.tvCurrentDateTime.text =  spannable
        })

        //TODO: load calendar based on the in first values of SPINNERs after loading
        viewmodel.returnAllCalendar_ofSelectedDay.observe(this, Observer { newValue ->
            displayProfiles(newValue)
        })

        //Go to next or to previous date!
        binding.btnDateNext.setOnClickListener {
            //کدهای زیر در این متد نوشته شده است بجای آنکرئیت ویو چون در آن متد هنوز اسپینرها ساخته نمی شد و باعث خطا میشد
            D = ((binding.spinnerDays.selectedItem).toString().toInt() + 1).toString()
            binding.spinnerDays.setSelection(binding.spinnerDays.selectedItemPosition + 1)
            //Firing updating RecyclerView
            viewmodel.fetchProfiles(Y, M, D)
            //Set New Long Timestamp after go to the next date
            viewmodel.getLongTimestampt_WithParticularDate(Y.toInt(),M.toInt(),D.toInt())
        }
        binding.btnDatePrevious.setOnClickListener {
            //کدهای زیر در این متد نوشته شده است بجای آنکرئیت ویو چون در آن متد هنوز اسپینرها ساخته نمی شد و باعث خطا میشد
            D = ((binding.spinnerDays.selectedItem).toString().toInt() - 1).toString()
            binding.spinnerDays.setSelection(binding.spinnerDays.selectedItemPosition - 1)
            //Firing updating RecyclerView
            viewmodel.fetchProfiles(Y, M, D)
            //Set New Long Timestamp after go to the next date
            viewmodel.getLongTimestampt_WithParticularDate(Y.toInt(),M.toInt(),D.toInt())
        }

        return binding.root
    }

    private fun displayProfiles(profiles: List<ModelCalendar>) {
        adapter.items.clear()
        adapter.items.addAll(profiles)
        adapter.notifyDataSetChanged()
    }

    private fun displayUpdatedProfile(oldProfile: ModelCalendar, newProfile: ModelCalendar) {
        //TODO در متغییر زیر مقدار ایندکس رکورد نگهداری میشود
        val index = adapter.items.indexOfFirst { profileToReplace ->
            profileToReplace.Id == oldProfile.Id
        }
        adapter.items[index] = newProfile
        // TODO فقط همان رکورد که با ایندکس مشخص شده تغییر میکند که این عالیه
        adapter.notifyItemChanged(index)
    }

//    fun getUserProfileCallback() {
//        object {
//            fun onSuccess(result: ModelCalendar) {
//                //binding.userLabel.text = "Hello, ${result.note}!"
//                //RetrofitClientInstance.setToken(oktaManager.getJwtToken())
//                //fetchProfiles()
//            }
//
//            fun onError(msg: String?) {
//                Log.d("HomeActivity1", "Error: $msg")
//            }
//        }
//    }

    private fun updateProfile(oldProfile: ModelCalendar) {
//        try {
//            //val profile = ProfileRequest(email = System.currentTimeMillis().toString())
//            val profile = ModelCalendarUpdate(datetime = "1800", note = "joon")
//            //Toast.makeText(this,profile.datetime.toString(),Toast.LENGTH_SHORT).show()
//            //Toast.makeText(this,oldProfile.Id.toString(),Toast.LENGTH_SHORT).show()
//            //val profile = ProfileRequest(note = "new")
//
//            compositeDisposable.add(
//                calendarDbHandler.updateProfile(oldProfile.Id, profile)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                        { newProfiles ->
//                            displayUpdatedProfile(oldProfile, newProfiles.first())
//                        },
//                        { throwable ->
//                            Log.e("HomeActivity56", throwable.message ?: "onError")
//                        }
//                    )
//            )
//        } catch (ex: Exception) {
//            Log.e("HomeActivity500", ex.message ?: "onError")
//        }
    }


    /************WHY the below codes not working??????????***************/
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}