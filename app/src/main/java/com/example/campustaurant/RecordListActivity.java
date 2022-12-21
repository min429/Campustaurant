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
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordListActivity extends AppCompatActivity implements ClickCallbackListener{
    private static final String TAG = "RecordListActivity";

    ArrayList<History> historyArrayList;
    private RecordListAdapter recordListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference historyRef;
    LinearLayout llRecord;
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
        historyArrayList = new ArrayList<>();
        recordListAdapter = new RecordListAdapter(historyArrayList, this); // historyArrayList에 담긴 것들을 어댑터에 담아줌
        // this -> RecordListActivity 객체
        recyclerView.setAdapter(recordListAdapter); // recyclerView에 recordListAdapter를 세팅해 주면 recyclerView가 이 어댑터를 사용해서 화면에 데이터를 띄워줌

        stMyToken = getIntent().getStringExtra("myToken");
        btnClose = findViewById(R.id.btn_close);

//        llRecord = findViewById(R.id.ll_recordList);
//        llRecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RecordListActivity.this, RecordActivity.class);
//                intent.putExtra("myToken", stMyToken);
//                startActivity(intent);
//            }
//        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        historyRef = database.getReference("History").child(stMyToken);
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyArrayList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    History history = postSnapshot.getValue(History.class);
                    history.setDate(postSnapshot.getKey());
                    historyArrayList.add(history);
                }
                recordListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onClick(int position) { // ClickCallbackListener 인터페이스의 메서드 -> RestaurantAdapter에서 사용
        HashMap<String, String> userMap = historyArrayList.get(position).getUser();
        String stRestaurant = historyArrayList.get(position).getRestaurant();
        String stDate = historyArrayList.get(position).getDate();

        Intent intent = new Intent(RecordListActivity.this, RecordActivity.class);
        intent.putExtra("userMap", userMap);
        intent.putExtra("date", stDate);
        intent.putExtra("restaurant", stRestaurant);
        startActivity(intent);
    }

    @Override
    public void delete(int position) {}

    @Override
    public void remove(int position) {}
}