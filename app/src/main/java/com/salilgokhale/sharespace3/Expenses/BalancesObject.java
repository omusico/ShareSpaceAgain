package com.salilgokhale.sharespace3.Expenses;

import android.test.RenamingDelegatingContext;

/**
 * Created by salilgokhale on 18/03/15.
 */
public class BalancesObject {

    public enum ColourStatus{
        GREEN,
        RED,
        NEUTRAL
    }

    private String Bname;
    private String Bdebt;
    private String BObjectID;
    private ColourStatus BcolourStatus = ColourStatus.NEUTRAL;


    public BalancesObject(String prop1, String prop2) {
        this.Bname = prop1;
        this.Bdebt = prop2;
    }

    public BalancesObject(String prop1, String prop2, String ID){
        this.Bname = prop1;
        this.Bdebt = prop2;
        this.BObjectID = ID;
    }

    public BalancesObject(String prop1, String prop2, ColourStatus prop3){
        this.Bname = prop1;
        this.Bdebt = prop2;
        this.BcolourStatus = prop3;
    }

    public String getBname() {
        return Bname;
    }

    public String getBdebt() {
        return Bdebt;
    }

    public String getBObjectID() {
        return BObjectID;
    }

    public ColourStatus getBcolourStatus(){ return BcolourStatus; }
}
