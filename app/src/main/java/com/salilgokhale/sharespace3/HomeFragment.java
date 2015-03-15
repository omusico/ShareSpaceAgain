package com.salilgokhale.sharespace3;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by salilgokhale on 04/03/15.
 */
public class HomeFragment extends Fragment {

    private Button bt1, bt2;
    private ListView lv;
    private ArrayList<String> strArr;
    private ArrayAdapter<String> adapter;

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Retrieve tasks from Parse User
        ParseUser user = ParseUser.getCurrentUser();

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("Owner", user);
        query.whereEqualTo("Completed", false);
        query.include("parentRota.nextPerson");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> taskList, ParseException e) {
                if (taskList != null){
                    Log.d("QueryTasks:", "Task found");


                    int number = taskList.size();
                    String[] taskArray = new String[number];
                    for(int i = 0; i < number; i++){
                        taskArray[i] = taskList.get(i).getString("Name");
                    }

                    final List<String> tasksList = new ArrayList<>(Arrays.asList(taskArray));
                    final ArrayAdapter <String> mtasksAdapter = new ArrayAdapter<>(getActivity(), R.layout.task_list_item,
                            R.id.task_list_item_textview, tasksList);

                    ListView listView = (ListView) rootView.findViewById(R.id.mytasks);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String task_name = mtasksAdapter.getItem(position);
                            Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                            intent.putExtra(Intent.EXTRA_TEXT, task_name);
                            startActivity(intent);
                        }
                    });


                    listView.setAdapter(mtasksAdapter);
                    // Create a ListView-specific touch listener. ListViews are given special treatment because
                    // by default they handle touches for their list items... i.e. they're in charge of drawing
                    // the pressed state (the list selector), handling list item clicks, etc.
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
                                                mtasksAdapter.remove(mtasksAdapter.getItem(position));
                                                final ParseObject therota = taskList.get(position).getParseObject("parentRota");

                                                if (therota != null ){

                                                    therota.put("Due", false);

                                                        ParseRelation relation = therota.getRelation("peopleInvolved");
                                                        ParseQuery query2 = relation.getQuery();
                                                        query2.findInBackground(new FindCallback<ParseObject>() {
                                                        @Override
                                                        public void done(List<ParseObject> list, ParseException e) {

                                                            String personNextName = therota.getParseObject("nextPerson").getObjectId();
                                                             Log.d("nextPerson's name", personNextName);


                                                             for (int i = 0; i < list.size(); i++){
                                                                 Log.d("peopleInvolved", list.get(i).getString("name"));

                                                                 if (personNextName.equals(list.get(i).getObjectId())){
                                                                     if (i == list.size() - 1){
                                                                         therota.put("nextPerson", list.get(0));
                                                                         Log.d("nextPerson position", " at end");
                                                                     }
                                                                     else{
                                                                         therota.put("nextPerson", list.get(i+1));
                                                                         Log.d("nextPerson position", " not at end");
                                                                     }
                                                                     break;
                                                                 }
                                                             }
                                                            therota.saveInBackground();
                                                        }});
                                                }


                                                taskList.get(position).put("Completed", true);
                                                taskList.get(position).saveInBackground();

                                            }
                                            mtasksAdapter.notifyDataSetChanged();
                                        }
                                    });
                    listView.setOnTouchListener(touchListener);
                    // Setting this scroll listener is required to ensure that during ListView scrolling,
                    // we don't look for swipes.
                    listView.setOnScrollListener(touchListener.makeScrollListener());



            }
                else {
                    Log.d("QueryTasks:", "Task not found");
                }
            }
        });

        return rootView;
    }





}