package com.salilgokhale.sharespace3.ExpensesTabsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salilgokhale.sharespace3.R;

/**
 * Created by salilgokhale on 17/03/15.
 */
public class BalancesFragment extends Fragment {

    public BalancesFragment(){}


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_balances, container, false);

        return rootView;
    }
}
