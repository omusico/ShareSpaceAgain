package com.salilgokhale.sharespace3.Expenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.salilgokhale.sharespace3.R;

import java.util.List;

/**
 * Created by salilgokhale on 24/03/15.
 */
public class SpecificExpenseLogAdapter extends BaseAdapter {

    private Context context;
    private List<String> titles;
    private List<String> payers;
    private List<String> values;
    private List<String> dates;
    private List<String> owes;
    //private List<String> owe_names;

    private LayoutInflater inflater;

    public SpecificExpenseLogAdapter(Context context, List<String> titles, List<String> payers, List<String> values, List<String> dates, List<String> owes){
        this.inflater = LayoutInflater.from(context);
        this.titles = titles;
        this.payers = payers;
        this.values = values;
        this.dates = dates;
        this.owes = owes;
        //this.owe_names = owe_names;
    }

    private class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        //TextView textView6;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.specific_expense_log_item, parent, false);
            holder.textView1 = (TextView) convertView.findViewById(R.id.specific_expense_log_date);
            holder.textView2 = (TextView) convertView.findViewById(R.id.specific_log_title);
            holder.textView3 = (TextView) convertView.findViewById(R.id.specific_log_payer);
            holder.textView4 = (TextView) convertView.findViewById(R.id.specific_amount);
            holder.textView5 = (TextView) convertView.findViewById(R.id.specific_owe);
            //holder.textView6 = (TextView) convertView.findViewById(R.id.specific_owe_name);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView1.setText(dates.get(position));
        holder.textView2.setText(titles.get(position));
        holder.textView3.setText(payers.get(position));
        holder.textView4.setText(values.get(position));
        holder.textView5.setText(owes.get(position));
        //holder.textView6.setText(owe_names.get(position));

        return convertView;
    }




}
