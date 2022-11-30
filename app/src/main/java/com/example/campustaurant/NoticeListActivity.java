package com.example.campustaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class NoticeListActivity extends AppCompatActivity {

    Button btnClose;
    LinearLayout llNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        btnClose = findViewById(R.id.btn_close);
        llNotice = findViewById(R.id.ll_noticeList);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        llNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeListActivity.this, NoticeActivity.class);
                startActivity(intent);
            }
        });
    }
}