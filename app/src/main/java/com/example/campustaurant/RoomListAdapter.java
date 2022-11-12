package com.example.campustaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.CustomViewHolder> { // CustomViewHolder는 직접 만들어줘야함

    private ArrayList<Room> roomArrayList;
    private ClickCallbackListener mListener;

    public RoomListAdapter(ArrayList<Room> roomArrayList, ClickCallbackListener mListener) { // Constructer(생성자)
        this.roomArrayList = roomArrayList; // RoomListActivity에서 생성된 roomArrayList로 초기화
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RoomListAdapter.CustomViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // 생명주기 생성단계에서 한번 실행되는 메서드

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list, parent, false); // room_list 레이아웃을 inflate해서 view 객체로 가져옴
        CustomViewHolder holder = new CustomViewHolder(view); // 만든 View객체 view를 전달하여 CustomViewHolder 객체 holder 생성


        return holder; // onBindViewHolder에서 holder를 인자로 받기 때문에 리턴해줘야함
    }

    @Override
    public void onBindViewHolder(@NonNull RoomListAdapter.CustomViewHolder holder, int position) { // RoomListAdapter.CustomViewHolder에서 생성된 holder를 받아옴
        holder.tvRoomName.setText(roomArrayList.get(position).getRoomName());
        holder.tvUserId.setText(roomArrayList.get(position).getUserId());
        holder.tvFood.setText(roomArrayList.get(position).getFood());
        holder.tvRestaurant.setText(roomArrayList.get(position).getRestaurant());
        // 클릭한 View의 position에 해당하는 index에 대해 roomArrayList(index)에 해당하는 Room객체의 변수들로 text를 세팅
        holder.itemView.setTag(position); // itemView의 position(위치)값을 가져옴

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // room_list의 각 LinearLayout을 짧게 누르면 발생
                mListener.onClick(holder.getAdapterPosition()); // 콜백함수
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != roomArrayList ? roomArrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRoomName;
        public TextView tvUserId;
        public TextView tvFood;
        public TextView tvRestaurant;

        public CustomViewHolder(@NonNull View itemView) { // Constructer(생성자) // 인자: 위에서 만든 View객체 view
            super(itemView);
            this.tvRoomName = (TextView) itemView.findViewById(R.id.tv_roomName);
            this.tvUserId = (TextView) itemView.findViewById(R.id.tv_userId);
            this.tvFood = (TextView) itemView.findViewById(R.id.tv_food);
            this.tvRestaurant = (TextView) itemView.findViewById(R.id.tv_restaurant);
        }
    }
}
