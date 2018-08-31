package com.example.johann.awsdocs.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSDocumentation;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AWSDocumentation> mAWSDocumentations;

    final private ServiceClickListener mClickListener;

    public interface ServiceClickListener {
        void onClick(int position);
    }

    public DetailRecyclerViewAdapter(ArrayList<AWSDocumentation> awsDocumentations, DetailRecyclerViewAdapter.ServiceClickListener clickListener) {

        this.mAWSDocumentations = awsDocumentations;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TextView textView;
        if (viewType == 1) {
            textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_title_recycler_view,parent,false);
            HeaderMainViewHolder vh = new HeaderMainViewHolder(textView);
            return vh;
        }
        else {
            textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_recycler_view,parent,false);
            MainViewHolder vh = new MainViewHolder(textView);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String documentation = mAWSDocumentations.get(position).getDocumentationName();
        if(mAWSDocumentations.get(position).isColumnHeader()) {

            HeaderMainViewHolder headerMainViewHolder = (HeaderMainViewHolder) holder;
            headerMainViewHolder.mTextView.setText(documentation);
        }
        else {
            MainViewHolder mainViewHolder = (MainViewHolder) holder;
            mainViewHolder.mTextView.setText(documentation);
        }
    }

    @Override
    public int getItemCount() {
        return mAWSDocumentations.size();
    }


    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        AWSDocumentation service = mAWSDocumentations.get(position);

        if(service.isColumnHeader()) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.detail_recycler_view_adapter)
        TextView mTextView;

        public MainViewHolder(View view){
            super(view);

            ButterKnife.bind(this,view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onClick(getAdapterPosition());
        }
    }

    public static class HeaderMainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.detail_title_recycler_view_adapter)
        TextView mTextView;

        public HeaderMainViewHolder(View view){
            super(view);

            ButterKnife.bind(this,view);
        }
    }
}
