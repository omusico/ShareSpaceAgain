package com.salilgokhale.sharespace3.Rotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.salilgokhale.sharespace3.R;

import java.util.List;

/**
 * Created by salilgokhale on 14/03/15.
 */
public class RotaParticipantAdapter extends BaseAdapter {
    private Context context;
    private boolean[] turns;
    private List<String> names;
    private LayoutInflater inflater;

    public RotaParticipantAdapter(Context context, boolean[] turns, List<String> names){
        this.inflater = LayoutInflater.from(context);
        this.turns = turns;
        this.names = names;
    }

    private class ViewHolder {
        TextView textView1;
        ImageView imageView1;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.rota_participant_list_item, parent, false);
            holder.textView1 = (TextView) convertView.findViewById(R.id.rota_participant_name);
            holder.imageView1 = (ImageView) convertView.findViewById(R.id.rota_participant_image);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

            holder.textView1.setText(names.get(position));
        if(turns[position]){
            holder.imageView1.setImageResource(R.drawable.ic_action_next_item);

        }

        return convertView;
    }




}
