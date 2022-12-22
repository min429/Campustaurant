package com.example.campustaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.CustomViewHolder> { // CustomViewHolder는 직접 만들어줘야함
    private static final String TAG = "RecordAdapter";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private ArrayList<Record> recordArrayList;
    private ClickCallbackListener mListener;

    public RecordListAdapter(ArrayList<Record> recordArrayList, ClickCallbackListener mListener) { // Constructer(생성자)
        this.recordArrayList = recordArrayList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecordListAdapter.CustomViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // 생명주기 생성단계에서 한번 실행되는 메서드

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list, parent, false); // restaurant_list 레이아웃을 inflate해서 view 객체로 가져옴
        CustomViewHolder holder = new CustomViewHolder(view); // 만든 View객체 view를 전달하여 CustomViewHolder 객체 holder 생성

        return holder; // onBindViewHolder에서 holder를 인자로 받기 때문에 리턴해줘야함
    }

    @Override
    public void onBindViewHolder(@NonNull RecordListAdapter.CustomViewHolder holder, int position) { // RecordAdapter.CustomViewHolder에서 생성된 holder를 받아옴
        holder.tvDate.setText(recordArrayList.get(position).getDate());
        holder.tvRestaurant.setText(recordArrayList.get(position).getRestaurant());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // restaurant_list의 각 LinearLayout을 짧게 누르면 발생
                mListener.onClick(holder.getAdapterPosition()); // 콜백함수
            }
        });

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
        DatabaseReference recordRef = database.getReference("Record");
        String stMyToken = firebaseUser.getUid();
        recordRef.child(stMyToken).child(recordArrayList.get(position).getDate()).setValue(null);
        try{
            recordArrayList.remove(position);
            notifyItemRemoved(position);
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != recordArrayList ? recordArrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvRestaurant;

        public CustomViewHolder(@NonNull View itemView) { // Constructer(생성자) // 인자: 위에서 만든 View객체 view
            super(itemView);
            this.tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            this.tvRestaurant = (TextView) itemView.findViewById(R.id.tv_restaurant);
        }
    }
}
