package com.salilgokhale.sharespace3.Expenses.ItemTabFragments;

import com.parse.ParseObject;

/**
 * Created by salilgokhale on 03/06/15.
 */
public class BillsObject {

    private ParseObject object;

    public BillsObject(ParseObject prop1) {
        this.object = prop1;

    }

    public String getname() {
        return object.getString("Name");
    }

    public boolean getdue() {
        return object.getBoolean("Due");
    }

    public String getObjectID() {
        return object.getObjectId();
    }




}
