package com.salilgokhale.sharespace3.Expenses;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragment2;
import com.salilgokhale.sharespace3.MainActivity;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.SpinnerListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// TODO make the input to the edit texts automatically calculate the values.

public class AddNewExpenseActivity extends ActionBarActivity {
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_expense);
        getSupportActionBar().setTitle("Add New Expense");

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        month++;                // Added because month is one behind for some reason
        String b = "/";
        String currentDate = String.valueOf(day) + b + String.valueOf(month) + b + String.valueOf(year);
        Button DateButton = (Button) findViewById(R.id.date_button_expense);
        DateButton.setText(currentDate);

        addItemsToSpinner();
        addCheckBoxes();
        //setListenerToTotalAmount();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_expense, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addItemsToSpinner(){
        Log.d("Function:", "Entered addItemsonSpinner");
        spinner = (Spinner) findViewById(R.id.payer_spinner);

        ParseUser user = ParseUser.getCurrentUser();
        Log.d("Current User", user.getString("username"));
        ParseObject userHouse = user.getParseObject("Home");
        if (userHouse != null) {
            Log.d("Home Object Name:", userHouse.getObjectId());
        }
        else{
            Log.d("Error", "Error");
        }
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (userList != null) {
                    List<String> list = new ArrayList<>();


                    for (int i = 0; i< userList.size(); i++){
                        list.add(userList.get(i).getString("name"));
                        Log.d("User Name:", userList.get(i).getString("name"));
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.spinner_item, list);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                    spinner.setOnItemSelectedListener(new SpinnerListener());
                }
            }});

    }


    public void addCheckBoxes(){
        ParseUser user = ParseUser.getCurrentUser();

        ParseObject userHouse = user.getParseObject("Home");
        if (userHouse != null) {
            Log.d("Home Object Name:", userHouse.getObjectId());
        }
        else{
            Log.d("Error", "Error");
        }
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(final List<ParseUser> userList, com.parse.ParseException e) {
                if (userList != null) {
                    ArrayList<ExpenseObject> objects = new ArrayList<>();

                    for (int i = 0; i< userList.size(); i++){
                        ExpenseObject temp = new ExpenseObject(userList.get(i).getString("name"), "0.00");
                        objects.add(temp);
                    }


                    final ListView listView = (ListView) findViewById(R.id.checkboxes);

                    final ExpenseAdapter expenseAdapter = new ExpenseAdapter (getApplicationContext(), objects);
                    listView.setAdapter(expenseAdapter);

                    final TextView RunningTotal = (TextView) findViewById(R.id.amount_total);
                    final EditText TotalAmount = (EditText) findViewById(R.id.amount_edit_text);
                    TotalAmount.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable s) {
                            if (!s.toString().equals("")) {
                                float f = Float.parseFloat(s.toString());

                                RunningTotal.setText("Total: 0.00/" + String.valueOf(f));

                                for (int j = 0; j < userList.size(); j++) {
                                    expenseAdapter.getItem(j).setAmount("0.00");
                                }
                                expenseAdapter.notifyDataSetChanged();
                            }
                        }
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            //XXX do something
                        }
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //XXX do something
                        }
                    });


                    final Button button = (Button) findViewById(R.id.auto_button);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (TotalAmount.getText() != null) {
                                int checked = 0;

                                for (int l = 0; l < userList.size(); l++) {
                                    if ((CheckBox) listView.getChildAt(l).findViewById(R.id.expense_checkbox) != null) {

                                        CheckBox cBox = (CheckBox) listView.getChildAt(l).findViewById(R.id.expense_checkbox);
                                        //EditText et = (EditText) listView.getChildAt(l).findViewById(R.id.checkbox_amount);
                                        if (cBox.isChecked()) {
                                            checked++;

                                        }
                                    }
                                }

                                float equalamount = Float.valueOf(TotalAmount.getText().toString())/checked;

                                for (int k = 0; k < userList.size(); k++) {
                                    if ((CheckBox) listView.getChildAt(k).findViewById(R.id.expense_checkbox) != null) {

                                        CheckBox cBox = (CheckBox) listView.getChildAt(k).findViewById(R.id.expense_checkbox);
                                        EditText et = (EditText) listView.getChildAt(k).findViewById(R.id.checkbox_amount);
                                        if (cBox.isChecked()) {
                                            et.setText(String.format("%.2f", equalamount));

                                        }
                                        else{
                                            et.setText("0.00");
                                        }
                                    }
                                }


                            }
                        }
                    });


               /* for (int m = 0; m < userList.size(); m++){
                    EditText et1 = (EditText) listView.getChildAt(m).findViewById(R.id.checkbox_amount);
                    et1.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable s) {
                            if (s != null) {
                                float total = Float.valueOf(TotalAmount.getText().toString());
                                float runningtotal2 = 0.0f;
                                for (int n = 0; n < userList.size(); n++) {
                                    CheckBox cBox1 = (CheckBox) listView.getChildAt(n).findViewById(R.id.expense_checkbox);
                                    EditText et = (EditText) listView.getChildAt(n).findViewById(R.id.checkbox_amount);
                                    if (cBox1.isChecked() && (et.getText() != null)){
                                        runningtotal2 += Float.valueOf(et.getText().toString());
                                    }

                                }

                                if (runningtotal2 > total){
                                    Toast toast = Toast.makeText(getApplicationContext(), "Sum > Total", Toast.LENGTH_SHORT);
                                    toast.show();

                                }

                            }
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            //XXX do something
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //XXX do something
                        }
                    });



                } */

                }
            }});



    }

    /*public void setListenerToAmount(){
        final ListView listView = (ListView) findViewById(R.id.checkboxes);


        EditText TotalAmount = (EditText) findViewById(R.id.amount_edit_text);
        TotalAmount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                float f = Float.parseFloat(s.toString());

                for(int i = 0;)


            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //XXX do something
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //XXX do something
            }
        });

    } */

