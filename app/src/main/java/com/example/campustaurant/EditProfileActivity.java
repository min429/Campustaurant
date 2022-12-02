package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";

    private final int GALLERY_CODE = 10;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("Profile");
    Profile profile;
    String stUserToken;
    Button btnClose;
    EditText etName;
    EditText etSex;
    EditText etIntroduce;
    ImageView ivProfile;
    TextView tvRating;
    String stName;
    String stSex;
    String stIntroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀바 삭제(setContentView전에 써줘야함)
        setContentView(R.layout.activity_edit_profile);

        stUserToken = getIntent().getStringExtra("myToken");
        btnClose = findViewById(R.id.btn_close);
        etName = findViewById(R.id.et_name);
        etSex = findViewById(R.id.et_sex);
        ivProfile = findViewById(R.id.iv_profile);
        tvRating = findViewById(R.id.tv_rating);
        etIntroduce = findViewById(R.id.et_introduce);

        dbRef = database.getReference("Profile").child(stUserToken);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profile = dataSnapshot.getValue(Profile.class);

                RequestManager glideRequestManager = Glide.with(getApplicationContext()); // Glide쓸 때 getApplicationContext를 통해 로드해야함

                if(profile != null){
                    if(profile.getUri() != null) glideRequestManager.load(profile.getUri()).into(ivProfile);
                    if(profile.getName() != null) etName.setText(profile.getName());
                    if(profile.getSex() != null) etSex.setText(profile.getSex());
                    if(profile.getIntroduce() != null) etIntroduce.setText(profile.getIntroduce());
                    tvRating.setText(Integer.toString(profile.getRating()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 확인
//                boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (!hasWritePerm){
                    // 권한 없을 시  권한설정 요청
                    ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, GALLERY_CODE);
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stName = etName.getText().toString();
                stSex = etSex.getText().toString();
                stIntroduce = etIntroduce.getText().toString();

                dbRef.child("name").setValue(stName);
                dbRef.child("sex").setValue(stSex);
                dbRef.child("introduce").setValue(stIntroduce);

                //setResult(RESULT_OK, getIntent());
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 버튼 방지 (주석처리 필요)
        // super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("Profile");
        stUserToken = mFirebaseUser.getUid();

        if(requestCode == GALLERY_CODE){
            // 갤러리 이미지 스토리지에 저장
            Uri uri = data.getData();
            StorageReference imageRef = storage.getReference().child("profile/"+stUserToken+".jpg");
            UploadTask uploadTask = imageRef.putFile(uri);

            // DB에 이미지 uri 저장
            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    dbRef.child(stUserToken).child("uri").setValue(String.valueOf(uri));
                }
            });


            // 갤러리 이미지 세팅
//            try{
//                InputStream in = getContentResolver().openInputStream(data.getData());
//                img = BitmapFactory.decodeStream(in);
//                in.close();
//                ivProfile.setImageBitmap(img);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}