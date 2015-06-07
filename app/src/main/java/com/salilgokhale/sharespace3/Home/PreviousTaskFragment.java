package com.salilgokhale.sharespace3.Home;

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
import com.salilgokhale.sharespace3.Expenses.BalancesAdapter;
import com.salilgokhale.sharespace3.Expenses.BalancesObject;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.SwipeDismissListViewTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by salilgokhale on 03/06/15.
 */
public class PreviousTaskFragment extends Fragment {

    public PreviousTaskFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_previous_task, container, false);

        ParseUser user = ParseUser.getCurrentUser();

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("Owner", user);
        query.whereEqualTo("Completed", true);
        query.setLimit(10);
        query.orderByDescending("dateDue");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> taskList, ParseException e) {
                if (taskList != null) {
                    Log.d("QueryTasks:", "Task found");

                    ArrayList<BalancesObject> objects = new ArrayList<>();

                    SimpleDateFormat fmt = new SimpleDateFormat("d/M");

                    for (int i = 0; i < taskList.size(); i++) {
                        ParseObject temptask = taskList.get(i);

                        BalancesObject temp = new BalancesObject(temptask.getString("Name"), fmt.format(temptask.getDate("dateDue")), temptask.getObjectId());
                        objects.add(temp);
                    }

                    final BalancesAdapter mtasksAdapter = new BalancesAdapter(getActivity(), objects);
                    ListView listView = (ListView) rootView.findViewById(R.id.myprevioustasks);
                    listView.setAdapter(mtasksAdapter);

                    SwipeDismissListViewTouchListener touchListener =
                            new SwipeDismissListViewTouchListener(
                                    listView,
                                    new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                        @Override
                                        public boolean canDismiss(int position) {
                                            return true;

                                        }

                                        @Override
                                        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                            for (int position : reverseSortedPositions) {
                                                //int realPosition = position - adapter.getRealPosition(position);
                                                //Log.d("Position: ", String.valueOf(position));
                                                //Log.d("Real Position: ", String.valueOf(realPosition));
                                                String taskID = (String) mtasksAdapter.getItem(position).getBObjectID();

                                                for (int i = 0; i < taskList.size(); i++) {
                                                    Calendar c0 = Calendar.getInstance();
                                                    c0.set(Calendar.HOUR_OF_DAY, 1);
                                                    final Date date = c0.getTime();
                                                    if (taskList.get(i).getObjectId().equals(taskID)) {
                                                        taskList.get(i).put("Completed", false);
                                                        taskList.get(i).put("dateDue", date);
                                                        taskList.get(i).saveInBackground();
                                                    }
                                                }

                                                mtasksAdapter.remove(position);
                                                mtasksAdapter.notifyDataSetChanged();


                                            }
                                        }
                                    });

                    listView.setOnTouchListener(touchListener);
                    // Setting this scroll listener is required to ensure that during ListView scrolling,
                    // we don't look for swipes.
                    listView.setOnScrollListener(touchListener.makeScrollListener());
                }
            }
        });


        return rootView;
    }


}
