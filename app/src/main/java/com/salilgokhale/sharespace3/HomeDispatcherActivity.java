package com.salilgokhale.sharespace3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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


        ParseUser user = ParseUser.getCurrentUser();


        Boolean HasHouse = user.getBoolean("Has_House");
        String str = String.valueOf(HasHouse);
        Log.d("HasHouse", str);
        if (HasHouse == true){
            ParseObject testObject = new ParseObject("TestObject");
            testObject.put("foo", "There is a House");
            testObject.saveInBackground();

            Intent intent = new Intent(this, CoreActivity.class);
            startActivity(intent);


        } else {
            ParseObject testObject = new ParseObject("TestObject");
            testObject.put("foo", "There is no House");
            testObject.saveInBackground();

            Intent intent = new Intent(this, HouseJoinOrCreateActivity.class);
            startActivity(intent);
        }
        this.finish();
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
