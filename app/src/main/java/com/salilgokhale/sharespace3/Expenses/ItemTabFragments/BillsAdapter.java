package com.salilgokhale.sharespace3.Expenses.ItemTabFragments;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.salilgokhale.sharespace3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salilgokhale on 03/06/15.
 */
public class BillsAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private List<ParseObject> objects;

    private class ViewHolder {
        TextView textView;
        Button button;
    }

    public BillsAdapter(Context context, List<ParseObject> objects) {
        this.objects = objects;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return objects.size();
    }

    public ParseObject getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void remove(int position){
        objects.remove(objects.get(position));

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.bills_list_item, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.bill_title);
            holder.button = (Button) convertView.findViewById(R.id.bill_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ParseObject temp = objects.get(position);

        holder.textView.setText(temp.getString("Name"));
        if (temp.getBoolean("Due")){
            //holder.button.setBackgroundColor(Color.parseColor("#F44336"));
            holder.button.setBackgroundResource(R.drawable.due);
        }
        else{
            //holder.button.setBackgroundColor(Color.parseColor("#4CAF50"));
            holder.button.setBackgroundResource(R.drawable.not_due);
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temp.getBoolean("Due")){
                    temp.put("Due", false);
                    temp.saveInBackground();
                    //holder.button.setBackgroundColor(Color.parseColor("#4CAF50"));
                    holder.button.setBackgroundResource(R.drawable.not_due);
                }
                else {
                    temp.put("Due", true);
                    temp.saveInBackground();
                    //holder.button.setBackgroundColor(Color.parseColor("#F44336"));
                    holder.button.setBackgroundResource(R.drawable.due);
                }


            }
        });


        return convertView;
    }


}
