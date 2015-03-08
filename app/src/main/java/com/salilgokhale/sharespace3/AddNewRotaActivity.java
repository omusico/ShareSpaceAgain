package com.salilgokhale.sharespace3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddNewRotaActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_rota);

        addCheckBoxItems();



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

        ParseObject newRota = new ParseObject("Rota");
        newRota.put("Name", et.getText().toString());
        newRota.saveInBackground();

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
                        featuresTable.addView(feature);
                    }



                }

            }
        });
    }



}
