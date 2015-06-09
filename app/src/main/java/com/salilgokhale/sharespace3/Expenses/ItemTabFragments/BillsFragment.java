package com.salilgokhale.sharespace3.Expenses.ItemTabFragments;

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
import com.salilgokhale.sharespace3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salilgokhale on 25/03/15.
 */
public class BillsFragment extends Fragment {

    public BillsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        return rootView;
    }

/*
    @Override
    public void onResume() {
        super.onResume();

        final View rootView = getView();

        if (rootView != null) {

            final ParseUser user = ParseUser.getCurrentUser();
            ParseObject house = user.getParseObject("Home");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Bills");
            query.whereEqualTo("House", house);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(final List<ParseObject> billsList, ParseException e) {
                    if (billsList != null) {
                        Log.d("QueryBills:", "Bills Found");

                        BillsAdapter adapter = new BillsAdapter(getActivity(), billsList);
                        ListView listView = (ListView) rootView.findViewById(R.id.mybills);
                        listView.setAdapter(adapter);

                    }
                }
            });
        }
    }

*/
}
