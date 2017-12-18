package com.map.develop.rutasaltillov2.Kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.map.develop.rutasaltillov2.R
import kotlinx.android.synthetic.main.activity_rutas.*
import org.json.JSONArray

class RutasActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rutas)
    }

}
