package com.ebs.banglalinkbangladhol.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.List;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.bean.SubJson;
import com.ebs.banglalinkbangladhol.bean.SubProduct;
import com.ebs.banglalinkbangladhol.json.SubJsonReader;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.HTTPGateway;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;

public class SubscriptionActivity extends AppCompatActivity {

    private ListView list;

    private TextView userStatus;
    private ProgressDialog pd;
    private ProgressBar splashbar;
    private String selected_pack;
    // Subsceme data

    private List<SubProduct> subproducts;
    private CharSequence[] subitems;
    private String url = "";
    private String message;
    private String subBack = "";
    private String unsubBack = "";

    private List<SubJson> jsonSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userStatus = (TextView) findViewById(R.id.tv_sub_head);
        list = (ListView) findViewById(android.R.id.list);
        splashbar = (ProgressBar) findViewById(R.id.splashSUBProgress);

        setTitle("Subscription");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        Invoke();

        super.onResume();
    }

    public void Invoke() {

        try {

            splashbar.setVisibility(View.VISIBLE);

            RequestThread reqThread = new RequestThread();

            reqThread.start();

        } catch (Exception ex) {

            Log.d("TAG", "Error in main thread");
        }
    }

    /**
     * To get User status using -- Thread*
     */
    public class RequestThread extends Thread {

        @Override
        public void run() {

            Looper.prepare();

            synchronized (this) {

                try {

                    CheckUserInfo.getUserMsisdnInfo(getApplicationContext());

                    CheckUserInfo.getUserLoginInfo(getApplicationContext(),
                            CheckUserInfo.getUserMsisdn(),
                            CheckUserInfo.getUserPinCode());

                    JSONObject jsonObject = ServerUtilities
                            .requestForSubSchemes(getApplicationContext(),
                                    CheckUserInfo.getUserMsisdn());

                    if (jsonObject != null) {

                        subproducts = SubJsonReader.getHome(jsonObject);

                    }

                } catch (Exception ex) {

                    Log.d("TAG", "Error in search");
                }

            }

            sub_handler.sendEmptyMessage(0);

        }
    }

    @SuppressLint("HandlerLeak")
    Handler sub_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            //pd.dismiss();
            splashbar.setVisibility(View.GONE);

            if (subproducts != null) {

                list.setAdapter(new SubAdapter());

                userStatus.setText(CheckUserInfo.getUserPackText());

            } else {

                userStatus.setText("Please try again later");

            }

        }

    };

    public class SubAdapter extends BaseAdapter {

        private class ViewHolder {
            public TextView sub_text, sub_txt_button;
            public RelativeLayout rl;
        }

        @Override
        public int getCount() {

            return subproducts.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = convertView;
            final ViewHolder holder;
            if (convertView == null) {

                view = mInflater.inflate(R.layout.item_subscription, parent,
                        false);
                holder = new ViewHolder();

                holder.rl = (RelativeLayout) view.findViewById(R.id.rl_box);

                holder.sub_text = (TextView) view
                        .findViewById(R.id.tv_sub_type);
                holder.sub_txt_button = (TextView) view
                        .findViewById(R.id.tv_item_subscription);

                view.setTag(holder);

            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.sub_text.setText(subproducts.get(position).getText());

            if (CheckUserInfo.getUserPackCode().contains(
                    subproducts.get(position).getPack())) {

                holder.sub_txt_button.setText("Unsubscribe");
                holder.sub_txt_button.setEnabled(true);
                holder.rl.setBackgroundResource(R.drawable.sub_type_selected);

            } else {

                if (CheckUserInfo.getUserPackCode().contains("nopack")) {

                    holder.sub_txt_button.setText("Subscribe");
                    holder.sub_txt_button.setEnabled(true);

                } else {

                    holder.sub_txt_button.setText("Subscribe");
                    holder.sub_txt_button.setEnabled(false);

                }

            }

            holder.sub_txt_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (holder.sub_txt_button.isEnabled() == false) {

                        Toast.makeText(
                                SubscriptionActivity.this,
                                "You need to Unsubscribe from current package to get a new one",
                                Toast.LENGTH_LONG).show();

                    }

                    String subtxt = holder.sub_txt_button.getText().toString();
                    selected_pack = subproducts.get(position).getPack();

                    if (subtxt.contains("Subscribe")) {

                        if (selected_pack != null) {

                            if (!CheckUserInfo.isValidPhoneNumber(CheckUserInfo
                                    .getUserMsisdn())) {

                                Intent mainIntent = new Intent(SubscriptionActivity.this, BanglaDholSignUpLogInActivity.class);
                                startActivity(mainIntent);

                            } else {

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        SubscriptionActivity.this,
                                        AlertDialog.THEME_HOLO_DARK);
                                alertDialogBuilder.setTitle(Html
                                        .fromHtml("<font color='#F07400'>Subscription</font>"));
                                alertDialogBuilder.setMessage(subproducts.get(
                                        position).getRegMsg());
                                // set dialog message
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton(
                                                "Confirm",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int id) {

                                                        if (isValidPhoneNumber(CheckUserInfo
                                                                .getUserMsisdn())) {

                                                            Subscription();

                                                        } else {

                                                            try {

                                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                        SubscriptionActivity.this,
                                                                        AlertDialog.THEME_HOLO_DARK);
                                                                alertDialogBuilder
                                                                        .setTitle(Html
                                                                                .fromHtml("<font color='#F07400'>Dear User</font>"));
                                                                alertDialogBuilder
                                                                        .setMessage(CheckUserInfo
                                                                                .getUserPackText());

                                                                // set dialog
                                                                // message
                                                                alertDialogBuilder
                                                                        .setCancelable(
                                                                                false)
                                                                        .setPositiveButton(
                                                                                "OK",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(
                                                                                            DialogInterface dialog,
                                                                                            int id) {

                                                                                        dialog.dismiss();

                                                                                    }
                                                                                });

                                                                // create alert
                                                                // dialog
                                                                AlertDialog alertDialog = alertDialogBuilder
                                                                        .create();
                                                                // show it
                                                                alertDialog
                                                                        .show();

                                                            } catch (Exception ex) {

                                                                Log.d("TAG",
                                                                        "Error Occured");

                                                            }

															/*
															 * Intent mainIntent
															 * = new Intent(
															 * "com.ebs.banglaflix.BANGLAFLIXSIGNUPLOGINACTIVITY"
															 * ); startActivity(
															 * mainIntent);
															 */

                                                        }

                                                    }
                                                })
                                        .setNegativeButton(
                                                "Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder
                                        .create();
                                // show it
                                alertDialog.show();

                            }

                        }

                    } else if (subtxt.contains("Unsubscribe")) {

                        if (selected_pack != null) {

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    SubscriptionActivity.this, AlertDialog.THEME_HOLO_DARK);
                            alertDialogBuilder.setTitle(Html
                                    .fromHtml("<font color='#F07400'>UnSubscription</font>"));
                            alertDialogBuilder
                                    .setMessage("Are you sure want to unsubscribe from "
                                            + subproducts.get(position)
                                            .getName() + " pack?");
                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            "Confirm",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {

                                                    UnSubscription();

                                                }
                                            })
                                    .setNegativeButton(
                                            "Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {
                                                    dialog.cancel();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder
                                    .create();
                            // show it
                            alertDialog.show();

                        }

                    } else {

                        Toast.makeText(SubscriptionActivity.this,
                                "Can't process your request now",
                                Toast.LENGTH_SHORT).show();

                    }

                }
            });

            return view;
        }
    }

    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{13}$";
        return mobile.matches(regEx);
    }

    public void Subscription() {

        try {

            pd = new ProgressDialog(SubscriptionActivity.this, ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Sending Subscription Request ...");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();

            RequestThreadSubscription reqThread = new RequestThreadSubscription();

            reqThread.start();

        } catch (Exception ex) {

            Log.d("TAG", "Error Occured");
        }
    }

    public class RequestThreadSubscription extends Thread {

        @Override
        public void run() {

            synchronized (this) {

                try {

                    subBack = ServerUtilities.requestForSubscription(CheckUserInfo.getUserMsisdn(),
                            selected_pack);

                } catch (Exception e) {

                    Log.d("TAG", "Error Occured");

                }

            }

            subscription_handler.sendEmptyMessage(0);

        }
    }

    Handler subscription_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            pd.dismiss();

            if (subBack != null && subBack.length() > 5) {

                try {

                    String response = "";
                    int play = 0;

                    jsonSub = HTTPGateway.getSubCredential(subBack);

                    for (int i = 0; i < jsonSub.size(); i++) {

                        SubJson tmpData = jsonSub.get(i);

                        response = tmpData.getResponse();
                        play = tmpData.getPlay();

                    }

                    if (play == 1) {

                        Toast.makeText(SubscriptionActivity.this, response,
                                Toast.LENGTH_LONG).show();

                        Invoke();

                    } else {

                        userStatus.setText(response);

                    }

                } catch (Exception e) {

                    Log.d("Tag", "Error while get from signup");

                }

            }

        }

    };

    public void UnSubscription() {

        try {

            pd = new ProgressDialog(SubscriptionActivity.this,
                    ProgressDialog.THEME_HOLO_DARK);
            pd.setMessage("Sending UnSubscription Request ...");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();

            RequestThreadUnSubscription reqThread = new RequestThreadUnSubscription();

            reqThread.start();

        } catch (Exception ex) {

            Log.d("TAG", "Error Occured");
        }
    }

    public class RequestThreadUnSubscription extends Thread {

        @Override
        public void run() {

            synchronized (this) {

                try {

                    unsubBack = ServerUtilities.requestForUnSubscription(
                            SubscriptionActivity.this, CheckUserInfo.getUserMsisdn(),
                            CheckUserInfo.deviceId, CheckUserInfo.imsi,
                            CheckUserInfo.imei, CheckUserInfo.softwareVersion,
                            CheckUserInfo.simSerialNumber,
                            CheckUserInfo.operator, CheckUserInfo.operatorName,
                            CheckUserInfo.brand, CheckUserInfo.model,
                            CheckUserInfo.release, CheckUserInfo.sdkVersion,
                            CheckUserInfo.versionCode);

					/*
					 * unsubBack = ServerUtilities.requestForUnSubscription(
					 * getActivity(), username, deviceId, imsi, imei,
					 * softwareVersion, simSerialNumber, operator, operatorName,
					 * brand, model, release, sdkVersion, versionCode);
					 */

                } catch (Exception e) {

                    Log.d("TAG", "Error Occured");

                }

            }

            unsubscription_handler.sendEmptyMessage(0);

        }
    }

    Handler unsubscription_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            pd.dismiss();

            if (unsubBack != null && unsubBack.length() > 5) {

                try {

                    String response = "";
                    int play = 0;

                    jsonSub = HTTPGateway.getSubCredential(unsubBack);

                    for (int i = 0; i < jsonSub.size(); i++) {

                        SubJson tmpData = jsonSub.get(i);

                        response = tmpData.getResponse();
                        play = tmpData.getPlay();

                    }

                    if (play == 0) {

                        CheckUserInfo.changePlayToSp(SubscriptionActivity.this, play);

                        Toast.makeText(SubscriptionActivity.this, response,
                                Toast.LENGTH_LONG).show();

                        Invoke();

                    } else if (response != null) {

                        userStatus.setText(response);

                    }

                } catch (Exception e) {

                    Log.d("Tag", "Error while get from unsub");

                }

            }

        }

    };
}
