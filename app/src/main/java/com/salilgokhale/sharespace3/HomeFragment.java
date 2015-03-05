package com.salilgokhale.sharespace3;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private List<String> taskArray = new ArrayList<>();
    //String[] taskArray;

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //bt1 = (Button) rootView.findViewById(R.id.add_button_home);
        //bt2 = (Button) rootView.findViewById(R.id.log_button_home);

        // Retrieve tasks from Parse User
        ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("Owner", user);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> taskList, ParseException e) {
                if (e == null){
                    Log.d("QueryTasks:", "Task found");


                    int number = taskList.size();
                    //taskArray = new String[number];
                    ParseObject temp = taskList.get(0);
                    String str = temp.getString("Name");
                    //taskArray[0] = str;
                    taskArray.add(str);
                    String[] taskStringArray = new String[taskArray.size()];
                    taskStringArray = taskArray.toArray(taskStringArray);
                    Log.d("Number of Tasks", String.valueOf(number));
                    Log.d("First Task:", str);
                    Log.d("Array Task", taskStringArray[0]);


                    final List<String> tasksList = new ArrayList<>(Arrays.asList(taskStringArray));
                    final ArrayAdapter <String> mrulesAdapter = new ArrayAdapter<>(getActivity(), R.layout.task_list_item,
                            R.id.task_list_item_textview, tasksList);

                    ListView listView = (ListView) rootView.findViewById(R.id.mytasks);
                    listView.setAdapter(mrulesAdapter);
            }
                else {
                    Log.d("QueryTasks:", "Task not found");
                }
            }
        });


        String[] rulesArray = {
                "Take out the Trash"


        };

        /*final List<String> tasksList = new ArrayList<>(Arrays.asList(rulesArray));
        //final List<String> tasksList = new ArrayList<>(taskArray);
        final ArrayAdapter <String> mrulesAdapter = new ArrayAdapter<>(getActivity(), R.layout.task_list_item,
                R.id.task_list_item_textview, tasksList);

        ListView listView = (ListView) rootView.findViewById(R.id.mytasks);
        listView.setAdapter(mrulesAdapter);



*/
        return rootView;
    }

}