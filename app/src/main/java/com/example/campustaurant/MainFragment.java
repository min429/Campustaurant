package com.example.campustaurant;

import static android.app.Activity.RESULT_OK;
import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.ArrayList;
import java.util.Random;

public class MainFragment extends Fragment{
    private static final String TAG = "MainFragment";

    private final int GALLERY_CODE = 10;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    FirebaseStorage storage;
    FirebaseDatabase database;
    StorageReference stRef;
    DatabaseReference locaRef;
    DatabaseReference proRef;
    private ArrayList<Location> locaArrayList;
    ArrayList<String> foodArrayList;
    Profile profile;
    LinearLayout llRoom;
    Button btnEnter;
    LinearLayout llRecommend;
    LinearLayout llRelate;
    EditText etFood;
    TextView tvFood;
    ImageView ivFood;
    ImageView ivProfile;
    String inputFood;
    String stUserId;
    String fileName;
    String stUserToken;
    int idx;
    //사이드바 메뉴
    DrawerLayout MainScreen;
    View Sidebar;
    ImageButton Ib_OpenSidebar;
    Button Bt_CloseSidebar;
    ImageView ivLogout;
    ImageButton ibSetting;
    Button btnRecord;
    Button btnNotice;
    TextView tvName;
    TextView tvSex;
    TextView tvIntroduce;
    TextView tvRating;
    //알림창
    View Notification;
    ImageButton Ib_OpenNotification;
    Button Bt_CloseNotification;
    //검색창
    private ImageButton Ib_searchopen;
    boolean searchopened;

    // Fragment에서 Activity에 있는 메서드를 사용하기 위해서 필요
    MainActivity mainActivity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        // Fragment에서 findViewById를  사용하기 위해서 필요

        MainScreen = (DrawerLayout) rootView.findViewById(R.id.dl_main);
        searchopened = false;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        stUserToken = mFirebaseUser.getUid();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        ivLogout = rootView.findViewById(R.id.iv_logout);
        llRoom = rootView.findViewById(R.id.ll_room);
        //btnEnter = rootView.findViewById(R.id.btn_enter);
        btnRecord = rootView.findViewById(R.id.btn_record);
        llRecommend = rootView.findViewById(R.id.ll_recommend);
        llRelate = rootView.findViewById(R.id.ll_relate);
        ivFood = rootView.findViewById(R.id.iv_foodimg);
        tvFood = rootView.findViewById(R.id.tv_foodName);
        locaArrayList = new ArrayList<>();
        stUserId = mainActivity.getIntent().getStringExtra("email"); // intent를 호출한 LoginActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장
        foodArrayList = mainActivity.getIntent().getStringArrayListExtra("foodArrayList");
        try {
            fileName = getArguments().getString("fileName"); // 새로고침 이후부터는 무조건 프래그먼트로 값 전달받음
        }catch(NullPointerException e){
            fileName = mainActivity.getIntent().getStringExtra("fileName"); // 처음에는 액티비티에서 값 전달받음
        }

        //검색창
        // 레이아웃 인플레이터 객체
        LayoutInflater layoutInflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Ib_searchopen = (ImageButton)rootView.findViewById(R.id.Ib_searchopen); // 열기 버튼
        Ib_searchopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인에 새로 생성한 레이아웃 추가
                searchopened = true;
                MainScreen.addView(layoutInflater.inflate(R.layout.search_view,null));
                etFood = rootView.findViewById(R.id.et_input);
                etFood.setHint("음식을 검색해보세요!");

