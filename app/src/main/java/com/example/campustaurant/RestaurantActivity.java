package com.example.campustaurant;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantActivity extends AppCompatActivity implements ClickCallbackListener{
    private static final String TAG = "RestaurantActivity";

    ArrayList<String> restaurantList;
    ArrayList<Location> locaArrayList;
    private RestaurantAdapter restaurantAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    FirebaseDatabase database;
    DatabaseReference ref;
    String stUserId;
    String stFood;
    String inputFood;
    EditText etFood;
    LatLng latLng;
    //검색창
    DrawerLayout MainScreen;
    private ImageButton Ib_searchopen;
    boolean searchopened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        MainScreen = (DrawerLayout) findViewById(R.id.dl_restaurant);
        searchopened = false;

        stUserId = getIntent().getStringExtra("email"); // intent를 호출한 MainActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장
        inputFood = getIntent().getStringExtra("inputFood");
        locaArrayList = (ArrayList<Location>)getIntent().getSerializableExtra("locaArrayList");
        Log.d(TAG, "locaArrayList: "+locaArrayList);
        Log.d(TAG, "inputFood: "+inputFood);
        database = FirebaseDatabase.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // LayoutManager 설정
        restaurantList = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(restaurantList, this); // restaurantList에 담긴 것들을 어댑터에 담아줌
        // this -> RestaurantActivity 객체
        recyclerView.setAdapter(restaurantAdapter); // recyclerView에 restaurantAdapter를 세팅해 주면 recyclerView가 이 어댑터를 사용해서 화면에 데이터를 띄워줌

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
                etFood = findViewById(R.id.et_input);
                etFood.setHint("음식을 입력해보세요!");

                etFood.setOnEditorActionListener(new TextView.OnEditorActionListener() { // 키보드에서 바로 검색
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                        switch (actionId)
                        {
                            case IME_ACTION_SEARCH :
                                inputFood = etFood.getText().toString();

                                searchopened = false;
                                MainScreen.removeViewAt(1);

                                // 새로고침
                                finish();// 인텐트 종료
                                overridePendingTransition(0, 0);// 인텐트 효과 없애기
                                Intent intent = getIntent(); // 인텐트
                                intent.putExtra("inputFood", inputFood);
                                intent.putExtra("email", stUserId);
                                intent.putExtra("locaArrayList", locaArrayList);
                                startActivity(intent); // 액티비티 열기
                                overridePendingTransition(0, 0);// 인텐트 효과 없애기
                                break;
                        }
                        return true;
                    }
                });

                ImageButton ibSearchClose = (ImageButton) findViewById(R.id.Ib_searchclose);
                ibSearchClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchopened = false;
                        MainScreen.removeViewAt(1);
                    }
                });
            }
        });

        ref = database.getReference("Food"); // Food하위에서 데이터를 읽기 위해
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                restaurantList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) { // food1,2,3... 하나씩 가져옴
                    stFood = postSnapshot.getKey(); // Food 하위의 키값 하나씩 가져옴
                    if(stFood.equals(inputFood)){
                        HashMap<String,String> map = (HashMap<String,String>)postSnapshot.getValue(); // 파이어베이스 DB는 Map형태로 저장되어있기 때문에 HashMap/Map으로 불러와야함
                        for(String stRestaurant : map.keySet()){ // map객체의 key값 리스트에서 값을 하나씩 가져와서 stRestaurant에 저장
                            restaurantList.add(stRestaurant);
                        }
                    }
                }
                restaurantAdapter.notifyDataSetChanged(); // 데이터가 바뀐다는 것을 알게 해줘야 함
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("RestaurantActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }

    @Override
    public void onClick(int position) { // ClickCallbackListener 인터페이스의 메서드 -> RestaurantAdapter에서 사용
        String inputRestaurant = restaurantList.get(position);

        // locaArrayList에서 가져온 location으로 latLng 초기화
        for(Location location : locaArrayList){
            if(location.getName().equals(inputRestaurant)){
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                break;
            }
        }
        Intent intent = new Intent(RestaurantActivity.this, RoomListActivity.class);
        intent.putExtra("inputRestaurant", inputRestaurant); // inputRestaurant값을 RoomListActivity에 넘겨줌
        intent.putExtra("email", stUserId);
        intent.putExtra("locaArrayList", locaArrayList);
        intent.putExtra("latLng", latLng);
        startActivity(intent);
    }

    @Override
    public void delete(int position) {}

    @Override
    public void remove(int position) {

    }
}