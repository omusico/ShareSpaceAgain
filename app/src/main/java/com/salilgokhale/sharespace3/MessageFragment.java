package com.salilgokhale.sharespace3;

//import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salilgokhale on 04/03/15.
 */
public class MessageFragment extends Fragment {

    public MessageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        /*final ParseUser user = ParseUser.getCurrentUser();
        ParseObject Home = user.getParseObject("Home");

        ParseQuery<ParseUser> query2 = ParseUser.getQuery();
        query2.whereEqualTo("Home", Home);
        query2.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (userList == null) {
                    Log.d("Home", "No object found");
                    /// Toast
                    Context context = getApplicationContext();
                    CharSequence text = "House Name and/or Pass Key not found";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                } else {
                    Log.d("Home", "object found");

                    for (int i = 0; i < userList.size(); i++) {
                        if (!userList.get(i).getObjectId().equals(user.getObjectId())) {
                            ParseObject oweExpense = new ParseObject("OweExpense");
                            oweExpense.put("Amount", 0);
                            oweExpense.put("Name1", user.getString("name"));
                            oweExpense.put("Name2", userList.get(i).getString("name"));
                            List<ParseUser> templist = new ArrayList<ParseUser>();
                            templist.add(user);
                            templist.add(userList.get(i));
                            oweExpense.put("OwnerArray", templist);
                            oweExpense.saveInBackground();
                        }
                    }
                }}});
    */

        return rootView;
    }
}
