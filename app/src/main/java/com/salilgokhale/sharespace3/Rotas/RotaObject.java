package com.salilgokhale.sharespace3.Rotas;



/**
 * Created by salilgokhale on 14/03/15.
 */
public class RotaObject {

    private String Rname;
    private String Rnextperson;
    private String Rnextdate;

    /*public RotaObject(String prop1, String prop2) {
        this.Rname = prop1;
        this.Rnextperson = prop2;
    }*/

    public RotaObject(String prop1, String prop2, String prop3){
        this.Rname = prop1;
        this.Rnextperson = prop2;
        this.Rnextdate = prop3;
    }

    public String getRname() {
        return Rname;
    }

    public String getRnextperson() {
        return Rnextperson;
    }

    public String getRnextdate(){ return Rnextdate; }

}
