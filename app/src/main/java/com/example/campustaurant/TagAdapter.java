package com.example.campustaurant;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.CustomViewHolder> { // CustomViewHolder는 직접 만들어줘야함

    private ArrayList<String> tagArrayList;
    private ClickCallbackListener mlistener;

    public TagAdapter(ArrayList<String> tagArrayList, ClickCallbackListener mlistener) { // Constructer(생성자)
        this.tagArrayList = tagArrayList; // RestaurantActivity에서 생성된 tagArrayList로 초기화
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public TagAdapter.CustomViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // 생명주기 생성단계에서 한번 실행되는 메서드

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_list, parent, false); // restaurant_list 레이아웃을 inflate해서 view 객체로 가져옴
        CustomViewHolder holder = new CustomViewHolder(view); // 만든 View객체 view를 전달하여 CustomViewHolder 객체 holder 생성

        return holder; // onBindViewHolder에서 holder를 인자로 받기 때문에 리턴해줘야함
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") int position) { // CreateRoomAdapter.CustomViewHolder에서 생성된 holder를 받아옴
        if(tagArrayList.get(position) != null)
            holder.tvTag.setText("#"+tagArrayList.get(position)+" ");
        // 클릭한 View의 position에 해당하는 tagArrayList(index)값으로 text를 세팅
        holder.itemView.setTag(position); // itemView의 position(위치)값을 가져옴

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // 롱 클릭
            @Override
            public boolean onLongClick(View view) {
                mlistener.remove(holder.getAdapterPosition()); // 뷰 지움
                mlistener.delete(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != tagArrayList ? tagArrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTag;

        public CustomViewHolder(@NonNull View itemView) { // Constructer(생성자) // 인자: 위에서 만든 View객체 view
            super(itemView);
            this.tvTag = (TextView) itemView.findViewById(R.id.tv_tag);

        }
    }
}
