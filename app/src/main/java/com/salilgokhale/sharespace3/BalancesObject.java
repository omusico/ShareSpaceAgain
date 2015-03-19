package com.salilgokhale.sharespace3;

/**
 * Created by salilgokhale on 18/03/15.
 */
public class BalancesObject {

    private String Bname;
    private String Bdebt;

    public BalancesObject(String prop1, String prop2) {
        this.Bname = prop1;
        this.Bdebt = prop2;
    }

    public String getBname() {
        return Bname;
    }

    public String getBdebt() {
        return Bdebt;
    }
}
