package com.salilgokhale.sharespace3.Expenses;

/**
 * Created by salilgokhale on 19/03/15.
 */
public class ExpenseObject {

    private String Ename;
    private String Eamount;
    private Boolean locked;
    private String Eid;

    public ExpenseObject(String prop1, String prop2) {
        this.Ename = prop1;
        this.Eamount = prop2;
        this.locked = false;
    }

    public ExpenseObject(String prop1, String prop2, String prop3) {
        this.Ename = prop1;
        this.Eamount = prop2;
        this.Eid = prop3;
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

    public String getEid(){ return Eid; }

    public Boolean getLocked(){ return locked; }

}
