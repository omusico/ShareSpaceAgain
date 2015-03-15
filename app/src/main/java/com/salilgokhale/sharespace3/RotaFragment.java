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
import java.util.List;

/**
 * Created by salilgokhale on 04/03/15.
 */
public class RotaFragment extends Fragment {
    List<String> nextPersonArray = new ArrayList<>();


    public RotaFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_rota, container, false);

        // Retrieve rotas that User belongs to

        ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rota");
        query.whereEqualTo("peopleInvolved", user);
        query.include("nextPerson");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> rotaList, ParseException e) {
                if (rotaList != null){
                    Log.d("QueryRotas:", "Rota Found");



                    int number = rotaList.size();
                    Log.d("Number of Rotas", String.valueOf(number));
                    String[] rotaArray = new String[number];

                    for(int i = 0; i < number; i++){
                        ParseObject personNext = rotaList.get(i).getParseObject("nextPerson");
                        rotaArray[i] = rotaList.get(i).getString("Name");
                        nextPersonArray.add(personNext.getString("name"));
                    }

                    ArrayList<RotaObject> objects = new ArrayList<>();

                    for (int i = 0; i < number; i++){
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
                            Intent intent = new Intent(getActivity(), ViewRotaActivity.class);
                            intent.putExtra(Intent.EXTRA_TEXT, rota_name);
                            startActivity(intent);
                        }
                    });

                }
                else {
                    Log.d("QueryRotas:", "Rotas not found");
                }
            }
        });

        return rootView;
    }
}

