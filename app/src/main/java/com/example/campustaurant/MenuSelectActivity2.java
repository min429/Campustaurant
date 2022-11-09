package com.example.campustaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MenuSelectActivity2 extends AppCompatActivity {
    private static final String TAG = "MenuSelectActivity2";

    private ArrayList<Menu> menuArrayList;
    private MenuSelectAdapter2 menuSelectAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select2);

        database = FirebaseDatabase.getInstance();

        recyclerView = (RecyclerView)findViewById(R.id.rv);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // LayoutManager 설정

        menuArrayList = new ArrayList<>();

        menuSelectAdapter = new MenuSelectAdapter2(menuArrayList); // menuArrayList에 담긴 것들을 어댑터에 담아줌
        recyclerView.setAdapter(menuSelectAdapter); // recyclerView에 menuSelectAdapter를 세팅해 주면 recyclerView가 이 어댑터를 사용해서 화면에 데이터를 띄워줌

        DatabaseReference ref = database.getReference("food2"); // food2하위에서 데이터를 읽기 위해
        
        // 파이어베이스가 수정이 될 때마다 실행되는 것들 (시작)
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) { // DB에 데이터가 추가되었을 때
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Menu menu = dataSnapshot.getValue(Menu.class); // DB에 있는 값들을 menu 객체에 가져옴
                menuArrayList.add(menu); // 객체배열에 menu 객체를 추가
                Log.d(TAG, "food: "+menu.getFood());
                menuSelectAdapter.notifyDataSetChanged(); // 데이터가 바뀐다는 것을 알게 해줘야 함
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
                Toast.makeText(MenuSelectActivity2.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // (끝)
    }
}