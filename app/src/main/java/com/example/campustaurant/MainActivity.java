package com.example.campustaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    FirebaseDatabase database;
    //FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); // 회원가입된 유저 객체를 가져온다.
    Button btnLogout;
    Button btnMatch;
    Button btnRoom;
    Button btnEnter;
    EditText etFood;
    String inputFood;
    String stUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // activity_main 레이아웃 표출

        mFirebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        btnLogout = findViewById(R.id.btn_logout);
        btnMatch = findViewById(R.id.btn_match);
        btnRoom = findViewById(R.id.btn_room);
        btnEnter = findViewById(R.id.btn_enter);
        etFood = findViewById(R.id.et_food);
        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 LoginActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputFood = etFood.getText().toString();

                Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 RestaurantActivity에 넘겨줌
                intent.putExtra("inputFood", inputFood);
                startActivity(intent);
            }
        });

        btnRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RoomListActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 RoomListActivity에 넘겨줌
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아웃 버튼 클릭시 처리 시작
                mFirebaseAuth.signOut(); // 로그아웃

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}