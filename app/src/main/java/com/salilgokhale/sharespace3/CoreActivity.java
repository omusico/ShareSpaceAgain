package com.salilgokhale.sharespace3;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.Expenses.AddNewExpenseActivity;
import com.salilgokhale.sharespace3.Expenses.ExpensesFragment;
import com.salilgokhale.sharespace3.Expenses.ItemTabFragments.AddNewBillActivity;
import com.salilgokhale.sharespace3.Home.AddNewTaskActivity;
import com.salilgokhale.sharespace3.Home.HomeFragment;
import com.salilgokhale.sharespace3.Rotas.AddNewRotaActivity;
import com.salilgokhale.sharespace3.Rotas.RotaFragment;
import com.salilgokhale.sharespace3.Rules.RulesFragment;
import com.salilgokhale.sharespace3.Trade.TradeFragment;
import com.salilgokhale.sharespace3.adapter.NavDrawerListAdapter;
import com.salilgokhale.sharespace3.model.NavDrawerItem;

import java.util.ArrayList;


public class CoreActivity extends ActionBarActivity{
        //implements MyTasksFragment.OnTrialInterface{

        /*
        public void passToMain(List<String> checked) {
            Log.d("Did pass to main work? ", "Yes");
            // The user selected the headline of an article from the HeadlinesFragment
            // Do something here to display that article

            Log.d("Actual Element: ", checked.get(0));

            //Fragment fragment = getSupportFragmentManager().findFragmentB
            DataHolderClass.getInstance().setDataList(checked);

        } */


        private DrawerLayout mDrawerLayout;
        private ListView mDrawerList;
        private ActionBarDrawerToggle mDrawerToggle;

        // nav drawer title
        private CharSequence mDrawerTitle;

        // used to store app title
        private CharSequence mTitle;

        // slide menu items
        private String[] navMenuTitles;
        private TypedArray navMenuIcons;

        private ArrayList<NavDrawerItem> navDrawerItems;
        private NavDrawerListAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_core);

            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("User", ParseUser.getCurrentUser());
            installation.saveInBackground();
            getSupportActionBar().setElevation(0);

            //mTitle = mDrawerTitle = getTitle();
              mTitle = mDrawerTitle = "ShareSpace";
            // load slide menu items
            navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

            // nav drawer icons from resources
            navMenuIcons = getResources()
                    .obtainTypedArray(R.array.nav_drawer_icons);

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

            navDrawerItems = new ArrayList<NavDrawerItem>();

            // adding nav drawer items to array
            // Home
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
            // Rotas
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
            // Expenses
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
            // Trade
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
            // Message
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
            // Rules
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));


            // Recycle the typed array
            navMenuIcons.recycle();

            // setting the nav drawer list adapter
            adapter = new NavDrawerListAdapter(getApplicationContext(),
                    navDrawerItems);
            mDrawerList.setAdapter(adapter);

            // enabling action bar app icon and behaving it as toggle button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.drawable.drawer_icon, //nav menu toggle icon
                    R.string.app_name, // nav drawer open - description for accessibility
                    R.string.app_name // nav drawer close - description for accessibility
            ){
                public void onDrawerClosed(View view) {
                    //getSupportActionBar().setTitle(mTitle);
                    setTitle(mTitle);
                    // calling onPrepareOptionsMenu() to show action bar icons
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    setTitle(mDrawerTitle);
                    //getSupportActionBar().setTitle(mDrawerTitle);
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    invalidateOptionsMenu();
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            if (savedInstanceState == null) {
                // on first time display view for first nav item
                displayView(0);
            }

            mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new RotaFragment();
                break;
            case 2:
                fragment = new ExpensesFragment();
                break;
            case 3:
                fragment = new TradeFragment();
                break;
            case 4:
                fragment = new MessageFragment();
                break;
            case 5:
                fragment = new RulesFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager()
                .beginTransaction()
                    .replace(R.id.frame_container, fragment, "My_Fragment").commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_core, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // toggle nav drawer on selecting action bar app icon/title
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            // Handle action bar actions click
            switch (item.getItemId()) {
                case R.id.action_logout:
                    Intent logOutIntent = new Intent(this, MainActivity.class);
                    //homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logOutIntent);
                    return true;
                case R.id.action_settings:
                    return true;
                case R.id.action_about:
                    Intent aboutIntent = new Intent(this, AboutActivity.class);
                    startActivity(aboutIntent);
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        /***
         * Called when invalidateOptionsMenu() is triggered
         */
        @Override
        public boolean onPrepareOptionsMenu(Menu menu) {
            // if nav drawer is opened, hide the action items
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
            return super.onPrepareOptionsMenu(menu);
        }

        @Override
        public void setTitle(CharSequence title) {

            //SpannableString s = new SpannableString(title);
            //s.setSpan(new TypefaceSpan(this, "MyriadPro-Light.otf"), 0, s.length(),
              //      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Update the action bar title with the TypefaceSpan instance
            //ActionBar actionBar = getActionBar();
            //actionBar.setTitle(s);

            mTitle = title;
            getSupportActionBar().setTitle(mTitle);
        }

        /**
         * When using the ActionBarDrawerToggle, you must call it during
         * onPostCreate() and onConfigurationChanged()...
         */

        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            // Sync the toggle state after onRestoreInstanceState has occurred.
            mDrawerToggle.syncState();
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            // Pass any configuration change to the drawer toggls
            mDrawerToggle.onConfigurationChanged(newConfig);
        }

    /* Add new task button function to take to new activity */

    public void addNewTask(View view){

        Intent intent = new Intent(this, AddNewTaskActivity.class);
        startActivity(intent);
    }

    public void addNewRota(View view){

        //getSupportFragmentManager().beginTransaction().addToBackStack("My_Fragment").commit();

        Intent intent = new Intent(this, AddNewRotaActivity.class);
        startActivity(intent);
    }

    public void addNewExpense(View view){
        Intent intent = new Intent(this, AddNewExpenseActivity.class);
        startActivity(intent);
    }

    public void addNewBill(View view){
        Intent intent = new Intent(this, AddNewBillActivity.class);
        startActivity(intent);
    }


    @Override
    public void onResume(){
        super.onResume();
        getSupportActionBar().setTitle("ShareSpace");
    }

}