public void createNewExpense(View view){
    final EditText title_et = (EditText) findViewById(R.id.expense_name_edit_text);
    final EditText amount_et = (EditText) findViewById(R.id.amount_edit_text);
    final String Payer = String.valueOf(spinner.getSelectedItem());
    Button DateButton = (Button) findViewById(R.id.date_button_expense);
    final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
    final ListView listView = (ListView) findViewById(R.id.checkboxes);
    final String userinput = DateButton.getText().toString();

    if(title_et.getText().toString().equals("")){
        Toast toast = Toast.makeText(getApplicationContext(), "Expense must have name", Toast.LENGTH_LONG);
        toast.show();
    }
    else if(Float.valueOf(amount_et.getText().toString()) == 0 || Float.valueOf(amount_et.getText().toString()).equals("")){
        Toast toast = Toast.makeText(getApplicationContext(), "Expense must have amount", Toast.LENGTH_LONG);
        toast.show();
    }
    else {

        ParseUser user = ParseUser.getCurrentUser();
        final ParseObject userHouse = user.getParseObject("Home");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);
        query.findInBackground(new FindCallback<ParseUser>() {
                                   public void done(List<ParseUser> userList, com.parse.ParseException e) {
                                       if (userList != null) {
                                           try {
                                               Log.d("No. of Users: ", String.valueOf(userList.size()));

                                               final Date date = formatter.parse(userinput);
                                               final List<ParseUser> peopleSplit = new ArrayList<>();
                                               final List<Float> amountSplit = new ArrayList<>();

                                               final ParseObject newExpense = new ParseObject("ExpenseLog");
                                               newExpense.put("Title", title_et.getText().toString());
                                               newExpense.put("Amount", Float.valueOf(amount_et.getText().toString()));
                                               newExpense.put("Date", date);
                                               newExpense.put("House", userHouse);
                                               newExpense.put("Settlement", false);

                                               float total = Float.valueOf(amount_et.getText().toString());
                                               float runningtotal2 = 0.0f;
                                               int payer_id = 0;

                                               for (int i = 0; i < userList.size(); i++) {

                                                   CheckBox cBox1 = (CheckBox) listView.getChildAt(i).findViewById(R.id.expense_checkbox);
                                                   EditText et = (EditText) listView.getChildAt(i).findViewById(R.id.checkbox_amount);
                                                   if (cBox1.isChecked() && (!et.getText().toString().equals(""))) {
                                                       float number = Float.valueOf(et.getText().toString());
                                                       runningtotal2 += number;
                                                       peopleSplit.add(userList.get(i));
                                                       amountSplit.add(number);
                                                   }


                                                   if (userList.get(i).getString("name").equals(Payer) && cBox1.isChecked()) {
                                                       newExpense.put("Payer", userList.get(i));
                                                       payer_id = i;
                                                   }

                                               }

                                               if (runningtotal2 > total) {
                                                   Toast toast = Toast.makeText(getApplicationContext(), "Sum > Total", Toast.LENGTH_LONG);
                                                   toast.show();

                                               }
                                               else if (runningtotal2 < total){
                                                   Toast toast = Toast.makeText(getApplicationContext(), "Sum < Total", Toast.LENGTH_LONG);
                                                   toast.show();

                                               } else {
                                                   newExpense.put("peopleSplit", peopleSplit);
                                                   newExpense.put("amountSplit", amountSplit);


                                                   final ParseUser temp_payer = userList.get(payer_id);

                                                   ParseQuery<ParseObject> query2 = ParseQuery.getQuery("OweExpense");
                                                   query2.whereEqualTo("OwnerArray", temp_payer);
                                                   query2.findInBackground(new FindCallback<ParseObject>() {
                                                                               public void done(final List<ParseObject> OweExpenseList, com.parse.ParseException e3) {
                                                                                   Log.d("Entered Query for ", "Owe Expenses");
                                                                                   if (OweExpenseList != null) {
                                                                                       Log.d("peopleSplit size: ", String.valueOf(peopleSplit.size()));
                                                                                       Log.d("No. OweExpenses:", String.valueOf(OweExpenseList.size()));

                                                                                       for (int i = 0; i < peopleSplit.size(); i++) {
                                                                                           Log.d("Iteration of people: ", String.valueOf(i));
                                                                                           if (!peopleSplit.get(i).getObjectId().equals(temp_payer.getObjectId())) {
                                                                                               Log.d("Iteration inside: ", String.valueOf(i));
                                                                                               for (int j = 0; j < OweExpenseList.size(); j++) {
                                                                                                   Log.d("Iteration OweExpense", String.valueOf(j));

                                                                                                   if (peopleSplit.get(i).getString("name").equals(OweExpenseList.get(j).getString("Name1"))) {
                                                                                                       Log.d("Hit number: ", String.valueOf(i));
                                                                                                       Number tempValue = OweExpenseList.get(j).getNumber("Amount");
                                                                                                       Float Value = tempValue.floatValue();
                                                                                                       Value += -amountSplit.get(i);
                                                                                                       Log.d("Value", String.valueOf(Value));
                                                                                                       OweExpenseList.get(j).put("Amount", Value);
                                                                                                       OweExpenseList.get(j).saveInBackground();
                                                                                                   } else if (peopleSplit.get(i).getString("name").equals(OweExpenseList.get(j).getString("Name2"))) {
                                                                                                       Log.d("Hit number: ", String.valueOf(i));
                                                                                                       Number tempValue = OweExpenseList.get(j).getNumber("Amount");
                                                                                                       Float Value = tempValue.floatValue();
                                                                                                       Value += amountSplit.get(i);
                                                                                                       Log.d("Value", String.valueOf(Value));
                                                                                                       OweExpenseList.get(j).put("Amount", Value);
                                                                                                       OweExpenseList.get(j).saveInBackground();
                                                                                                   }


                                                                                               }


                                                                                           }
                                                                                       }
                                                                                       newExpense.saveInBackground(new SaveCallback() {
                                                                                           @Override
                                                                                           public void done(com.parse.ParseException e) {
                                                                                               if (e == null) {
                                                                                                   closeactivity();
                                                                                               }
                                                                                           }
                                                                                       });

                                                                                   }

                                                                               }
                                                                           }
                                                   );


                                               }


                                           } catch (ParseException e2) {
                                               e2.printStackTrace();
                                               Log.d("Date:", "Error");
                                           }


                                       }
                                   }
                               }
        );
    }

}
    public void closeactivity(){
        this.finish();
    }

    public void showDatePickerDialog2(View v) {
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }



}


