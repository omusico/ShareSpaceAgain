package com.salilgokhale.sharespace3.ExpensesTabsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salilgokhale.sharespace3.ExpensesTabsFragments.ItemTabFragments.BillsFragment;
import com.salilgokhale.sharespace3.ExpensesTabsFragments.ItemTabFragments.CommonItemsFragment;
import com.salilgokhale.sharespace3.R;

/**
 * Created by salilgokhale on 17/03/15.
 */
public class ItemsFragment extends Fragment {

    public ItemsFragment(){}

    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_items, container, false);

        //return rootView;

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("bills").setIndicator("Bills"),
                BillsFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("commonItems").setIndicator("Common Items"),
                CommonItemsFragment.class, null);

        return rootView;
    }

}
