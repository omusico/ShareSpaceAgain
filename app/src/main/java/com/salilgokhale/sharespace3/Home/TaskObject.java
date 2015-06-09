package com.salilgokhale.sharespace3.Home;

/**
 * Created by salilgokhale on 08/06/15.
 */
public class TaskObject {

        private String Tname;
        private String Tdate;
        private String TObjectID;


        public TaskObject(String prop1, String prop2) {
            this.Tname = prop1;
            this.Tdate = prop2;
        }

        public TaskObject(String prop1, String prop2, String ID){
            this.Tname = prop1;
            this.Tdate = prop2;
            this.TObjectID = ID;
        }

        public String getTname() {
            return Tname;
        }

        public String getTdate() {
            return Tdate;
        }

        public String getTObjectID() {
            return TObjectID;
        }


}
