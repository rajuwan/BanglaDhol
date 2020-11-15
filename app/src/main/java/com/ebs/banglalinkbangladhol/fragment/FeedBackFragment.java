package com.ebs.banglalinkbangladhol.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.activity.BanglaDholSignUpLogInActivity;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedBackFragment extends Fragment {


    public FeedBackFragment() {
        // Required empty public constructor
    }

    ProgressBar splashbar;
    private ProgressDialog pd;

    private TextView tv_fb_header, tv_textcount, tv_sendFeedback;
    private EditText et_userFeedback;
    private String user_feedback = "";


    public static final String FRAGMENT_TAG = "FeedBackFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_feed_back, container, false);

        splashbar = (ProgressBar) rootView.findViewById(R.id.feedbackProgress);
        tv_fb_header = (TextView) rootView.findViewById(R.id.tv_fb_loginText);
        et_userFeedback = (EditText) rootView.findViewById(R.id.et_userFeedBack);
        tv_textcount = (TextView) rootView.findViewById(R.id.tv_textCount);
        tv_sendFeedback = (TextView) rootView.findViewById(R.id.btnSend);

        et_userFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {

                    tv_textcount.setText(String.valueOf(s.length()) + " of 500");

                } catch (Exception ex){
                    Log.d("Error afterTextChanged", ex.toString());
                }
            }
        });

        tv_sendFeedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    if (!CheckUserInfo.isValidPhoneNumber(CheckUserInfo
                            .getUserMsisdn())) {

                        Intent mainIntent = new Intent(getActivity(), BanglaDholSignUpLogInActivity.class);
                        startActivity(mainIntent);

                    } else {

                        user_feedback = et_userFeedback.getText().toString().trim();

                        if (user_feedback.length() >= 5) {

                            PostFeedBack();

                        } else {

                            Toast.makeText(getActivity(), "Please type at least 5 letters",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                } catch (Exception ex){

                    Log.d("Error in Refund", ex.toString());

                }

            }
        });

        /*rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    Fragment fragment = new HomeFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.home_container, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                    return true;
                } else {
                    return false;
                }
            }
        });*/

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        setHeaderText();
    }

    public void setHeaderText(){

        try{

            if(CheckUserInfo.isValidPhoneNumber(CheckUserInfo.getUserMsisdn())){
                tv_fb_header.setText("Your Account no : " + CheckUserInfo.getUserMsisdn());
            } else {
                tv_fb_header.setText("Please LogIn to send feedback");
            }

        } catch (Exception ex){
            Log.d("setHeaderText Error", ex.toString());
        }
    }

    public void PostFeedBack() {

        try {

            pd = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Sending Feedback...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

            RequestThreadPostFeedBack reqThread = new RequestThreadPostFeedBack();

            reqThread.start();

        } catch (Exception ex) {

            Log.d("TAG", "Error Occured");
        }
    }

    public class RequestThreadPostFeedBack extends Thread {

        @Override
        public void run() {

            synchronized (this) {

                try {

                    ServerUtilities.postFeedBack(CheckUserInfo.getUserMsisdn(), user_feedback);

                } catch (Exception e) {

                    Log.d("TAG", "Error Occured");

                }

            }

            comment_handler.sendEmptyMessage(0);

        }
    }

    Handler comment_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            pd.dismiss();
            et_userFeedback.setText("");
            Toast.makeText(getActivity(), "Send Successfully", Toast.LENGTH_LONG).show();
        }

    };


}
