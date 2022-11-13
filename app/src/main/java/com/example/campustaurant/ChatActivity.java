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
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;
    EditText etText;
    Button btnSend;
    Button btnFinish;
    String stUserId; // DB에 넣을 email값
    String stOtherId; // 매칭 상대 아이디
    FirebaseDatabase database;
    ArrayList<Chat> chatArrayList; // Chat 객체 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        chatArrayList = new ArrayList<>();
        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 MainActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장
        stOtherId = getIntent().getStringExtra("other");
        btnFinish = findViewById(R.id.btnFinish);
        btnSend = (Button)findViewById(R.id.btnSend);
        etText = (EditText) findViewById(R.id.etText);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        btnFinish.setOnClickListener((v) -> {finish(); }); // ChatActivity를 종료하면 다시 MainActivity로 돌아감

        recyclerView.setHasFixedSize(true); // recyclerView의 레이아웃 사이즈를 고정시킴

        layoutManager = new LinearLayoutManager(this); // layoutManager를 생성 // LinearLayoutManager: 리스트를 상/하로 보여주는 타입(뷰가 수직으로 쌓임)
        recyclerView.setLayoutManager(layoutManager); // 리사이클러뷰의 LayoutManager를 위에서 생성한 layoutManager로 세팅

        chatAdapter = new ChatAdapter(chatArrayList, stUserId); // chat객체 배열, email값을 어댑터에 넣어줌
        recyclerView.setAdapter(chatAdapter);

        DatabaseReference ref = database.getReference("Chat"); // Chat하위에 데이터 저장하기 위해

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) { // chat1,2,3... 하나씩 가져옴
                    Chat chat = postSnapshot.getValue(Chat.class);
                    if(chat.userId.equals(stUserId) || chat.userId.equals(stOtherId)){
                        chatArrayList.add(chat);
                    }
                }
                chatAdapter.notifyDataSetChanged(); // 데이터가 바뀐다는 것을 알게 해줘야 함
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("SelectedListActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stText = etText.getText().toString();
                Toast.makeText(ChatActivity.this, "MSG: " + stText, Toast.LENGTH_SHORT).show();
                // Write a message to the database


                Calendar c = Calendar.getInstance(); // 현재 날짜정보 가져옴
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 날짜 포맷 설정
                String datetime = dateformat.format(c.getTime()); // datetime을 현재 날짜정보로 설정

                DatabaseReference myRef = database.getReference("Chat").child(datetime); // Chat하위에 datetime이 저장되도록 함

                Hashtable<String, String> numbers // DB테이블에 데이터 입력
                        = new Hashtable<String, String>();
                numbers.put("userId", stUserId); // DB의 userId란에 stUserId 값
                numbers.put("text", stText); // DB의 text란에 stText 값
                // Chat클래스의 멤버변수의 명칭과 똑같은 이름으로 DB에 입력해야 Chat객체에 값을 읽어올 수 있음

                myRef.setValue(numbers); // 입력
            }
        });
    }
}