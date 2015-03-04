package com.salilgokhale.sharespace3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by salilgokhale on 03/03/15.
 */
public class HomeDispatcherActivity extends Activity {



    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //runHomeDispatch();

       /* ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUser");
        query.getInBackground(userID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                } else {
                    // something went wrong
                }
            }
        }); */

        ParseUser user = ParseUser.getCurrentUser();
        Boolean HasHouse = user.getBoolean("Has_House");
        if (HasHouse == true){
            ParseObject testObject = new ParseObject("TestObject");
            testObject.put("foo", "There is a House");
            testObject.saveInBackground();

            //Intent intent = new Intent(this, HouseJoinOrCreateActivity.class);
            //startActivity(intent);


        } else {
            ParseObject testObject = new ParseObject("TestObject");
            testObject.put("foo", "There is no House");
            testObject.saveInBackground();
        }
        /*user.getParseObject("House")
                .fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject house, ParseException e) {
                        if (e == null) {
                            ParseObject testObject = new ParseObject("TestObject");
                            testObject.put("foo", "There is no House");
                            testObject.saveInBackground();
                        }
                        else {
                            ParseObject testObject = new ParseObject("TestObject");
                            testObject.put("foo", "There is a House");
                            testObject.saveInBackground();
                        }
                    }
                });*/
    }

   /* private void runHomeDispatch() {
        if (ParseUser.getCurrentUser() != null) {
            debugLog(getString(R.string.com_parse_ui_login_dispatch_user_logged_in) + getTargetClass());
            startActivityForResult(new Intent(this, getTargetClass()), TARGET_REQUEST);
        } else {
            debugLog(getString(R.string.com_parse_ui_login_dispatch_user_not_logged_in));
            startActivityForResult(getParseLoginIntent(), LOGIN_REQUEST);
        }
    }
*/


}
