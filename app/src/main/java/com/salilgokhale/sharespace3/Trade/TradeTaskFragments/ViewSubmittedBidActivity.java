package com.salilgokhale.sharespace3.Trade.TradeTaskFragments;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.Expenses.BalancesAdapter;
import com.salilgokhale.sharespace3.Expenses.BalancesObject;
import com.salilgokhale.sharespace3.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ViewSubmittedBidActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_submitted_bid);
        getSupportActionBar().setTitle("Review Trade Bid");
        final TextView textView = (TextView) findViewById(R.id.their_tasks);


        Intent intent = this.getIntent();
        String tradeid = intent.getStringExtra(Intent.EXTRA_TEXT);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("TradeOffer");
        query.whereEqualTo("objectId", tradeid);
        //query.include("SubmitTrader");
        query.include("ReceiveTrader");
        query.include("SubmitterTasks");
        query.include("ReceiverTasks");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject object, ParseException e) {
                if (object != null) {

                    final ArrayList<ParseObject> ReceiverTasks = (ArrayList) object.get("ReceiverTasks");
                    final ArrayList<ParseObject> SubmitterTasks = (ArrayList) object.get("SubmitterTasks");

                    textView.setText(object.getParseObject("ReceiveTrader").getString("name") + "'s Tasks");

                    final SimpleDateFormat fmt = new SimpleDateFormat("d/M");
                    ArrayList<BalancesObject> Myobjects = new ArrayList<BalancesObject>();
                    ArrayList<BalancesObject> Theirobjects = new ArrayList<BalancesObject>();

                    for (int i = 0; i < ReceiverTasks.size(); i++) {
                        ParseObject temptask = ReceiverTasks.get(i);
                        BalancesObject balancesObject = new BalancesObject(temptask.getString("Name"), fmt.format(temptask.getDate("dateDue")));
                        Theirobjects.add(balancesObject);
                    }

                    for (int i = 0; i < SubmitterTasks.size(); i++) {
                        ParseObject temptask = SubmitterTasks.get(i);
                        BalancesObject balancesObject = new BalancesObject(temptask.getString("Name"), fmt.format(temptask.getDate("dateDue")));
                        Myobjects.add(balancesObject);
                    }

                    BalancesAdapter MyAdapter = new BalancesAdapter(getApplicationContext(), Myobjects);
                    ListView listView = (ListView) findViewById(R.id.seemytasks);
                    listView.setAdapter(MyAdapter);

                    BalancesAdapter TheirAdapter = new BalancesAdapter(getApplicationContext(), Theirobjects);
                    ListView listView2 = (ListView) findViewById(R.id.seetheirtasks);
                    listView2.setAdapter(TheirAdapter);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_submitted_bid, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_delete_bid:
                Intent intent = this.getIntent();
                String tradeid = intent.getStringExtra(Intent.EXTRA_TEXT);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("TradeOffer");
                query.whereEqualTo("objectId", tradeid);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(final ParseObject object, ParseException e) {
                        if (object != null) {
                            object.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null){
                                        CloseActivity();
                                    }
                                }
                            });
                        }
                    }
                });

                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void CloseActivity(){
        this.finish();
    }
}
