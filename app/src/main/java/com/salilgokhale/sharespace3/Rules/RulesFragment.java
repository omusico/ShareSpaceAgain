package com.salilgokhale.sharespace3.Rules;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.SwipeDismissListViewTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salilgokhale on 04/03/15.
 */
public class RulesFragment extends Fragment {

    public RulesFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_rules, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        final View rootView = getView();
        final Button bt = (Button) rootView.findViewById(R.id.add_rule_button);
        final EditText et = (EditText) rootView.findViewById(R.id.add_rule_edit_text);

        ParseUser user = ParseUser.getCurrentUser();
        final ParseObject userHouse = user.getParseObject("Home");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rules");
        query.whereEqualTo("House", userHouse);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> rules, ParseException e) {

                    Log.d("QueryRules:", "Rules found");

                    final ArrayList<String> rulesList = new ArrayList<>();
                    final ArrayList<String> rulesIDs = new ArrayList<String>();

                    for (int i = 0; i < rules.size(); i++) {
                        rulesList.add(rules.get(i).getString("ruleTitle"));
                        rulesIDs.add(rules.get(i).getObjectId());
                    }

                    final RulesAdapter mrulesAdapter = new RulesAdapter(getActivity(), rulesList, rulesIDs);



                        ListView listView = (ListView) rootView.findViewById(R.id.rules);


                        listView.setAdapter(mrulesAdapter);




                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String Input = et.getText().toString();

                            if (!Input.equals("")) {

                                /*if (rules.size() == 0) {
                                    rulesList.add(Input);


                                }
                                else {*/


                                rulesList.add(Input);
                                mrulesAdapter.notifyDataSetChanged();

                                ParseObject new_rule = new ParseObject("Rules");
                                new_rule.put("ruleTitle", Input);
                                new_rule.put("House", userHouse);
                                new_rule.saveInBackground();
                                et.setText("");


                            }
                            else{
                                Toast toast = Toast.makeText(getActivity(), "Rule must have name", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    });

                    SwipeDismissListViewTouchListener touchListener =
                            new SwipeDismissListViewTouchListener(
                                    listView,
                                    new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                        @Override
                                        public boolean canDismiss(int position) {
                                            return true;
                                        }

                                        @Override
                                        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                            for (int position : reverseSortedPositions) {
                                                //int realPosition = position - adapter.getRealPosition(position);
                                                //Log.d("Position: ", String.valueOf(position));
                                                //Log.d("Real Position: ", String.valueOf(realPosition));
                                                String ruleID = (String) mrulesAdapter.getItemObjectId(position);
                                                int number2 = 0;
                                                for (int i = 0; i < rulesList.size(); i++) {
                                                    if (rules.get(i).getObjectId().equals(ruleID)) {
                                                        number2 = i;
                                                        break;
                                                    }
                                                }


                                                mrulesAdapter.remove(position);


                                                Log.d("Rule being removed:", rules.get(number2).getString("ruleTitle"));
                                                rules.get(number2).deleteInBackground();
                                            }
                                            mrulesAdapter.notifyDataSetChanged();
                                        }
                                    });
                    listView.setOnTouchListener(touchListener);
                    // Setting this scroll listener is required to ensure that during ListView scrolling,
                    // we don't look for swipes.
                    listView.setOnScrollListener(touchListener.makeScrollListener());

                }
            });



    }


}
