package com.example.campustaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecordListActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference recordRef = database.getReference("Record");
    TextView tvName;
    TextView tvRate;
    ImageView ivProfile;
    ImageButton ibRateGood;
    ImageButton ibRateBad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        tvName = findViewById(R.id.tv_name);
        tvRate = findViewById(R.id.tv_rate);
        ivProfile = findViewById(R.id.iv_profile);
        ibRateGood = findViewById(R.id.ib_rategood);
        ibRateBad = findViewById(R.id.ib_ratebad);

        ibRateGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recordRef.child("GvzJKeUd8BSi9dQDCo0oYHtkhbJ3").child("rate")
            }
        });

        ibRateBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}