package com.salilgokhale.sharespace3.Rotas;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.MainActivity;
import com.salilgokhale.sharespace3.R;

import java.util.ArrayList;
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

        //noinspection SimplifiableIfStatement
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_delete_rota:
                Intent intent = this.getIntent();
                String Rota_Name = intent.getStringExtra(Intent.EXTRA_TEXT);
                ParseUser user = ParseUser.getCurrentUser();

                ParseQuery<ParseObject> query4 = ParseQuery.getQuery("Rota");
                query4.whereEqualTo("Name", Rota_Name);
                query4.whereEqualTo("peopleInvolved", user);
                query4.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(final ParseObject object, ParseException e) {
                        if (object == null) {
                            Log.d("Rota:", "Not Found");
                        }
                        else {
                            ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Tasks");
                            query3.whereEqualTo("parentRota", object);
                            query3.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, ParseException e) {
                                    for (int i = 0; i < list.size(); i++){
                                        list.get(i).deleteInBackground();
                                    }
                                    object.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null){
                                                CloseActivity();
                                            }
                                        }
                                    });

                                }});
                        }}});

                return true;

            case R.id.action_logout:
                Intent logOutIntent = new Intent(this, MainActivity.class);
                //homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logOutIntent);
                return true;
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

                        Log.d("Next Person Object ID", object.getParseObject("nextPerson").getObjectId());

                        // TODO Implement cloud function call here to send a push

                        /*// Find users near a given location
                        ParseQuery userQuery = ParseUser.getQuery();
                        userQuery.whereEqualTo("objectID", object.getParseObject("nextPerson").getObjectId());

                        // Find devices associated with these users
                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereMatchesQuery("User", userQuery);

                        // Send push notification to query
                        ParsePush push = new ParsePush();
                        push.setQuery(pushQuery); // Set our Installation query
                        push.setMessage(Rota_Name);
                        push.sendInBackground(); */

                        object.put("Due", true);
                        object.saveInBackground();

                    }
                }}});


    }

    public void CloseActivity(){
        this.finish();
    }

}
