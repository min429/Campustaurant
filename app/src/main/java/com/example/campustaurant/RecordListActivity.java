package com.example.campustaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecordListActivity extends AppCompatActivity {
    private static final String TAG = "RecordListActivity";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference recordRef = database.getReference("Record");
    DatabaseReference profileRef = database.getReference("Profile");
    TextView tvName;
    TextView tvRating;
    ImageView ivProfile;
    ImageButton ibRateGood;
    ImageButton ibRateBad;
    boolean rate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        tvName = findViewById(R.id.tv_name);
        tvRating = findViewById(R.id.tv_rating);
        ivProfile = findViewById(R.id.iv_profile);
        ibRateGood = findViewById(R.id.ib_rategood);
        ibRateBad = findViewById(R.id.ib_ratebad);

        ibRateGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!rate){
                    Toast.makeText(RecordListActivity.this, "up", Toast.LENGTH_SHORT).show();
                    recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            int rating = dataSnapshot.getValue(Integer.class);
                            rating += 1;
                            profileRef.child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").setValue(rating);
                        }
                    });
                    rate = true;
                }
            }
        });

        ibRateBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!rate){
                    Toast.makeText(RecordListActivity.this, "down", Toast.LENGTH_SHORT).show();
                    recordRef.child("2022-11-27").child("profile").child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            int rating = dataSnapshot.getValue(Integer.class);
                            rating -= 1;
                            profileRef.child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rating").setValue(rating);
                        }
                    });
                    rate = true;
                }
            }
        });
    }
}