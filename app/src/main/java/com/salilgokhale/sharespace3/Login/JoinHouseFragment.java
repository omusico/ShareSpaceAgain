package com.salilgokhale.sharespace3.Login;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salilgokhale.sharespace3.R;

/**
 * Created by salilgokhale on 04/03/15.
 */
public class JoinHouseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle
            savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_housejoinorcreate3,
                container, false);
        return view;
    }
}
