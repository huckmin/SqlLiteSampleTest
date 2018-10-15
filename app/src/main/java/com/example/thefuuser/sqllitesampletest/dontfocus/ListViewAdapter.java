package com.example.thefuuser.sqllitesampletest.dontfocus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thefuuser.sqllitesampletest.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<ListData> data;

    public ListViewAdapter(Context context, ArrayList<ListData> data) {

        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.list_data_view,viewGroup, false);
        }
        TextView titleText = view.findViewById(R.id.listTitle);
        TextView subTitleText = view.findViewById(R.id.listSubtitle);
        titleText.setText(data.get(i).getTitle());
        subTitleText.setText(data.get(i).getSubTitle());

        return view;
    }

}
