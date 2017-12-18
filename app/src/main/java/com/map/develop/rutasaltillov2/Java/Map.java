package com.map.develop.rutasaltillov2.Java;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.map.develop.rutasaltillov2.Kotlin.MapsActivity;
import com.map.develop.rutasaltillov2.R;

public class Map extends AppCompatActivity {

    Button rMapa;
    public AutoCompleteTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        rMapa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Map.this,MapsActivity.class);
                startActivity(intent);

            }
        });

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String)parent.getItemAtPosition(position);
                System.out.println(selection);
            }
        });
    }



}
