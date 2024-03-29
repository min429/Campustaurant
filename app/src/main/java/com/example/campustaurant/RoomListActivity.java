package com.example.campustaurant;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class RoomListActivity extends AppCompatActivity implements ClickCallbackListener, OnMapReadyCallback{
    private static final String TAG = "RoomListActivity";

    private MapView mapView;
    private static NaverMap naverMap;
    //마커 변수 선언 및 초기화
    private Marker marker = new Marker();
    private ArrayList<Location> locaArrayList;
    private ArrayList<Room> roomArrayList;
    private RoomListAdapter roomListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference userRef;
    String stUserToken;
    String stHostToken;
    String stUserId;
    String inputRestaurant;
    EditText etRestaurant;
    FloatingActionButton btnCreate;
    LatLng latLng;
    //검색창
    DrawerLayout MainScreen;
    private ImageButton Ib_searchopen;
    boolean searchopened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        MainScreen = (DrawerLayout) findViewById(R.id.dl_roomList);
        searchopened = false;

        latLng = getIntent().getParcelableExtra("latLng");
        // 지도 초기화 전에 정의해줘야 지도 초기화 할 때 이 값을 사용 가능함

        //네이버 지도 // 밑에 3줄 실행한 순간 onMapReady(지도 초기화) 호출됨
        mapView = (MapView) findViewById(R.id.mv_naver);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser(); // 현재 로그인한 유저 객체를 가져옴
        stUserToken = mFirebaseUser.getUid(); // 가져온 유저 객체의 토큰정보를 가져옴
        database = FirebaseDatabase.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // LayoutManager 설정
        roomArrayList = new ArrayList<>();
        roomListAdapter = new RoomListAdapter(roomArrayList, this); // roomArrayList에 담긴 것들을 어댑터에 담아줌
        // this -> RoomListActivity 객체
        recyclerView.setAdapter(roomListAdapter); // recyclerView에 roomListAdapter를 세팅해 주면 recyclerView가 이 어댑터를 사용해서 화면에 데이터를 띄워줌

        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 MainActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장
        inputRestaurant = getIntent().getStringExtra("inputRestaurant");
        locaArrayList = (ArrayList<Location>)getIntent().getSerializableExtra("locaArrayList");
        // getSerializableExtra : ArrayList를 intent를 통해 받아올 때 직렬화 되어있는 리스트를 받아와야 함
        Log.d(TAG, "locaArrayList: "+locaArrayList);
        Log.d(TAG, "inputRestaurant: "+inputRestaurant);

        if (inputRestaurant == null || inputRestaurant.equals("")) {
            if (mapView.getVisibility() == View.VISIBLE)
                mapView.setVisibility(View.GONE);
        }else{
            if (mapView.getVisibility() == View.INVISIBLE)
                mapView.setVisibility(View.VISIBLE);
        }

        //검색창
        // 레이아웃 인플레이터 객체
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Ib_searchopen = (ImageButton)findViewById(R.id.Ib_searchopen); // 열기 버튼
        Ib_searchopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인에 새로 생성한 레이아웃 추가
                searchopened = true;
                MainScreen.addView(layoutInflater.inflate(R.layout.search_view,null));
                etRestaurant = findViewById(R.id.et_input);
                etRestaurant.setHint("음식점을 입력해보세요!");

                etRestaurant.setOnEditorActionListener(new TextView.OnEditorActionListener() { // 키보드에서 바로 검색
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                        switch (actionId)
                        {
                            case IME_ACTION_SEARCH :
                                inputRestaurant = etRestaurant.getText().toString();

                                searchopened = false;
                                MainScreen.removeViewAt(1);

                                // locaArrayList에서 가져온 location으로 latLng 초기화
                                for(Location location : locaArrayList){
                                    if(location.getName().equals(inputRestaurant)){
                                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        break;
                                    }
                                }
                                // 인텐트 새로고침
                                finish(); // 인텐트 종료
                                overridePendingTransition(0, 0); // 인텐트 효과 없애기
                                Intent intent = getIntent(); // 인텐트
                                intent.putExtra("email", stUserId);
                                intent.putExtra("inputRestaurant", inputRestaurant);
                                intent.putExtra("locaArrayList", locaArrayList);
                                intent.putExtra("latLng", latLng);
                                startActivity(intent);
                                overridePendingTransition(0, 0); // 인텐트 효과 없애기
                        }
                        return true;
                    }
                });

                ImageButton ibSearchClose = (ImageButton) findViewById(R.id.Ib_searchclose);
                ibSearchClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchopened = false;
                        MainScreen.removeViewAt(2);
                    }
                });
            }
        });

        btnCreate = findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomListActivity.this, CreateRoomActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 CreateRoomActivity에 넘겨줌
                intent.putExtra("myToken", stUserToken);
                intent.putExtra("locaArrayList", locaArrayList);
                startActivity(intent);
                finish();
            }
        });

        ref = database.getReference("Room"); // Room하위에서 데이터를 읽기 위해

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomArrayList.clear();
                if (inputRestaurant == null || inputRestaurant.equals("")) { // 대기열 화면을 처음 띄울 때 or 입력창에 아무것도 입력하지 않았을 때
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { // room1,2,3... 하나씩 가져옴
                        Room room = postSnapshot.getValue(Room.class);
                        roomArrayList.add(room);
                    }
                } else {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { // room1,2,3... 하나씩 가져옴
                        Room room = postSnapshot.getValue(Room.class);
                        if (room.getRestaurant().equals(inputRestaurant)) {
                            roomArrayList.add(room);
                        }
                    }
                }
                roomListAdapter.notifyDataSetChanged(); // 데이터가 바뀐다는 것을 알게 해줘야 함
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("RoomListActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        userRef = database.getReference("User").child(stUserToken);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stHostToken = dataSnapshot.child("room").getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(int position) { // ClickCallbackListener 인터페이스의 메서드 -> RoomListAdapter에서 사용
        Room room = roomArrayList.get(position);

        if(stHostToken != null && !room.getHostToken().equals(stHostToken)){ // 이미 참가한 대기방이 있으면
            Toast.makeText(RoomListActivity.this, "이미 참가한 대기방이 있습니다.", Toast.LENGTH_SHORT).show();
        }
        else { // 아직 참가한 대기방이 없거나 참여중인 대기방인 경우
            userRef.child("room").setValue(room.getHostToken()); // 들어간 방 확정

            Intent intent = new Intent(RoomListActivity.this, ChatActivity.class);
            intent.putExtra("email", stUserId); // stUserId값(자신의 아이디)을 ChatActivity에 넘겨줌
            intent.putExtra("hostId", room.getHostId()); // room.userId값(방장 아이디)을 ChatActivity에 넘겨줌
            intent.putExtra("hostToken", room.getHostToken()); // room.hostToken값(방장 아이디토큰)을 ChatActivity에 넘겨줌
            intent.putExtra("myToken", stUserToken); // stUserToken값(자신의 아이디토큰)을 ChatActivity에 넘겨줌
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void delete(int position) {}

    @Override
    public void remove(int position) {}

    private void setMarker(Marker marker,  double lat, double lng, int resourceID, int zIndex)
    {
        //원근감 표시
        marker.setIconPerspectiveEnabled(true);
        //아이콘 지정
        marker.setIcon(OverlayImage.fromResource(resourceID));
        //마커의 투명도
        marker.setAlpha(0.8f);
        //마커 위치
        marker.setPosition(new LatLng(lat, lng));
        //마커 우선순위
        marker.setZIndex(zIndex);
        //마커 표시
        marker.setMap(naverMap);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap)
    {
        this.naverMap = naverMap;

        //배경 지도 선택
        naverMap.setMapType(NaverMap.MapType.Navi);

        //건물 표시
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        Log.d(TAG, "latLng: "+latLng);

        //위치 및 각도 조정
        CameraPosition cameraPosition = new CameraPosition(
                latLng,   // 위치 지정
                15,                                     // 줌 레벨
                0,                                       // 기울임 각도
                0                                     // 방향
        );
        naverMap.setCameraPosition(cameraPosition);

        setMarker(marker, latLng.latitude, latLng.longitude, R.drawable.ic_baseline_place_24, 0);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}