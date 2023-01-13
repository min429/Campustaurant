package com.example.campustaurant;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.CustomViewHolder> { // CustomViewHolder는 직접 만들어줘야함
    private static final String TAG = "RecordAdapter";

    private ArrayList<Profile> profileArrayList;
    private ClickCallbackListener mListener;

    public RecordAdapter(ArrayList<Profile> profileArrayList, ClickCallbackListener mListener) { // Constructer(생성자)
        this.profileArrayList = profileArrayList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecordAdapter.CustomViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // 생명주기 생성단계에서 한번 실행되는 메서드

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list, parent, false); // restaurant_list 레이아웃을 inflate해서 view 객체로 가져옴
        CustomViewHolder holder = new CustomViewHolder(view); // 만든 View객체 view를 전달하여 CustomViewHolder 객체 holder 생성

        return holder; // onBindViewHolder에서 holder를 인자로 받기 때문에 리턴해줘야함
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.CustomViewHolder holder, int position) { // RecordAdapter.CustomViewHolder에서 생성된 holder를 받아옴
        holder.tvName.setText(profileArrayList.get(position).getName());
        holder.tvRating.setText(Integer.toString(profileArrayList.get(position).getRating()));
        Glide.with(holder.itemView).load(profileArrayList.get(position).getUri()).into(holder.ivProfile);

        // 버튼을 클릭했을 때만 클릭을 감지하도록 findViewById(R.id.btn_rate) 추가
        holder.itemView.findViewById(R.id.btn_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // restaurant_list의 각 LinearLayout을 짧게 누르면 발생
                mListener.onClick(holder.getAdapterPosition()); // 콜백함수
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != profileArrayList ? profileArrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvRating;
        public ImageView ivProfile;

        public CustomViewHolder(@NonNull View itemView) { // Constructer(생성자) // 인자: 위에서 만든 View객체 view
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_name);
            this.tvRating = (TextView) itemView.findViewById(R.id.tv_rating);
            this.ivProfile = (ImageView) itemView.findViewById(R.id.iv_profile);

        }
    }
}
