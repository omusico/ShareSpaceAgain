package com.salilgokhale.sharespace3.Home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.salilgokhale.sharespace3.R;

import java.util.ArrayList;

/**
 * Created by salilgokhale on 08/06/15.
 */
public class TaskAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<TaskObject> objects;

    private class ViewHolder {
        TextView textView3;
        TextView textView4;
    }

    public TaskAdapter(Context context, ArrayList<TaskObject> objects) {
        this.objects = objects;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void remove(int position){
        this.objects.remove(position);
    }

    public int getCount() {
        return objects.size();
    }

    public TaskObject getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.home_list_item, parent, false);
            holder.textView3 = (TextView) convertView.findViewById(R.id.home_list_item_textview1);
            holder.textView4 = (TextView) convertView.findViewById(R.id.home_list_item_textview2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView3.setText(objects.get(position).getTname());
        holder.textView4.setText(objects.get(position).getTdate());

        return convertView;
    }
}
