package com.wh.statdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * @author KwunYamShan.
 * @time 2019/6/25.
 * @explain
 */
public class MainActivity2 extends AppCompatActivity {
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
        ReportUtil.getInstance().setReportData(tvHello, "3000块", "婚纱照", "BJ旅拍");
        ReportUtil.getInstance().setPriceReport(tvPrice, tvPrice.getText().toString());
        ReportUtil.getInstance().setBlockNameTag(tvBlock, tvBlock.getText().toString());
        ReportUtil.getInstance().setItemNameTag(tvItem, tvItem.getText().toString());

    }

}
