package com.salilgokhale.sharespace3.Expenses.ItemTabFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.SwipeDismissListViewTouchListener;

import java.util.List;

/**
 * Created by salilgokhale on 25/03/15.
 */
public class CommonItemsFragment extends Fragment {

    public CommonItemsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        return rootView;
    }

/*
    @Override
    public void onResume() {
        super.onResume();

        final View rootView = getView();


        if (rootView != null) {

            final Button bt = (Button) rootView.findViewById(R.id.add_item_button);
            final EditText et = (EditText) rootView.findViewById(R.id.add_item_edit_text);

            final ParseUser user = ParseUser.getCurrentUser();
            final ParseObject house = user.getParseObject("Home");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("CommonItem");
            query.whereEqualTo("House", house);

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(final List<ParseObject> itemsList, ParseException e) {
                    if (itemsList != null) {
                        Log.d("QueryItems:", "Items Found");

                        final BillsAdapter adapter = new BillsAdapter(getActivity(), itemsList);
                        ListView listView = (ListView) rootView.findViewById(R.id.myitems);
                        listView.setAdapter(adapter);


                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String Input = et.getText().toString();

                                if (!Input.equals("")) {

                                /*if (rules.size() == 0) {
                                    rulesList.add(Input);


                                }
                                else {*/
/*
                                    ParseObject newItem = new ParseObject("CommonItem");
                                    newItem.put("Name", Input);
                                    newItem.put("House", house);
                                    newItem.put("Due", false);
                                    itemsList.add(newItem);
                                    adapter.notifyDataSetChanged();

                                    newItem.saveInBackground();
                                    et.setText("");


                                } else {
                                    Toast toast = Toast.makeText(getActivity(), "Item must have name", Toast.LENGTH_LONG);
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
                                                    String itemID = (String) adapter.getItem(position).getObjectId();
                                                    int number2 = 0;
                                                    for (int i = 0; i < itemsList.size(); i++) {
                                                        if (itemsList.get(i).getObjectId().equals(itemID)) {
                                                            number2 = i;
                                                            break;
                                                        }
                                                    }

                                                    adapter.remove(position);

                                                    Log.d("Item being removed:", itemsList.get(number2).getString("Name"));
                                                    itemsList.get(number2).deleteInBackground();
                                                }


                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                        listView.setOnTouchListener(touchListener);
                        // Setting this scroll listener is required to ensure that during ListView scrolling,
                        // we don't look for swipes.
                        listView.setOnScrollListener(touchListener.makeScrollListener());

                    }
                }
                });




            }
    } */
}
