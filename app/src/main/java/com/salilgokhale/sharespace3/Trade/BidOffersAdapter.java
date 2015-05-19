package com.salilgokhale.sharespace3.Trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.salilgokhale.sharespace3.R;

import java.util.ArrayList;

/**
 * Created by salilgokhale on 28/03/15.
 */
public class BidOffersAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<BidOffersObject> objects;

    private class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
    }

    public BidOffersAdapter(Context context, ArrayList<BidOffersObject> objects) {
        this.objects = objects;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return objects.size();
    }

    public BidOffersObject getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.bid_list_item, parent, false);
            holder.textView1= (TextView) convertView.findViewById(R.id.bid_person);
            holder.textView2 = (TextView) convertView.findViewById(R.id.bid_tasks1);
            holder.textView3 = (TextView) convertView.findViewById(R.id.bid_tasks2);
            holder.textView4 = (TextView) convertView.findViewById(R.id.bid_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView1.setText(objects.get(position).getBOperson());
        holder.textView2.setText(objects.get(position).getBOtasks1());
        holder.textView3.setText(objects.get(position).getBOtasks2());

        if(objects.get(position).getStatus().equals("Accepted")) {
            holder.textView4.setText("Accepted");
        }
        else if (objects.get(position).getStatus().equals("Pending")) {
            holder.textView4.setText("Pending");

        }
        else if (objects.get(position).getStatus().equals("Rejected")){
            holder.textView4.setText("Rejected");
        }

        return convertView;
    }

}
