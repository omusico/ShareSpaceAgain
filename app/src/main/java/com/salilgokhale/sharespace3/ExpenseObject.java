package com.salilgokhale.sharespace3;

/**
 * Created by salilgokhale on 19/03/15.
 */
public class ExpenseObject {

    private String Ename;
    private String Eamount;
    private Boolean locked;

    public ExpenseObject(String prop1, String prop2) {
        this.Ename = prop1;
        this.Eamount = prop2;
        this.locked = false;
    }

    public void setAmount(String amount){
        this.Eamount = amount;
    }

    public String getEname() {
        return Ename;
    }

    public String getEamount() {
        return Eamount;
    }

    public Boolean getLocked(){ return locked; }

}
