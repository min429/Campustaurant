package com.example.campustaurant;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;

public class CreateRoomActivity extends AppCompatActivity implements OnMapReadyCallback, ClickCallbackListener {
    private static final String TAG = "CreateRoomActivity";

    private MapView mapView;
    private static NaverMap naverMap;
    //마커 변수 선언 및 초기화
    private Marker marker = new Marker();
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference chatRef;
    DatabaseReference userRef;
    ArrayAdapter<CharSequence> adspin1, adspin2, adspin3;
    TagAdapter tagAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    EditText etRoomName;
    EditText etTag;
    String stUserToken;
    String stHostToken;
    String stRoomName;
    String stUserId;
    String stFood;
    String stRestaurant;
    Button btnRegister;
    ImageView ivMap;
    ImageButton ibEnter;
    ArrayList<Location> locaArrayList;
    ArrayList<String> tagArrayList;
    LatLng latLng = new LatLng(36.628881, 127.460586);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        //네이버 지도 // 밑에 3줄 실행한 순간 onMapReady(지도 초기화) 호출됨
        mapView = (MapView) findViewById(R.id.mv_naver);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(CreateRoomActivity.this);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Room");
        etRoomName = findViewById(R.id.et_roomName);
        etTag = findViewById(R.id.et_tag);
        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 RoomListActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장
        stUserToken = getIntent().getStringExtra("myToken");
        locaArrayList = (ArrayList<Location>)getIntent().getSerializableExtra("locaArrayList");
        tagArrayList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.rv_tag);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false); // 수평 리사이클러뷰
        recyclerView.setLayoutManager(linearLayoutManager);
        tagAdapter = new TagAdapter(tagArrayList, this); // tagArrayList에 담긴 것들을 어댑터에 담아줌
        recyclerView.setAdapter(tagAdapter); // recyclerView에 tagAdapter를 세팅해 주면 recyclerView가 이 어댑터를 사용해서 화면에 데이터를 띄워줌
        btnRegister = (Button)findViewById(R.id.btn_register);
        ivMap = findViewById(R.id.iv_map);
        ibEnter = findViewById(R.id.ib_enter);
        final Spinner spin1 = (Spinner)findViewById(R.id.spinner1);
        final Spinner spin2 = (Spinner)findViewById(R.id.spinner2);
        final Spinner spin3 = (Spinner)findViewById(R.id.spinner3);
//xml과 class에 변수들을 연결해줍니다. final를 사용한 이유는 spin2가 함수안에서 사용되기 때문에 코딩전체로 선언한 것입니다.
        adspin1 = ArrayAdapter.createFromResource(this, R.array.nation, android.R.layout.simple_spinner_dropdown_item);
