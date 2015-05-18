package com.salilgokhale.sharespace3.TradeTabFragments;

/**
 * Created by salilgokhale on 28/03/15.
 */
public class BidOffersObject {

    private String BOperson;
    private String BOtasks1;
    private String BOtasks2;
    private String Status;
    private String BObjectID;

    public BidOffersObject(String prop1, String prop2, String prop3, String prop4, String prop5) {
        this.BOperson = prop1;
        this.BOtasks1 = prop2;
        this.BOtasks2 = prop3;
        this.Status = prop4;
        this.BObjectID = prop5;
    }

    public void setStatus(String status){
        this.Status = status;
    }

    public String getBOperson() {
        return BOperson;
    }

    public String getBOtasks1() {
        return BOtasks1;
    }

    public String getBOtasks2(){ return BOtasks2; }

    public String getStatus(){ return Status; }

    public String getBObjectID(){ return BObjectID; }
}
