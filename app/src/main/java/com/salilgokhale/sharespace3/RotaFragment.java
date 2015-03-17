package com.salilgokhale.sharespace3;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// TODO Must find a way to access objects outside of a query

/**
 * Created by salilgokhale on 04/03/15.
 */
public class RotaFragment extends Fragment {
    List<String> nextPersonArray = new ArrayList<>();


    public RotaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_rota, container, false);

        // Retrieve rotas that User belongs to



        final ParseUser user = ParseUser.getCurrentUser();

    /*    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Rota");
        query1.whereEqualTo("peopleInvolved", user);
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> rotaList1, ParseException e) {
                if (rotaList1 != null) {
                    int number = rotaList1.size();
                    Log.d("Query1:", "Entered Query");

                    for (int j = 0; j < number; j++) {
                        final ParseObject temp_rota = rotaList1.get(j);
                        if (temp_rota.getString("Frequency").equals("When Needed")) {
                            Log.d("This Rota:", "is asynchronous");
                        } else {
                            Log.d("This Rota", "is not asynchronous");
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
                            query.whereEqualTo("parentRota", rotaList1.get(j));
                            query.orderByAscending("dateDue");
                            query.include("Owner");
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(final List<ParseObject> rotaTaskList, ParseException e) {
                                    if (rotaTaskList != null) {

                                        final Calendar c = Calendar.getInstance();
                                        c.add(Calendar.DATE, -1);
                                        Date current_date = c.getTime();
                                        for (int l = 0; l <rotaTaskList.size(); l++){
                                            Log.d("Task found", rotaTaskList.get(l).getString("Name"));
                                            if(current_date.before(rotaTaskList.get(l).getDate("dateDue"))){
                                                temp_rota.put("nextPerson", rotaTaskList.get(l).getParseObject("Owner"));
                                                temp_rota.saveInBackground();
                                                Log.d("Temp Rota", "is set");
                                                break;
                                            }

                                        }

                                    }


                                }


                            });
                        }
                    } */


        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Rota");
        query2.whereEqualTo("peopleInvolved", user);
        query2.include("nextPerson");
        query2.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> rotaList, ParseException e) {
                if (rotaList != null) {
                    Log.d("QueryRotas:", "Rota Found");

                    int number = rotaList.size();

                  /*  for (int j = 0; j < number; j++) {
                        final ParseObject temp_rota = rotaList.get(j);
                        if (temp_rota.getString("Frequency").equals("When Needed")) {
                            Log.d("This Rota:", "is asynchronous");
                        } else {
                            Log.d("This Rota", "is not asynchronous");
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
                            query.whereEqualTo("parentRota", rotaList.get(j));
                            query.orderByAscending("dateDue");
                            query.include("Owner");
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(final List<ParseObject> rotaTaskList, ParseException e) {
                                    if (rotaTaskList != null) {

                                        final Calendar c = Calendar.getInstance();
                                        c.add(Calendar.DATE, -1);
                                        Date current_date = c.getTime();
                                        for (int l = 0; l <rotaTaskList.size(); l++){
                                            Log.d("Task found", rotaTaskList.get(l).getString("Name"));
                                            if(current_date.before(rotaTaskList.get(l).getDate("dateDue"))){
                                                temp_rota.put("nextPerson", rotaTaskList.get(l).getParseObject("Owner"));

                                                temp_rota.saveInBackground();
                                                Log.d("Temp Rota", "is set");
                                                break;
                                            }

                                        }

                                    }


                                }


                            }); */


                    Log.d("Number of Rotas", String.valueOf(number));
                    String[] rotaArray = new String[number];

                    for (int i = 0; i < number; i++) {
                        final ParseObject temp_rota = rotaList.get(i);
                        rotaArray[i] = temp_rota.getString("Name");

                        if (temp_rota.getString("Frequency").equals("When Needed")) {

                            ParseObject personNext = temp_rota.getParseObject("nextPerson");

                            nextPersonArray.add(personNext.getString("name"));
                        } else {

                            Log.d("This Rota", "is not asynchronous");
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
                            query.whereEqualTo("parentRota", temp_rota);
                            query.orderByAscending("dateDue");
                            query.include("Owner");
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(final List<ParseObject> rotaTaskList, ParseException e) {
                                    if (rotaTaskList != null) {

                                        final Calendar c = Calendar.getInstance();
                                        c.add(Calendar.DATE, -1);
                                        Date current_date = c.getTime();
                                        for (int l = 0; l <rotaTaskList.size(); l++){
                                            Log.d("Task found", rotaTaskList.get(l).getString("Name"));
                                            if(current_date.before(rotaTaskList.get(l).getDate("dateDue"))){
                                                nextPersonArray.add(rotaTaskList.get(l).getParseObject("Owner").getString("name"));
                                                Log.d("Temp Rota", "is set");
                                                break;
                                            }

                                        }

                                    }


                                }


                            });



                        }
                    }

                    ArrayList<RotaObject> objects = new ArrayList<>();

                    for (int i = 0; i < number; i++) {
                        RotaObject temp = new RotaObject(rotaArray[i], nextPersonArray.get(i));
                        objects.add(temp);
                    }


                    ListView listView = (ListView) rootView.findViewById(R.id.myrotas);

                    final RotaAdapter rotaAdapter = new RotaAdapter(getActivity(), objects);
                    listView.setAdapter(rotaAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String rota_name = rotaAdapter.getItem(position).getRname();
                            if (rotaList.get(position).getString("Frequency").equals("When Needed")) {
                                Intent intent = new Intent(getActivity(), ViewRotaActivity.class);
                                intent.putExtra(Intent.EXTRA_TEXT, rota_name);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), ViewFreqRotaActivity.class);
                                intent.putExtra(Intent.EXTRA_TEXT, rota_name);
                                startActivity(intent);
                            }
                        }
                    });

                } else {
                    Log.d("QueryRotas:", "Rotas not found");
                }
            }
        });
             //   }
           // }
        //});

        return rootView;
    }

}

