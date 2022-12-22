package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatProfileActivity extends AppCompatActivity {
    private static final String TAG = "ChatProfileActivity";

    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference roomRef;
    DatabaseReference chatRef;
    DatabaseReference profileRef;
    DatabaseReference userRef;
    ImageView ivProfile;
    Button btnClose;
    TextView tvKick;
    TextView tvName;
    TextView tvSex;
    TextView tvRating;
    TextView tvIntroduce;
    String stUserToken;
    String stHostToken;
    String stMyToken;
    String stUserName;
    String stOtherName;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀바 삭제(setContentView전에 써줘야함)
        setContentView(R.layout.activity_chat_profile);

        database = FirebaseDatabase.getInstance();
        ivProfile = findViewById(R.id.iv_profile);
        btnClose = findViewById(R.id.btn_close);
        tvKick = findViewById(R.id.tv_kick);
        tvName = findViewById(R.id.tv_name);
        tvSex = findViewById(R.id.tv_sex);
        tvIntroduce = findViewById(R.id.tv_introduce);
        tvRating =findViewById(R.id.tv_rating);
        stUserToken = getIntent().getStringExtra("userToken"); // 해당 채팅의 유저토큰
        stHostToken = getIntent().getStringExtra("hostToken"); // 방장의 유저토큰
        stMyToken = getIntent().getStringExtra("myToken"); // 자신의 유저토큰
        stUserName = getIntent().getStringExtra("userName"); // 자신의 이름

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });

        roomRef = database.getReference("Room").child(stHostToken);
        chatRef = database.getReference("Chat").child(stHostToken);
        profileRef = database.getReference("Profile").child(stUserToken);
        userRef = database.getReference("User").child(stUserToken);

        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stOtherName = dataSnapshot.child("name").getValue(String.class);
                if(stOtherName == null) stOtherName = "익명";
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvKick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stMyToken.equals(stHostToken) && !stUserToken.equals(stHostToken)){ // 방장일 경우, 방장이외의 유저를 강퇴할 수 있음
                    roomRef.child("ban").child(stUserToken).setValue(""); // ban처리
                    userRef.child("room").child(stHostToken).setValue(null); // 들어간 방 목록에서 삭제
                    roomRef.child("guest").child(stUserToken).setValue(null); // guest목록에서 삭제

                    Calendar c = Calendar.getInstance(); // 현재 날짜정보 가져옴
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 날짜 포맷 설정
                    String datetime = dateformat.format(c.getTime()); // datetime을 현재 날짜정보로 설정

                    Hashtable<String, String> table // DB테이블에 넣을 해시테이블
                            = new Hashtable<String, String>();
                    table.put("userToken", ""); // DB의 userToken란에 stUserToken값
                    table.put("datetime", datetime); // DB의 datetime란에 datetime값
                    table.put("userId", ""); // DB의 userId란에 stUserId값
                    table.put("text", stOtherName+"님이 강퇴당하셨습니다."); // DB의 text란에 stText값
                    // Chat클래스의 멤버변수의 명칭과 똑같은 이름으로 DB에 입력해야 Chat객체에 값을 읽어올 수 있음

                    chatRef.child(datetime).setValue(table); // 입력

                    finish();
                }
            }
        });

        ref = database.getReference("Profile").child(stUserToken);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                profile = datasnapshot.getValue(Profile.class);

                if(profile != null){
                    if(profile.getUri() != null) Glide.with(ChatProfileActivity.this).load(profile.getUri()).into(ivProfile);
                    if(profile.getName() != null) tvName.setText(profile.getName());
                    if(profile.getSex() != null) tvSex.setText(profile.getSex());
                    if(profile.getIntroduce() != null) tvIntroduce.setText(profile.getIntroduce());
                    tvRating.setText(Integer.toString(profile.getRating()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}