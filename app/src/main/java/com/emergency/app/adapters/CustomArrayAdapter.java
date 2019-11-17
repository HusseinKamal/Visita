package com.emergency.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.emergency.app.R;
import com.emergency.app.views.CustomTextView;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final ArrayList<String> items;
    private int resourceId;
    public CustomArrayAdapter(int resourceId, @NonNull Context context, @NonNull ArrayList<String> objects) {
        super(context, 0, objects);
        this.resourceId=resourceId;
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        View rowView = mInflater.inflate(resourceId, parent, false);
        CustomTextView tvData = rowView.findViewById(R.id.tvData);
        String data = items.get(position);
        tvData.setText(data);
        return rowView;
    }
}
