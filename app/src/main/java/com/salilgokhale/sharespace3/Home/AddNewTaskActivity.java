package com.salilgokhale.sharespace3.Home;


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

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragment;
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createNewTask(View view) {
        final EditText et = (EditText) findViewById(R.id.task_name_edit_text);
        Button DateButton = (Button) findViewById(R.id.date_button);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");


        final String userinput = DateButton.getText().toString();

        String Owner = String.valueOf(spinner.getSelectedItem());

        ParseUser user = ParseUser.getCurrentUser();
        ParseObject userHouse = user.getParseObject("Home");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);
        query.whereEqualTo("name", Owner);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (userList != null) {
                    try {

                        Date date = formatter.parse(userinput);

                        Calendar calendar = Calendar.getInstance();

                        calendar.setTime(date);
                        calendar.set(Calendar.HOUR_OF_DAY, 1);
                        date = calendar.getTime();

                        ParseObject newTask = new ParseObject("Tasks");
                        newTask.put("Name", et.getText().toString());
                        newTask.put("dateDue", date);


                        newTask.put("Owner", userList.get(0));
                        newTask.put("Completed", false);

                        newTask.saveInBackground();



                        Log.d("Date:", userinput);

                    } catch (ParseException e2) {
                        e2.printStackTrace();
                        Log.d("Date:", "Error");
                    }

                }
            }
        });

        this.finish();
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
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                    spinner.setOnItemSelectedListener(new SpinnerListener());
                    }
                }});

        }

    }