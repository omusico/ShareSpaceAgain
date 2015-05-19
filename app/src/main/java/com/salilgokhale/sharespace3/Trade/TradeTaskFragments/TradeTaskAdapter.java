package com.salilgokhale.sharespace3.Trade.TradeTaskFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.salilgokhale.sharespace3.Expenses.ExpenseObject;
import com.salilgokhale.sharespace3.R;

import java.util.ArrayList;

/**
 * Created by salilgokhale on 26/03/15.
 */
public class TradeTaskAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<ExpenseObject> objects;

    private class ViewHolder {
        CheckBox checkBox;
        TextView textView1;
        TextView textView2;
    }

    public TradeTaskAdapter(Context context, ArrayList<ExpenseObject> objects) {
        this.objects = objects;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return objects.size();
    }

    public ExpenseObject getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.trade_task_item, parent, false);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.task_checkbox);
            holder.textView1 = (TextView) convertView.findViewById(R.id.task_checkbox_date);
            holder.textView2 = (TextView) convertView.findViewById(R.id.task_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setText(objects.get(position).getEname());
        holder.textView1.setText(objects.get(position).getEamount());
        holder.textView2.setText(objects.get(position).getEid());
        //holder.editText.addTextChangedListener();
        /*if(objects.get(position).getLocked()){
            holder.editText.setEnabled(false);
            holder.editText.setInputType(InputType.TYPE_NULL);
        }
        else {
            holder.editText.setEnabled(true);
            holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        } */


        return convertView;
    }
}
