package com.salilgokhale.sharespace3;

import android.content.Context;
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
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;


public class EditTaskActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        EditText et = (EditText) findViewById(R.id.task_name_edit_text);
        ParseUser user = ParseUser.getCurrentUser();

        Intent intent = this.getIntent();
        //if(null != intent && intent.hasExtra(Intent.EXTRA_TEXT)){
            String tasknamestr = intent.getStringExtra(Intent.EXTRA_TEXT);
            et.setText(tasknamestr);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
            query.whereEqualTo("Name", tasknamestr);
            query.whereEqualTo("Owner", user);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object == null) {
                        Log.d("Task", "No object found");

                    } else {
                        Log.d("Task", "object found");
                        Date date = (Date) object.get("dateDue");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                        String taskdate = formatter.format(date);

                        Button DateButton = (Button) findViewById(R.id.date_button);
                        DateButton.setText(taskdate);
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

    public void createNewTask(View view){


        Intent intent = this.getIntent();
        //if(null != intent && intent.hasExtra(Intent.EXTRA_TEXT)){
            String previoustaskname = intent.getStringExtra(Intent.EXTRA_TEXT);
        //}

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("Name", previoustaskname);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("Task", "No object found");

                } else {
                    Log.d("Task", "object found");

                    EditText et = (EditText) findViewById(R.id.task_name_edit_text);
                    Button DateButton = (Button) findViewById(R.id.date_button);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

                    String userinput = DateButton.getText().toString();
                    try {
                        Date date = formatter.parse(userinput);


                        object.put("Name", et.getText().toString());
                        object.put("dateDue", date);


                        object.saveInBackground();

                        GoToCore();



                        Log.d("Date:", userinput);

                    } catch (java.text.ParseException e1) {
                        e.printStackTrace();
                        Log.d("Date:", "Error");
                    }






                }
            }
        });







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