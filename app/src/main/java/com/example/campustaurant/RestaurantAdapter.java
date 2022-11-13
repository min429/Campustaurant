package com.example.campustaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.CustomViewHolder> { // CustomViewHolder는 직접 만들어줘야함

    private ArrayList<String> restaurantList;
    private ClickCallbackListener mListener;

    public RestaurantAdapter(ArrayList<String> restaurantList, ClickCallbackListener mListener) { // Constructer(생성자)
        this.restaurantList = restaurantList; // RestaurantActivity에서 생성된 restaurantList로 초기화
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RestaurantAdapter.CustomViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // 생명주기 생성단계에서 한번 실행되는 메서드

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list, parent, false); // restaurant_list 레이아웃을 inflate해서 view 객체로 가져옴
        CustomViewHolder holder = new CustomViewHolder(view); // 만든 View객체 view를 전달하여 CustomViewHolder 객체 holder 생성

        return holder; // onBindViewHolder에서 holder를 인자로 받기 때문에 리턴해줘야함
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.CustomViewHolder holder, int position) { // RestaurantAdapter.CustomViewHolder에서 생성된 holder를 받아옴
        holder.tvRestaurant.setText(restaurantList.get(position));
        // 클릭한 View의 position에 해당하는 restaurantList(index)값으로 text를 세팅
        holder.itemView.setTag(position); // itemView의 position(위치)값을 가져옴

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // restaurant_list의 각 LinearLayout을 짧게 누르면 발생
                mListener.onClick(holder.getAdapterPosition()); // 콜백함수
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != restaurantList ? restaurantList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRestaurant;

        public CustomViewHolder(@NonNull View itemView) { // Constructer(생성자) // 인자: 위에서 만든 View객체 view
            super(itemView);
            this.tvRestaurant = (TextView) itemView.findViewById(R.id.tv_restaurant);

        }
    }
}
