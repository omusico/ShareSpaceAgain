package com.salilgokhale.sharespace3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseObject;

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
        newHouse.saveInBackground();

    }


}
