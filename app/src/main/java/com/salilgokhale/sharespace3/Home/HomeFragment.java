package com.salilgokhale.sharespace3.Home;

//import android.app.Fragment;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.Expenses.BalancesAdapter;
import com.salilgokhale.sharespace3.Expenses.BalancesObject;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.SwipeDismissListViewTouchListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by salilgokhale on 04/03/15.
 */
public class HomeFragment extends Fragment {

    private Button bt1, bt2;
    private ListView lv;
    private ArrayList<String> strArr;
    private ArrayAdapter<String> adapter;

    // SectionHeaders
    private final static String[] headers = new String[]{"Today", "Upcoming"};


    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //printKeyHash(getActivity());

        return rootView;
    }

    @Override
    public void onResume(){
        Log.e("DEBUG", "onResume of HomeFragment");
        super.onResume();

        final View rootView = getView();

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

                    Calendar c = Calendar.getInstance();
                    Date date = c.getTime();
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat fmt2 = new SimpleDateFormat("d/M");

                    int number = taskList.size();
                    List<String> todaytaskArray = new ArrayList<String>();
                    List<String> todaytaskDate = new ArrayList<String>();
                    List<String> todaytaskID = new ArrayList<String>();
                    List<String> upcomingtaskArray = new ArrayList<String>();
                    List<String> upcomingtaskDate = new ArrayList<String>();
                    List<String> upcomingtaskID = new ArrayList<String>();


                    for (int i = 0; i < number; i++) {
                        //taskArray[i] = taskList.get(i).getString("Name");
                        ParseObject temptask = taskList.get(i);
                        Date taskdate = temptask.getDate("dateDue");
                        if (fmt.format(date).equals(fmt.format(taskdate))) {
                            todaytaskArray.add(temptask.getString("Name"));
                            todaytaskDate.add(fmt2.format(taskdate));
                            todaytaskID.add(temptask.getObjectId());
                        } else {
                            upcomingtaskArray.add(temptask.getString("Name"));
                            upcomingtaskDate.add(fmt2.format(taskdate));
                            upcomingtaskID.add(temptask.getObjectId());
                        }
                    }

                    ArrayList<BalancesObject> todayobjects = new ArrayList<>();
                    ArrayList<BalancesObject> upcomingobjects = new ArrayList<>();

                    for (int i = 0; i < todaytaskArray.size(); i++) {
                        BalancesObject temp = new BalancesObject(todaytaskArray.get(i), todaytaskDate.get(i), todaytaskID.get(i));
                        todayobjects.add(temp);
                        Log.d("Object Name: ", todayobjects.get(i).getBname());
                        Log.d("Object Debt: ", todayobjects.get(i).getBdebt());
                    }

                    for (int i = 0; i < upcomingtaskArray.size(); i++) {
                        BalancesObject temp = new BalancesObject(upcomingtaskArray.get(i), upcomingtaskDate.get(i), upcomingtaskID.get(i));
                        upcomingobjects.add(temp);
                        Log.d("Object Name: ", upcomingobjects.get(i).getBname());
                        Log.d("Object Debt: ", upcomingobjects.get(i).getBdebt());
                    }


                    //final List<String> tasksList = new ArrayList<>(Arrays.asList(taskArray));
                    //final ArrayAdapter <String> mtasksAdapter = new ArrayAdapter<>(getActivity(), R.layout.task_list_item,
                    //R.id.task_list_item_textview, tasksList);

                    // Create the ListView Adapter
                    final HomeSeparatedListAdapter adapter = new HomeSeparatedListAdapter(getActivity());
                    //final ArrayAdapter<String> mtasksAdapter = new ArrayAdapter<String>(getActivity(), R.layout.task_list_item, todaytaskArray);
                    //final ArrayAdapter<String> mupcomingAdapter = new ArrayAdapter<String>(getActivity(), R.layout.task_list_item, upcomingtaskArray);
                    final BalancesAdapter mtasksAdapter = new BalancesAdapter(getActivity(), todayobjects);
                    final BalancesAdapter mupcomingAdapter = new BalancesAdapter(getActivity(), upcomingobjects);


                    // Add Sections

                    adapter.addSection(headers[0], mtasksAdapter);
                    adapter.addSection(headers[1], mupcomingAdapter);

                    //adapter.notifyDataSetChanged();

                    ListView listView = (ListView) rootView.findViewById(R.id.mytasks);
                    /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String task_name = mtasksAdapter.getItem(position);
                            Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                            intent.putExtra(Intent.EXTRA_TEXT, task_name);
                            startActivity(intent);
                        }
                    }); */

                   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
                            String task_id = (String) adapter.getItemsID(position);
                            Toast.makeText(getActivity(), task_id, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                            intent.putExtra(Intent.EXTRA_TEXT, task_id);
                            startActivity(intent);
                        }
                    });


                    listView.setAdapter(adapter);
                    // Create a ListView-specific touch listener. ListViews are given special treatment because
                    // by default they handle touches for their list items... i.e. they're in charge of drawing
                    // the pressed state (the list selector), handling list item clicks, etc.
                    SwipeDismissListViewTouchListener touchListener =
                            new SwipeDismissListViewTouchListener(
                                    listView,
                                    new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                        @Override
                                        public boolean canDismiss(int position) {
                                            if (adapter.checkIfHeader(position)) {
                                                return false;
                                            }
                                            else return true;
                                        }

                                        @Override
                                        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                            for (int position : reverseSortedPositions) {
                                                //int realPosition = position - adapter.getRealPosition(position);
                                                //Log.d("Position: ", String.valueOf(position));
                                                //Log.d("Real Position: ", String.valueOf(realPosition));
                                                String taskID = (String) adapter.getItemsID(position);
                                                int number2 = 0;
                                                for(int i = 0; i < taskList.size(); i++){
                                                    if(taskList.get(i).getObjectId().equals(taskID)){
                                                        number2 = i;
                                                        break;
                                                    }
                                                }


                                                adapter.remove(position);

                                                final ParseObject therota = taskList.get(number2).getParseObject("parentRota");

                                                if (therota != null) {
                                                    if (therota.getString("Frequency").equals("When Needed")) {

                                                        therota.put("Due", false);

                                                        ParseRelation relation = therota.getRelation("peopleInvolved");
                                                        ParseQuery query2 = relation.getQuery();
                                                        query2.findInBackground(new FindCallback<ParseObject>() {
                                                            @Override
                                                            public void done(List<ParseObject> list, ParseException e) {

                                                                String personNextName = therota.getParseObject("nextPerson").getObjectId();
                                                                Log.d("nextPerson's name", personNextName);


                                                                for (int i = 0; i < list.size(); i++) {
                                                                    Log.d("peopleInvolved", list.get(i).getString("name"));

                                                                    if (personNextName.equals(list.get(i).getObjectId())) {
                                                                        if (i == list.size() - 1) {
                                                                            therota.put("nextPerson", list.get(0));
                                                                            Log.d("nextPerson position", " at end");
                                                                        } else {
                                                                            therota.put("nextPerson", list.get(i + 1));
                                                                            Log.d("nextPerson position", " not at end");
                                                                        }
                                                                        break;
                                                                    }
                                                                }
                                                                therota.saveInBackground();
                                                            }
                                                        });
                                                    }
                                                }

                                                Log.d("Task being removed:", taskList.get(number2).getString("Name"));
                                                taskList.get(number2).put("Completed", true);
                                                taskList.get(number2).saveInBackground();


                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                    listView.setOnTouchListener(touchListener);
                    // Setting this scroll listener is required to ensure that during ListView scrolling,
                    // we don't look for swipes.
                    listView.setOnScrollListener(touchListener.makeScrollListener());


                } else {
                    Log.d("QueryTasks:", "Task not found");
                }
            }
        });

    }


    public static void printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        //return key;
    }


}