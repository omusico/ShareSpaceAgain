package com.salilgokhale.sharespace3.ExpensesTabsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.ExpenseLogAdapter;
import com.salilgokhale.sharespace3.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by salilgokhale on 17/03/15.
 */
public class ExpenseLogFragment extends Fragment {

    public ExpenseLogFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_expenselog, container, false);

        ParseUser user = ParseUser.getCurrentUser();
        ParseObject userHouse = user.getParseObject("Home");

        //Could perform query = where user is payer OR where user is in split with this code http://stackoverflow.com/questions/24713002/android-how-to-combine-two-parse-queries-using-or

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ExpenseLog");
        query.whereEqualTo("House", userHouse);
        query.include("Payer");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> ExpenseLogList, ParseException e) {
                if (ExpenseLogList != null) {
                    List<String> titles = new ArrayList<String>();
                    List<String> payers = new ArrayList<String>();
                    List<String> values = new ArrayList<String>();
                    List<String> dates = new ArrayList<String>();

                    for (int i = 0; i < ExpenseLogList.size(); i++){

                        titles.add(ExpenseLogList.get(i).getString("Title"));
                        payers.add(ExpenseLogList.get(i).getParseObject("Payer").getString("name"));
                        values.add(String.valueOf(ExpenseLogList.get(i).getNumber("Amount")));
                        Log.d("Payer name: ", payers.get(i));
                        DateFormat df = new SimpleDateFormat("d/M/yy");
                        String date = df.format(ExpenseLogList.get(i).getDate("Date"));
                        dates.add(date);

                    }

                    ListView listView = (ListView) rootView.findViewById(R.id.expense_logs);
                    ExpenseLogAdapter adapter = new ExpenseLogAdapter(getActivity(), titles, payers, values, dates);
                    listView.setAdapter(adapter);


                }
            }});
        return rootView;
    }

}
