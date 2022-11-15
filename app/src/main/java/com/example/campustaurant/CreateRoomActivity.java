package com.example.campustaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    String stUserToken;
    String stRoomName;
    String stUserId;
    String stFood;
    String stRestaurant;
    String inputRestaurant;
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
        stUserToken = getIntent().getStringExtra("userToken");
        inputRestaurant = getIntent().getStringExtra("inputRestaurant");
        btnRegister = (Button)findViewById(R.id.btn_register);
        final Spinner spin1 = (Spinner)findViewById(R.id.spinner1);
        final Spinner spin2 = (Spinner)findViewById(R.id.spinner2);
        final Spinner spin3 = (Spinner)findViewById(R.id.spinner3);
//xml과 class에 변수들을 연결해줍니다. final를 사용한 이유는 spin2가 함수안에서 사용되기 때문에 코딩전체로 선언한 것입니다.
        adspin1 = ArrayAdapter.createFromResource(this, R.array.nation, android.R.layout.simple_spinner_dropdown_item);
//처번째 어댑터에 값을 넣습니다. this=는 현재class를 의미합니다. R.array.spinner_nation는 이곳에 도시를 다 쓸 경우 코딩이 길어지기 때문에 value->string.xml에 따로 String값들을 선언해두었습니다.
//R.layout.simple_spinner_dropdown_item은 안드로이드에서 기본제공하는 spinner 모양입니다. 다른것도 있는데 비슷합니다.
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin1.setAdapter(adspin1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adspin1.getItem(i).equals("한식")) {
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.korean, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("보쌈")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.bossam, android.R.layout.simple_spinner_dropdown_item);
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
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.western, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("파스타")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.pasta, android.R.layout.simple_spinner_dropdown_item);
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
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.chinese, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("짜장면")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.jajangmyeon, android.R.layout.simple_spinner_dropdown_item);
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
                    adspin2 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.japanese, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(adspin2.getItem(i).equals("돈까스")){
                                stFood = adspin2.getItem(i).toString();

                                adspin3 = ArrayAdapter.createFromResource(CreateRoomActivity.this, R.array.donkkaseu, android.R.layout.simple_spinner_dropdown_item);
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
                stRoomName = etRoomName.getText().toString();

                Hashtable<String, String> Data // DB테이블에 데이터 입력
                        = new Hashtable<String, String>();
                Data.put("userToken", stUserToken);  // DB의 userToken란에 stUserToken 값
                Data.put("roomName", stRoomName);
                Data.put("userId", stUserId);
                Data.put("food", stFood);
                Data.put("restaurant", stRestaurant);
                myRef.child(stUserToken).setValue(Data); // 입력

                Intent intent = new Intent(CreateRoomActivity.this, RoomListActivity.class);
                intent.putExtra("email", stUserId);
                intent.putExtra("inputRestaurant", inputRestaurant);
                startActivity(intent);
                finish();
            }
        });
    }
}
