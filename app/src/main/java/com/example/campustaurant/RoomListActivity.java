package com.example.campustaurant;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomListActivity extends AppCompatActivity implements ClickCallbackListener{
    private static final String TAG = "RoomListActivity";

    private ArrayList<Room> roomArrayList;
    private RoomListAdapter roomListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase database;
    DatabaseReference ref;
    String stUserToken;
    String stUserId;
    String stRestaurant;
    String inputRestaurant;
    EditText etRestaurant;
    Button btnCreate;
    Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser(); // 현재 로그인한 유저 객체를 가져옴
        stUserToken = mFirebaseUser.getUid(); // 가져온 유저 객체의 토큰정보를 가져옴
        database = FirebaseDatabase.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // LayoutManager 설정
        roomArrayList = new ArrayList<>();
        roomListAdapter = new RoomListAdapter(roomArrayList, this); // roomArrayList에 담긴 것들을 어댑터에 담아줌
        // this -> RoomListActivity 객체
        recyclerView.setAdapter(roomListAdapter); // recyclerView에 roomListAdapter를 세팅해 주면 recyclerView가 이 어댑터를 사용해서 화면에 데이터를 띄워줌

        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 MainActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장

        etRestaurant = findViewById(R.id.et_restaurant);

        btnEnter = findViewById(R.id.btn_enter);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputRestaurant = etRestaurant.getText().toString();
                Intent intent = new Intent(RoomListActivity.this, SelectedListActivity.class);
                intent.putExtra("inputRestaurant", inputRestaurant);
                startActivity(intent);
                finish();
            }
        });

        btnCreate = findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomListActivity.this, CreateRoomActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 CreateRoomActivity에 넘겨줌
                intent.putExtra("userToken", stUserToken);
                startActivity(intent);
                finish();
            }
        });

        ref = database.getReference("Room"); // Room하위에서 데이터를 읽기 위해

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) { // room1,2,3... 하나씩 가져옴
                    Room room = postSnapshot.getValue(Room.class);
                    roomArrayList.add(room);
                    if(room.userId.equals(stUserId)){
                        stRestaurant = room.getRestaurant();
                    }
                }
                roomListAdapter.notifyDataSetChanged(); // 데이터가 바뀐다는 것을 알게 해줘야 함
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("RoomListActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }

    @Override
    public void onClick(int position) { // ClickCallbackListener 인터페이스의 메서드 -> RoomListAdapter에서 사용
        Room room = roomArrayList.get(position);
        Log.d(TAG, "room.userId: "+room.userId+", stUserId: "+stUserId+", room.restaurant: "+room.restaurant+", stRestaurant: "+stRestaurant);
        if(!room.userId.equals(stUserId) && room.restaurant.equals(stRestaurant)){ // 자신이외의 유저중에 자신과 같은 음식점을 선택한 사람이 있는 경우
            ref.child(stUserToken).setValue(null); // 현재 유저가 생성한 방을 폭파함
            ref.child(room.userToken).setValue(null); // 매칭상대가 생성한 방을 폭파함
            Intent intent = new Intent(RoomListActivity.this, ChatActivity.class);
            intent.putExtra("email", stUserId); // stUserId값(자신의 아이디)을 ChatActivity에 넘겨줌
            intent.putExtra("other", room.userId); // room.userId값(상대방 아이디)을 ChatActivity에 넘겨줌
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(RoomListActivity.this, "다른 유저와 매칭해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}