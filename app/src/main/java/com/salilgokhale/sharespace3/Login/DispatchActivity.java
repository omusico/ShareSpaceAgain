package com.salilgokhale.sharespace3.Login;

import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Created by salilgokhale on 03/03/15.
 */
public class DispatchActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {

        return HomeDispatcherActivity.class;
    }
}
