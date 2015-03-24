package com.salilgokhale.sharespace3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salilgokhale on 04/03/15.
 */
public class HouseJoinOrCreateActivity extends Activity {


   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_housejoinorcreate);

    }

    public void createHouse (View view) {
        EditText HouseName = (EditText) findViewById(R.id.house_name);
        EditText PassKey = (EditText) findViewById(R.id.passkey);

        ParseObject newHouse = new ParseObject("House");
        newHouse.put("Name", HouseName.getText().toString());
        newHouse.put("Passkey", PassKey.getText().toString());

        ParseUser user = ParseUser.getCurrentUser();
        user.put("Home", newHouse);
        user.put("Has_House", "true");
        user.saveInBackground();

        GoToCore();


        /*ParseQuery<ParseObject> query = ParseQuery.getQuery("House");
        query.whereEqualTo("Name", HouseName.getText().toString());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("Home","No object found");
                } else {
                    Log.d("Home", "object found");
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("House", object);
                    user.saveInBackground();
                }
            }
        }); */
    }

      public void joinHouse (View view) {
        EditText HouseName = (EditText) findViewById(R.id.house_name);
        EditText PassKey = (EditText) findViewById(R.id.passkey);

        String housenamestr = HouseName.getText().toString();
        String passkeystr = PassKey.getText().toString();
        Log.d("House Name", housenamestr);
        Log.d("Pass Key", passkeystr);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("House");
        query.whereEqualTo("Name", housenamestr);
        //query.setLimit(1);
        ParseQuery<ParseUser> query2 = ParseUser.getQuery();
        query2.whereMatchesQuery("Home", query);
        query2.include("Home");
        //query.whereEqualTo("PassKey", passkeystr);
        query2.findInBackground(new FindCallback<ParseUser>() {
            public void done(List <ParseUser> userList, ParseException e) {
                if (userList == null) {
                    Log.d("Home", "No object found");
                    /* Toast */
                    Context context = getApplicationContext();
                    CharSequence text = "House Name and/or Pass Key not found";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                } else {
                    Log.d("Home", "object found");

                    ParseObject newHome = userList.get(0).getParseObject("Home");
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("Home", newHome);
                    user.put("Has_House", true);
                    user.saveInBackground();

                    for (int i = 0; i < userList.size(); i++){
                            if (!userList.get(i).equals(user)) {
                                ParseObject oweExpense = new ParseObject("OweExpense");
                                oweExpense.put("Amount", 0);
                                oweExpense.put("Name1", user.getString("name"));
                                oweExpense.put("Name2", userList.get(i).getString("name"));
                                List <ParseUser> templist = new ArrayList<ParseUser>();
                                templist.add(user);
                                templist.add(userList.get(i));
                                oweExpense.put("OwnerArray", templist);
                                oweExpense.saveInBackground();
                            }
                    }


                    GoToCore();


                }
            }
        });

    }

    public void GoToCore(){
        Intent intent = new Intent(this, CoreActivity.class);
        startActivity(intent);
        this.finish();
    }


}
