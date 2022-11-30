package com.example.campustaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class RecordListActivity extends AppCompatActivity {

    LinearLayout llRecord;
    String myToken;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        myToken = getIntent().getStringExtra("myToken");
        btnClose = findViewById(R.id.btn_close);

        llRecord = findViewById(R.id.ll_recordList);
        llRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordListActivity.this, RecordActivity.class);
                intent.putExtra("myToken", myToken);
                startActivity(intent);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}