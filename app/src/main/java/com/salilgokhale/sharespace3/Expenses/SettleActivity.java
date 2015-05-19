package com.salilgokhale.sharespace3.Expenses;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragment3;
import com.salilgokhale.sharespace3.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SettleActivity extends ActionBarActivity {
    final ParseUser user = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle);
        getSupportActionBar().setTitle("Payment");


        final ParseObject userHouse = user.getParseObject("Home");

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        month++;                // Added because month is one behind for some reason
        String b = "/";
        String currentDate = String.valueOf(day) + b + String.valueOf(month) + b + String.valueOf(year);
        final Button DateButton = (Button) findViewById(R.id.date_button_payment);
        DateButton.setText(currentDate);

        TextView firstname = (TextView) findViewById(R.id.payment_payer);
        TextView secondname = (TextView) findViewById(R.id.payment_payee);
        final EditText et = (EditText) findViewById(R.id.payment_edit_text);

        final Bundle extras = getIntent().getExtras();
        final String payer = extras.getString("1st Name");
        final String payee = extras.getString("2nd Name");
        final String oweexpenseid = extras.getString("OweExpenseID");
        final String otherID;
        if (payer.equals(user.getString("name"))){
            otherID = extras.getString("2nd Name Object ID");
        }
        else{
            otherID = extras.getString("1st Name Object ID");
        }
        firstname.setText(payer);
        secondname.setText(payee);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(otherID, new GetCallback<ParseUser>() {
            @Override
            public void done(final ParseUser parseUser, ParseException e) {

                final Button button = (Button) findViewById(R.id.payment_add);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (!et.getText().toString().equals("") || !(Float.valueOf(et.getText().toString()) == 0)) {
                            try {
                                final String userinput = DateButton.getText().toString();
                                final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                                final Date date = formatter.parse(userinput);
                                Float number = Float.valueOf(et.getText().toString());


                                ParseObject settlelog = new ParseObject("ExpenseLog");
                                settlelog.put("Amount", number);
                                settlelog.put("House", userHouse);
                                settlelog.put("Date", date);
                                settlelog.put("Settlement", true);
                                if (extras.getString("1st Name Object ID").equals(user.getObjectId())) {
                                    settlelog.put("Payer", user);
                                    settlelog.put("SettlementPayee", parseUser);
                                    settlelog.put("Title", user.getString("name") + " to");
                                } else {
                                    settlelog.put("Payer", parseUser);
                                    settlelog.put("SettlementPayee", user);
                                    settlelog.put("Title", parseUser.getString("name") + " to");
                                }

                                settlelog.saveInBackground();

                                updateOweExpense(oweexpenseid, number);

                            }
                            catch (java.text.ParseException e2) {
                                e2.printStackTrace();
                                Log.d("Date:", "Error");
                            }
                        }
                    }
                });



            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settle, menu);
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

    public void showDatePickerDialog3(View v) {
        DialogFragment newFragment = new DatePickerFragment3();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void closeactivity(){
        this.finish();
    }

    public void updateOweExpense(String oweexpenseid, final Float amount){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("OweExpense");
        query.getInBackground(oweexpenseid, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Float number = object.getNumber("Amount").floatValue();
                    if (object.getString("Name1").equals(user.getString("name"))){
                        number = number - amount;
                    }
                    else
                    {
                        number = number + amount;
                    }
                    object.put("Amount", number);
                    object.saveInBackground();
                    closeactivity();
                }
            }
        });
    }
}
