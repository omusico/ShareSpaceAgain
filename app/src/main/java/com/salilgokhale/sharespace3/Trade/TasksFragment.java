package com.salilgokhale.sharespace3.Trade;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;


import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.Trade.TradeTaskFragments.DataHolderClass;
import com.salilgokhale.sharespace3.Trade.TradeTaskFragments.HouseMateTasksFragment;
import com.salilgokhale.sharespace3.Trade.TradeTaskFragments.MyTasksFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salilgokhale on 25/03/15.
 */
public class TasksFragment extends Fragment {

    public TasksFragment(){}

    private FragmentTabHost mTabHost;

    //List <String> tempList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_items, container, false);


        //return rootView;

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
        Log.d("First tab created?", "yes");

        mTabHost.addTab(mTabHost.newTabSpec("mytasks").setIndicator("My Tasks"),
                MyTasksFragment.class, null);

        final ParseUser user = ParseUser.getCurrentUser();
        ParseObject userHouse = user.getParseObject("Home");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Home", userHouse);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(final List<ParseUser> userList, com.parse.ParseException e) {
                if (userList != null) {
                    Log.d("Entered Query", "yes");

                    final List<String> ObjectIDs = new ArrayList<String>();


                    for (int i = 0; i < userList.size(); i++) {

                        if (!userList.get(i).getObjectId().equals(user.getObjectId())) {
                            Log.d("Iteration: ", userList.get(i).getString("name"));
                            ObjectIDs.add(userList.get(i).getObjectId());
                            mTabHost.addTab(mTabHost.newTabSpec(userList.get(i).getObjectId()).setIndicator(userList.get(i).getString("name")),
                                    HouseMateTasksFragment.class, null);
                        }
                    }

                    mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
                        @Override
                        public void onTabChanged(String tabId) {
                            DataHolderClass object = DataHolderClass.getInstance();
                            object.setDistributor_id(tabId);
                            //if (!(tempList.size() == 0)) {
                           //     object.setDataList(tempList);
                        //    }

                        }
                });

                }
            }});
        return rootView;
    }

   /* public void getMyTaskList(List<String> list){
        tempList = list;
    }*/


}
