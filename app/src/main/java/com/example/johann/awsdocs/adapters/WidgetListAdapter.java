package com.example.johann.awsdocs.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetListAdapter extends ArrayAdapter<AWSService>{


    @BindView(R.id.widget_adapter_text_view)
    TextView mWidgetTextView;

    public WidgetListAdapter(Context context, ArrayList<AWSService> services) {
        super(context,0,services);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        AWSService awsService = getItem(position);

        if(convertView == null) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.awswidget_list_view,parent);
            ButterKnife.bind(this,view);
        }

        mWidgetTextView.setText(awsService.getServiceName());

        return convertView;
    }
}
