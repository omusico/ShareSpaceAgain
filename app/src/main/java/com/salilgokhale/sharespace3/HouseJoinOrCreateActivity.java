package com.salilgokhale.sharespace3;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by salilgokhale on 04/03/15.
 */
public class HouseJoinOrCreateActivity extends Activity {

    //private EditText HouseName;
    //private EditText PassKey;


   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_housejoinorcreate);
        //HouseName = (EditText)

    }

    public void createHouse (View view) {
        EditText HouseName = (EditText) findViewById(R.id.house_name);
        EditText PassKey = (EditText) findViewById(R.id.passkey);

        ParseObject newHouse = new ParseObject("House");
        newHouse.put("Name", HouseName.getText().toString());
        newHouse.put("Passkey", PassKey.getText().toString());

        /*ParseObject newTestObject = new ParseObject("TestObject");
        newTestObject.put("foo", "this worked");
        newTestObject.put("Home", newHouse);

        newTestObject.saveInBackground();*/

        ParseUser user = ParseUser.getCurrentUser();
        user.put("Home", newHouse);
        user.put("Has_House", "true");
        user.saveInBackground();


        //ParseUser user = ParseUser.getCurrentUser();
        //user.put("TestColumn", "TestDATA");
        //user.saveInBackground();


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




       // ParseUser user = ParseUser.getCurrentUser();
       // user.put("House", newHouse);





    }

  /*  public void joinHouse (View view) {
        EditText HouseName = (EditText) findViewById(R.id.house_name);
        EditText PassKey = (EditText) findViewById(R.id.passkey);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("House");
        query.whereEqualTo("Name", HouseName.getText().toString());
        query.whereEqualTo("PassKey", )
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("Home","No object found");

                } else {
                    Log.d("Home", "object found");



                }
            }
        });



    }   */




}
