package com.salilgokhale.sharespace3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ViewRotaActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rota);

        Intent intent = this.getIntent();
        String Rota_Name = intent.getStringExtra(Intent.EXTRA_TEXT);
        String Title = Rota_Name + " Rota";
        getSupportActionBar().setTitle(Title);

        ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rota");
        query.whereEqualTo("Name", Rota_Name);
        query.whereEqualTo("peopleInvolved", user);
        query.include("nextPerson");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("Rota:", "Not Found");
                }
                else {
                    Button DueButton = (Button) findViewById(R.id.rota_status_button);
                    if (object.getBoolean("Due")){
                        DueButton.setBackgroundColor(0xffff0000);
                    }
                    else {
                        DueButton.setBackgroundColor(0xff00ff00);
                    }

                    final ParseObject nextPerson = object.getParseObject("nextPerson");

                    ParseRelation relation = object.getRelation("peopleInvolved");
                    ParseQuery query2 = relation.getQuery();
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            List <String> peopleinvolvedarray = new ArrayList<>();
                            boolean [] turn = new boolean[list.size()];

                            for (int i = 0; i < list.size(); i++){

                                Log.d("People Involved:", list.get(i).getString("name"));
                                peopleinvolvedarray.add(list.get(i).getString("name"));
                                if (nextPerson.getObjectId().equals(list.get(i).getObjectId())){
                                    turn[i] = true;
                                }
                                else{
                                    turn[i] = false;
                                }
                            }

                            ListView listView = (ListView) findViewById(R.id.rota_participants);
                            RotaParticipantAdapter adapter = new RotaParticipantAdapter(getApplicationContext(), turn, peopleinvolvedarray);
                            listView.setAdapter(adapter);

                        }
                    });

                }

    }});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_rota, menu);
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

    public void changeRotaStatus(View view){

        ParseUser user = ParseUser.getCurrentUser();
        Intent intent = this.getIntent();
        final String Rota_Name = intent.getStringExtra(Intent.EXTRA_TEXT);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rota");
        query.whereEqualTo("Name", Rota_Name);
        query.whereEqualTo("peopleInvolved", user);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("Rota:", "Not Found");
                }
                else {
                    Button DueButton = (Button) findViewById(R.id.rota_status_button);
                    if (object.getBoolean("Due")){
                        //DueButton.setBackgroundColor(0xff00ff00);
                        //object.put("Due", false);
                        //object.saveInBackground();
                    }
                    else{
                        DueButton.setBackgroundColor(0xffff0000);
                        Date date = new Date();

                        ParseObject newTask = new ParseObject("Tasks");

                        newTask.put("Name", Rota_Name);
                        newTask.put("dateDue", date);


                        newTask.put("Owner", object.getParseObject("nextPerson"));
                        newTask.put("Completed", false);
                        newTask.put("parentRota", object);
                        newTask.saveInBackground();

                        object.put("Due", true);
                        object.saveInBackground();

                    }
                }}});


    }



}
