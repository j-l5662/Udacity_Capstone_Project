package com.example.johann.awsdocs.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.johann.awsdocs.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_recycler_view_adapter)
        TextView mTextView;

        public MainViewHolder(View view){
            super(view);

            ButterKnife.bind(this,view);
//            view.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            mClickListener.onClick(getAdapterPosition());
//        }
    }

    public static class HeaderMainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_title_recycler_view_adapter)
        TextView mTextView;

        public HeaderMainViewHolder(View view){
            super(view);

            ButterKnife.bind(this,view);
        }
    }
}
