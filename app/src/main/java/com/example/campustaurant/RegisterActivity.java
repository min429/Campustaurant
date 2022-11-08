package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd; // 회원가입 입력필드
    private Button mBtnRegister; // 회원가입 입력버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // super class 호출(Activity를 구현하는데 필요)
        setContentView(R.layout.activity_register); // activity_register.xml 레이아웃을 받아와서 화면을 출력함

        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 사용 준비
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("User"); // 실시간 데이터베이스 사용준비

        mEtEmail = findViewById(R.id.et_email); // 회원가입 입력필드 초기화 // mEtemail이 et_email이라는 걸 알려줌
        mEtPwd = findViewById(R.id.et_pwd); // 회원가입 입력필드 초기화
        mBtnRegister = findViewById(R.id.btn_register); // 회원가입 입력버튼 초기화

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 버튼 클릭시 처리 시작
                String strEmail = mEtEmail.getText().toString(); // 이메일 입력칸에 입력한 text를 가져와서 string으로 변환 후 저장
                String strPwd = mEtPwd.getText().toString();

                // Firebase Auth 진행
                // 이메일과 비번 정보를 통해 유저를 생성한다.
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) { // 회원가입이 이루어졌을 때 처리할 것들
                                if(task.isSuccessful()){ // 정상적으로 회원가입이 처리되었으면
                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); // 회원가입된 유저 객체를 가져온다.
                                    User account = new User(); // 유저 객체 생성
                                    account.setUserId(firebaseUser.getEmail()); // email은 firebaseUser 객체로부터 가져와야 함
                                    account.setPassword(strPwd); // pwd는 사용자가 입력했던 것을 이용해서 그대로 가져와야 함

                                    // setValue : database에 insert (삽입) 행위 // child : 자식(User의 하위개념)
                                    mDatabaseRef.child(firebaseUser.getUid()).setValue(account); // database의 idToken 하위에 유저 객체 set

                                    // Toast메세지
                                    Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}