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
    HashMap<String, String> userMap;
    Button btnClose;
    TextView tvDate;
    TextView tvRestaurant;
    TextView tvPeopleNum;
    String stDate;
    String stRestaurant;
    int peopleNum;
    boolean rate = false;

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
        peopleNum = userMap.size();

        tvDate.setText(stDate);
        tvRestaurant.setText(stRestaurant);
        tvPeopleNum.setText(String.valueOf(peopleNum));

        /*
        recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,String> map = (HashMap<String,String>)dataSnapshot.getValue(); // 파이어베이스 DB는 Map형태로 저장되어있기 때문에 HashMap/Map으로 불러와야함
                if(map != null){
                    for(String rateUser : map.keySet()){ // map객체의 key값 리스트에서 값을 하나씩 가져와서 rateUser에 저장
                        if(rateUser.equals(stMyToken)) rate = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */

        /*
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, ReviewActivity.class);
                intent.putExtra("myToken", stMyToken);
                startActivity(intent);
            }
        });
        */

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
        Profile profile = profileArrayList.get(position);
        Intent intent = new Intent(RecordActivity.this, ReviewActivity.class);
        intent.putExtra("profile", (Serializable) profile);
        startActivity(intent);
    }

    @Override
    public void delete(int position) {}

    @Override
    public void remove(int position) {}
}