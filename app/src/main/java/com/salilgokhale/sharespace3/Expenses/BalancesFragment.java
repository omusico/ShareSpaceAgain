package com.salilgokhale.sharespace3.Expenses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.Expenses.BalancesAdapter;
import com.salilgokhale.sharespace3.Expenses.BalancesObject;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.Expenses.ViewBalanceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salilgokhale on 17/03/15.
 */
public class BalancesFragment extends Fragment {


    public BalancesFragment() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_balances, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        final View rootView = getView();

        if (rootView != null) {

            final TextView toPayText = (TextView) rootView.findViewById(R.id.to_pay);
            final TextView toBePaidText = (TextView) rootView.findViewById(R.id.to_be_paid);
            final ParseUser user = ParseUser.getCurrentUser();
            final String username = user.getString("name");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("OweExpense");
            query.whereEqualTo("OwnerArray", user);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(final List<ParseObject> OweExpenseList, ParseException e) {
                    if (OweExpenseList != null) {
                        Log.d("Owe Expenses", "found");

                        Log.d("No. of OweExpenses: ", String.valueOf(OweExpenseList.size()));

                        List<String> Debtors = new ArrayList<>();
                        List<String> Debts = new ArrayList<>();
                        List<BalancesObject.ColourStatus> Colours = new ArrayList<>();
                        Float ToPay = 0.0f;
                        Float ToBePaid = 0.0f;


                        for (int i = 0; i < OweExpenseList.size(); i++) {

                            ParseObject temp = OweExpenseList.get(i);
                            String debt;
                            Number tempamount = temp.getNumber("Amount");
                            Float amount = tempamount.floatValue();

                            if (temp.getString("Name1").equals(username)) {
                                Debtors.add(temp.getString("Name2"));

                                Log.d("Amount: ", String.valueOf(amount));
                                if (amount > 0) {
                                    debt = "£" + String.format("%.2f", amount) + " to be paid";
                                    ToBePaid += amount;
                                    Colours.add(BalancesObject.ColourStatus.GREEN);
                                } else if (amount < 0) {
                                    debt = "£" + String.format("%.2f", Math.abs(amount)) + " to pay";
                                    ToPay += amount;
                                    Colours.add(BalancesObject.ColourStatus.RED);
                                } else {
                                    debt = "Even";
                                    Colours.add(BalancesObject.ColourStatus.NEUTRAL);
                                }

                            } else {
                                Debtors.add(temp.getString("Name1"));

                                Log.d("Amount: ", String.valueOf(amount));
                                if (amount < 0) {
                                    debt = "£" + String.format("%.2f", Math.abs(amount)) + " to be paid";
                                    ToBePaid += amount;
                                    Colours.add(BalancesObject.ColourStatus.GREEN);
                                } else if (amount > 0) {
                                    debt = "£" + String.format("%.2f", amount) + " to pay";
                                    ToPay += amount;
                                    Colours.add(BalancesObject.ColourStatus.RED);
                                } else {
                                    debt = "Even";
                                    Colours.add(BalancesObject.ColourStatus.NEUTRAL);
                                }


                            }


                            Debts.add(debt);

                        }

                        toPayText.setText("£" + String.format("%.2f", Math.abs(ToPay)) + " to pay");
                        toBePaidText.setText("£" + String.format("%.2f", Math.abs(ToBePaid)) + " to be paid");

                        ArrayList<BalancesObject> objects = new ArrayList<>();

                        for (int i = 0; i < OweExpenseList.size(); i++) {
                            BalancesObject temp = new BalancesObject(Debtors.get(i), Debts.get(i), Colours.get(i));
                            objects.add(temp);
                            Log.d("Object Name: ", objects.get(i).getBname());
                            Log.d("Object Debt: ", objects.get(i).getBdebt());
                        }


                        ListView listView = (ListView) rootView.findViewById(R.id.myoweexpenses);

                        final BalancesAdapter balancesAdapter = new BalancesAdapter(getActivity(), objects);
                        listView.setAdapter(balancesAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                String objectID = OweExpenseList.get(position).getObjectId();

                                Intent intent = new Intent(getActivity(), ViewBalanceActivity.class);
                                intent.putExtra(Intent.EXTRA_TEXT, objectID);
                                startActivity(intent);

                            }
                        });


                    } else {
                        Log.d("Owe Expenses", "not found");
                    }
                }
            });


        }

    }
}
