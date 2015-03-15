package com.salilgokhale.sharespace3;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddNewRotaActivity extends ActionBarActivity {
    private Spinner spinner;
    private Button StartDateButton;
    private Button EndDateButton;
    static final int START_DATE_DIALOG_ID = 1;
    static final int END_DATE_DIALOG_ID2 = 2;
    int cur = 0;

    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_rota);

        addCheckBoxItems();
        addSpinnerItems();
        setDateItems();
        addListenerOnDateItems();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_rota, menu);
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


    public void createNewRota(View view){
        final EditText et = (EditText) findViewById(R.id.rota_name_edit_text);


        ParseUser user = ParseUser.getCurrentUser();

        ParseObject userHouse = user.getParseObject("Home");
        if (userHouse != null) {
            Log.d("Home Object Name:", userHouse.getObjectId());
        } else {
            Log.d("Error", "Error");
        }
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (userList != null) {
                    //List<String> list = new ArrayList<>();

                    ParseObject newRota = new ParseObject("Rota");
                    newRota.put("Name", et.getText().toString());
                    ParseRelation<ParseObject> relation = newRota.getRelation("peopleInvolved");



                    boolean firstTime = true;
                    for (int i = 0; i < userList.size(); i++) {
                        CheckBox feature = (CheckBox) findViewById(i);
                        if (feature.isChecked()){

                            relation.add(userList.get(i));
                            if (firstTime) {
                                newRota.put("nextPerson", userList.get(i));
                                firstTime = false;
                            }
                        }
                    }
                newRota.put("Due", false);

                //String Frequency = String.valueOf(spinner.getSelectedItem());




                newRota.saveInBackground();

                }}});

        this.finish();




        //String Owner = String.valueOf(spinner.getSelectedItem());

        /*ParseUser user = ParseUser.getCurrentUser();
        ParseObject userHouse = user.getParseObject("Home");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);
        //query.whereEqualTo("name", Owner);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (userList != null) {
                    try {

                        //Date date = formatter.parse(userinput);

                        ParseObject newTask = new ParseObject("Tasks");
                        newTask.put("Name", et.getText().toString());
                        //newTask.put("dateDue", date);


                        newTask.put("Owner", userList.get(0));
                        newTask.put("Completed", false);

                        newTask.saveInBackground();

                        GoToCore();

                        Log.d("Date:", userinput);

                    } catch (ParseException e2) {
                        e.printStackTrace();
                        Log.d("Date:", "Error");
                    }

                }
            }
        });*/
    }

    public void addCheckBoxItems() {
        ParseUser user = ParseUser.getCurrentUser();
        Log.d("Current User", user.getString("username"));
        ParseObject userHouse = user.getParseObject("Home");
        if (userHouse != null) {
            Log.d("Home Object Name:", userHouse.getObjectId());
        } else {
            Log.d("Error", "Error");
        }
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (userList != null) {
                    List<String> list = new ArrayList<>();

                    LinearLayout featuresTable = (LinearLayout) findViewById(R.id.rota_members_checkboxes);

                    for (int i = 0; i < userList.size(); i++) {
                        list.add(userList.get(i).getString("name"));
                        Log.d("User Name:", userList.get(i).getString("name"));

                        CheckBox feature = new CheckBox(getApplicationContext());
                        feature.setText(userList.get(i).getString("name"));
                        feature.setId(i);
                        featuresTable.addView(feature);
                    }



                }

            }
        });
    }

    public void addSpinnerItems(){
        spinner = (Spinner) findViewById(R.id.rota_frequency_spinner);

        String[] frequency_items = getResources().getStringArray(R.array.frequency_options);
        final List<String> frequencyList = new ArrayList<>(Arrays.asList(frequency_items));

        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item, frequencyList);
        freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(freqAdapter);
        spinner.setOnItemSelectedListener(new SpinnerListener());

    }

    public void setDateItems(){

        StartDateButton = (Button) findViewById(R.id.start_date_button_rota);
        EndDateButton = (Button) findViewById(R.id.end_date_button_rota);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        month++;                // Added because month is zero based
        String b = "/";
        String currentDate = String.valueOf(day) + b + String.valueOf(month) + b + String.valueOf(year);

        StartDateButton.setText(currentDate);
        EndDateButton.setText(currentDate);

    }

    public void addListenerOnDateItems() {

        StartDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(START_DATE_DIALOG_ID);
            }

        });
        EndDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(END_DATE_DIALOG_ID2);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case START_DATE_DIALOG_ID:
                System.out.println("onCreateDialog  : " + id);
                cur = START_DATE_DIALOG_ID;
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);
            case END_DATE_DIALOG_ID2:
                cur = END_DATE_DIALOG_ID2;
                System.out.println("onCreateDialog2  : " + id);
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            String b = "/";
            String Date = String.valueOf(day) + b + String.valueOf(month) + b + String.valueOf(year);

            if(cur == START_DATE_DIALOG_ID){
                // set selected date into textview

                StartDateButton.setText(Date);

            }
            else{
                EndDateButton.setText(Date);
            }

        }
    };


}
