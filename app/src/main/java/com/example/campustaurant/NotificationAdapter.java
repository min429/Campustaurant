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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CustomViewHolder> { // CustomViewHolder는 직접 만들어줘야함
    private static final String TAG = "NotificationAdapter";

    private ArrayList<Notification> notificationList;

    public NotificationAdapter(ArrayList<Notification> notificationList) { // Constructer(생성자)
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationAdapter.CustomViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // 생명주기 생성단계에서 한번 실행되는 메서드

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list, parent, false); // restaurant_list 레이아웃을 inflate해서 view 객체로 가져옴
        CustomViewHolder holder = new CustomViewHolder(view); // 만든 View객체 view를 전달하여 CustomViewHolder 객체 holder 생성

        return holder; // onBindViewHolder에서 holder를 인자로 받기 때문에 리턴해줘야함
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.CustomViewHolder holder, int position) { // NotificationAdapter.CustomViewHolder에서 생성된 holder를 받아옴
        holder.tvDatetime.setText(notificationList.get(position).getDatetime());
        holder.tvName.setText(notificationList.get(position).getUserName());
        holder.tvReview.setText(notificationList.get(position).getReview());
        holder.tvRating.setText(Integer.toString(notificationList.get(position).getRating()));
        Glide.with(holder.itemView).load(notificationList.get(position).getUri()).into(holder.ivProfile);
        if(notificationList.get(position).getRate().equals("good")){
            Glide.with(holder.itemView).load("https://firebasestorage.googleapis.com/v0/b/campustaurant.appspot.com/o/rate%2Fgood.png?alt=media&token=45798afe-40cf-48d1-b963-1f7f1a722883").into(holder.ivRating);
        }
        else if(notificationList.get(position).getRate().equals("bad")){
            Glide.with(holder.itemView).load("https://firebasestorage.googleapis.com/v0/b/campustaurant.appspot.com/o/rate%2Fbad.png?alt=media&token=f62171b2-9099-4cec-86a0-567f9d267482").into(holder.ivRating);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // 롱 클릭
            @Override
            public boolean onLongClick(View view) {
                remove(holder.getAdapterPosition()); // 뷰 지움
                return true;
            }
        });
    }

    public void remove(int position){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference notificationRef = database.getReference("Notification");
        notificationRef.child(notificationList.get(position).getDatetime()).setValue(null);
        try{
            notificationList.remove(position);
            notifyItemRemoved(position);
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != notificationList ? notificationList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDatetime;
        public TextView tvName;
        public TextView tvReview;
        public TextView tvRating;
        public ImageView ivProfile;
        public ImageView ivRating;

        public CustomViewHolder(@NonNull View itemView) { // Constructer(생성자) // 인자: 위에서 만든 View객체 view
            super(itemView);
            this.tvDatetime = (TextView) itemView.findViewById(R.id.tv_datetime);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_name);
            this.tvReview = (TextView) itemView.findViewById(R.id.tv_review);
            this.tvRating = (TextView) itemView.findViewById(R.id.tv_rating);
            this.ivProfile = (ImageView) itemView.findViewById(R.id.iv_profile);
            this.ivRating = (ImageView) itemView.findViewById(R.id.iv_rating);

        }
    }
}
