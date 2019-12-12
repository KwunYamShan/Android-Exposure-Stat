package com.wh.statdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * describe  Java类作用描述.
 *
 * @author konglingrong
 *
 * data      2019-12-12 13:54
 */
public class TestActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_test);
    final TextView textView = findViewById(R.id.tv_test);
    final String canonicalName = getClass().getCanonicalName();
    textView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(TestActivity.this, textView.getText().toString(), Toast.LENGTH_SHORT);
        Log.e("abc", "onClick: " + canonicalName);
      }
    });
    ReportUtil.getInstance().setReportData(textView, "3000块", "婚纱照", "BJ旅拍测试");
  }
}
