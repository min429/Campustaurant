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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseAuth mFirebaseAuth;
    FirebaseDatabase database;
    private DatabaseReference myRef;
    //FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); // 회원가입된 유저 객체를 가져온다.
    ArrayList<Match> matchArrayList = new ArrayList<>();
    Button btnLogout;
    Button btnMatch;
    Button btnEnter;
    EditText etRestaurant;
    String stUserId;
    String stRestaurant;
    String inputRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // activity_main 레이아웃 표출

        mFirebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Match");

        btnLogout = findViewById(R.id.btn_logout);
        btnMatch = findViewById(R.id.btn_match);
        btnEnter = findViewById(R.id.btn_enter);

        etRestaurant = findViewById(R.id.et_restaurant);

        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 LoginActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아웃 버튼 클릭시 처리 시작
                mFirebaseAuth.signOut(); // 로그아웃

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                // finish(); // 앱 종료
            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputRestaurant = etRestaurant.getText().toString();

                Calendar c = Calendar.getInstance(); // 현재 날짜정보 가져옴
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 날짜 포맷 설정
                String datetime = dateformat.format(c.getTime()); // datetime을 현재 날짜정보로 설정

                Hashtable<String, String> Data // DB테이블에 데이터 입력
                        = new Hashtable<String, String>();
                Data.put("userId", stUserId); // DB의 restaurant란에 inputRestaurant 값
                Data.put("restaurant", inputRestaurant); // DB의 restaurant란에 inputRestaurant 값
                myRef.child(datetime).setValue(Data); // 입력
            }
        });

        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Match user : matchArrayList){
                    Log.d(TAG, "user.userId: "+user.userId+", stUserId: "+stUserId+", user.restaurant: "+user.restaurant+", stRestaurant: "+stRestaurant);
                    if(!user.userId.equals(stUserId) && user.restaurant.equals(stRestaurant)){ // 자신이외의 유저중에 자신과 같은 음식점을 선택한 사람이 있는 경우
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