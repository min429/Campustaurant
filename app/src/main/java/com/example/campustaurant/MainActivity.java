package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseAuth mFirebaseAuth;
    FirebaseStorage storage;
    StorageReference stRef;
    ArrayList<String> foodArrayList;
    Button btnLogout;
    Button btnRoom;
    Button btnEnter;
    Button btnRefresh;
    Button btnRelate;
    EditText etFood;
    ImageView ivFood;
    String inputFood;
    String stUserId;
    String fileName;
    int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // activity_main 레이아웃 표출

        mFirebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        btnLogout = findViewById(R.id.btn_logout);
        btnRoom = findViewById(R.id.btn_room);
        btnEnter = findViewById(R.id.btn_enter);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnRelate = findViewById(R.id.btn_relate);
        etFood = findViewById(R.id.et_food);
        ivFood = findViewById(R.id.iv_foodimg);
        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 LoginActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장
        foodArrayList = getIntent().getStringArrayListExtra("foodArrayList");
        fileName = getIntent().getStringExtra("fileName");

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random random = new Random();
                idx = random.nextInt(foodArrayList.size());
                fileName = foodArrayList.get(idx);

                Log.d(TAG, "fileName: "+fileName);

                // 새로고침
                finish();//인텐트 종료
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                Intent intent = getIntent(); //인텐트
                intent.putExtra("email", stUserId);
                intent.putExtra("fileName", fileName);
                intent.putExtra("foodArrayList", foodArrayList);
                startActivity(intent); //액티비티 열기
                overridePendingTransition(0, 0);//인텐트 효과 없애기
            }
        });

        btnRelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 RestaurantActivity에 넘겨줌
                intent.putExtra("inputFood", fileName);
                startActivity(intent);
            }
        });

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

        stRef = storage.getReference();

        stRef.child(fileName+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MainActivity.this).load(uri).into(ivFood);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
}