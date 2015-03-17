package com.salilgokhale.sharespace3;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by salilgokhale on 06/03/15.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String b = "/";
        String userinput = String.valueOf(day) + b + String.valueOf(month+1) + b + String.valueOf(year);
        Button DateButton2 = (Button) getActivity().findViewById(R.id.date_button);
        DateButton2.setText(userinput);
        /*try {
            Date date = formatter.parse(userinput);



            Log.d("Date:", userinput);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Date:", "Error");
        }*/
    }
}
