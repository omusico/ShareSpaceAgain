package com.salilgokhale.sharespace3.Login;

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
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.salilgokhale.sharespace3.CoreActivity;
import com.salilgokhale.sharespace3.R;

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

        String HomeName = HouseName.getText().toString();
        String passKey = PassKey.getText().toString();

        if(!HomeName.equals("") && !passKey.equals("")) {

            HomeName = HomeName.trim();
            passKey = passKey.trim();

            final ParseObject newHouse = new ParseObject("House");
            newHouse.put("Name", HomeName);
            newHouse.put("Passkey", passKey);
            newHouse.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        ParseUser user = ParseUser.getCurrentUser();
                        Log.d("UserName: ", user.getString("name"));
                        user.put("Home", newHouse);
                        user.put("Has_House", "true");
                        user.saveInBackground();

                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        installation.put("User", ParseUser.getCurrentUser());
                        installation.saveInBackground();

                        GoToCore();
                    }
                }
            });



        }
        else if (HomeName.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Home needs a name", Toast.LENGTH_LONG);
            toast.show();

        }
        else if (passKey.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Home needs a key", Toast.LENGTH_LONG);
            toast.show();
        }
    }

      public void joinHouse (View view) {
        EditText HouseName = (EditText) findViewById(R.id.house_name);
        EditText PassKey = (EditText) findViewById(R.id.passkey);

        String HomeName = HouseName.getText().toString();
        String passKey = PassKey.getText().toString();

        if(!HomeName.equals("") && !passKey.equals("")) {

            HomeName = HomeName.trim();
            passKey = passKey.trim();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("House");
            query.whereEqualTo("Name", HomeName);
            query.whereEqualTo("PassKey", passKey);
            ParseQuery<ParseUser> query2 = ParseUser.getQuery();
            query2.whereMatchesQuery("Home", query);
            query2.include("Home");

            query2.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> userList, ParseException e) {
                    if (userList == null) {
                        Log.d("Home", "No object found");

                        Toast toast = Toast.makeText(getApplicationContext(), "House Name and/or Pass Key not found" , Toast.LENGTH_LONG);
                        toast.show();

                    } else {
                        Log.d("Home", "object found");

                        ParseObject newHome = userList.get(0).getParseObject("Home");
                        ParseUser user = ParseUser.getCurrentUser();
                        user.put("Home", newHome);
                        user.put("Has_House", true);
                        user.saveInBackground();

                        for (int i = 0; i < userList.size(); i++) {
                            if (!userList.get(i).equals(user)) {
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

                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        installation.put("User", ParseUser.getCurrentUser());
                        installation.saveInBackground();

                        GoToCore();


                    }
                }
            });
        }
        else if (HomeName.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Home needs a name", Toast.LENGTH_LONG);
            toast.show();

        }
        else if (passKey.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Home needs a key", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void GoToCore(){
        Intent intent = new Intent(this, CoreActivity.class);
        startActivity(intent);
        this.finish();
    }


}
