package com.phoebus.appdemowallet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.phoebus.appdemowallet.R;

import java.util.ArrayList;


public class AdapterCustomIcon extends BaseAdapter {

    private final ArrayList<String> options;
    private final Activity activity;

    public AdapterCustomIcon(ArrayList<String> options, Activity activity) {
        this.options = options;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public Object getItem(int position) {
        return options.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.activity_menu_list_icon, parent, false);
        String menuText = options.get(position);

        TextView text = view.findViewById(R.id.menu_text);
        text.setText(menuText);

        return view;
    }
}
