package com.example.campustaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseAuth mFirebaseAuth;
    FirebaseDatabase database;
    private DatabaseReference myRef;
    ArrayList<Match> matchArrayList = new ArrayList<>();
    Button btnMatch;
    String stUserId;
    String stRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // activity_main 레이아웃 표출

        mFirebaseAuth = FirebaseAuth.getInstance();

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아웃 버튼 클릭시 처리 시작
                mFirebaseAuth.signOut(); // 로그아웃

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                // finish(); // 앱 종료
            }
        });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Match");

        btnMatch = findViewById(R.id.btn_match);
        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 LoginActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장

        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Match user : matchArrayList){
                    if(user.userId != stUserId && user.restaurant == stRestaurant){ // 자신이외의 유저중에 자신과 같은 음식점을 선택한 사람이 있는 경우
                        Toast.makeText(MainActivity.this, "user matched", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        intent.putExtra("email", stUserId); // stUserId값을 ChatActivity에 넘겨줌
                        startActivity(intent);
                        break;
                    }
                    else{
                        Toast.makeText(MainActivity.this, "not matched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) { // user1,2,3... 하나씩 가져옴
                    Match user = postSnapshot.getValue(Match.class);
                    matchArrayList.add(user);
                    if(user.userId.equals(stUserId)){
                        stRestaurant = user.getRestaurant();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

    }
}