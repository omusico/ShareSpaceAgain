package com.salilgokhale.sharespace3.TradeTabFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.salilgokhale.sharespace3.R;
import com.salilgokhale.sharespace3.TradeTabFragments.TradeTaskFragments.ConfirmBidActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salilgokhale on 26/03/15.
 */
public class BidOffersFragment extends Fragment {

    public BidOffersFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_bid_offers, container, false);

        final ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("TradeOffer");
        query1.whereEqualTo("SubmitTrader", user);
        query1.include("ReceiveTrader");
        query1.include("SubmitterTasks");
        query1.include("ReceiverTasks");
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {

                ArrayList<BidOffersObject> bids = new ArrayList<BidOffersObject>();


                for (int i = 0; i < results.size(); i++) {
                    ParseObject temp = results.get(i);
                    String submitterTasks = "";
                    String receiverTasks = "";

                    ArrayList<ParseObject> submitterArray = (ArrayList<ParseObject>) temp.get("SubmitterTasks");
                    ArrayList<ParseObject> receiverArray = (ArrayList<ParseObject>) temp.get("ReceiverTasks");

                    for (int j = 0; j < submitterArray.size(); j++) {
                        if (j == 0) {
                            submitterTasks = submitterArray.get(j).getString("Name");
                        } else if (j == 2) {
                            submitterTasks += ", ...";
                            break;
                        } else {
                            submitterTasks += ", " + submitterArray.get(j).getString("Name");
                        }
                    }

                    for (int l = 0; l < receiverArray.size(); l++) {
                        if (l == 0) {
                            receiverTasks = receiverArray.get(l).getString("Name");
                        } else if (l == 2) {
                            receiverTasks += ", ...";
                            break;
                        } else {
                            receiverTasks += ", " + receiverArray.get(l).getString("Name");
                        }

                    }

                    BidOffersObject bid = new BidOffersObject(temp.getParseObject("ReceiveTrader").getString("name")
                            , "My: " + submitterTasks, "For: " + receiverTasks, temp.getString("Received"),
                            temp.getObjectId());
                    bids.add(bid);


                }

                final BidOffersAdapter bidAdapter = new BidOffersAdapter(getActivity(), bids);
                ListView listView1 = (ListView) rootView.findViewById(R.id.Bids);
                listView1.setAdapter(bidAdapter);

                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {

                        String id = bidAdapter.getItem(position).getBObjectID();
                        Intent intent = new Intent(getActivity(), ConfirmBidActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT, id);
                        startActivity(intent);
                    }
                });

                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("TradeOffer");
                query2.whereEqualTo("ReceiveTrader", user);
                query2.include("SubmitTrader");
                query2.include("SubmitterTasks");
                query2.include("ReceiverTasks");
                query2.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> results2, ParseException e) {
                        if( e == null){

                            ArrayList<BidOffersObject> offers = new ArrayList<BidOffersObject>();

                            for (int i = 0; i < results2.size(); i++) {
                                ParseObject temp = results2.get(i);
                                String submitterTasks = "";
                                String receiverTasks = "";

                                ArrayList<ParseObject> submitterArray = (ArrayList<ParseObject>) temp.get("SubmitterTasks");
                                ArrayList<ParseObject> receiverArray = (ArrayList<ParseObject>) temp.get("ReceiverTasks");

                                for (int j = 0; j < submitterArray.size(); j++) {
                                    if (j == 0) {
                                        submitterTasks = submitterArray.get(j).getString("Name");
                                    } else if (j == 2) {
                                        submitterTasks += ", ...";
                                        break;
                                    } else {
                                        submitterTasks += ", " + submitterArray.get(j).getString("Name");
                                    }
                                }

                                for (int l = 0; l < receiverArray.size(); l++) {
                                    if (l == 0) {
                                        receiverTasks = receiverArray.get(l).getString("Name");
                                    } else if (l == 2) {
                                        receiverTasks += ", ...";
                                        break;
                                    } else {
                                        receiverTasks += ", " + receiverArray.get(l).getString("Name");
                                    }

                                }


                                BidOffersObject offer = new BidOffersObject(temp.getParseObject("SubmitTrader").getString("name")
                                        , "My: " + receiverTasks, "For: " + submitterTasks, temp.getString("Received"),
                                        temp.getObjectId());
                                offers.add(offer);



                            }

                            final BidOffersAdapter offersAdapter = new BidOffersAdapter(getActivity(), offers);
                            ListView listView2 = (ListView) rootView.findViewById(R.id.Offers);
                            listView2.setAdapter(offersAdapter);

                            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {

                                    String id = offersAdapter.getItem(position).getBObjectID();
                                    Intent intent = new Intent(getActivity(), ViewBidOfferActivity.class);
                                    intent.putExtra(Intent.EXTRA_TEXT, id);
                                    startActivity(intent);
                                }
                            });


                        }
                    }
                });

            }
        });


        return rootView;

    }
}
