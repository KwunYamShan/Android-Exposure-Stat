package com.wh.statdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.wh.stat.HBHStatistical;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvHello = findViewById(R.id.tv_hello);
        tvHello.setTag(HBHStatistical.getInstance().getMarkId(),"Hello World");
    }
}
