package com.example.campustaurant;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private static final String TAG = "ChatAdapter";

    private ClickCallbackListener mListener;
    private ArrayList<Chat> mDataset;
    String stUserId = "";

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView tvName;
        public ImageView ivProfile;

        public MyViewHolder(View v) {
            super(v);
            // Define click listener for the MyViewHolder's View
            textView = v.findViewById(R.id.tvChat);
            ivProfile = v.findViewById(R.id.iv_profile);
            tvName = v.findViewById(R.id.tv_name);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mDataset.get(position).getUserId().equals("")){
            return 3;
        }
        if(mDataset.get(position).getUserId().equals(stUserId)){ // DB에 있는 (채팅별)email값과 로그인 할때 적은 email이 같은 경우
            return 1;
        }
        else{
            return 2;
        }
    }

    public ChatAdapter(ArrayList<Chat> mydataSet, String stEmail, ClickCallbackListener mListener) {
        mDataset = mydataSet;
        this.stUserId = stEmail;
        this.mListener = mListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // viewType: 메서드 getItemViewType의 리턴값
        // Create a new view, which defines the UI of the list item

        View v;

        if(viewType == 1){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false); // v를 my_text_view로 적용
        }
        else if(viewType == 2) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.other_text_view, parent, false); // v를 other_text_view로 적용
        }
        else{
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.all_text_view, parent, false); // v를 all_text_view로 적용
        }

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.textView.setText(mDataset.get(position).getText());
        if(holder.tvName != null) // 레이아웃에 tv_name이 있는 경우
        {
            if(!(position >= 1 && mDataset.get(position-1).getUserToken().equals(mDataset.get(position).getUserToken()))){ // 같은사람이 여러번 채팅을 치지않은 경우
                holder.tvName.setText(mDataset.get(position).getUserName());
            }
            else{
                holder.tvName.setVisibility(View.GONE);
            }
        }
        if(holder.ivProfile != null){ // 레이아웃에 ivProfile이 있는 경우
            Glide.with(holder.itemView).load(mDataset.get(position).getUri()).into(holder.ivProfile);
            if((position >= 1 && mDataset.get(position-1).getUserToken().equals(mDataset.get(position).getUserToken()))){ // 같은사람이 여러번 채팅을 친 경우
                holder.ivProfile.setVisibility(View.INVISIBLE); // 이미지 안보이게
            }
        }
        holder.itemView.setTag(position); // itemView의 position(위치)값을 가져옴
        if(holder.itemView.findViewById(R.id.iv_profile) != null){ // 채팅에 프로필이 있는 경우에만
            // 프로필을 눌렀을 때에만 클릭을 감지하도록 findViewById(R.id.iv_profile) 추가
            holder.itemView.findViewById(R.id.iv_profile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // room_list의 각 LinearLayout을 짧게 누르면 발생
                    mListener.onClick(holder.getAdapterPosition()); // 콜백함수
                }
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size(); // ArrayList(mDataset)의 사이즈
    }
}
