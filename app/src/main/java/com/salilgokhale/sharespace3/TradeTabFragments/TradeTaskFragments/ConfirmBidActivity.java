package com.salilgokhale.sharespace3.TradeTabFragments.TradeTaskFragments;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.salilgokhale.sharespace3.R;

import java.util.ArrayList;

public class ConfirmBidActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_bid);

        Intent intent = getIntent();
        ArrayList<String> mytaskids = intent.getStringArrayListExtra("MyTaskIDs");
        ArrayList<String> theirtaskids = intent.getStringArrayListExtra("TheirTaskIDs");

        for (int i = 0; i < mytaskids.size(); i++){
            Log.d("My tasks: ", mytaskids.get(i));
        }
        for (int j = 0; j < theirtaskids.size(); j++){
            Log.d("Their tasks: ", theirtaskids.get(j));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_bid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
