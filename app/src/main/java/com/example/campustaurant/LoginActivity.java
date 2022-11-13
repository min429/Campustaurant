package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    ArrayList<String> foodArrayList;
    DatabaseReference ref;
    FirebaseDatabase database;
    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    EditText mEtEmail, mEtPwd; // 로그인 입력필드
    String stEmail, stPwd;
    String fileName;
    int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // 로그인 액티비티 표출 // 하나의 액티비티(화면)는 하나의 XML만 연동 가능

        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 사용 준비
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("User"); // 실시간 데이터베이스 사용준비 // path : 앱 이름
        database = FirebaseDatabase.getInstance();
        foodArrayList = new ArrayList<>();
        mEtEmail = (EditText) findViewById(R.id.et_email); // 회원가입 입력필드 초기화 // R: res // R.id -> res 내에서 id가 et_email인 것을 찾아옴 // 단, 현재 액티비티와 연동된 XML에서만 찾음
        mEtPwd = (EditText) findViewById(R.id.et_pwd); // 회원가입 입력필드 초기화

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인 버튼 눌렀을 때 처리 시작

                // getText -> 클릭했을 때 처리를 시작해야함(클릭하기 전까지는 mEtEmail에 email값이 담겨있지 않기 때문)
                stEmail = mEtEmail.getText().toString(); // 이메일 입력칸에 입력한 text를 가져와서 string으로 변환 후 저장
                stPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(stEmail, stPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("email", stEmail); // stEmail값을 MainActivity에 넘겨줌
                            intent.putExtra("fileName", fileName);
                            intent.putExtra("foodArrayList", foodArrayList);
                            startActivity(intent);
                            finish(); // 로그인 이후에는 로그인 화면으로 다시 돌아올 일이 없으므로 현재 액티비티를 파기시킴
                        } else{
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 버튼을 눌렀을 때 처리 시작
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); // Intent -> 액티비티 이동(화면 이동) 현재 Activity -> 이동할 Activity
                startActivity(intent);
            }
        });

        ref = database.getReference("Food"); // Food하위에서 데이터를 읽기 위해

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> map = (HashMap<String,String>)dataSnapshot.getValue(); // 파이어베이스 DB는 Map형태로 저장되어있기 때문에 HashMap/Map으로 불러와야함
                for(String stFood : map.keySet()){ // map객체의 key값 리스트에서 값을 하나씩 가져와서 stFood에 저장
                    foodArrayList.add(stFood); // map객체의 key값 리스트에서 값을 하나씩 가져와서 stFood에 저장
                }

                Calendar c = Calendar.getInstance(); // 현재 날짜정보 가져옴
                SimpleDateFormat dateformat = new SimpleDateFormat("dd"); // 날짜 포맷 설정
                String stDate = dateformat.format(c.getTime()); // stDate를 현재 날짜정보로 설정

                int date = Integer.parseInt(stDate);
                idx = date%foodArrayList.size();
                Log.d(TAG, "date: "+date);

                fileName = foodArrayList.get(idx);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }
}