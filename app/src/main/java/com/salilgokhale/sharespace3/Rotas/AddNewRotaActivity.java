package com.salilgokhale.sharespace3.Rotas;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragmentRotaE;
import com.salilgokhale.sharespace3.DatePickers.DatePickerFragmentRotaS;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.SpinnerListener;
import com.salilgokhale.sharespace3.Trade.TradeTaskFragments.DataHolderClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// TODO When new frequency rota is created, the next person is set to the first task. This means that the start date
// TODO cannot be in the past. Must add a block message to stop users setting the start date in the past.
// TODO Also there is the date picker bug - must pick start date and then end date otherwise it messes up.

public class AddNewRotaActivity extends ActionBarActivity {
    private Spinner spinner;
    private Button StartDateButton;
    private Button EndDateButton;

    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_rota);
        getSupportActionBar().setTitle("Add New Rota");

        addCheckBoxItems();
        addSpinnerItems();
        setDateItems();
        //addCheckBoxItemListener();

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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void createNewRota(View view){
        final EditText et = (EditText) findViewById(R.id.rota_name_edit_text);
        final String rota_name = et.getText().toString();
        final ListView listView = (ListView) findViewById(R.id.rota_members_checkboxes);

        final ParseUser user = ParseUser.getCurrentUser();

        ParseObject userHouse = user.getParseObject("Home");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (userList != null) {
                    List<ParseObject> rotaparticipants = new ArrayList<ParseObject>();
                    List<Integer> order = new ArrayList<Integer>();
                    List<Integer> position = new ArrayList<Integer>();

                    ParseObject newRota = new ParseObject("Rota");
                    newRota.put("Name", rota_name);
                    //ParseRelation<ParseObject> relation = newRota.getRelation("peopleInvolved");


                    String Frequency = String.valueOf(spinner.getSelectedItem());
                    newRota.put("Frequency", Frequency);

                    boolean empty = true;

                    for (int i = 0; i < userList.size(); i++) {
                        CheckBox feature = (CheckBox) listView.getChildAt(i).findViewById(R.id.rota_checkbox);
                        //TextView feature_t = (TextView) listView.getChildAt(i).findViewById(R.id.checkbox_order);
                        if (feature.isChecked()) {
                            empty = false;
                            break;
                        }
                    }


                    if (!rota_name.equals("") && !empty) {

                        if (Frequency.equals("When Needed")) {
                            /*
                            boolean firstTime = true;
                            for (int i = 0; i < userList.size(); i++) {
                                CheckBox feature = (CheckBox) findViewById(i);
                                if (feature.isChecked()) {
                                    relation.add(userList.get(i));
                                    if (firstTime) {
                                        newRota.put("nextPerson", userList.get(i));
                                        firstTime = false;
                                    }
                                }
                            } */

                            for (int i = 0; i < userList.size(); i++) {
                                CheckBox cBox1 = (CheckBox) listView.getChildAt(i).findViewById(R.id.rota_checkbox);
                                TextView t = (TextView) listView.getChildAt(i).findViewById(R.id.checkbox_order);
                                if (cBox1.isChecked()) {
                                    order.add(Integer.parseInt(t.getText().toString()) - 1);
                                    position.add(i);
                                    Log.d("Number in textView: ", t.getText().toString());
                                }
                            }

                            int[] Array;
                            int[] positionArray;

                            Array = new int[order.size()];
                            positionArray = new int[order.size()];
                            Log.d("Array Values", ":");

                            for (int x = 0; x < order.size(); x++) {
                                Array[order.get(x)] = x;
                                positionArray[order.get(x)] = position.get(x);
                                Log.d(String.valueOf(x), String.valueOf(order.get(x)));

                            }

                            newRota.put("nextPerson", userList.get(positionArray[0]));

                            for (int w = 0; w < order.size(); w++) {
                                //relation.add(userList.get(Array[w]));
                                rotaparticipants.add(userList.get(positionArray[w]));
                                Log.d("User Added: ", userList.get(positionArray[w]).getString("name"));
                            }

                            newRota.put("orderPeople", rotaparticipants);
                            newRota.put("Due", false);
                            newRota.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (e == null) {
                                        CloseActivity();
                                    } else {
                                        Log.d("Save: ", "Failed");
                                    }
                                }
                            });
                        } else {
                            /*for (int i = 0; i < userList.size(); i++) {
                                CheckBox feature = (CheckBox) findViewById(i);
                                if (feature.isChecked()) {
                                    rotaparticipants.add(userList.get(i));
                                    relation.add(userList.get(i));
                                }
                            } */

                            for (int i = 0; i < userList.size(); i++) {
                                CheckBox cBox1 = (CheckBox) listView.getChildAt(i).findViewById(R.id.rota_checkbox);
                                TextView t = (TextView) listView.getChildAt(i).findViewById(R.id.checkbox_order);
                                if (cBox1.isChecked()) {
                                    order.add(Integer.parseInt(t.getText().toString()) - 1);
                                    position.add(i);
                                    Log.d("Number in textView: ", t.getText().toString());
                                }
                            }

                            int[] Array;
                            int[] positionArray;

                            Array = new int[order.size()];
                            positionArray = new int[order.size()];
                            Log.d("Array Values", ":");

                            for (int x = 0; x < order.size(); x++) {
                                Array[order.get(x)] = x;
                                positionArray[order.get(x)] = position.get(x);
                                Log.d(String.valueOf(x), String.valueOf(order.get(x)));

                            }

                            //newRota.put("nextPerson", userList.get(positionArray[0]));

                            for (int w = 0; w < order.size(); w++) {
                                //relation.add(userList.get(Array[w]));
                                rotaparticipants.add(userList.get(positionArray[w]));
                                Log.d("User Added: ", userList.get(positionArray[w]).getString("name"));
                            }

                            newRota.put("orderPeople", rotaparticipants);

                            final String start_date = StartDateButton.getText().toString();
                            final String end_date = EndDateButton.getText().toString();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar c0 = Calendar.getInstance();
                            Calendar c1 = Calendar.getInstance();
                            Calendar c2 = Calendar.getInstance();
                            try {
                                c0.add(Calendar.DATE, -1);
                                c1.setTime(sdf.parse(start_date));
                                c1.add(Calendar.HOUR, 1);
                                c2.setTime(sdf.parse(end_date));
                                c2.add(Calendar.HOUR, 1);
                                Date startdate = c1.getTime();
                                Log.d("Start Date", startdate.toString());
                                Date enddate = c2.getTime();
                                Log.d("End Date", enddate.toString());
                                newRota.put("startDate", startdate);
                                newRota.put("endDate", enddate);
                            } catch (ParseException e2) {
                                e2.printStackTrace();
                            }

                            if (c1.before(c2) && c0.before(c1)) {

                                //newRota.saveInBackground();
                                int number = rotaparticipants.size();
                                int i = 0;
                                boolean firstTime = true;
                                switch (Frequency) {
                                    case "Every 1 Week":

                                        while (c1.before(c2) || c1.equals(c2)) {

                                            Log.d("Iteration", "of calendar");
                                            Date date = c1.getTime();
                                            ParseObject temp = new ParseObject("Tasks");
                                            temp.put("Name", rota_name);
                                            temp.put("Owner", rotaparticipants.get(i % number));
                                            temp.put("dateDue", date);
                                            temp.put("parentRota", newRota);
                                            temp.put("Completed", false);
                                            temp.saveInBackground();
                                            if (firstTime) {
                                                newRota.put("nextPerson", rotaparticipants.get(i % number));
                                                newRota.put("nextDate", date);
                                                //newRota.saveInBackground();
                                                firstTime = false;
                                            }
                                            c1.add(Calendar.DATE, 7);
                                            i++;
                                        }

                                        break;
                                    case "Every 2 Weeks":
                                        while (c1.before(c2) || c1.equals(c2)) {
                                            Log.d("Iteration", "of calendar");
                                            ParseObject temp = new ParseObject("Tasks");
                                            temp.put("Name", rota_name);
                                            temp.put("Owner", rotaparticipants.get(i % number));
                                            temp.put("dateDue", c1.getTime());
                                            temp.put("parentRota", newRota);
                                            temp.put("Completed", false);
                                            temp.saveInBackground();
                                            if (firstTime) {
                                                newRota.put("nextPerson", rotaparticipants.get(i % number));
                                                newRota.put("nextDate", c1.getTime());
                                                //newRota.saveInBackground();
                                                firstTime = false;
                                            }
                                            c1.add(Calendar.DATE, 14);
                                            i++;
                                        }
                                        break;
                                    case "Every 3 Weeks":
                                        while (c1.before(c2) || c1.equals(c2)) {
                                            Log.d("Iteration", "of calendar");
                                            ParseObject temp = new ParseObject("Tasks");
                                            temp.put("Name", rota_name);
                                            temp.put("Owner", rotaparticipants.get(i % number));
                                            temp.put("dateDue", c1.getTime());
                                            temp.put("parentRota", newRota);
                                            temp.put("Completed", false);
                                            temp.saveInBackground();
                                            if (firstTime) {
                                                newRota.put("nextPerson", rotaparticipants.get(i % number));
                                                newRota.put("nextDate", c1.getTime());
                                                //newRota.saveInBackground();
                                                firstTime = false;
                                            }
                                            c1.add(Calendar.DATE, 21);
                                            i++;
                                        }
                                        break;
                                    case "Every 4 Weeks":
                                        while (c1.before(c2) || c1.equals(c2)) {
                                            Log.d("Iteration", "of calendar");
                                            ParseObject temp = new ParseObject("Tasks");
                                            temp.put("Name", rota_name);
                                            temp.put("Owner", rotaparticipants.get(i % number));
                                            temp.put("dateDue", c1.getTime());
                                            temp.put("parentRota", newRota);
                                            temp.put("Completed", false);
                                            temp.saveInBackground();
                                            if (firstTime) {
                                                newRota.put("nextPerson", rotaparticipants.get(i % number));
                                                newRota.put("nextDate", c1.getTime());
                                                //newRota.saveInBackground();
                                                firstTime = false;
                                            }
                                            c1.add(Calendar.DATE, 28);
                                            i++;
                                        }
                                        break;
                                    case "Monthly":
                                        while (c1.before(c2) || c1.equals(c2)) {
                                            Log.d("Iteration", "of calendar");
                                            ParseObject temp = new ParseObject("Tasks");
                                            temp.put("Name", rota_name);
                                            temp.put("Owner", rotaparticipants.get(i % number));
                                            temp.put("dateDue", c1.getTime());
                                            temp.put("parentRota", newRota);
                                            temp.put("Completed", false);
                                            temp.saveInBackground();
                                            if (firstTime) {
                                                newRota.put("nextPerson", rotaparticipants.get(i % number));
                                                newRota.put("nextDate", c1.getTime());
                                                //newRota.saveInBackground();
                                                firstTime = false;
                                            }
                                            c1.add(Calendar.MONTH, 1);
                                            i++;
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                newRota.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(com.parse.ParseException e) {
                                        if (e == null) {
                                            CloseActivity();
                                        }
                                    }
                                });


                            } else {
                                if (!c1.before(c2)) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Start Date can't be after End Date", Toast.LENGTH_LONG);
                                    toast.show();
                                } else if (!c0.before(c1)) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Start Date can't be in past", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        }
                    } else {
                        if (rota_name.equals("")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Rota must have name", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        if (empty) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Rota must have members", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }


                }
            }
        });
    }

    public void addCheckBoxItems() {
        ParseUser user = ParseUser.getCurrentUser();
        Log.d("Current User", user.getString("username"));
        final ParseObject userHouse = user.getParseObject("Home");

        DataHolderClass.getInstance().setAnInt(1);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(final List<ParseUser> userList, com.parse.ParseException e) {
                if (userList != null) {
                    ArrayList<String> list = new ArrayList<>();

                    final ListView featuresTable = (ListView) findViewById(R.id.rota_members_checkboxes);

                    for (int i = 0; i < userList.size(); i++) {
                        list.add(userList.get(i).getString("name"));
                        Log.d("User Name:", userList.get(i).getString("name"));

                    }

                    RotaOrderAdapter rotaOrderAdapter = new RotaOrderAdapter(getApplicationContext(), list);
                    featuresTable.setAdapter(rotaOrderAdapter);


                }
            }
        });
    }

    public void addSpinnerItems(){
        spinner = (Spinner) findViewById(R.id.rota_frequency_spinner);
        StartDateButton = (Button) findViewById(R.id.start_date_button_rota);
        EndDateButton = (Button) findViewById(R.id.end_date_button_rota);

        String[] frequency_items = getResources().getStringArray(R.array.frequency_options);
        final List<String> frequencyList = new ArrayList<>(Arrays.asList(frequency_items));

        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, frequencyList);
        freqAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(freqAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    StartDateButton.setEnabled(false);
                    EndDateButton.setEnabled(false);
                } else {
                    StartDateButton.setEnabled(true);
                    EndDateButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setDateItems(){

        StartDateButton = (Button) findViewById(R.id.start_date_button_rota);
        EndDateButton = (Button) findViewById(R.id.end_date_button_rota);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        //year2 = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        //month2 = c.get(Calendar.MONTH);
        int temp_month = month + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        //day2 = c.get(Calendar.DAY_OF_MONTH);
        //month++;                // Added because month is zero based
        String b = "/";
        String currentDate = String.valueOf(day) + b + String.valueOf(temp_month) + b + String.valueOf(year);

        StartDateButton.setText(currentDate);
        EndDateButton.setText(currentDate);

    }

    public void showRotaStartDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragmentRotaS();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showRotaEndDatePickerDialog(View v1) {
        DialogFragment newFragment2 = new DatePickerFragmentRotaE();
        newFragment2.show(getSupportFragmentManager(), "datePicker");
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
