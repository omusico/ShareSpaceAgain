package com.salilgokhale.sharespace3.Trade;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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

public class ViewBidOfferActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bid_offer);
        getSupportActionBar().setTitle("Review Trade");

        Intent intent = this.getIntent();
        String tradeid = intent.getStringExtra(Intent.EXTRA_TEXT);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("TradeOffer");
        query.whereEqualTo("objectId", tradeid);
        query.include("SubmitTrader");
        //query.include("ReceiveTrader");
        query.include("SubmitterTasks");
        query.include("ReceiverTasks");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject object, ParseException e) {
                if (object != null) {

                    final ArrayList <ParseObject> ReceiverTasks = (ArrayList) object.get("ReceiverTasks");
                    final ArrayList <ParseObject> SubmitterTasks = (ArrayList) object.get("SubmitterTasks");
                    final ParseObject SubmitTrader = object.getParseObject("SubmitTrader");
                    final ParseUser user = ParseUser.getCurrentUser();

                    final SimpleDateFormat fmt = new SimpleDateFormat("d/M");
                    ArrayList<BalancesObject> Myobjects = new ArrayList<BalancesObject>();
                    ArrayList<BalancesObject> Theirobjects = new ArrayList<BalancesObject>();

                    for (int i = 0; i < ReceiverTasks.size(); i++){
                        ParseObject temptask = ReceiverTasks.get(i);
                        BalancesObject balancesObject = new BalancesObject(temptask.getString("Name"), fmt.format(temptask.getDate("dateDue")));
                        Myobjects.add(balancesObject);
                    }

                    for (int i = 0; i < SubmitterTasks.size(); i++){
                        ParseObject temptask = SubmitterTasks.get(i);
                        BalancesObject balancesObject = new BalancesObject(temptask.getString("Name"), fmt.format(temptask.getDate("dateDue")));
                        Theirobjects.add(balancesObject);
                    }

                    BalancesAdapter MyAdapter = new BalancesAdapter(getApplicationContext(), Myobjects);
                    ListView listView = (ListView) findViewById(R.id.viewmytasks);
                    listView.setAdapter(MyAdapter);

                    BalancesAdapter TheirAdapter = new BalancesAdapter(getApplicationContext(), Theirobjects);
                    ListView listView2 = (ListView) findViewById(R.id.viewtheirtasks);
                    listView2.setAdapter(TheirAdapter);

                    Button button1 = (Button) findViewById(R.id.accept_button);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < ReceiverTasks.size(); i++){
                                ReceiverTasks.get(i).put("Owner", SubmitTrader);
                                ReceiverTasks.get(i).saveInBackground();
                            }

                            for (int j = 0; j < SubmitterTasks.size(); j++){
                                SubmitterTasks.get(j).put("Owner", user);
                                SubmitterTasks.get(j).saveInBackground();
                            }
                            object.put("Received", "Accepted");
                            object.saveInBackground();
                            CloseActivity();
                        }
                    });

                    Button button2 = (Button) findViewById(R.id.reject_button);
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            object.put("Received", "Rejected");
                            object.saveInBackground();
                            CloseActivity();
                        }
                    });


                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_bid_offer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
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
