package com.example.johann.awsdocs.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<AWSService> mServicesList;
    final private ServiceClickListener mClickListener;

    public interface ServiceClickListener {
        void onClick(int position);
    }

    public MainRecyclerViewAdapter(ArrayList<AWSService> mServicesList, ServiceClickListener mClickListener) {

        this.mServicesList = mServicesList;
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TextView textView;
        if(viewType == 0) {
            textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_view,parent,false);
            MainViewHolder vh = new MainViewHolder(textView);
            return vh;
        }
        else{
            textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.main_title_recycler_view,parent,false);
            HeaderMainViewHolder th = new HeaderMainViewHolder(textView);
            return th;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String serviceName = mServicesList.get(position).returnName();

        if(mServicesList.get(position).isColumnHeader()) {

            HeaderMainViewHolder headerMainViewHolder = (HeaderMainViewHolder) holder;
            headerMainViewHolder.mTextView.setText(serviceName);
        }
        else {
            MainViewHolder mainViewHolder = (MainViewHolder) holder;
            mainViewHolder.mTextView.setText(serviceName);
        }

    }

    @Override
    public int getItemCount() {
        return mServicesList.size();
    }

    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        AWSService service = mServicesList.get(position);

        if(service.isColumnHeader()) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public void updateData(ArrayList<AWSService> servicesList) {

        if(servicesList != null || servicesList.size() > 0) {
            this.mServicesList.clear();
            this.mServicesList.addAll(servicesList);
        }
        else {
            this.mServicesList = servicesList;
        }
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.main_recycler_view_adapter)
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

        @BindView(R.id.main_title_recycler_view_adapter)
        TextView mTextView;

        public HeaderMainViewHolder(View view){
            super(view);

            ButterKnife.bind(this,view);
        }
    }
}
