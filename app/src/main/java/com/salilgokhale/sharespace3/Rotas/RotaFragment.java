package com.salilgokhale.sharespace3.Rotas;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO Put in Next Date into the adapter and listview

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
/*


        final ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Rota");
        query2.whereEqualTo("peopleInvolved", user);
        query2.include("nextPerson");
        query2.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> rotaList, ParseException e) {
                if (rotaList != null) {
                    Log.d("QueryRotas:", "Rota Found");

                    final int number = rotaList.size();



                    Log.d("Number of Rotas", String.valueOf(number));
                    //String[] rotaArray = new String[number];
                    ArrayList<RotaObject> objects = new ArrayList<>();


                    for (int i = 0; i < number; i++) {
                        RotaObject temp;
                        final ParseObject temp_rota = rotaList.get(i);
                        //rotaArray[i] = temp_rota.getString("Name");
                        String tempName = temp_rota.getString("Name");
                        String personNext = temp_rota.getParseObject("nextPerson").getString("name");

                        if (!temp_rota.getString("Frequency").equals("When Needed")) {
                            Date date = (Date) temp_rota.get("nextDate");
                            SimpleDateFormat formatter = new SimpleDateFormat("d/M");
                            String nextDate = formatter.format(date);
                            temp = new RotaObject(tempName, personNext, nextDate);


                        } else {
                            temp = new RotaObject(tempName, personNext, "");

                       }
                        objects.add(temp);
                    }
*/

                    /*
                    for (int i = 0; i < number; i++) {
                        RotaObject temp = new RotaObject(rotaArray[i], nextPersonArray.get(i));
                        objects.add(temp);
                    }
                    */
/*
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
*/
        return rootView;
    }


    @Override
    public void onResume(){
        super.onResume();

        final View rootView = getView();

        final ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Rota");
        //query2.whereEqualTo("peopleInvolved", user);
        query2.whereEqualTo("orderPeople", user);
        query2.include("nextPerson");
        query2.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> rotaList, ParseException e) {
                if (rotaList != null) {
                    Log.d("QueryRotas:", "Rota Found");

                    final int number = rotaList.size();



                    Log.d("Number of Rotas", String.valueOf(number));
                    //String[] rotaArray = new String[number];
                    ArrayList<RotaObject> objects = new ArrayList<>();


                    for (int i = 0; i < number; i++) {
                        RotaObject temp;
                        final ParseObject temp_rota = rotaList.get(i);
                        //rotaArray[i] = temp_rota.getString("Name");
                        String tempName = temp_rota.getString("Name");
                        String personNext = temp_rota.getParseObject("nextPerson").getString("name");
                        String tempID = temp_rota.getObjectId();

                        if (!temp_rota.getString("Frequency").equals("When Needed")) {
                            Date date = (Date) temp_rota.get("nextDate");
                            SimpleDateFormat formatter = new SimpleDateFormat("d/M");
                            String nextDate = formatter.format(date);
                            temp = new RotaObject(tempName, personNext, nextDate, tempID);


                        } else {
                            temp = new RotaObject(tempName, personNext, "", tempID);

                        }
                        objects.add(temp);
                    }


                    /*
                    for (int i = 0; i < number; i++) {
                        RotaObject temp = new RotaObject(rotaArray[i], nextPersonArray.get(i));
                        objects.add(temp);
                    }
                    */

                    ListView listView = (ListView) rootView.findViewById(R.id.myrotas);

                    final RotaAdapter rotaAdapter = new RotaAdapter(getActivity(), objects);
                    listView.setAdapter(rotaAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String rota_id = rotaAdapter.getItem(position).getRid();
                            if (rotaList.get(position).getString("Frequency").equals("When Needed")) {
                                Intent intent = new Intent(getActivity(), ViewRotaActivity.class);
                                intent.putExtra(Intent.EXTRA_TEXT, rota_id);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), ViewFreqRotaActivity.class);
                                intent.putExtra(Intent.EXTRA_TEXT, rota_id);
                                startActivity(intent);
                            }
                        }
                    });

                } else {
                    Log.d("QueryRotas:", "Rotas not found");
                }
            }
        });

    }
}

