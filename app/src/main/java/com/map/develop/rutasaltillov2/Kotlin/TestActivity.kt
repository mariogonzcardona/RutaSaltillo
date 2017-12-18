package com.map.develop.rutasaltillov2.Kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.map.develop.rutasaltillov2.JSonParsers.jsonParseRutas
import com.map.develop.rutasaltillov2.R

class TestActivity : AppCompatActivity() {

    internal lateinit var click: Button
    lateinit var dataTest: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        click = findViewById<View>(R.id.button) as Button
        dataTest = findViewById<View>(R.id.fetchdata) as TextView

        click.setOnClickListener {
            val process = jsonParseRutas()
            process.execute()
        }
    }
}
