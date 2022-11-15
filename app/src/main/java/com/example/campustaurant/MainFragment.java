package com.example.campustaurant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Random;

public class MainFragment extends Fragment{
    private static final String FragTAG = "1";

    private FirebaseAuth mFirebaseAuth;
    FirebaseStorage storage;
    StorageReference stRef;
    ArrayList<String> foodArrayList;
    Button btnLogout;
    Button btnRoom;
    Button btnEnter;
    Button btnRefresh;
    Button btnRelate;
    EditText etFood;
    ImageView ivFood;
    String inputFood;
    String stUserId;
    String fileName;
    int idx;

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

        mFirebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        btnLogout = rootView.findViewById(R.id.btn_logout);
        btnRoom = rootView.findViewById(R.id.btn_room);
        btnEnter = rootView.findViewById(R.id.btn_enter);
        btnRefresh = rootView.findViewById(R.id.btn_refresh);
        btnRelate = rootView.findViewById(R.id.btn_relate);
        etFood = rootView.findViewById(R.id.et_food);
        ivFood = rootView.findViewById(R.id.iv_foodimg);
        stUserId = mainActivity.getIntent().getStringExtra("email"); // intent를 호출한 LoginActivity에서 email이라는 이름으로 넘겨받은 값을 가져와서 저장
        foodArrayList = mainActivity.getIntent().getStringArrayListExtra("foodArrayList");
        try {
            fileName = getArguments().getString("fileName"); // 새로고침 이후부터는 무조건 프래그먼트로 값 전달받음
        }catch(NullPointerException e){
            fileName = mainActivity.getIntent().getStringExtra("fileName"); // 처음에는 액티비티에서 값 전달받음
        }

        btnRefresh.setOnClickListener(new View.OnClickListener() {
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

        btnRelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, RestaurantActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 RestaurantActivity에 넘겨줌
                intent.putExtra("inputFood", fileName);
                startActivity(intent);
            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputFood = etFood.getText().toString();

                Intent intent = new Intent(mainActivity, RestaurantActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 RestaurantActivity에 넘겨줌
                intent.putExtra("inputFood", inputFood);
                startActivity(intent);
            }
        });

        btnRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, RoomListActivity.class);
                intent.putExtra("email", stUserId); // stUserId값을 RoomListActivity에 넘겨줌
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아웃 버튼 클릭시 처리 시작
                mFirebaseAuth.signOut(); // 로그아웃

                Intent intent = new Intent(mainActivity, LoginActivity.class);
                startActivity(intent);
            }
        });

        stRef = storage.getReference();

        stRef.child(fileName+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mainActivity).load(uri).into(ivFood);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        return rootView;
    }
}