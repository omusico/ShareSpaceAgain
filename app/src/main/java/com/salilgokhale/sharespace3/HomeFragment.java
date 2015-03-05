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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("Owner", user);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> taskList, ParseException e) {
                if (e == null){
                    Log.d("QueryTasks:", "Task found");


                    int number = taskList.size();
                    String[] taskArray = new String[number];
                    for(int i = 0; i < number; i++){
                        taskArray[i] = taskList.get(i).getString("Name");
                    }

                    Log.d("Array Task", taskArray[0]);

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

            }
                else {
                    Log.d("QueryTasks:", "Task not found");
                }
            }
        });

        return rootView;
    }





}