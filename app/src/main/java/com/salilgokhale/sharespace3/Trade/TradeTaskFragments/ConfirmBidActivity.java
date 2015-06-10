package com.salilgokhale.sharespace3.Trade.TradeTaskFragments;


/*

*/

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.Expenses.BalancesAdapter;
import com.salilgokhale.sharespace3.Expenses.BalancesObject;
import com.salilgokhale.sharespace3.Home.TaskAdapter;
import com.salilgokhale.sharespace3.Home.TaskObject;
import com.salilgokhale.sharespace3.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfirmBidActivity extends ActionBarActivity {

    List<ParseObject> MyTasks = new ArrayList<>();
    List<ParseObject> TheirTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_bid);
        getSupportActionBar().setTitle("Confirm Trade Bid");

        Intent intent = getIntent();
        final ArrayList<String> mytaskids = intent.getStringArrayListExtra("MyTaskIDs");
        final ArrayList<String> theirtaskids = intent.getStringArrayListExtra("TheirTaskIDs");
        final String TheirID = intent.getStringExtra("TraderID");

        final TextView textView = (TextView) findViewById(R.id.their_tasks);
        final Button button = (Button) findViewById(R.id.submit_trade);
        //textView.setText(TheirID + "'s Tasks");

        ParseQuery<ParseUser> queryuser = ParseUser.getQuery();
        queryuser.getInBackground(TheirID, new GetCallback<ParseUser>() {
            public void done(final ParseUser otheruser, ParseException e) {
                if (e == null) {
                    textView.setText(otheruser.getString("name") + "'s Tasks");


        for (int i = 0; i < mytaskids.size(); i++){
            Log.d("My tasks: ", mytaskids.get(i));
        }
        for (int j = 0; j < theirtaskids.size(); j++){
            Log.d("Their tasks: ", theirtaskids.get(j));
        }

        final ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Tasks");
        query1.whereEqualTo("Owner", user);
        query1.whereEqualTo("Completed", false);
        query1.orderByAscending("dateDue");
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> taskList1, ParseException e) {
                if (taskList1 != null) {
                    Log.d("Tasks: ", "found!");
                    final SimpleDateFormat fmt = new SimpleDateFormat("d/M");

                    for (int h = 0; h < mytaskids.size(); h++){
                        for (int i = 0; i < taskList1.size(); i++){
                            if (mytaskids.get(h).equals(taskList1.get(i).getObjectId())){
                                MyTasks.add(taskList1.get(i));
                                Log.d("Task added", taskList1.get(i).getString("Name"));
                                break;
                            }
                        }
                    }

                    ArrayList<TaskObject> Myobjects = new ArrayList<TaskObject>();

                    Log.d("MyTasksSize = ", String.valueOf(MyTasks.size()));
                    for (int i = 0; i < MyTasks.size(); i++){
                        ParseObject temptask = MyTasks.get(i);
                        TaskObject taskObject = new TaskObject(temptask.getString("Name"), fmt.format(temptask.getDate("dateDue")));
                        Myobjects.add(taskObject);
                    }

                    TaskAdapter MyAdapter = new TaskAdapter(getApplicationContext(), Myobjects);
                    ListView listView = (ListView) findViewById(R.id.confirmmytasks);
                    listView.setAdapter(MyAdapter);


                    ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                    query2.whereEqualTo("objectId", TheirID);
                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
                    query.whereMatchesQuery("Owner", query2);
                    query.whereEqualTo("Completed", false);
                    query.orderByAscending("dateDue");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(final List<ParseObject> taskList2, ParseException e) {
                            if (taskList2 != null) {
                                Log.d("QueryTasks:", "Task found");

                                for (int h = 0; h < theirtaskids.size(); h++){
                                    for (int i = 0; i < taskList2.size(); i++){
                                        if (theirtaskids.get(h).equals(taskList2.get(i).getObjectId())){
                                            TheirTasks.add(taskList2.get(i));
                                            break;
                                        }
                                    }
                                }

                                ArrayList<TaskObject> Theirobjects = new ArrayList<TaskObject>();

                                for (int i = 0; i < TheirTasks.size(); i++){
                                    ParseObject temptask = TheirTasks.get(i);
                                    TaskObject taskObject = new TaskObject(temptask.getString("Name"), fmt.format(temptask.getDate("dateDue")));
                                    Theirobjects.add(taskObject);
                                }

                                TaskAdapter TheirAdapter = new TaskAdapter(getApplicationContext(), Theirobjects);
                                ListView listView = (ListView) findViewById(R.id.confirmtheirtasks);
                                listView.setAdapter(TheirAdapter);


                                button.setOnClickListener( new View.OnClickListener(){
                                            public void onClick(View v){
                                                ParseObject newTrade = new ParseObject("TradeOffer");
                                                newTrade.put("SubmitTrader", user);
                                                newTrade.put("ReceiveTrader", otheruser);
                                                newTrade.put("SubmitterTasks", MyTasks);
                                                newTrade.put("ReceiverTasks", TheirTasks);
                                                newTrade.put("Received", "Pending");
                                                newTrade.saveInBackground();

                                                HashMap<String, Object> params = new HashMap<String, Object>();
                                                params.put("receiver", otheruser.getObjectId());
                                                params.put("sender", user.getString("name"));

                                                ParseCloud.callFunctionInBackground("TradeNotify", params, new FunctionCallback<String>() {
                                                    public void done(String result, com.parse.ParseException e) {
                                                        if (e == null) {
                                                            // result is "Hello world!"
                                                            Log.d("Result is: ", result);
                                                        }
                                                    }
                                                });



                                                closeActivity();
                                            }
                                       }


                                );


                                }
                            }
                        }
                    );





                }
            }});

                }
            }
        });




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

    public void closeActivity(){ this.finish(); }
}
