package com.salilgokhale.sharespace3.Trade;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salilgokhale.sharespace3.R;


/**
 * Created by salilgokhale on 04/03/15.
 */
public class TradeFragment extends Fragment {

    public TradeFragment(){}

    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_trade, container, false);

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tasks").setIndicator("Tasks"),
                TasksFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("bidoffers").setIndicator("Bids & Offers"),
                BidOffersFragment.class, null);


        return rootView;
    }
}
