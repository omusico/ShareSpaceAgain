package com.salilgokhale.sharespace3;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by salilgokhale on 06/03/15.
 */
public class SpinnerListener implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        /*Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
