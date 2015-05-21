package com.salilgokhale.sharespace3.Rotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.salilgokhale.sharespace3.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by salilgokhale on 21/05/15.
 */
public class KeyAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<String> Names;
    private ArrayList<Integer> Colours;

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    public KeyAdapter(Context context, ArrayList<String> Names, ArrayList<Integer> Colours) {
        this.Names = Names;
        this.Colours = Colours;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return Names.size();
    }

    public String getItem(int position) {
        return Names.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.key_list_item, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.key_list_item_textview);
            holder.imageView = (ImageView) convertView.findViewById(R.id.key_list_item_imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(Names.get(position));
        switch (Colours.get(position)){
            case 5:
                holder.imageView.setImageResource(R.drawable.green);
                break;
            case 4:
                holder.imageView.setImageResource(R.drawable.purple);
                break;
            case 3:
                holder.imageView.setImageResource(R.drawable.orange);
                break;
            case 2:
                holder.imageView.setImageResource(R.drawable.blue);
                break;
            case 1:
                holder.imageView.setImageResource(R.drawable.red);
                break;
            default:
                holder.imageView.setImageResource(R.drawable.green);
        }

        return convertView;
    }

}
