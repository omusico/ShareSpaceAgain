package com.salilgokhale.sharespace3.Home;


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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragment;
import com.salilgokhale.sharespace3.MainActivity;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.SpinnerListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddNewTaskActivity extends ActionBarActivity {
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        getSupportActionBar().setTitle("Add New Task");

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        month++;                // Added because month is one behind for some reason
        String b = "/";
        String currentDate = String.valueOf(day) + b + String.valueOf(month) + b + String.valueOf(year);
        Button DateButton = (Button) findViewById(R.id.date_button);
        DateButton.setText(currentDate);

        addItemsonSpinner();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_task, menu);
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

    public void createNewTask(View view) {
        final EditText et = (EditText) findViewById(R.id.task_name_edit_text);
        Button DateButton = (Button) findViewById(R.id.date_button);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");


        final String userinput = DateButton.getText().toString();

        String Owner = String.valueOf(spinner.getSelectedItem());

        try {


            Date date1 = formatter.parse(userinput);

            Calendar calendar = Calendar.getInstance();
            Calendar c0 = Calendar.getInstance();
            c0.add(Calendar.DATE, -1);
            calendar.setTime(date1);
            calendar.set(Calendar.HOUR_OF_DAY, 1);
            final Date date = calendar.getTime();

            if (c0.before(calendar) && !et.getText().toString().equals("")) {


                ParseUser user = ParseUser.getCurrentUser();
                ParseObject userHouse = user.getParseObject("Home");

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("Home", userHouse);
                query.whereEqualTo("name", Owner);
                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> userList, com.parse.ParseException e) {
                        if (userList != null) {


                            ParseObject newTask = new ParseObject("Tasks");
                            newTask.put("Name", et.getText().toString());
                            newTask.put("dateDue", date);


                            newTask.put("Owner", userList.get(0));
                            newTask.put("Completed", false);

                            newTask.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (e == null) {
                                        CloseActivity();
                                    } else {
                                        Log.d("Save: ", "Failed");
                                    }
                                }
                            });


                            Log.d("Date:", userinput);


                        }
                    }
                });

            }
            else
            {
                if (!c0.before(calendar)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Date can't be in past", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Task must have name", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }   catch (ParseException e2) {
            e2.printStackTrace();
            Log.d("Date:", "Error");
        }

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addItemsonSpinner(){
        Log.d("Function:", "Entered addItemsonSpinner");
        spinner = (Spinner) findViewById(R.id.owner_spinner);

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

    public void CloseActivity(){
        this.finish();
    }

    }

