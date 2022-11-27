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
import java.util.HashMap;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity implements ClickCallbackListener{
    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;
    EditText etText;
    Button btnSend;
    Button btnFinish;
    String stUserId; // DB에 넣을 email값
    String stOtherId; // 방장 아이디
    String stHostToken; // 방장 토큰
    String stUserToken; // 유저 토큰
    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference roomRef;
    ArrayList<Chat> chatArrayList; // Chat 객체 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        chatArrayList = new ArrayList<>();
        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 MainActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장
        stOtherId = getIntent().getStringExtra("hostId");
        stHostToken = getIntent().getStringExtra("hostToken");
        stUserToken = getIntent().getStringExtra("myToken");
        btnFinish = findViewById(R.id.btnFinish);
        btnSend = (Button)findViewById(R.id.btnSend);
        etText = (EditText) findViewById(R.id.etText);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        roomRef = database.getReference("Room").child(stHostToken);

        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Room.class) == null){
                    if(!stUserToken.equals(stHostToken)){ // 방장이 아니라면
                        Toast.makeText(ChatActivity.this, "더이상 존재하지 않는 방입니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                DataSnapshot childSnapshot = dataSnapshot.child("ban");
                HashMap<String,String> map = (HashMap<String,String>)childSnapshot.getValue(); // 파이어베이스 DB는 Map형태로 저장되어있기 때문에 HashMap/Map으로 불러와야함
                if(map != null){
                    for(String banUser : map.keySet()){ // map객체의 key값 리스트에서 값을 하나씩 가져와서 banUser에 저장
                        if(stUserToken.equals(banUser)){
                            Toast.makeText(ChatActivity.this, "강퇴당하셨습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // ChatActivity를 종료하면 다시 MainActivity로 돌아감

                if(stUserId.equals(stOtherId)){ // 방장이 나가면
                    roomRef.setValue(null); // 대기방을 폭파함
                    ref.setValue(null); // 채팅방을 폭파함
                }
            }
        });

        recyclerView.setHasFixedSize(true); // recyclerView의 레이아웃 사이즈를 고정시킴

        layoutManager = new LinearLayoutManager(this); // layoutManager를 생성 // LinearLayoutManager: 리스트를 상/하로 보여주는 타입(뷰가 수직으로 쌓임)
        recyclerView.setLayoutManager(layoutManager); // 리사이클러뷰의 LayoutManager를 위에서 생성한 layoutManager로 세팅

        chatAdapter = new ChatAdapter(chatArrayList, stUserId, this); // chat객체 배열, email값을 어댑터에 넣어줌
        recyclerView.setAdapter(chatAdapter);

        ref = database.getReference("Chat").child(stHostToken); // Chat하위에 데이터 저장하기 위해

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatArrayList.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Chat chat = postSnapshot.getValue(Chat.class);
                    chatArrayList.add(chat);
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
                etText.setText("");
                // Write a message to the database

                Calendar c = Calendar.getInstance(); // 현재 날짜정보 가져옴
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 날짜 포맷 설정
                String datetime = dateformat.format(c.getTime()); // datetime을 현재 날짜정보로 설정

                DatabaseReference myRef = database.getReference("Chat").child(stHostToken).child(datetime); // Chat하위에 datetime이 저장되도록 함

                Hashtable<String, String> numbers // DB테이블에 넣을 해시테이블
                        = new Hashtable<String, String>();
                numbers.put("userToken", stUserToken); // DB의 userToken란에 stUserToken값
                numbers.put("datetime", datetime); // DB의 datetime란에 datetime값
                numbers.put("userId", stUserId); // DB의 userId란에 stUserId값
                numbers.put("text", stText); // DB의 text란에 stText값
                // Chat클래스의 멤버변수의 명칭과 똑같은 이름으로 DB에 입력해야 Chat객체에 값을 읽어올 수 있음

                myRef.setValue(numbers); // 입력
            }
        });
    }

    @Override
    public void onClick(int position) { // ClickCallbackListener 인터페이스의 메서드 -> ProfileActivity에서 사용
        Chat chat = chatArrayList.get(position);

        Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
        intent.putExtra("userToken", chat.getUserToken()); // 해당 채팅의 유저토큰을 넘겨줌
        intent.putExtra("myToken", stUserToken); // 자신의 유저토큰을 넘겨줌
        intent.putExtra("hostToken", stHostToken); // 방장의 유저토큰을 넘겨줌
        startActivity(intent);
    }

    @Override
    public void delete(int position) {}

    @Override
    public void remove(int position) {

    }
}