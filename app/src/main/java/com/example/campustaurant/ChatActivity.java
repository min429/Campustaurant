package com.example.campustaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private RecyclerView recyclerView;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    EditText etText;
    Button btnSend;
    String stUserId; // DB에 넣을 email값
    FirebaseDatabase database;
    ArrayList<Chat> chatArrayList; // Chat 객체 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database = FirebaseDatabase.getInstance();

        chatArrayList = new ArrayList<>();
        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 MainActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장
        Log.d(TAG, "stUserId: " + stUserId);
        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener((v) -> {finish(); }); // ChatActivity를 종료하면 다시 MainActivity로 돌아감
        btnSend = (Button)findViewById(R.id.btnSend);
        etText = (EditText) findViewById(R.id.etText);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true); // recyclerView의 레이아웃 사이즈를 고정시킴

        layoutManager = new LinearLayoutManager(this); // layoutManager를 생성 // LinearLayoutManager: 리스트를 상/하로 보여주는 타입(뷰가 수직으로 쌓임)
        recyclerView.setLayoutManager(layoutManager); // 리사이클러뷰의 LayoutManager를 위에서 생성한 layoutManager로 세팅

        mAdapter = new MyAdapter(chatArrayList, stUserId); // chat객체 배열, email값을 어댑터에 넣어줌
        recyclerView.setAdapter(mAdapter);

        DatabaseReference ref = database.getReference("message"); // message하위에 데이터 저장하기 위해

        // 파이어베이스가 수정이 될 때마다 실행되는 것들 (시작)
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) { // DB에 데이터가 추가되었을 때
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Chat chat = dataSnapshot.getValue(Chat.class); // DB에 있는 값들을 chat 객체에 가져옴
                chatArrayList.add(chat); // 객체배열에 chat 객체를 추가
                mAdapter.notifyDataSetChanged(); // 데이터가 바뀐다는 것을 알게 해줘야 함
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
                Toast.makeText(ChatActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // (끝)

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stText = etText.getText().toString();
                Toast.makeText(ChatActivity.this, "MSG: " + stText, Toast.LENGTH_SHORT).show();
                // Write a message to the database


                Calendar c = Calendar.getInstance(); // 현재 날짜정보 가져옴
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 날짜 포맷 설정
                String datetime = dateformat.format(c.getTime()); // datetime을 현재 날짜정보로 설정
                System.out.println(datetime);

                DatabaseReference myRef = database.getReference("message").child(datetime); // message하위에 datetime이 저장되도록 함

                Hashtable<String, String> numbers // DB테이블에 데이터 입력
                        = new Hashtable<String, String>();
                numbers.put("userId", stUserId); // DB의 email란에 stUserId 값
                numbers.put("text", stText); // DB의 text란에 stText 값
                // Chat클래스의 멤버변수의 명칭과 똑같은 이름으로 DB에 입력해야 Chat객체에 값을 읽어올 수 있음

                myRef.setValue(numbers); // 입력
            }
        });
    }
}