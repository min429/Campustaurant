package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatProfileActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference roomRef;
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

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });

        roomRef = database.getReference("Room");

        tvKick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stMyToken.equals(stHostToken) && !stUserToken.equals(stHostToken)){ // 방장일 경우, 방장이외의 유저를 강퇴할 수 있음
                    roomRef.child(stHostToken).child("ban").child(stUserToken).setValue("");
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