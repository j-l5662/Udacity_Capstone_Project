package com.example.johann.awsdocs.recyclerviews;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSService;

import java.util.ArrayList;

public class MainRecyclerView extends RecyclerView.Adapter<MainRecyclerView.MainViewHolder>{

    ArrayList<AWSService> servicesList;

    public MainRecyclerView(ArrayList<AWSService> servicesList) {
        this.servicesList = servicesList;

    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_view,parent,false);

        MainViewHolder vh = new MainViewHolder(textView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        String serviceName = servicesList.get(position).returnName();

        holder.mTextView.setText(serviceName);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public MainViewHolder(View view){
            super(view);

            mTextView = view.findViewById(R.id.main_recycler_view_adapter);
        }
    }
}
