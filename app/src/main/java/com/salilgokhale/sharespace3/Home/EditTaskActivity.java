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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragment;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragment4;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.SpinnerListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
// TODO
// Add owner spinner functionality

public class EditTaskActivity extends ActionBarActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        getSupportActionBar().setTitle("Edit Task");

        EditText et = (EditText) findViewById(R.id.task_name_edit_text2);
        final Button DateButton = (Button) findViewById(R.id.date_button_edit_task);

        ParseUser user = ParseUser.getCurrentUser();

        Intent intent = this.getIntent();
        String tasknamestr = intent.getStringExtra(Intent.EXTRA_TEXT);
        et.setText(tasknamestr);

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Tasks");
        query1.whereEqualTo("Name", tasknamestr);
        query1.whereEqualTo("Owner", user);
        query1.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("Task", "No object found");

                } else {
                    Log.d("Task", "object found");
                    Date date = (Date) object.get("dateDue");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String taskdate = formatter.format(date);


                    DateButton.setText(taskdate);

                    addItemsonSpinner();

                }

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
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

    public void editTask(View view){

        final EditText et = (EditText) findViewById(R.id.task_name_edit_text2);
        final Button DateButton = (Button) findViewById(R.id.date_button_edit_task);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        final String userinput = DateButton.getText().toString();

        Intent intent = this.getIntent();
        //if(null != intent && intent.hasExtra(Intent.EXTRA_TEXT)){
        String previoustaskname = intent.getStringExtra(Intent.EXTRA_TEXT);
        //}

        final String Owner = String.valueOf(spinner.getSelectedItem());
        Log.d("New Owner", Owner);
        ParseUser user = ParseUser.getCurrentUser();
        final ParseObject userHouse = user.getParseObject("Home");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("Name", previoustaskname);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("Task", "No object found");

                } else {
                    Log.d("Task", "object found");

                    //Button DateButton = (Button) findViewById(R.id.date_button_edit_task);



                    try {
                        Date date = formatter.parse(userinput);
                        Calendar calendar = Calendar.getInstance();

                        calendar.setTime(date);
                        calendar.set(Calendar.HOUR_OF_DAY, 1);
                        date = calendar.getTime();
                        final Date date1 = date;

                        ParseQuery<ParseUser> query3 = ParseUser.getQuery();
                        query3.whereEqualTo("Home", userHouse);
                        query3.whereEqualTo("name", Owner);
                        query3.findInBackground(new FindCallback<ParseUser>() {
                            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                                if (userList != null) {

                                    object.put("Name", et.getText().toString());
                                    object.put("dateDue", date1);
                                    object.put("Owner", userList.get(0));
                                    object.saveInBackground();

                                    Log.d("Date:", userinput);

                                }}});

                            }

                            catch(
                            java.text.ParseException e1
                            )

                            {
                                e.printStackTrace();
                                Log.d("Date:", "Error");
                            }
                        }
                    }
        });



    this.finish();



    }

    public void showDatePickerDialog3(View v) {
        DialogFragment newFragment = new DatePickerFragment4();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addItemsonSpinner(){
        Log.d("Function:", "Entered addItemsonSpinner");
        spinner = (Spinner) findViewById(R.id.owner_spinner_edit_task);

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
