package com.salilgokhale.sharespace3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by salilgokhale on 18/03/15.
 */
public class BalancesAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<BalancesObject> objects;

    private class ViewHolder {
        TextView textView3;
        TextView textView4;
    }

    public BalancesAdapter(Context context, ArrayList<BalancesObject> objects) {
        this.objects = objects;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return objects.size();
    }

    public BalancesObject getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.owe_expense_list_item, parent, false);
            holder.textView3 = (TextView) convertView.findViewById(R.id.owe_expense_list_item_textview1);
            holder.textView4 = (TextView) convertView.findViewById(R.id.owe_expense_list_item_textview2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView3.setText(objects.get(position).getBname());
        holder.textView4.setText(objects.get(position).getBdebt());
        return convertView;
    }
}
