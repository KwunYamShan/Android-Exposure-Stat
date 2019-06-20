package com.wh.statdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = AppCompatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        TextView tvHello = findViewById(R.id.tv_hello);
        TextView tvPrice = findViewById(R.id.tv_price);
        TextView tvBlock = findViewById(R.id.tv_block);
        TextView tvItem = findViewById(R.id.tv_item);

        //埋点
        ReportUtil.getInstance().setReportData(tvHello,"3000块","婚纱照","BJ旅拍");
        ReportUtil.getInstance().setPriceReport(tvPrice, tvPrice.getText().toString());
        ReportUtil.getInstance().setBlockNameTag(tvBlock, tvBlock.getText().toString());
        ReportUtil.getInstance().setItemNameTag(tvItem, tvItem.getText().toString());

    }

}
