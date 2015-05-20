package com.salilgokhale.sharespace3.Rotas;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.R;

import java.util.Calendar;
import java.util.List;


public class ViewFreqRotaActivity extends ActionBarActivity {
    //CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_freq_rota);

        //initialiseCalendar();

        Intent intent = this.getIntent();
        String Rota_Name = intent.getStringExtra(Intent.EXTRA_TEXT);
        String Title = Rota_Name + " Rota";
        getSupportActionBar().setTitle(Title);

        ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> innerquery = ParseQuery.getQuery("Rota");
        innerquery.whereEqualTo("Name", Rota_Name);
        innerquery.whereEqualTo("peopleInvolved", user);
        //query.include("nextPerson");
        ParseQuery<ParseObject> outerquery = ParseQuery.getQuery("Tasks");
        outerquery.whereMatchesQuery("Rota", innerquery);
        outerquery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> rota_tasks, ParseException e) {
                if (rota_tasks == null) {
                    Log.d("Rota:", "Not Found");
                } else {


                }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public void initialiseCalendar(){
        calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setShowWeekNumber(true);
        calendar.setFirstDayOfWeek(2);
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
        calendar.setSelectedDateVerticalBar(R.color.darkgreen);
        calendar.setOnDateChangeListener(new OnDateChangeListener() {
                    //show the selected date as a toast
            @Override

            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();

            }

        });
*/

    }