//처번째 어댑터에 값을 넣습니다. this=는 현재class를 의미합니다. R.array.spinner_nation는
// 이곳에 도시를 다 쓸 경우 코딩이 길어지기 때문에 value->string.xml에 따로 String값들을 선언해두었습니다.
//R.layout.simple_spinner_dropdown_item은 안드로이드에서 기본제공하는 spinner 모양입니다. 다른것도 있는데 비슷합니다.
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin1.setAdapter(adspin1);
        spin1.setPrompt("구분");
        spin2.setPrompt("음식");
        spin3.setPrompt("식당");

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adspin1.getItem(i).equals("한식")) {
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.korean, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("보쌈")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.bossam, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("비빔밥")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.bibimppap, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("냉면")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.naengmyon, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("볶음밥")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.bokkeumbap, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("삼겹살")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.samgyopssal, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                        }
                    });
                } else if (adspin1.getItem(i).equals("양식")) {
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.western, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("파스타")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.pasta, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("필라프")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.pilrapeu, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("피자")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.pizza, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("치킨")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.chicken, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("샐러드")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.ssaelrodeu, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                        }
                    });
                }else if (adspin1.getItem(i).equals("중식")) {
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.chinese, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("짜장면")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.jajangmyeon, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("짬뽕")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.jjamppong, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("탕수육")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.tangsuyuk, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("깐풍기")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.kkanpunggi, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("마라탕")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.maratang, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                        }
                    });
                }else if (adspin1.getItem(i).equals("일식")) {
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.japanese, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("돈까스")){
                                stFood = adspin2.getItem(i).toString();
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.donkkaseu, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("초밥")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.chobap, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("회")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.hwe, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("라멘")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.ramen, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                            if(adspin2.getItem(i).equals("우동")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.udong, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                                    }
                                });
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK); //텍스트 색 지정
                        }
                    });
                }
            }

            //********************************************************************************************************************************************************************************************************************************
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        userRef = database.getReference("User").child(stUserToken);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stHostToken = dataSnapshot.child("room").getValue(String.class);
                Log.d(TAG, "stHostToken: "+stHostToken);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ibEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagArrayList.add(etTag.getText().toString());
                tagAdapter.notifyDataSetChanged();
                etTag.setText("");
            }
        });

        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // locaArrayList에서 가져온 location으로 latLng 초기화
                for(Location location : locaArrayList){
                    if(location.getName().equals(stRestaurant)){
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        break;
                    }
                }
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng)
                        .animate(CameraAnimation.Easing, 2000)
                        .reason(1000);

                naverMap.moveCamera(cameraUpdate);

                setMarker(marker, latLng.latitude, latLng.longitude, R.drawable.ic_baseline_place_24, 0);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        HashMap<String,String> hostMap = (HashMap<String,String>)dataSnapshot.getValue(); // 파이어베이스 DB는 Map형태로 저장되어있기 때문에 HashMap/Map으로 불러와야함
                        for(String host: hostMap.keySet()){
                            if(host.equals(stHostToken)){ // 이미 참가한 대기방이 있으면
                                Toast.makeText(CreateRoomActivity.this, "이미 참여중인 대기방이 있습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        // 아직 참가한 대기방이 없거나 참여중인 대기방인 경우
                        stRoomName = etRoomName.getText().toString();

                        Hashtable<String, String> Data // DB테이블에 데이터 입력
                                = new Hashtable<String, String>();
                        Data.put("hostToken", stUserToken); // DB의 hostToken란에 stUserToken 값
                        Data.put("roomName", stRoomName);
                        Data.put("hostId", stUserId);
                        Data.put("food", stFood);
                        Data.put("restaurant", stRestaurant);
                        myRef.child(stUserToken).setValue(Data); // 입력

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference imageRef = storage.getReference().child("food/"+stFood+".jpg");

                        // DB에 이미지 uri 저장
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                myRef.child(stUserToken).child("uri").setValue(String.valueOf(uri));
                            }
                        });

                        for(int i=0; i<tagArrayList.size(); i++){
                            myRef.child(stUserToken).child("tag").child("tag"+(i+1)).setValue(tagArrayList.get(i));
                        }
                        myRef.child(stUserToken).child("ban").child("userToken").setValue("");
                        userRef.child("room").setValue(stUserToken); // 들어간 방 확정

                        chatRef = database.getReference("Chat").child(stUserToken);
                        chatRef.setValue(null); // 기존의 채팅방 삭제

                        Intent intent = new Intent(CreateRoomActivity.this, RoomListActivity.class);
                        intent.putExtra("email", stUserId);
                        intent.putExtra("locaArrayList", locaArrayList);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

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

    @Override
    public void onClick(int position) {}

    @Override
    public void delete(int position) { // ClickCallbackListener 인터페이스의 메서드 -> TagAdapter에서 사용
        myRef.child(stUserToken).child("tag").child("tag"+(position+1)).setValue(null);
        // DB에서 해당 태그 삭제
    }

    @Override
    public void remove(int position) {
        try{
            tagArrayList.remove(position);
            tagAdapter.notifyItemRemoved(position);
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
}
