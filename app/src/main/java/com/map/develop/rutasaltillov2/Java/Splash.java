package com.map.develop.rutasaltillov2.Java;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.map.develop.rutasaltillov2.Kotlin.LoginActivity;
import com.map.develop.rutasaltillov2.R;

/**
 * Created by Mario on 08/12/2017.
 */

public class Splash extends Activity {

    private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv=(TextView) findViewById(R.id.tv);
        iv=(ImageView)findViewById(R.id.iv);

        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        final Intent i=new Intent(this,LoginActivity.class);
        Thread timer=new Thread(){
            public void run()
            {
                try{
                    sleep(5000);
                }catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
