package com.salilgokhale.sharespace3;


import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddNewTaskActivity extends ActionBarActivity {



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
}
