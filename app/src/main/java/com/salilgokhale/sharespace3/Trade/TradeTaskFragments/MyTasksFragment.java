package com.salilgokhale.sharespace3.Trade.TradeTaskFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.Expenses.ExpenseObject;
import com.salilgokhale.sharespace3.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by salilgokhale on 26/03/15.
 */
public class MyTasksFragment extends Fragment {

    public MyTasksFragment(){}

    List<String> savedIDs = new ArrayList<>();

    int tasklistsize = 0;

    // SectionHeaders
    private final static String[] headers = new String[]{"Exchange Arena", "Tasks"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        final View rootView = inflater.inflate(R.layout.fragment_trade_tasks, container, false);

       // if (savedInstanceState == null) {

            // Retrieve tasks from Parse User
            ParseUser user = ParseUser.getCurrentUser();

            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
            query.whereEqualTo("Owner", user);
            query.whereEqualTo("Completed", false);
            query.setLimit(10);
            query.orderByAscending("dateDue");
            query.include("parentRota.nextPerson");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(final List<ParseObject> taskList, ParseException e) {
                    if (taskList != null) {
                        Log.d("QueryTasks:", "Task found");

                        SimpleDateFormat fmt = new SimpleDateFormat("d/M");

                        int number = taskList.size();
                        tasklistsize = number;

                        ArrayList<ExpenseObject> EAobjects = new ArrayList<ExpenseObject>();
                        ArrayList<ExpenseObject> Tobjects = new ArrayList<ExpenseObject>();

                        for (int i = 0; i < number; i++) {
                            ParseObject temptask = taskList.get(i);
                            ExpenseObject expenseObject = new ExpenseObject(temptask.getString("Name"), fmt.format(temptask.getDate("dateDue")), temptask.getObjectId());
                            if (temptask.getBoolean("ExchangeArena")) {
                                EAobjects.add(expenseObject);
                            } else {
                                Tobjects.add(expenseObject);
                            }
                        }

                        TradeTaskSeparatedListAdapter adapter = new TradeTaskSeparatedListAdapter(getActivity());
                        TradeTaskAdapter EAadapter = new TradeTaskAdapter(getActivity(), EAobjects);
                        TradeTaskAdapter Tadapter = new TradeTaskAdapter(getActivity(), Tobjects);

                        adapter.addSection(headers[0], EAadapter);
                        adapter.addSection(headers[1], Tadapter);

                        ListView listView = (ListView) rootView.findViewById(R.id.mytradetasks);
                        listView.setAdapter(adapter);


                    }
                }
            });
        //}
        //else{
         //   boolean[] temp = savedInstanceState.getBooleanArray("boolArray");
        //    Log.d("First element:", String.valueOf(temp[0]));
        //}

        return rootView;
    }


    @Override
    public void onPause(){

        Log.d("Stop ", "Called");
        List<String> ischecked = new ArrayList<>();

        View rootView = getView();
        ListView listView = (ListView) rootView.findViewById(R.id.mytradetasks);
        //TODO use listview.getadapter to access the adapter and the actual objects.

        for (int i = 0; i < (tasklistsize+2); i++){
            if ((CheckBox) listView.getChildAt(i).findViewById(R.id.task_checkbox) != null) {
                Log.d("Found a checkbox", "yes");
                CheckBox cBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.task_checkbox);
                TextView textView = (TextView) listView.getChildAt(i).findViewById(R.id.task_id);
                if (cBox.isChecked()) {
                    ischecked.add(textView.getText().toString());
                }
            }

        }
        DataHolderClass.getInstance().setDataList(ischecked);
        //mCallback.passToMain(ischecked);
        super.onPause();
    }

}
