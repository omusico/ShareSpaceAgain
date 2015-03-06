package com.salilgokhale.sharespace3;


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

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void createNewTask(View view){
        EditText et = (EditText) findViewById(R.id.task_name_edit_text);
        Button DateButton = (Button) findViewById(R.id.date_button);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        String userinput = DateButton.getText().toString();
        try {
            Date date = formatter.parse(userinput);

            ParseObject newTask = new ParseObject("Tasks");
            newTask.put("Name", et.getText().toString());
            newTask.put("dateDue", date);
            ParseUser user = ParseUser.getCurrentUser();
            newTask.put("Owner", user);
            newTask.put("Completed", false);

            newTask.saveInBackground();

            GoToCore();



            Log.d("Date:", userinput);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Date:", "Error");
        }



        /*ParseObject newTask = new ParseObject("Tasks");
        newTask.put("Name", et.getText().toString());

        ParseUser user = ParseUser.getCurrentUser();
        newTask.put("Owner", user);
        newTask.put("Completed", false);

        newTask.saveInBackground();

        GoToCore();*/

    }

    public void GoToCore(){
        Intent intent = new Intent(this, CoreActivity.class);
        startActivity(intent);
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
/*
        Log.d("Function:", "Entered addItemsonSpinner");
        spinner = (Spinner) findViewById(R.id.owner_spinner);

        ParseUser user = ParseUser.getCurrentUser();
        ParseObject userHouse = user.getParseObject("Home");


        if (userHouse != null) {
            Log.d("Home Object id:", userHouse.getObjectId());
            //Log.d("Home Object Name:", userHouse.getString("Name"));
        }
        else{
            Log.d("Error", "Error");
        }
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);                           // NO PROBLEM WITH SPINNER
        query.findInBackground(new FindCallback<ParseUser>() {                        // ALL WITH THE QUERY
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (userList != null) {
                    Log.d("userList:", "Not Null");

                    int number = userList.size();
                    Log.d("userListsize:", String.valueOf(number));
                    String[] userArray = new String [number];

                    for (int i = 0; i< number; i++){
                        userArray[i] = userList.get(i).getString("name");
                    }

                    final List<String> usersList = new ArrayList<>(Arrays.asList(userArray));
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, usersList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                    spinner.setOnItemSelectedListener(new SpinnerListener());
                }
                else{
                    Log.d("userList:", "Null");
                }
            }}); */

                }

            }
