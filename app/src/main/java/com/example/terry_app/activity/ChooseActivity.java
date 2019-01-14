package com.example.terry_app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.terrysaolei.R;

public class ChooseActivity extends Activity implements View.OnClickListener {

    Button begin1,newroom,joinroom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_choose);
        begin1=(Button)findViewById(R.id.begin1);
        begin1.setOnClickListener(this);
        newroom=(Button)findViewById(R.id.newroom);
        newroom.setOnClickListener(this);
        joinroom=(Button)findViewById(R.id.joinroom);
        joinroom.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.begin1:
                Intent intent = new Intent(ChooseActivity.this, OneActivity.class);
                startActivity(intent);
                break;
            case R.id.newroom:
                Intent intent2 = new Intent(ChooseActivity.this, DoubleActivity.class);
                startActivity(intent2);
                break;
            case R.id.joinroom:
                Intent intent3= new Intent(ChooseActivity.this, DoubleclientActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
