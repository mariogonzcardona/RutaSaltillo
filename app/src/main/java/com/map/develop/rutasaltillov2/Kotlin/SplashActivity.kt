package com.map.develop.rutasaltillov2.Kotlin

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.map.develop.rutasaltillov2.R

class SplashActivity : Activity() {

    private var tv: TextView? = null
    private var iv: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tv = findViewById<View>(R.id.tv) as TextView
        iv = findViewById<View>(R.id.iv) as ImageView

        val myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition)
        tv!!.startAnimation(myanim)
        iv!!.startAnimation(myanim)
        val i = Intent(this, LoginActivity::class.java)
        val timer = object : Thread() {
            override fun run() = try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                startActivity(i)
                finish()
            }
        }
        timer.start()
    }
}
