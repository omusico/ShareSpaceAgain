package com.salilgokhale.sharespace3.Expenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import com.salilgokhale.sharespace3.R;

import java.util.ArrayList;

/**
 * Created by salilgokhale on 19/03/15.
 */
public class ExpenseAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<ExpenseObject> objects;

    private class ViewHolder {
        CheckBox checkBox;
        EditText editText;
    }

    public ExpenseAdapter(Context context, ArrayList<ExpenseObject> objects) {
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
            convertView = this.inflater.inflate(R.layout.expense_checkbox_item, parent, false);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.expense_checkbox);
            holder.editText = (EditText) convertView.findViewById(R.id.checkbox_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setText(objects.get(position).getEname());
        if(!objects.get(position).getEamount().equals("")) {
            holder.editText.setText(objects.get(position).getEamount());
        }
        else{
            holder.editText.setHint("0.00");
        }

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
