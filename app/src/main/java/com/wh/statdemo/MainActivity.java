package com.wh.statdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author KwunYamShan.
 * @time 2019/6/25.
 * @explain
 */
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
        tvHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
            }
        });
        TextView tvPrice = findViewById(R.id.tv_price);
        TextView tvBlock = findViewById(R.id.tv_block);
        TextView tvItem = findViewById(R.id.tv_item);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TestActivity.class));
            }
        });

        //埋点
        ReportUtil.getInstance().setReportData(tvHello, "3000块", "婚纱照", "BJ旅拍");
        ReportUtil.getInstance().setPriceReport(tvPrice, tvPrice.getText().toString());
        ReportUtil.getInstance().setBlockNameTag(tvBlock, tvBlock.getText().toString());
        ReportUtil.getInstance().setItemNameTag(tvItem, tvItem.getText().toString());

    }

}
