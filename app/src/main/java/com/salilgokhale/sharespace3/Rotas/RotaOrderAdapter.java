package com.salilgokhale.sharespace3.Rotas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.SpinnerListener;
import com.salilgokhale.sharespace3.Trade.TradeTaskFragments.DataHolderClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by salilgokhale on 30/05/15.
 */
public class RotaOrderAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<String> members;

    private class ViewHolder {
        CheckBox checkBox;
        TextView textView;
    }

    public RotaOrderAdapter(Context context, ArrayList<String> members) {
        this.members = members;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return members.size();
    }

    public String getItem(int position) {
        return members.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.rota_checkbox_item, parent, false);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.rota_checkbox);
            holder.textView = (TextView) convertView.findViewById(R.id.checkbox_order);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setText(members.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isChecked = holder.checkBox.isChecked();
                int temp = DataHolderClass.getInstance().getAnInt();
                if (isChecked){

                    holder.textView.setText(String.valueOf(temp));
                    holder.textView.setVisibility(View.VISIBLE);

                    DataHolderClass.getInstance().setAnInt(temp+1);
                    Log.d("Old DataHolder Value: ", String.valueOf(temp));
                    Log.d("New DataHolder Value: ", String.valueOf(temp+1));


                }
                else{
                    Log.d("DataHolder Value: ", String.valueOf(DataHolderClass.getInstance().getAnInt()));
                    Log.d("Checkbox Rank: ", String.valueOf(Integer.parseInt(holder.textView.getText().toString())));
                    if (Integer.parseInt(holder.textView.getText().toString()) < (DataHolderClass.getInstance().getAnInt() -1 )){
                        holder.checkBox.setChecked(true);
                    }
                    else {
                        holder.textView.setText("");
                        holder.textView.setVisibility(View.INVISIBLE);

                        DataHolderClass.getInstance().setAnInt(temp - 1);
                    }

                }


            }
        });



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
