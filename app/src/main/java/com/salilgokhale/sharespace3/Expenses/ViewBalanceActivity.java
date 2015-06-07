package com.salilgokhale.sharespace3.Expenses;

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
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViewBalanceActivity extends ActionBarActivity {

    public enum Direction {
        USEROWES,
        USEROWED,
        EVEN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_balance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            /*case R.id.action_delete_owe_expense:
                Intent intent = this.getIntent();
                final String oweexpenseid = intent.getStringExtra(Intent.EXTRA_TEXT);


                ParseQuery<ParseObject> query4 = ParseQuery.getQuery("OweExpense");
                query4.whereEqualTo("objectId", oweexpenseid);

                query4.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(final ParseObject object, ParseException e) {
                        if (object == null) {
                            Log.d("OweExpense:", "Not Found");
                        }
                        else {
                            object.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null){
                                        CloseActivity();
                                    }
                                }
                            });


                        }}});

                return true; */
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        setContentView(R.layout.activity_view_balance);
        getSupportActionBar().setTitle("");


        final TextView balance_view = (TextView) findViewById(R.id.balance_view);

        Intent intent = this.getIntent();
        final String oweexpenseid = intent.getStringExtra(Intent.EXTRA_TEXT);
        final ParseUser user = ParseUser.getCurrentUser();

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("OweExpense");
        query.include("OwnerArray");
        query.getInBackground(oweexpenseid, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    String title;
                    final String debt;
                    Number tempamount = object.getNumber("Amount");
                    final Float amount = tempamount.floatValue();
                    final Direction direction;
                    final boolean path;
                    final String secondname;

                    if(object.getString("Name1").equals(user.getString("name"))){
                        title = "Account with " + object.getString("Name2");
                        path = false;
                        secondname = object.getString("Name2");

                        if (amount > 0){
                            debt = "£" + String.format("%.2f", amount) + " to be paid";
                            direction = Direction.USEROWED;

                        }
                        else if(amount < 0){
                            debt = "£" + String.format("%.2f", Math.abs(amount)) + " to pay";
                            direction = Direction.USEROWES;
                        }
                        else {
                            debt = "Even";
                            direction = Direction.EVEN;
                        }



                    }
                    else{
                        title = "Account with " + object.getString("Name1");
                        path = true;
                        secondname = object.getString("Name1");

                        if (amount < 0){
                            debt = "£" + String.format("%.2f", Math.abs(amount)) + " to be paid";
                            direction = Direction.USEROWED;
                        }
                        else if(amount > 0){
                            debt = "£" + String.format("%.2f", amount) + " to pay";
                            direction = Direction.USEROWES;
                        }
                        else {
                            debt = "Even";
                            direction = Direction.EVEN;
                        }


                    }
                    balance_view.setText(debt);
                    getSupportActionBar().setTitle(title);

                    List<ParseUser> owners = object.getList("OwnerArray");
                    final ParseObject OtherUser;
                    if (!owners.get(0).getObjectId().equals(user.getObjectId())){
                        OtherUser = owners.get(0);
                    }
                    else {
                        OtherUser = owners.get(1);
                    }

                    ArrayList<ParseQuery<ParseObject>> queries = new ArrayList<>();
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("ExpenseLog");
                    query2.whereEqualTo("Settlement", false);
                    query2.whereEqualTo("Payer", user);
                    query2.whereEqualTo("peopleSplit", OtherUser);
                    ParseQuery<ParseObject> query3 = ParseQuery.getQuery("ExpenseLog");
                    query3.whereEqualTo("Settlement", false);
                    query3.whereEqualTo("Payer", OtherUser);
                    query3.whereEqualTo("peopleSplit", user);
                    ParseQuery<ParseObject> query4 = ParseQuery.getQuery("ExpenseLog");
                    query4.whereEqualTo("Settlement", true);
                    query4.whereEqualTo("Payer", user);
                    query4.whereEqualTo("SettlementPayee", OtherUser);
                    ParseQuery<ParseObject> query5 = ParseQuery.getQuery("ExpenseLog");
                    query5.whereEqualTo("Payer", OtherUser);
                    query5.whereEqualTo("SettlementPayee", user);
                    queries.add(query2);
                    queries.add(query3);
                    queries.add(query4);
                    queries.add(query5);
                    ParseQuery<ParseObject> superQuery = ParseQuery.or(queries);
                    superQuery.include("peopleSplit");
                    superQuery.include("amountSplit");
                    superQuery.include("Payer");
                    superQuery.include("SettlementPayee");
                    superQuery.orderByDescending("Date");
                    superQuery.findInBackground(new FindCallback<ParseObject>() {
                                                    public void done(final List<ParseObject> expenseList, ParseException e) {
                                                        if (expenseList != null) {
                                                            List<String> titles = new ArrayList<String>();
                                                            List<String> payers = new ArrayList<String>();
                                                            List<String> values = new ArrayList<String>();
                                                            List<String> dates = new ArrayList<String>();
                                                            List<String> owes = new ArrayList<String>();
                                                            //List<String> owe_names = new ArrayList<String>();
                                                            List<ParseUser> peopleSplit = new ArrayList<>();
                                                            List<Float> amountSplit = new ArrayList<>();

                                                            for (int i = 0; i < expenseList.size(); i++){
                                                                ParseObject expense = expenseList.get(i);
                                                                Log.d("Expense Name: ", expense.getString("Title"));
                                                                titles.add(expense.getString("Title"));

                                                                values.add("£" + String.valueOf(expense.getNumber("Amount")));
                                                                //Log.d("Payer name: ", payers.get(i));
                                                                DateFormat df = new SimpleDateFormat("d/M/yy");
                                                                String date = df.format(expense.getDate("Date"));
                                                                dates.add(date);

                                                                if (expense.getBoolean("Settlement")){
                                                                    //owe_names.add("");
                                                                    owes.add("");
                                                                    payers.add(expense.getParseObject("SettlementPayee").getString("name"));
                                                                }
                                                                else {
                                                                    payers.add(expense.getParseObject("Payer").getString("name"));
                                                                    peopleSplit = expense.getList("peopleSplit");
                                                                    amountSplit = expense.getList("amountSplit");

                                                                    if (expense.getParseObject("Payer").getObjectId().equals(user.getObjectId())) {
                                                                        for (int j = 0; j < peopleSplit.size(); j++) {
                                                                            if (peopleSplit.get(j).getObjectId().equals(OtherUser.getObjectId())) {
                                                                                //owe_names.add(OtherUser.getString("name"));
                                                                                String temp = OtherUser.getString("name");
                                                                                String arr[] = temp.split(" ", 2);

                                                                                String firstName = arr[0];

                                                                                owes.add(firstName + " owes you £" + String.valueOf(amountSplit.get(j)));
                                                                            }
                                                                        }
                                                                    } else {
                                                                        for (int j = 0; j < peopleSplit.size(); j++) {
                                                                            if (peopleSplit.get(j).getObjectId().equals(user.getObjectId())) {
                                                                                owes.add("You owe £" + String.valueOf(amountSplit.get(j)));
                                                                            }
                                                                        }
                                                                    }

                                                                }
                                                            }


                                                            ListView listView = (ListView) findViewById(R.id.specific_expense_logs);
                                                            SpecificExpenseLogAdapter adapter = new SpecificExpenseLogAdapter(getApplicationContext(), titles, payers, values, dates, owes);
                                                            listView.setAdapter(adapter);


                                                            final Button button = (Button) findViewById(R.id.settle_button);
                                                            button.setOnClickListener(new View.OnClickListener() {
                                                                public void onClick(View v) {
                                                                    if (direction != Direction.EVEN) {
                                                                        Intent intent = new Intent(getApplicationContext(), SettleActivity.class);
                                                                        Bundle extras = new Bundle();
                                                                        if (direction == Direction.USEROWES) {
                                                                            extras.putString("1st Name", user.getString("name"));
                                                                            extras.putString("1st Name Object ID", user.getObjectId());
                                                                            extras.putString("2nd Name", OtherUser.getString("name"));
                                                                            extras.putString("2nd Name Object ID", OtherUser.getObjectId());
                                                                        }
                                                                        else {
                                                                            extras.putString("2nd Name", user.getString("name"));
                                                                            extras.putString("2nd Name Object ID", user.getObjectId());
                                                                            extras.putString("1st Name", OtherUser.getString("name"));
                                                                            extras.putString("1st Name Object ID", OtherUser.getObjectId());
                                                                        }
                                                                        extras.putString("OweExpenseID", oweexpenseid);
                                                                        extras.putString("Debt", debt);
                                                                        intent.putExtras(extras);
                                                                        startActivity(intent);
                                                                    }
                                                                    else{
                                                                        Toast toast = Toast.makeText(getApplicationContext(), "Nothing to Settle!", Toast.LENGTH_LONG);
                                                                        toast.show();
                                                                    }
                                                                }
                                                            });

                                                            final Button button1 = (Button) findViewById(R.id.remind_button);
                                                            button1.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {

                                                                    if ((path && (amount < 0)) || (!path && (amount > 0)) ) {

                                                                        HashMap<String, Object> params = new HashMap<String, Object>();
                                                                        params.put("owed", user.getString("name"));
                                                                        //params.put("ower", user.getObjectId());
                                                                        params.put("ower", OtherUser.getObjectId());
                                                                        params.put("amount", String.format("%.2f", Math.abs(amount)));

                                                                        ParseCloud.callFunctionInBackground("OweReminder", params, new FunctionCallback<String>() {
                                                                            public void done(String result, ParseException e) {
                                                                                if (e == null) {
                                                                                    // result is "Hello world!"
                                                                                    Log.d("Result is: ", result);
                                                                                }
                                                                            }
                                                                        });

                                                                    }
                                                                    else {
                                                                        Log.d("No Amount", " Owed.");
                                                                        Toast toast = Toast.makeText(getApplicationContext(), "Nothing to Remind them!", Toast.LENGTH_LONG);
                                                                        toast.show();
                                                                    }
                                                                }
                                                            });

                                                        }
                                                        else {
                                                            Log.d("Did it work?:", " No");
                                                        }
                                                    }
                                                }
                    );



                }
            }
        });





    }

    public void CloseActivity(){
        this.finish();
    }

}
