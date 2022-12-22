package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordListActivity extends AppCompatActivity implements ClickCallbackListener{
    private static final String TAG = "RecordListActivity";

    ArrayList<Record> recordArrayList;
    private RecordListAdapter recordListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference recordRef;
    String stMyToken;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // LayoutManager 설정
        recordArrayList = new ArrayList<>();
        recordListAdapter = new RecordListAdapter(recordArrayList, this); // recordArrayList에 담긴 것들을 어댑터에 담아줌
        // this -> RecordListActivity 객체
        recyclerView.setAdapter(recordListAdapter); // recyclerView에 recordListAdapter를 세팅해 주면 recyclerView가 이 어댑터를 사용해서 화면에 데이터를 띄워줌

        stMyToken = getIntent().getStringExtra("myToken");
        btnClose = findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recordRef = database.getReference("Record").child(stMyToken);
        recordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recordArrayList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Record record = postSnapshot.getValue(Record.class);
                    record.setDate(postSnapshot.getKey());
                    recordArrayList.add(record);
                }
                recordListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onClick(int position) { // ClickCallbackListener 인터페이스의 메서드 -> RestaurantAdapter에서 사용
        HashMap<String, String> userMap = recordArrayList.get(position).getUser();
        String stRestaurant = recordArrayList.get(position).getRestaurant();
        String stDate = recordArrayList.get(position).getDate();

        Intent intent = new Intent(RecordListActivity.this, RecordActivity.class);
        intent.putExtra("userMap", userMap);
        intent.putExtra("date", stDate);
        intent.putExtra("restaurant", stRestaurant);
        intent.putExtra("myToken", stMyToken);
        startActivity(intent);
    }

    @Override
    public void delete(int position) {}

    @Override
    public void remove(int position) {}
}