package com.salilgokhale.sharespace3.TradeTabFragments.TradeTaskFragments;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.ExpenseObject;
import com.salilgokhale.sharespace3.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by salilgokhale on 26/03/15.
 */
public class HouseMateTasksFragment extends Fragment {

    public HouseMateTasksFragment(){}


    // SectionHeaders
    private final static String[] headers = new String[]{"Exchange Arena", "Tasks"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_mate_tasks, container, false);

        final ImageButton fabImageButton = (ImageButton) rootView.findViewById(R.id.fab_image_button);

        // Trial

        final String data = DataHolderClass.getInstance().getDistributor_id();
        List<String> dataList = DataHolderClass.getInstance().getDataList();
        if (dataList.size() != 0) {Log.d("MyTask: ", dataList.get(0));}
        Log.d("TabID: ", data);

        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
        query1.whereEqualTo("objectId", data);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereMatchesQuery("Owner", query1);
        query.whereEqualTo("Completed", false);
        //query.setLimit(10);
        query.orderByAscending("dateDue");
        query.include("parentRota.nextPerson");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> taskList, ParseException e) {
                if (taskList != null) {
                    Log.d("QueryTasks:", "Task found");
                    Log.d("Tasklistsize: ", String.valueOf(taskList.size()));
                    SimpleDateFormat fmt = new SimpleDateFormat("d/M");

                    int number = taskList.size();

                    ArrayList<ExpenseObject> EAobjects = new ArrayList<ExpenseObject>();
                    final ArrayList<ExpenseObject> Tobjects = new ArrayList<ExpenseObject>();

                    for (int i = 0; i < number; i++){
                        ParseObject temptask = taskList.get(i);
                        ExpenseObject expenseObject = new ExpenseObject(temptask.getString("Name"), fmt.format(temptask.getDate("dateDue")), temptask.getObjectId());
                        if (temptask.getBoolean("ExchangeArena")){
                            EAobjects.add(expenseObject);
                        }
                        else{
                            Tobjects.add(expenseObject);
                        }
                    }

                    final TradeTaskSeparatedListAdapter adapter = new TradeTaskSeparatedListAdapter(getActivity());
                    TradeTaskAdapter EAadapter = new TradeTaskAdapter(getActivity(), EAobjects);
                    TradeTaskAdapter Tadapter = new TradeTaskAdapter(getActivity(), Tobjects);

                    adapter.addSection(headers[0], EAadapter);
                    adapter.addSection(headers[1], Tadapter);

                    final ListView listView = (ListView) rootView.findViewById(R.id.mymatetasks);
                    listView.setAdapter(adapter);

                    fabImageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (DataHolderClass.getInstance().getDataList().size() != 0){
                                ArrayList<String> theirtaskids = new ArrayList<String>();
                                for (int i = 0; i < (taskList.size()+2); i++){
                                    if ((CheckBox) listView.getChildAt(i).findViewById(R.id.task_checkbox) != null) {
                                        Log.d("Found a checkbox", "yes");
                                        CheckBox cBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.task_checkbox);
                                        TextView textView = (TextView) listView.getChildAt(i).findViewById(R.id.task_id);
                                        if (cBox.isChecked()) {
                                            theirtaskids.add(textView.getText().toString());
                                        }
                                    }

                                }



                                ArrayList<String> mytaskids = new ArrayList<String>();
                                mytaskids.addAll(DataHolderClass.getInstance().getDataList());

                                Intent intent = new Intent(getActivity(), ConfirmBidActivity.class);
                                intent.putStringArrayListExtra("MyTaskIDs", mytaskids);
                                intent.putStringArrayListExtra("TheirTaskIDs", theirtaskids);
                                intent.putExtra("TraderID", data);

                                startActivity(intent);
                            }
                            else{
                                Toast toast = Toast.makeText(getActivity(), "No my tasks selected", Toast.LENGTH_LONG);
                                toast.show();
                            }



                        }
                    });

                }}});

        return rootView;
    }





}
