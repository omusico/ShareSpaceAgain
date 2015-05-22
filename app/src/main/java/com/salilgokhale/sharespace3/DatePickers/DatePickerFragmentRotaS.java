package com.salilgokhale.sharespace3.DatePickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import com.salilgokhale.sharespace3.R;

import java.util.Calendar;

/**
 * Created by salilgokhale on 22/05/15.
 */
public class DatePickerFragmentRotaS extends DialogFragment
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
        Button DateButton = (Button) getActivity().findViewById(R.id.start_date_button_rota);
        DateButton.setText(userinput);
        /*try {
            Date date = formatter.parse(userinput);



            Log.d("Date:", userinput);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Date:", "Error");
        }*/
    }
}