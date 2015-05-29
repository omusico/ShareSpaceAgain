package com.salilgokhale.sharespace3.Rules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.salilgokhale.sharespace3.R;

import java.util.ArrayList;

/**
 * Created by salilgokhale on 26/05/15.
 */
public class RulesAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<String> ruleTitles;
    private ArrayList<String> ruleIDs;

    private class ViewHolder {
        TextView textView9;

    }

    public RulesAdapter(Context context, ArrayList<String> ruleTitles, ArrayList<String> ruleIDs) {
        this.ruleTitles = ruleTitles;
        this.ruleIDs = ruleIDs;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void remove(int position){
        this.ruleTitles.remove(position);
    }

    public int getCount() {
        return ruleTitles.size();
    }

    public String getItem(int position) {
        return ruleTitles.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public String getItemObjectId (int position){ return ruleIDs.get(position); }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.task_list_item, parent, false);
            holder.textView9 = (TextView) convertView.findViewById(R.id.task_list_item_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView9.setText(ruleTitles.get(position));

        return convertView;
    }
}
