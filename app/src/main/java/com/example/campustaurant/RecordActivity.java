package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    private static final String TAG = "RecordActivity";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference recordRef = database.getReference("Record");
    TextView tvName;
    TextView tvRating;
    ImageView ivProfile;
    Button btnRate;
    Button btnClose;
    String myToken;
    boolean rate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //tvName = findViewById(R.id.tv_name);
        tvRating = findViewById(R.id.tv_rating);
        //ivProfile = findViewById(R.id.iv_profile);
        btnClose = findViewById(R.id.btn_close);
        btnRate = findViewById(R.id.btn_rate);
        ivProfile = findViewById(R.id.iv_profile);
        tvName = findViewById(R.id.tv_name);
        myToken = getIntent().getStringExtra("myToken");

        recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,String> map = (HashMap<String,String>)dataSnapshot.getValue(); // 파이어베이스 DB는 Map형태로 저장되어있기 때문에 HashMap/Map으로 불러와야함
                if(map != null){
                    for(String rateUser : map.keySet()){ // map객체의 key값 리스트에서 값을 하나씩 가져와서 rateUser에 저장
                        if(rateUser.equals(myToken)) rate = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, ReviewActivity.class);
                intent.putExtra("myToken", myToken);
                startActivity(intent);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        ibRateGood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!rate){
//                    Toast.makeText(RecordActivity.this, "up", Toast.LENGTH_SHORT).show();
//                    recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                        @Override
//                        public void onSuccess(DataSnapshot dataSnapshot) {
//                            int rating = dataSnapshot.getValue(Integer.class);
//                            rating += 1;
//                            profileRef.child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").setValue(rating);
//                            recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").setValue(rating);
//                        }
//                    });
//                    recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rate").child(myToken).setValue("");
//                }
//            }
//        });
//
//        ibRateBad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!rate){
//                    Toast.makeText(RecordActivity.this, "down", Toast.LENGTH_SHORT).show();
//                    recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                        @Override
//                        public void onSuccess(DataSnapshot dataSnapshot) {
//                            int rating = dataSnapshot.getValue(Integer.class);
//                            rating -= 1;
//                            profileRef.child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").setValue(rating);
//                            recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").setValue(rating);
//                        }
//                    });
//                    recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rate").child(myToken).setValue("");
//                }
//            }
//        });



        recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvName.setText(dataSnapshot.child("name").getValue(String.class));
                tvRating.setText(Integer.toString(dataSnapshot.child("rating").getValue(Integer.class)));
                Glide.with(RecordActivity.this).load(dataSnapshot.child("uri").getValue(String.class)).into(ivProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}