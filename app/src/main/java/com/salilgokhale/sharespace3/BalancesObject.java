package com.salilgokhale.sharespace3;

/**
 * Created by salilgokhale on 18/03/15.
 */
public class BalancesObject {

    private String Bname;
    private String Bdebt;
    private String BObjectID;

    public BalancesObject(String prop1, String prop2) {
        this.Bname = prop1;
        this.Bdebt = prop2;
    }

    public BalancesObject(String prop1, String prop2, String ID){
        this.Bname = prop1;
        this.Bdebt = prop2;
        this.BObjectID = ID;
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
}
