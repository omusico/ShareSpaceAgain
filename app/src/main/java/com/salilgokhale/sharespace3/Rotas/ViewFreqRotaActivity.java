package com.salilgokhale.sharespace3.Rotas;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.MainActivity;
import com.salilgokhale.sharespace3.R;
import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class ViewFreqRotaActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        final String Rota_ID = intent.getStringExtra(Intent.EXTRA_TEXT);
        //String Title = Rota_Name + " Rota";
        getSupportActionBar().setTitle("");

        final ParseUser user = ParseUser.getCurrentUser();
        ParseObject userhouse = user.getParseObject("Home");

        ParseQuery<ParseUser> userquery = ParseUser.getQuery();
        userquery.whereEqualTo("Home", userhouse);
        userquery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(final List<ParseUser> parseUsers, ParseException e) {

                ParseQuery<ParseObject> innerquery = ParseQuery.getQuery("Rota");
                innerquery.whereEqualTo("objectId", Rota_ID);
                innerquery.whereEqualTo("peopleInvolved", user);
                ParseQuery<ParseObject> outerquery = ParseQuery.getQuery("Tasks");
                outerquery.whereMatchesQuery("parentRota", innerquery);
                outerquery.whereEqualTo("Completed", false);
                outerquery.include("parentRota");
                outerquery.include("Owner");
                outerquery.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> rota_tasks, ParseException e) {
                        if (rota_tasks == null) {
                            Log.d("Rota:", "Not Found");
                        } else {

                            int[] ColourArray = {
                                    5, 4, 3, 2, 1
                            };
                            int k = 5;
                            ArrayList<String> Names = new ArrayList<String>();
                            ArrayList<Integer> Colours = new ArrayList<Integer>();

                            for (int h = 0; h < parseUsers.size(); h++) {
                                Names.add(parseUsers.get(h).getString("name"));
                                Colours.add(ColourArray[h]);
                            }

                            getSupportActionBar().setTitle(rota_tasks.get(0).getParseObject("parentRota").getString("Name") + " Rota");
                            setContentView(R.layout.activity_view_freq_rota);

                            ExtendedCalendarView calendar = (ExtendedCalendarView) findViewById(R.id.calendar);

                            getContentResolver().delete(CalendarProvider.CONTENT_URI, null, null);

                            for (int i = 0; i < rota_tasks.size(); i++) {
                                ParseObject task_event = rota_tasks.get(i);
                                ParseUser task_user = task_event.getParseUser("Owner");

                                Calendar cal = Calendar.getInstance();
                                TimeZone tz = TimeZone.getDefault();

                                Date date = task_event.getDate("dateDue");
                                cal.setTime(date);
                                int startday = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));

                                ContentValues values1 = new ContentValues();

                                for (int j = 0; j < parseUsers.size(); j++) {
                                    if (task_user.getObjectId().equals(parseUsers.get(j).getObjectId())) {
                                        k = j;
                                        Log.d("Parse User Matches", String.valueOf(k));
                                    }
                                }

                                values1.put(CalendarProvider.COLOR, ColourArray[k]);


                                values1.put(CalendarProvider.DESCRIPTION, "Some Description2");
                                values1.put(CalendarProvider.LOCATION, "Some location2");
                                values1.put(CalendarProvider.EVENT, "Event name2");

                                values1.put(CalendarProvider.START, cal.getTimeInMillis());
                                values1.put(CalendarProvider.START_DAY, startday);

                                values1.put(CalendarProvider.END, cal.getTimeInMillis());
                                values1.put(CalendarProvider.END_DAY, startday);

                                Uri uri1 = getContentResolver().insert(CalendarProvider.CONTENT_URI, values1);

                            }

                            ListView listView = (ListView) findViewById(R.id.key_list);
                            KeyAdapter adapter = new KeyAdapter(getApplicationContext(), Names, Colours);
                            listView.setAdapter(adapter);

                        }
                    }
                });

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_freq_rota, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_delete_freq_rota:
                Intent intent = this.getIntent();
                String Rota_ID = intent.getStringExtra(Intent.EXTRA_TEXT);
                ParseUser user = ParseUser.getCurrentUser();

                ParseQuery<ParseObject> query4 = ParseQuery.getQuery("Rota");
                query4.whereEqualTo("objectId", Rota_ID);
                query4.whereEqualTo("peopleInvolved", user);
                query4.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(final ParseObject object, ParseException e) {
                        if (object == null) {
                            Log.d("Rota:", "Not Found");
                        } else {
                            ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Tasks");
                            query3.whereEqualTo("parentRota", object);
                            query3.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, ParseException e) {
                                    for (int i = 0; i < list.size(); i++) {
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

                                }
                            });
                        }
                    }
                });

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

    public void CloseActivity(){
        this.finish();
    }
}





