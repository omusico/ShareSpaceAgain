package com.salilgokhale.sharespace3.Rotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.salilgokhale.sharespace3.R;

import java.util.ArrayList;

/**
 * Created by salilgokhale on 14/03/15.
 */
public class RotaAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<RotaObject> objects;

    private class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }

    public RotaAdapter(Context context, ArrayList<RotaObject> objects) {
        this.objects = objects;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return objects.size();
    }

    public RotaObject getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.rota_list_item, parent, false);
            holder.textView1 = (TextView) convertView.findViewById(R.id.rota_list_item_textview1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.rota_list_item_textview2);
            holder.textView3 = (TextView) convertView.findViewById(R.id.rota_list_item_textview3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView1.setText(objects.get(position).getRname());
        holder.textView2.setText(objects.get(position).getRnextperson());
        //if (objects.get(position).getRnextdate().equals("")) {
            holder.textView3.setText(objects.get(position).getRnextdate());
        //}
        //else {
        //    holder.textView3.setText("");
        //}
        return convertView;
    }


}
