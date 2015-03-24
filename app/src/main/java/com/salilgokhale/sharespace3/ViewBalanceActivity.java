package com.salilgokhale.sharespace3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ViewBalanceActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_balance);

        final TextView balance_view = (TextView) findViewById(R.id.balance_view);

        Intent intent = this.getIntent();
        String oweexpenseid = intent.getStringExtra(Intent.EXTRA_TEXT);
        final ParseUser user = ParseUser.getCurrentUser();

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("OweExpense");
        query.include("OwnerArray");
        query.getInBackground(oweexpenseid, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    String title;
                    String debt;
                    Number tempamount = object.getNumber("Amount");
                    Float amount = tempamount.floatValue();

                    if(object.getString("Name1").equals(user.getString("name"))){
                        title = "Account with " + object.getString("Name2");
                        if (amount > 0){
                            debt = "£" + String.format("%.2f", amount) + " to be paid";

                        }
                        else if(amount < 0){
                            debt = "£" + String.format("%.2f", Math.abs(amount)) + " to pay";

                        }
                        else {
                            debt = "Even";
                        }



                    }
                    else{
                        title = "Account with " + object.getString("Name1");

                        if (amount < 0){
                            debt = "£" + String.format("%.2f", Math.abs(amount)) + " to be paid";

                        }
                        else if(amount > 0){
                            debt = "£" + String.format("%.2f", amount) + " to pay";

                        }
                        else {
                            debt = "Even";
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
                    query2.whereEqualTo("Payer", user);
                    query2.whereEqualTo("peopleSplit", OtherUser);
                    ParseQuery<ParseObject> query3 = ParseQuery.getQuery("ExpenseLog");
                    query3.whereEqualTo("Payer", OtherUser);
                    query3.whereEqualTo("peopleSplit", user);
                    queries.add(query2);
                    queries.add(query3);
                    ParseQuery<ParseObject> superQuery = ParseQuery.or(queries);
                    superQuery.include("peopleSplit");
                    superQuery.include("amountSplit");
                    superQuery.include("Payer");
                    superQuery.findInBackground(new FindCallback<ParseObject>() {
                            public void done(final List<ParseObject> expenseList, ParseException e) {
                                if (expenseList != null) {
                                    List<String> titles = new ArrayList<String>();
                                    List<String> payers = new ArrayList<String>();
                                    List<String> values = new ArrayList<String>();
                                    List<String> dates = new ArrayList<String>();
                                    List<String> owes = new ArrayList<String>();
                                    List<ParseUser> peopleSplit = new ArrayList<>();
                                    List<Float> amountSplit = new ArrayList<>();

                                    for (int i = 0; i < expenseList.size(); i++){
                                        ParseObject expense = expenseList.get(i);
                                        Log.d("Expense Name: ", expense.getString("Title"));
                                        titles.add(expense.getString("Title"));
                                        payers.add(expense.getParseObject("Payer").getString("name"));
                                        values.add(String.valueOf(expense.getNumber("Amount")));
                                        Log.d("Payer name: ", payers.get(i));
                                        DateFormat df = new SimpleDateFormat("d/M/yy");
                                        String date = df.format(expense.getDate("Date"));
                                        dates.add(date);

                                        peopleSplit = expense.getList("peopleSplit");
                                        amountSplit = expense.getList("amountSplit");

                                        if (expense.getParseObject("Payer").getObjectId().equals(user.getObjectId())){
                                            for (int j = 0; j < peopleSplit.size(); j++){
                                                if (peopleSplit.get(j).getObjectId().equals(OtherUser.getObjectId())){
                                                    owes.add(OtherUser.getString("name") + " owes you £" + String.valueOf(amountSplit.get(j)));
                                                }
                                            }
                                        }
                                        else{
                                            for (int j = 0; j < peopleSplit.size(); j++){
                                                if (peopleSplit.get(j).getObjectId().equals(user.getObjectId())){
                                                    owes.add("You owe £" + String.valueOf(amountSplit.get(j)));
                                                }
                                            }
                                        }


                                    }


                                    ListView listView = (ListView) findViewById(R.id.specific_expense_logs);
                                    SpecificExpenseLogAdapter adapter = new SpecificExpenseLogAdapter(getApplicationContext(), titles, payers, values, dates, owes);
                                    listView.setAdapter(adapter);

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
