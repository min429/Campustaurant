package com.example.campustaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

    private final int GALLERY_CODE = 10;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("Profile");
    Profile profile;
    String stUserToken;
    ImageButton ibCross;
    EditText etName;
    EditText etSex;
    EditText etOld;
    ImageView ivProfile;
    String stName;
    String stSex;
    String stOld;
    Bitmap img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀바 삭제(setContentView전에 써줘야함)
        setContentView(R.layout.activity_edit_profile);

        stUserToken = getIntent().getStringExtra("myToken");
        ibCross = findViewById(R.id.ib_cross);
        etName = findViewById(R.id.et_name);
        etSex = findViewById(R.id.et_sex);
        etOld = findViewById(R.id.et_old);
        ivProfile = findViewById(R.id.iv_profile);

        dbRef = database.getReference("Profile").child(stUserToken);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                profile = datasnapshot.getValue(Profile.class);

                Glide.with(EditProfileActivity.this).load(profile.getUri()).into(ivProfile);
                etName.setText(profile.getName());
                etSex.setText(profile.getSex());
                etOld.setText(profile.getOld());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });

        ibCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stName = etName.getText().toString();
                stSex = etSex.getText().toString();
                stOld = etOld.getText().toString();

                dbRef.child(stUserToken).child("name").setValue(stName);
                dbRef.child(stUserToken).child("sex").setValue(stSex);
                dbRef.child(stUserToken).child("old").setValue(stOld);

                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode ==KeyEvent.KEYCODE_BACK) { // 뒤로가기 버튼 눌렀을 때
            stName = etName.getText().toString();
            stSex = etSex.getText().toString();
            stOld = etOld.getText().toString();

            dbRef.child(stUserToken).child("name").setValue(stName);
            dbRef.child(stUserToken).child("sex").setValue(stSex);
            dbRef.child(stUserToken).child("old").setValue(stOld);

            setResult(RESULT_OK, getIntent());
            finish();
            return true;
        }
        return false;
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