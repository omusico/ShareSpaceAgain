package com.salilgokhale.sharespace3.Expenses.ItemTabFragments;

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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragmentBillsE;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragmentBillsS;
import com.salilgokhale.sharespace3.Expenses.ExpenseAdapter;
import com.salilgokhale.sharespace3.Expenses.ExpenseObject;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.SpinnerListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNewBillActivity extends ActionBarActivity {
    private Spinner spinner1;
    private Spinner spinner2;
    private Button StartDateButton;
    private Button EndDateButton;

    private int year;
    private int month;
    private int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bill);
        getSupportActionBar().setTitle("Add New Bill");

        addCheckBoxItems();
        addSpinnerItems();
        addItemsToSpinner();
        setDateItems();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_bill, menu);
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

    public void createNewBill(){

        final EditText title_et = (EditText) findViewById(R.id.bill_name_edit_text);
        final EditText amount_et = (EditText) findViewById(R.id.bill_amount_edit_text);
        final String Payer = String.valueOf(spinner2.getSelectedItem());
        Button StartDateButton = (Button) findViewById(R.id.start_date_button_bills);
        Button EndDateButton = (Button) findViewById(R.id.end_date_button_bills);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        final ListView listView = (ListView) findViewById(R.id.bill_checkboxes);

        final String Frequency = String.valueOf(spinner1.getSelectedItem());


        final String start_date = StartDateButton.getText().toString();
        final String end_date = EndDateButton.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c0 = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c0.add(Calendar.DATE, -1);
            c1.setTime(sdf.parse(start_date));
            c1.add(Calendar.HOUR, 1);
            c2.setTime(sdf.parse(end_date));
            c2.add(Calendar.HOUR, 1);
            final Date startdate = c1.getTime();
            Log.d("Start Date", startdate.toString());
            final Date enddate = c2.getTime();
            Log.d("End Date", enddate.toString());




        if(title_et.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Expense must have name", Toast.LENGTH_LONG);
            toast.show();
        }
        else if(amount_et.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Expense must have amount", Toast.LENGTH_LONG);
            toast.show();
        }
        else if(Float.valueOf(amount_et.getText().toString()) == 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Expense must have amount", Toast.LENGTH_LONG);
            toast.show();
        }
        else if (!c1.before(c2)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Start Date can't be after End Date", Toast.LENGTH_LONG);
            toast.show();
        } else if (!c0.before(c1)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Start Date can't be in past", Toast.LENGTH_LONG);
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

                                               if (Frequency.equals("When Needed")) {

                                                   // When needed bills will be like aperiodic rotas. will be red/green

                                                   final ParseObject newBill = new ParseObject("Bills");
                                                   newBill.put("Name", title_et.getText().toString());
                                                   //newBill.put("Amount", Float.valueOf(amount_et.getText().toString()));
                                                   newBill.put("House", userHouse);


                                                   boolean empty = true;
                                                   boolean payer_selected = false;

                                                   for (int i = 0; i < userList.size(); i++) {
                                                       CheckBox feature = (CheckBox) listView.getChildAt(i).findViewById(R.id.expense_checkbox);
                                                       EditText feature_et = (EditText) listView.getChildAt(i).findViewById(R.id.checkbox_amount);
                                                       if (feature.isChecked() && !feature_et.getText().toString().equals("")) {
                                                           empty = false;
                                                           break;
                                                       }
                                                   }

                                                   for (int i = 0; i < userList.size(); i++) {
                                                       CheckBox feature = (CheckBox) listView.getChildAt(i).findViewById(R.id.expense_checkbox);
                                                       if (feature.isChecked() && feature.getText().toString().equals(Payer)) {
                                                           payer_selected = true;
                                                           break;
                                                       }
                                                   }

                                                   if (!empty && payer_selected) {
                                                       final List<ParseUser> peopleSplit = new ArrayList<>();

                                                       for (int i = 0; i < userList.size(); i++) {

                                                           CheckBox cBox1 = (CheckBox) listView.getChildAt(i).findViewById(R.id.expense_checkbox);
                                                           EditText et = (EditText) listView.getChildAt(i).findViewById(R.id.checkbox_amount);
                                                           if (cBox1.isChecked() && (!et.getText().toString().equals(""))) {
                                                               peopleSplit.add(userList.get(i));

                                                           }


                                                           if (userList.get(i).getString("name").equals(Payer) && cBox1.isChecked()) {
                                                               newBill.put("Payer", userList.get(i));

                                                           }

                                                       }

                                                   }




                                               } else {

                                                   // Periodic bills will be like periodic rotas - name, next date, Payer and Amount below
                                                   // Cloud function will run every day and check if bill is due
                                                   // If it is, it will post as an expense to everybody

                                                   Log.d("No. of Users: ", String.valueOf(userList.size()));

                                                   boolean empty = true;
                                                   boolean payer_selected = false;

                                                   for (int i = 0; i < userList.size(); i++) {
                                                       CheckBox feature = (CheckBox) listView.getChildAt(i).findViewById(R.id.expense_checkbox);
                                                       EditText feature_et = (EditText) listView.getChildAt(i).findViewById(R.id.checkbox_amount);
                                                       if (feature.isChecked() && !feature_et.getText().toString().equals("")) {
                                                           empty = false;
                                                           break;
                                                       }
                                                   }

                                                   for (int i = 0; i < userList.size(); i++) {
                                                       CheckBox feature = (CheckBox) listView.getChildAt(i).findViewById(R.id.expense_checkbox);
                                                       if (feature.isChecked() && feature.getText().toString().equals(Payer)) {
                                                           payer_selected = true;
                                                           break;
                                                       }
                                                   }

                                                   if (!empty && payer_selected) {


                                                       final List<ParseUser> peopleSplit = new ArrayList<>();
                                                       final List<Float> amountSplit = new ArrayList<>();


                                                       final ParseObject newBill = new ParseObject("Bills");
                                                       newBill.put("Name", title_et.getText().toString());
                                                       newBill.put("Amount", Float.valueOf(amount_et.getText().toString()));
                                                       newBill.put("StartDate", startdate);
                                                       newBill.put("EndDate", enddate);
                                                       newBill.put("House", userHouse);


                                                       float total = Float.valueOf(amount_et.getText().toString());
                                                       float runningtotal2 = 0.0f;
                                                       int payer_id = 0;
                                                       Log.d("Actual Total: ", String.valueOf(total));

                                                       for (int i = 0; i < userList.size(); i++) {

                                                           CheckBox cBox1 = (CheckBox) listView.getChildAt(i).findViewById(R.id.expense_checkbox);
                                                           EditText et = (EditText) listView.getChildAt(i).findViewById(R.id.checkbox_amount);
                                                           if (cBox1.isChecked() && (!et.getText().toString().equals(""))) {
                                                               float number = Float.valueOf(et.getText().toString());
                                                               Log.d("Value: ", String.valueOf(number));
                                                               runningtotal2 += number;
                                                               Log.d("Running Total: ", String.valueOf(runningtotal2));
                                                               peopleSplit.add(userList.get(i));
                                                               amountSplit.add(number);
                                                           }


                                                           if (userList.get(i).getString("name").equals(Payer) && cBox1.isChecked()) {
                                                               newBill.put("Payer", userList.get(i));
                                                               payer_id = i;
                                                           }

                                                       }

                                                       NumberFormat formatter = new DecimalFormat("#0.00");
                                                       Float runningtotal3 = 0.0f;
                                                       runningtotal3 = Float.valueOf(formatter.format(runningtotal2));


                                                       if (runningtotal3 > total) {
                                                           Toast toast = Toast.makeText(getApplicationContext(), "Sum > Total", Toast.LENGTH_LONG);
                                                           toast.show();

                                                       } else if (runningtotal3 < total) {
                                                           Toast toast = Toast.makeText(getApplicationContext(), "Sum < Total", Toast.LENGTH_LONG);
                                                           toast.show();

                                                       } else {
                                                           newBill.put("peopleSplit", peopleSplit);
                                                           newBill.put("amountSplit", amountSplit);


                                                           final ParseUser temp_payer = userList.get(payer_id);


                                                           newBill.saveInBackground(new SaveCallback() {
                                                               @Override
                                                               public void done(com.parse.ParseException e) {
                                                                   if (e == null) {
                                                                       closeactivity();
                                                                   }
                                                               }
                                                           });


                                                       }

                                                   } else {
                                                       if (empty) {
                                                           Toast toast = Toast.makeText(getApplicationContext(), "Expense must have members", Toast.LENGTH_LONG);
                                                           toast.show();
                                                       } else {
                                                           Toast toast = Toast.makeText(getApplicationContext(), "Payer must be selected", Toast.LENGTH_LONG);
                                                           toast.show();
                                                       }
                                                   }


                                               }
                                           }
                                       }
                                   }
            );
        }

        } catch (ParseException e2) {
            e2.printStackTrace();
        }

    }


    public void addCheckBoxItems(){
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

                    for (int i = 0; i < userList.size(); i++) {
                        ExpenseObject temp = new ExpenseObject(userList.get(i).getString("name"), "0.00");
                        objects.add(temp);
                    }


                    final ListView listView = (ListView) findViewById(R.id.checkboxes);

                    final ExpenseAdapter expenseAdapter = new ExpenseAdapter(getApplicationContext(), objects);
                    listView.setAdapter(expenseAdapter);

                    //final TextView RunningTotal = (TextView) findViewById(R.id.amount_total);
                    final EditText TotalAmount = (EditText) findViewById(R.id.amount_edit_text);
                    /*

                    TotalAmount.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable s) {
                            if (!s.toString().equals("")) {
                                float f = Float.parseFloat(s.toString());

                                //RunningTotal.setText("Total: 0.00/" + String.valueOf(f));

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

                    */


                    final Button button = (Button) findViewById(R.id.auto_button_bills);
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

                                float equalamount = Float.valueOf(TotalAmount.getText().toString()) / checked;

                                for (int k = 0; k < userList.size(); k++) {
                                    if ((CheckBox) listView.getChildAt(k).findViewById(R.id.expense_checkbox) != null) {

                                        CheckBox cBox = (CheckBox) listView.getChildAt(k).findViewById(R.id.expense_checkbox);
                                        EditText et = (EditText) listView.getChildAt(k).findViewById(R.id.checkbox_amount);
                                        if (cBox.isChecked()) {
                                            et.setText(String.format("%.2f", equalamount));

                                        } else {
                                            et.setText("0.00");
                                        }
                                    }
                                }


                            }
                        }
                    });


                }
            }
        });



    }

    public void addSpinnerItems(){
        spinner1 = (Spinner) findViewById(R.id.bill_frequency_spinner);

        String[] frequency_items = getResources().getStringArray(R.array.bill_frequency_options);
        final List<String> frequencyList = new ArrayList<>(Arrays.asList(frequency_items));

        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, frequencyList);
        freqAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner1.setAdapter(freqAdapter);
        spinner1.setOnItemSelectedListener(new SpinnerListener());

    }


    public void addItemsToSpinner(){
        Log.d("Function:", "Entered addItemsonSpinner");
        spinner2 = (Spinner) findViewById(R.id.bill_payer_spinner);

        final ParseUser user = ParseUser.getCurrentUser();
        Log.d("Current User", user.getString("username"));
        ParseObject userHouse = user.getParseObject("Home");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (userList != null) {
                    List<String> list = new ArrayList<>();

                    list.add(user.getString("name"));

                    for (int i = 0; i < userList.size(); i++) {
                        if (!userList.get(i).getObjectId().equals(user.getObjectId())) {
                            list.add(userList.get(i).getString("name"));
                            Log.d("User Name:", userList.get(i).getString("name"));
                        }
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.spinner_item, list);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinner2.setAdapter(dataAdapter);
                    spinner2.setOnItemSelectedListener(new SpinnerListener());
                }
            }
        });

    }


    public void setDateItems(){

        StartDateButton = (Button) findViewById(R.id.start_date_button_bills);
        EndDateButton = (Button) findViewById(R.id.end_date_button_bills);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        //year2 = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        //month2 = c.get(Calendar.MONTH);
        int temp_month = month + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        //day2 = c.get(Calendar.DAY_OF_MONTH);
        //month++;                // Added because month is zero based
        String b = "/";
        String currentDate = String.valueOf(day) + b + String.valueOf(temp_month) + b + String.valueOf(year);

        StartDateButton.setText(currentDate);
        EndDateButton.setText(currentDate);

    }

    public void showRotaStartDatePickerDialogB(View v) {
        DialogFragment newFragment = new DatePickerFragmentBillsS();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showRotaEndDatePickerDialogB(View v1) {
        DialogFragment newFragment2 = new DatePickerFragmentBillsE();
        newFragment2.show(getSupportFragmentManager(), "datePicker");
    }

    public void closeactivity(){
        this.finish();
    }

}
