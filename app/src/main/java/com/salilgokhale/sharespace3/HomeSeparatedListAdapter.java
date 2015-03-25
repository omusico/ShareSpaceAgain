package com.salilgokhale.sharespace3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by salilgokhale on 25/03/15.
 */
public class HomeSeparatedListAdapter extends BaseAdapter {

    public final Map<String, ArrayAdapter> sections = new LinkedHashMap<>();
    public final ArrayAdapter<String> headers;
    public final static int TYPE_SECTION_HEADER = 0;

    public HomeSeparatedListAdapter(Context context)
    {
        headers = new ArrayAdapter<String>(context, R.layout.home_list_header);
    }

    public void addSection(String section, ArrayAdapter adapter)
    {
        this.headers.add(section);
        this.sections.put(section, adapter);
    }

    public boolean checkIfHeader(int position){
        for (Object section : this.sections.keySet())
        {
            ArrayAdapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) return true;
            if (position < size) return false;

            // otherwise jump into next section
            position -= size;
        }
        return false;
    }

    public int getRealPosition(int position){

        int number = 1;
        for (Object section : this.sections.keySet())
        {
            ArrayAdapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) return number;
            if (position < size) return number;

            // otherwise jump into next section
            position -= size;
            number++;
        }
        return number;
    }

    public Object getItem(int position)
    {
        for (Object section : this.sections.keySet())
        {
            ArrayAdapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) return section;
            if (position < size) return adapter.getItem(position - 1);

            // otherwise jump into next section
            position -= size;
        }
        return null;
    }

    public void remove(int position){
        for (Object section : this.sections.keySet())
        {
            ArrayAdapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) break;
            if (position < size) {
                adapter.remove(adapter.getItem(position - 1));
                break;
            }

            // otherwise jump into next section
            position -= size;
        }

    }

    public int getCount()
    {
        // total together all sections, plus one for each section header
        int total = 0;
        for (ArrayAdapter adapter : this.sections.values())
            total += adapter.getCount() + 1;
        return total;
    }

    @Override
    public int getViewTypeCount()
    {
        // assume that headers count as one, then total all sections
        int total = 1;
        for (ArrayAdapter adapter : this.sections.values())
            total += adapter.getViewTypeCount();
        return total;
    }

    @Override
    public int getItemViewType(int position)
    {
        int type = 1;
        for (Object section : this.sections.keySet())
        {
            ArrayAdapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) return TYPE_SECTION_HEADER;
            if (position < size) return type + adapter.getItemViewType(position - 1);

            // otherwise jump into next section
            position -= size;
            type += adapter.getViewTypeCount();
        }
        return -1;
    }

    public boolean areAllItemsSelectable()
    {
        return false;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return (getItemViewType(position) != TYPE_SECTION_HEADER);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        int sectionnum = 0;
        for (Object section : this.sections.keySet())
        {
            ArrayAdapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) return headers.getView(sectionnum, convertView, parent);
            if (position < size) return adapter.getView(position - 1, convertView, parent);

            // otherwise jump into next section
            position -= size;
            sectionnum++;
        }
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

}
