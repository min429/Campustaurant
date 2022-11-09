package com.example.campustaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private static final String TAG = "ChatAdapter";

    private ArrayList<Chat> mDataset;
    String stUserId = "";

    /**
     * Provide a reference to the type of views that you are using
     * (custom MyViewHolder).
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public MyViewHolder(View v) {
            super(v);
            // Define click listener for the MyViewHolder's View

            textView = v.findViewById(R.id.tvChat);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mDataset.get(position).userId.equals(stUserId)){ // DB에 있는 (채팅별)email값과 로그인 할때 적은 email이 같은 경우
            return 1;
        }
        else{
            return 2;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param mydataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public ChatAdapter(ArrayList<Chat> mydataSet, String stEmail) {
        mDataset = mydataSet;
        this.stUserId = stEmail;
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
        else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.other_text_view, parent, false); // v를 other_text_view로 적용
        }

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.textView.setText(mDataset.get(position).getText());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size(); // ArrayList(mDataset)의 사이즈
    }
}
