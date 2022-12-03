package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    private FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference recordRef = database.getReference("Record");
    DatabaseReference profileRef = database.getReference("Profile");
    DatabaseReference notificationRef = database.getReference("Notification");
    ImageView ivGood;
    ImageView ivBad;
    Button btnSubmit;
    Button btnClose;
    EditText etReview;
    String myToken;
    String stUserName;
    String stUserToken;
    String stOtherToken;
    String stUri;
    String stRate;
    String stReview;
    int intRating;
    boolean good = false;
    boolean bad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser(); // 현재 로그인한 유저 객체를 가져옴
        stUserToken = mFirebaseUser.getUid(); // 가져온 유저 객체의 토큰정보를 가져옴
        ivGood = findViewById(R.id.iv_good);
        ivBad = findViewById(R.id.iv_bad);
        btnSubmit = findViewById(R.id.btn_submit);
        btnClose = findViewById(R.id.btn_close);
        etReview = findViewById(R.id.et_review);
        myToken = getIntent().getStringExtra("myToken");

        profileRef.child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stOtherToken = dataSnapshot.getKey();
                Profile profile = dataSnapshot.getValue(Profile.class);
                stUserName = profile.getName();
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
                Glide.with(ReviewActivity.this).load("https://firebasestorage.googleapis.com/v0/b/campustaurant.appspot.com/o/rate%2Fgoodcheck.png?alt=media&token=df9e942c-faf8-486b-b8e4-f5b425008bd8").into(ivGood);
            }
        });

        ivBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bad = true;
                Glide.with(ReviewActivity.this).load("https://firebasestorage.googleapis.com/v0/b/campustaurant.appspot.com/o/rate%2Fbadcheck.png?alt=media&token=556caf46-0445-4a99-a5ed-c110cf3bc6d1").into(ivBad);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(good){
                    recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            int rating = dataSnapshot.getValue(Integer.class);
                            rating += 1;
                            profileRef.child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").setValue(rating);
                            recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").setValue(rating);
                        }
                    });
                    recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rate").child(myToken).setValue("");
                    stRate = "good";
                }
                else if(bad){
                    recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            int rating = dataSnapshot.getValue(Integer.class);
                            rating -= 1;
                            profileRef.child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").setValue(rating);
                            recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").setValue(rating);
                        }
                    });
                    recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rate").child(myToken).setValue("");
                    stRate = "bad";
                }
                stReview = etReview.getText().toString();

                Calendar c = Calendar.getInstance(); // 현재 날짜정보 가져옴
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 날짜 포맷 설정
                String datetime = dateformat.format(c.getTime()); // datetime을 현재 날짜정보로 설정

                Hashtable<String, String> table // DB테이블에 넣을 해시테이블
                        = new Hashtable<String, String>();
                table.put("datetime", datetime); // DB의 datetime란에 datetime값
                table.put("otherToken", stOtherToken); // DB의 otherToken란에 stOtherToken값
                table.put("userName", stUserName); // DB의 userName란에 stUserName값
                table.put("uri", stUri); // DB의 uri란에 stUri값
                table.put("rate", stRate); // DB의 rate란에 stRate값
                table.put("review", stReview); // DB의 review란에 stReview값

                notificationRef.child(datetime).setValue(table); // 입력
                notificationRef.child(datetime).child("rating").setValue(intRating); // DB의 rating란에 intRating값

                finish();
            }
        });

    }
}