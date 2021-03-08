package com.c1ctech.jetpackprotodatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var personDataStore: PersonDataStore
    var age = 0
    var fname = ""
    var lname = ""
    var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        personDataStore = PersonDataStore(this)


        //Gets the user input and saves it
        btn_save.setOnClickListener {
            fname = et_fname.text.toString()
            lname = et_lname.text.toString()
            age = et_age.text.toString().toInt()
            val isMale = switch_gender.isChecked

            //Stores the values
            lifecycleScope.launch {
                personDataStore.storeData(age, fname, lname, isMale)
            }


        }

        observeData()
    }


    private fun observeData() {

        personDataStore.user.asLiveData().observe(this, Observer {
            if (it != null && it.age > 0) {
                tv_fname.text = it.firstName
                tv_lname.text = it.lastName
                tv_age.text = it.age.toString()
                gender = if (it.gender) "Male" else "Female"
                tv_gender.text = gender

            } else {
                tv_fname.text = ""
                tv_lname.text = ""
                tv_age.text = ""
                tv_gender.text = ""
            }
        })
    }
}
