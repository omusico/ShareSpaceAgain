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
import java.text.SimpleDateFormat;
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
    private int year2;
    private int month2;
    private int day2;

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
        final String rota_name = et.getText().toString();

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
                    List<ParseObject> rotaparticipants = new ArrayList<ParseObject>();


                    ParseObject newRota = new ParseObject("Rota");
                    newRota.put("Name", rota_name);
                    ParseRelation<ParseObject> relation = newRota.getRelation("peopleInvolved");


                    String Frequency = String.valueOf(spinner.getSelectedItem());
                    newRota.put("Frequency", Frequency);

                    if (Frequency.equals("When Needed")) {

                        boolean firstTime = true;
                        for (int i = 0; i < userList.size(); i++) {
                            CheckBox feature = (CheckBox) findViewById(i);
                            if (feature.isChecked()) {
                                relation.add(userList.get(i));
                                if (firstTime) {
                                    newRota.put("nextPerson", userList.get(i));
                                    firstTime = false;
                                }
                            }
                        }
                        newRota.put("Due", false);
                    }

                    else {
                        for (int i = 0; i < userList.size(); i++) {
                            CheckBox feature = (CheckBox) findViewById(i);
                            if (feature.isChecked()) {
                                rotaparticipants.add(userList.get(i));
                                Log.d("RotaParticipant:", rotaparticipants.get(i).getString("name"));
                                relation.add(userList.get(i));
                                }
                            }

                        newRota.put("nextPerson", rotaparticipants.get(0));

                        final String start_date = StartDateButton.getText().toString();
                        final String end_date = EndDateButton.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar c1 = Calendar.getInstance();
                        Calendar c2 = Calendar.getInstance();
                        try {
                            c1.setTime(sdf.parse(start_date));
                            c2.setTime(sdf.parse(end_date));
                            Date startdate = c1.getTime();
                            Date enddate = c2.getTime();
                            newRota.put("startDate", startdate);
                            newRota.put("endDate", enddate);
                        } catch (ParseException e2) {
                            e2.printStackTrace();
                        }

                        int number = rotaparticipants.size();
                        int i = 0;

                        switch (Frequency){
                            case "Every 1 Week":

                                while (c1.before(c2) || c1.equals(c2)){
                                    Log.d("Iteration", "of calendar");
                                    Date date = c1.getTime();
                                    ParseObject temp = new ParseObject("Tasks");
                                    temp.put("Name", rota_name);
                                    temp.put("Owner", rotaparticipants.get(i%number));
                                    temp.put("dateDue", date);
                                    temp.put("Completed", false);
                                    temp.saveInBackground();
                                    c1.add(Calendar.DATE, 7);
                                    i++;
                                }

                                break;
                            case "Every 2 Weeks":
                                while (c1.before(c2) || c1.equals(c2)){
                                    Log.d("Iteration", "of calendar");
                                    ParseObject temp = new ParseObject("Tasks");
                                    temp.put("Name", rota_name);
                                    temp.put("Owner", rotaparticipants.get(i%number));
                                    temp.put("dateDue", c1.getTime());
                                    temp.put("Completed", false);
                                    temp.saveInBackground();
                                    c1.add(Calendar.DATE, 14);
                                    i++;
                                }
                                break;
                            case "Every 3 Weeks":
                                while (c1.before(c2) || c1.equals(c2)){
                                    Log.d("Iteration", "of calendar");
                                    ParseObject temp = new ParseObject("Tasks");
                                    temp.put("Name", rota_name);
                                    temp.put("Owner", rotaparticipants.get(i%number));
                                    temp.put("dateDue", c1.getTime());
                                    temp.put("Completed", false);
                                    temp.saveInBackground();
                                    c1.add(Calendar.DATE, 21);
                                    i++;
                                }
                                break;
                            case "Every 4 Weeks":
                                while (c1.before(c2) || c1.equals(c2)){
                                    Log.d("Iteration", "of calendar");
                                    ParseObject temp = new ParseObject("Tasks");
                                    temp.put("Name", rota_name);
                                    temp.put("Owner", rotaparticipants.get(i%number));
                                    temp.put("dateDue", c1.getTime());
                                    temp.put("Completed", false);
                                    temp.saveInBackground();
                                    c1.add(Calendar.DATE, 28);
                                    i++;
                                }
                                break;
                            case "Monthly":
                                while (c1.before(c2) || c1.equals(c2)){
                                    Log.d("Iteration", "of calendar");
                                    ParseObject temp = new ParseObject("Tasks");
                                    temp.put("Name", rota_name);
                                    temp.put("Owner", rotaparticipants.get(i%number));
                                    temp.put("dateDue", c1.getTime());
                                    temp.put("Completed", false);
                                    temp.saveInBackground();
                                    c1.add(Calendar.MONTH, 1);
                                    i++;
                                }
                                break;
                            default:
                                break;
                        }

                    }

                newRota.saveInBackground();

                }}});

        this.finish();
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
        year2 = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        month2 = c.get(Calendar.MONTH);
        int temp_month = month + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        day2 = c.get(Calendar.DAY_OF_MONTH);
        //month++;                // Added because month is zero based
        String b = "/";
        String currentDate = String.valueOf(day) + b + String.valueOf(temp_month) + b + String.valueOf(year);

        StartDateButton.setText(currentDate);
        EndDateButton.setText(currentDate);

    }

    public void addListenerOnDateItems() {
        StartDateButton = (Button) findViewById(R.id.start_date_button_rota);
        EndDateButton = (Button) findViewById(R.id.end_date_button_rota);

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
                return new DatePickerDialog(this, datePickerListener, year2, month2,
                        day2);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            StartDateButton = (Button) findViewById(R.id.start_date_button_rota);
            EndDateButton = (Button) findViewById(R.id.end_date_button_rota);


            if(cur == START_DATE_DIALOG_ID){
                // set selected date into textview
                year = selectedYear;
                month = selectedMonth;
                int temp_month2 = month + 1;
                day = selectedDay;
                Log.d("onDateSet:", "start date");
                String b = "/";
                String Date = String.valueOf(day) + b + String.valueOf(temp_month2) + b + String.valueOf(year);

                StartDateButton.setText(Date);

            }
            else{
                year2 = selectedYear;
                month2 = selectedMonth;
                int temp_month2 = month2 + 1;
                day2 = selectedDay;
                Log.d("onDateSet:", "end date");
                String b = "/";
                String Date = String.valueOf(day2) + b + String.valueOf(temp_month2) + b + String.valueOf(year2);
                EndDateButton.setText(Date);
            }

        }
    };


}
