package com.example.campustaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.CustomViewHolder> { // CustomViewHolder는 직접 만들어줘야함

    private ArrayList<Room> roomArrayList;
    private ClickCallbackListener mListener;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool(); // RecyclerView 최적화를 위해

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
        holder.tvFood.setText(roomArrayList.get(position).getFood());
        holder.tvRestaurant.setText(roomArrayList.get(position).getRestaurant());
        Glide.with(holder.itemView).load(roomArrayList.get(position).getUri()).into(holder.ivFood);
        // 클릭한 View의 position에 해당하는 index에 대해 roomArrayList(index)에 해당하는 Room객체의 변수들로 text를 세팅

        if(roomArrayList.get(position).getTag() != null){
            // 자식 레이아웃 매니저 설정
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    holder.rvSub.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );

            layoutManager.setInitialPrefetchItemCount(roomArrayList.get(position).getTag().size()); // 해시맵 사이즈를 가져와서 세팅

            // 자식 어답터 설정
            TagAdapter tagAdapter = new TagAdapter(new ArrayList(roomArrayList.get(position).getTag().values()), mListener);

            holder.rvSub.setLayoutManager(layoutManager);
            holder.rvSub.setAdapter(tagAdapter);
            holder.rvSub.setRecycledViewPool(viewPool); // RecyclerView 최적화
        }
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
        public TextView tvFood;
        public TextView tvRestaurant;
        public RecyclerView rvSub;
        public ImageView ivFood;

        public CustomViewHolder(@NonNull View itemView) { // Constructer(생성자) // 인자: 위에서 만든 View객체 view
            super(itemView);
            this.tvRoomName = (TextView) itemView.findViewById(R.id.tv_roomName);
            this.tvFood = (TextView) itemView.findViewById(R.id.tv_food);
            this.tvRestaurant = (TextView) itemView.findViewById(R.id.tv_restaurant);
            this.rvSub = itemView.findViewById(R.id.rv_tag);
            this.ivFood = itemView.findViewById(R.id.iv_food);
        }
    }
}