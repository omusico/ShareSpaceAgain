package com.salilgokhale.sharespace3;

//import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salilgokhale.sharespace3.ExpensesTabsFragments.BalancesFragment;
import com.salilgokhale.sharespace3.ExpensesTabsFragments.ExpenseLogFragment;
import com.salilgokhale.sharespace3.ExpensesTabsFragments.ItemsFragment;

/**
 * Created by salilgokhale on 04/03/15.
 */
public class ExpensesFragment extends Fragment {

    public ExpensesFragment(){}

    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_expenses, container, false);

        //return rootView;

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("balances").setIndicator("Balances"),
                BalancesFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("expenseLog").setIndicator("Log"),
                ExpenseLogFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("items").setIndicator("Items"),
                ItemsFragment.class, null);


        /*Bundle arg1 = new Bundle();
        arg1.putInt("Arg for Frag1", 1);
        mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Frag Tab1"),
                BalancesFragment.class, arg1);

        Bundle arg2 = new Bundle();
        arg2.putInt("Arg for Frag2", 2);
        mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Frag Tab2"),
                ItemsFragment.class, arg2);

        Bundle arg3 = new Bundle();
        arg3.putInt("Arg for Frag3", 3);
        mTabHost.addTab(mTabHost.newTabSpec("Tab3").setIndicator("Frag Tab3"),
                ExpenseLogFragment.class, arg3);

        return mTabHost; */
        return rootView;
    }
}