                etFood.setOnEditorActionListener(new TextView.OnEditorActionListener() { // 키보드에서 바로 검색
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                        switch (actionId)
                        {
                            case IME_ACTION_SEARCH :
                                inputFood = etFood.getText().toString();
                                etFood.setText("");

                                searchopened = false;
                                MainScreen.removeViewAt(3);

                                Intent intent = new Intent(mainActivity, RestaurantActivity.class);
                                intent.putExtra("email", stUserId); // stUserId값을 RestaurantActivity에 넘겨줌
                                intent.putExtra("inputFood", inputFood);
                                intent.putExtra("locaArrayList", locaArrayList);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });

                ImageButton ibSearchClose = (ImageButton) rootView.findViewById(R.id.Ib_searchclose);
                ibSearchClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchopened = false;
                        MainScreen.removeViewAt(3);
                    }
                });
            }
        });

        // 사이드바 메뉴
        ivProfile = rootView.findViewById(R.id.iv_profile);
        tvName = rootView.findViewById(R.id.tv_name);
        tvSex = rootView.findViewById(R.id.tv_sex);
        tvIntroduce = rootView.findViewById(R.id.tv_introduce);
        tvRating = rootView.findViewById(R.id.tv_rating);
        ibSetting = rootView.findViewById(R.id.btn_setting);

        btnRecord = rootView.findViewById(R.id.btn_record);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, RecordListActivity.class);
                intent.putExtra("myToken", stUserToken);
                startActivity(intent);
            }
        });

        btnNotice = (Button)rootView.findViewById(R.id.btn_notice); // 사이드바 버튼
        btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, NoticeListActivity.class);
                startActivity(intent);
            }
        });

        MainScreen = rootView.findViewById(R.id.dl_main); // 해당 레이아웃 아이디
        Sidebar = (View) rootView.findViewById(R.id.Sidebar); // 사이드바 레이아웃 아이디
        Ib_OpenSidebar = (ImageButton)rootView.findViewById(R.id.Ib_OpenSidebar); //SideBar
        Ib_OpenSidebar.setOnClickListener(new View.OnClickListener() { // 메뉴 클릭 시
            @Override
            public void onClick(View view) {
                if(searchopened) {
                    searchopened = false;
                    MainScreen.removeViewAt(3);
                }
                MainScreen.openDrawer(Sidebar);
            }
        });

        Bt_CloseSidebar = (Button) rootView.findViewById(R.id.Bt_CloseSidebar);
        Bt_CloseSidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainScreen.closeDrawers();
            }
        });

        Bt_CloseSidebar = rootView.findViewById(R.id.Bt_CloseSidebar);
        Bt_CloseSidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainScreen.closeDrawers();
            }
        });


        ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, EditProfileActivity.class);
                intent.putExtra("myToken", stUserToken);
                startActivity(intent);
                //startActivityForResult(intent, GALLERY_CODE);
            }
        });

        // 알림창 기능
        MainScreen = (DrawerLayout) rootView.findViewById(R.id.dl_main); // 해당 레이아웃 아이디
        Notification = (View) rootView.findViewById(R.id.Notification);
        Ib_OpenNotification = (ImageButton)rootView.findViewById(R.id.Ib_OpenNotification);
        Ib_OpenNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchopened) {
                    searchopened = false;
                    MainScreen.removeViewAt(3);
                }
                MainScreen.openDrawer(Notification);
            }
        });
        Bt_CloseNotification = (Button) rootView.findViewById(R.id.Bt_CloseNotification);
        Bt_CloseNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainScreen.closeDrawers();
            }
        });

//        //사이드바 기능
//        DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//
//            }
//
//            @Override
//            public void onDrawerOpened(@NonNull View drawerView) {
//
//            }
//
//            @Override
//            public void onDrawerClosed(@NonNull View drawerView) {
//
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//
//            }
//        };

        llRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random random = new Random();
                idx = random.nextInt(foodArrayList.size());
                fileName = foodArrayList.get(idx);

                // 프래그먼트에 값 전달
                Bundle bundle = new Bundle();
                bundle.putString("fileName", fileName);

                // 프래그먼트 새로고침(프래그먼트를 새로 만들어서 불러옴)
                MainFragment mainFragment = new MainFragment();
                mainFragment.setArguments(bundle);
                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.lo_frame_main, mainFragment).commitAllowingStateLoss();
            }
        });

        llRelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, RestaurantActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 RestaurantActivity에 넘겨줌
                intent.putExtra("inputFood", fileName);
                intent.putExtra("locaArrayList", locaArrayList);
                startActivity(intent);
            }
        });

/*
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputFood = etFood.getText().toString();

                Intent intent = new Intent(mainActivity, RestaurantActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 RestaurantActivity에 넘겨줌
                intent.putExtra("inputFood", inputFood);
                intent.putExtra("locaArrayList", locaArrayList);
                startActivity(intent);
            }
        });
*/

        llRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, RoomListActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 RoomListActivity에 넘겨줌
                intent.putExtra("locaArrayList", locaArrayList);
                startActivity(intent);
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아웃 버튼 클릭시 처리 시작
                mFirebaseAuth.signOut(); // 로그아웃

                Intent intent = new Intent(mainActivity, LoginActivity.class);
                startActivity(intent);
                mainActivity.finish();
            }
        });

        stRef = storage.getReference();
        stRef.child("food/"+fileName+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mainActivity).load(uri).into(ivFood);
                tvFood.setText(fileName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        locaRef = database.getReference("Restaurant"); // Restaurant하위에서 데이터를 읽기 위해
        locaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { // restaurant1,2,3... 하나씩 가져옴
                    Location location = postSnapshot.getValue(Location.class);
                    locaArrayList.add(location);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MainFragment", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        proRef = database.getReference("Profile").child(stUserToken);
        proRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profile = dataSnapshot.getValue(Profile.class);

                RequestManager glideRequestManager = Glide.with(mainActivity);

                if(profile != null){
                    if(profile.getUri() != null) glideRequestManager.load(profile.getUri()).into(ivProfile);
                    if(profile.getName() != null) tvName.setText(profile.getName());
                    if(profile.getSex() != null) tvSex.setText(profile.getSex());
                    if(profile.getIntroduce() != null) tvIntroduce.setText(profile.getIntroduce());
                    tvRating.setText(Integer.toString(profile.getRating()));
                }
                else{
                    proRef.child("rating").setValue(0);
                    tvRating.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return rootView;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == GALLERY_CODE){
//            if(resultCode == RESULT_OK){
//            }
//        }
//    }
}