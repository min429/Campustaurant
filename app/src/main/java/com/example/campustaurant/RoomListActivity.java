package com.example.campustaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RoomListActivity extends AppCompatActivity {
    private static final String TAG = "RoomListActivity";

    private ArrayList<Match> matchArrayList;
    private RoomListAdapter roomListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    FirebaseDatabase database;

    String stUserId;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        database = FirebaseDatabase.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // LayoutManager 설정
        matchArrayList = new ArrayList<>();
        roomListAdapter = new RoomListAdapter(matchArrayList); // matchArrayList에 담긴 것들을 어댑터에 담아줌
        recyclerView.setAdapter(roomListAdapter); // recyclerView에 roomListAdapter를 세팅해 주면 recyclerView가 이 어댑터를 사용해서 화면에 데이터를 띄워줌

        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 MainActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장

        btnCreate = findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomListActivity.this, CreateRoomActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 ChatActivity에 넘겨줌
                startActivity(intent);
            }
        });

        DatabaseReference ref = database.getReference("Match"); // Match하위에서 데이터를 읽기 위해

        // 파이어베이스가 수정이 될 때마다 실행되는 것들 (시작)
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) { // DB에 데이터가 추가되었을 때
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Match match = dataSnapshot.getValue(Match.class); // DB에 있는 값들을 match 객체에 가져옴
                matchArrayList.add(match); // 객체배열에 match 객체를 추가
                roomListAdapter.notifyDataSetChanged(); // 데이터가 바뀐다는 것을 알게 해줘야 함
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) { // DB에 데이터가 수정되었을 때
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.


                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { // DB에 데이터가 삭제되었을 때
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RoomListActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // (끝)
    }
}