package com.salilgokhale.sharespace3.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragment;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragment4;
import com.salilgokhale.sharespace3.MainActivity;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.SpinnerListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

        final EditText et = (EditText) findViewById(R.id.task_name_edit_text2);
        final Button DateButton = (Button) findViewById(R.id.date_button_edit_task);

        ParseUser user = ParseUser.getCurrentUser();

        Intent intent = this.getIntent();
        String taskid = intent.getStringExtra(Intent.EXTRA_TEXT);
        //et.setText(tasknamestr);

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Tasks");
        query1.whereEqualTo("objectId", taskid);
        //query1.whereEqualTo("Owner", user);
        query1.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("Task", "No object found");

                } else {
                    Log.d("Task", "object found");
                    Date date = (Date) object.get("dateDue");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                    String taskdate = formatter.format(date);

                    et.setText(object.getString("Name"));

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

    public void editTask(View view){

        final EditText et = (EditText) findViewById(R.id.task_name_edit_text2);
        final Button DateButton = (Button) findViewById(R.id.date_button_edit_task);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        final String userinput = DateButton.getText().toString();

        Intent intent = this.getIntent();
        //if(null != intent && intent.hasExtra(Intent.EXTRA_TEXT)){
        String taskid = intent.getStringExtra(Intent.EXTRA_TEXT);
        //}

        final String Owner = String.valueOf(spinner.getSelectedItem());
        Log.d("New Owner", Owner);
        final ParseUser user = ParseUser.getCurrentUser();
        final ParseObject userHouse = user.getParseObject("Home");

        try {
            Date date = formatter.parse(userinput);
            Calendar calendar = Calendar.getInstance();
            Calendar c0 = Calendar.getInstance();
            c0.add(Calendar.DATE, -1);
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 1);
            date = calendar.getTime();
            final Date date1 = date;

            if(c0.before(calendar) && !et.getText().toString().equals("")) {


                ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
                query.whereEqualTo("objectId", taskid);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(final ParseObject object, ParseException e) {
                        if (object == null) {
                            Log.d("Task", "No object found");

                        } else {
                            Log.d("Task", "object found");

                            //Button DateButton = (Button) findViewById(R.id.date_button_edit_task);


                            ParseQuery<ParseUser> query3 = ParseUser.getQuery();
                            query3.whereEqualTo("Home", userHouse);
                            query3.whereEqualTo("name", Owner);
                            query3.findInBackground(new FindCallback<ParseUser>() {
                                public void done(List<ParseUser> userList, com.parse.ParseException e) {
                                    if (userList != null) {

                                        object.put("Name", et.getText().toString());
                                        object.put("dateDue", date1);
                                        object.put("Owner", userList.get(0));

                                        if(!userList.get(0).equals(user)){
                                            HashMap<String, Object> params = new HashMap<String, Object>();
                                            params.put("owner", userList.get(0).getObjectId());
                                            params.put("sender", user.getString("name"));

                                            ParseCloud.callFunctionInBackground("TaskNotify", params, new FunctionCallback<String>() {
                                                public void done(String result, com.parse.ParseException e) {
                                                    if (e == null) {
                                                        // result is "Hello world!"
                                                        Log.d("Result is: ", result);
                                                    }
                                                }
                                            });

                                        }

                                        object.saveInBackground(new SaveCallback() {
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
                    }
                });
            }
            else {
                if (!c0.before(calendar)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Date can't be in past", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Task must have name", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }

        catch(java.text.ParseException e1){
            e1.printStackTrace();
            Log.d("Date:", "Error");
        }



    }

    public void showDatePickerDialog3(View v) {
        DialogFragment newFragment = new DatePickerFragment4();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addItemsonSpinner(){
        Log.d("Function:", "Entered addItemsonSpinner");
        spinner = (Spinner) findViewById(R.id.owner_spinner_edit_task);

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
                    spinner.setAdapter(dataAdapter);
                    spinner.setOnItemSelectedListener(new SpinnerListener());
                }
            }
        });

    }

    public void CloseActivity(){
        this.finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

}
