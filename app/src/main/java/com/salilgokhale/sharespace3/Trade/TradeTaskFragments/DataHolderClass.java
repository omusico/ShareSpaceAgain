package com.salilgokhale.sharespace3.Trade.TradeTaskFragments;

import java.util.List;

/**
 * Created by salilgokhale on 27/03/15.
 */
public class DataHolderClass {
    private static DataHolderClass dataObject = null;

    private DataHolderClass() {
        // left blank intentionally
    }

    public static DataHolderClass getInstance() {
        if (dataObject == null)
            dataObject = new DataHolderClass();
        return dataObject;
    }
    private String distributor_id;

    public String getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(String distributor_id) {
        this.distributor_id = distributor_id;
    }

    private List<String> dataList;

    public List<String> getDataList(){ return dataList; }

    public void setDataList(List<String> dataList) { this.dataList = dataList; }

    private int anInt;

    public void setAnInt(int anInt){ this.anInt = anInt; }

    public int getAnInt(){ return anInt; }
}

