package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements ClickCallbackListener{
    private static final String TAG = "RecordActivity";

    ArrayList<Profile> profileArrayList;
    private RecordAdapter recordAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference profileRef = database.getReference("Profile");
    DatabaseReference recordRef = database.getReference("Record");
    HashMap<String, String> userMap;
    Button btnClose;
    TextView tvDate;
    TextView tvRestaurant;
    TextView tvPeopleNum;
    String stDate;
    String stRestaurant;
    String stMyToken;
    int peopleNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // LayoutManager 설정
        profileArrayList = new ArrayList<>();
        recordAdapter = new RecordAdapter(profileArrayList, this); // profileArrayList에 담긴 것들을 어댑터에 담아줌
        // this -> RecordActivity 객체
        recyclerView.setAdapter(recordAdapter); // recyclerView에 recordAdapter를 세팅해 주면 recyclerView가 이 어댑터를 사용해서 화면에 데이터를 띄워줌
        btnClose = findViewById(R.id.btn_close);
        tvDate = findViewById(R.id.tv_date);
        tvRestaurant = findViewById(R.id.tv_restaurant);
        tvPeopleNum = findViewById(R.id.tv_peopleNum);
        userMap = (HashMap<String, String>) getIntent().getSerializableExtra("userMap");
        stDate = getIntent().getStringExtra("date");
        stRestaurant = getIntent().getStringExtra("restaurant");
        stMyToken = getIntent().getStringExtra("myToken");
        peopleNum = userMap.size();

        tvDate.setText(stDate);
        tvRestaurant.setText(stRestaurant);
        tvPeopleNum.setText(String.valueOf(peopleNum));

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileArrayList.clear();
                for(String userToken: userMap.keySet()){
                    profileRef.child(userToken).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            Profile profile = dataSnapshot.getValue(Profile.class);
                            profile.setUserToken(userToken);
                            profileArrayList.add(profile);
                            recordAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onClick(int position) {
        // 이미 리뷰썼는지 확인
        recordRef.child(stMyToken).child(stDate).child("user").child(profileArrayList.get(position).getUserToken()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String rate = dataSnapshot.getValue(String.class);
                if(rate.equals("check")) // 이미 썼다면
                    Toast.makeText(RecordActivity.this, "이미 리뷰를 남긴 유저입니다.", Toast.LENGTH_SHORT).show();
                else { // 아직 쓰지 않았다면
                    Profile profile = profileArrayList.get(position);
                    Intent intent = new Intent(RecordActivity.this, ReviewActivity.class);
                    intent.putExtra("profile", (Serializable) profile);
                    intent.putExtra("date", stDate);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void delete(int position) {}

    @Override
    public void remove(int position) {}
}