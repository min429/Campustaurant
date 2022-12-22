package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

public class ReviewActivity extends AppCompatActivity {
    private static final String TAG = "ReviewActivity";

    private FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference profileRef = database.getReference("Profile");
    DatabaseReference notificationRef = database.getReference("Notification");
    DatabaseReference recordRef = database.getReference("Record");
    ImageView ivGood;
    ImageView ivBad;
    Button btnSubmit;
    Button btnClose;
    EditText etReview;
    Profile profile;
    String stMyToken;
    String stMyName;
    String stOtherToken;
    String stUri;
    String stRate;
    String stReview;
    String stDate;
    int intRating;
    boolean good = false;
    boolean bad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser(); // 현재 로그인한 유저 객체를 가져옴
        stMyToken = mFirebaseUser.getUid(); // 가져온 유저 객체의 토큰정보를 가져옴
        ivGood = findViewById(R.id.iv_good);
        ivBad = findViewById(R.id.iv_bad);
        btnSubmit = findViewById(R.id.btn_submit);
        btnClose = findViewById(R.id.btn_close);
        etReview = findViewById(R.id.et_review);
        profile = (Profile) getIntent().getSerializableExtra("profile");
        stDate = getIntent().getStringExtra("date");
        stOtherToken = profile.getUserToken();

        profileRef.child(stMyToken).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                stMyName = profile.getName();
                stUri = profile.getUri();
                intRating = profile.getRating();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                good = true;
                bad = false;
                Glide.with(ReviewActivity.this).load("https://firebasestorage.googleapis.com/v0/b/campustaurant.appspot.com/o/rate%2Fgoodcheck.png?alt=media&token=df9e942c-faf8-486b-b8e4-f5b425008bd8").into(ivGood);
                Glide.with(ReviewActivity.this).load("https://firebasestorage.googleapis.com/v0/b/campustaurant.appspot.com/o/rate%2Fbad.png?alt=media&token=f62171b2-9099-4cec-86a0-567f9d267482").into(ivBad);
            }
        });

        ivBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bad = true;
                good = false;
                Glide.with(ReviewActivity.this).load("https://firebasestorage.googleapis.com/v0/b/campustaurant.appspot.com/o/rate%2Fbadcheck.png?alt=media&token=556caf46-0445-4a99-a5ed-c110cf3bc6d1").into(ivBad);
                Glide.with(ReviewActivity.this).load("https://firebasestorage.googleapis.com/v0/b/campustaurant.appspot.com/o/rate%2Fgood.png?alt=media&token=45798afe-40cf-48d1-b963-1f7f1a722883").into(ivGood);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!good && !bad){ // 평점을 남기지 않은 경우
                    Toast.makeText(ReviewActivity.this, "평점을 남겨주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(good){
                        profileRef.child(stOtherToken).child("rating").setValue(profile.getRating()+1);
                        stRate = "good";
                    }
                    else if(bad){
                        profileRef.child(stOtherToken).child("rating").setValue(profile.getRating()-1);
                        stRate = "bad";
                    }
                    stReview = etReview.getText().toString();

                    Calendar c = Calendar.getInstance(); // 현재 날짜정보 가져옴
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 날짜 포맷 설정
                    String datetime = dateformat.format(c.getTime()); // datetime을 현재 날짜정보로 설정

                    Hashtable<String, String> table // DB테이블에 넣을 해시테이블
                            = new Hashtable<String, String>();
                    table.put("datetime", datetime); // DB의 datetime란에 datetime값
                    table.put("userName", stMyName); // DB의 userName란에 stMyName값
                    table.put("uri", stUri); // DB의 uri란에 stUri값
                    table.put("rate", stRate); // DB의 rate란에 stRate값
                    table.put("review", stReview); // DB의 review란에 stReview값

                    notificationRef.child(stOtherToken).child(datetime).setValue(table); // 입력
                    notificationRef.child(stOtherToken).child(datetime).child("rating").setValue(intRating); // DB의 rating란에 intRating값

                    recordRef.child(stMyToken).child(stDate).child("user").child(stOtherToken).setValue("check");

                    finish();
                }
            }
        });

    }
}