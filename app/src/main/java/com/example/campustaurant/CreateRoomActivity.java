package com.example.campustaurant;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class CreateRoomActivity extends AppCompatActivity {
    private static final String TAG = "CreateRoomActivity";
    
    ArrayAdapter<CharSequence> adspin1, adspin2, adspin3;
    EditText etRoomName;
    String stRoomName;
    String stUserId;
    String stFood;
    String stRestaurant;
    Button btnRegister;
    FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Room");
        etRoomName = findViewById(R.id.et_roomName);
        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 RoomListActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장
        btnRegister = (Button)findViewById(R.id.btn_register);
        final Spinner spin1 = (Spinner)findViewById(R.id.spinner1);
        final Spinner spin2 = (Spinner)findViewById(R.id.spinner2);
        final Spinner spin3 = (Spinner)findViewById(R.id.spinner3);
//xml과 class에 변수들을 연결해줍니다. final를 사용한 이유는 spin2가 함수안에서 사용되기 때문에 코딩전체로 선언한 것입니다.
        adspin1 = ArrayAdapter.createFromResource(this, R.array.spinner_nation, android.R.layout.simple_spinner_dropdown_item);
//처번째 어댑터에 값을 넣습니다. this=는 현재class를 의미합니다. R.array.spinner_nation는 이곳에 도시를 다 쓸 경우 코딩이 길어지기 때문에 value->string.xml에 따로 String값들을 선언해두었습니다.
//R.layout.simple_~~~는 안드로이드에서 기본제공하는 spinner 모양입니다. 다른것도 있는데 비슷합니다.
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin1.setAdapter(adspin1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adspin1.getItem(i).equals("한식")) {
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.spinner_korean, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("보쌈")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.spinner_bossam, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {}
                                });
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                } else if (adspin1.getItem(i).equals("양식")) {
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.spinner_western, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("파스타")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.spinner_pasta, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {}
                                });
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("중식")) {
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.spinner_chinese, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("짜장면")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.spinner_jajangmyeon, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {}
                                });
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("일식")) {
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.spinner_japanese, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("돈까스")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.spinner_donkkaseu, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin3.setAdapter(adspin3);

                                spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stRestaurant = adspin3.getItem(i).toString();
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {}
                                });
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }
            //************************************************************************************************************************************************************************************************
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(CreateRoomActivity.this, stRoomName, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "roomName: "+stRoomName);
                Log.d(TAG, "userId: "+stUserId);
                Log.d(TAG, "food: "+stFood);
                Log.d(TAG, "restaurant: "+stRestaurant);

                stRoomName = etRoomName.getText().toString();

                Calendar c = Calendar.getInstance(); // 현재 날짜정보 가져옴
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 날짜 포맷 설정
                String datetime = dateformat.format(c.getTime()); // datetime을 현재 날짜정보로 설정

                Hashtable<String, String> Data // DB테이블에 데이터 입력
                        = new Hashtable<String, String>();
                Data.put("roomName", stRoomName); // DB의 roomName란에 stRoomName 값
                Data.put("userId", stUserId);
                Data.put("food", stFood);
                Data.put("restaurant", stRestaurant);
                myRef.child(datetime).setValue(Data); // 입력

                finish();
            }
        });
    }
}
