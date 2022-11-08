package com.example.campustaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuSelectActivity extends AppCompatActivity {

    Button btnKorean;
    Button btnWestern;
    Button btnChinese;
    Button btnJapanese;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        btnKorean = findViewById(R.id.btn_korean);
        btnWestern = findViewById(R.id.btn_western);
        btnChinese = findViewById(R.id.btn_chinese);
        btnJapanese = findViewById(R.id.btn_japanese);

        btnKorean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuSelectActivity.this, MenuSelectActivity1.class);
                startActivity(intent);
            }
        });

        btnWestern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuSelectActivity.this, MenuSelectActivity2.class);
                startActivity(intent);
            }
        });

        btnChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuSelectActivity.this, MenuSelectActivity3.class);
                startActivity(intent);
            }
        });

        btnJapanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuSelectActivity.this, MenuSelectActivity4.class);
                startActivity(intent);
            }
        });
    }
}