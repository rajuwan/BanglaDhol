package com.ebs.banglalinkbangladhol.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;

public class InfoActivity extends AppCompatActivity {

    private ProgressDialog pd;
    public String val, res, message, title, type;
    private TextView tv_device_Brand, tv_device_Model, tv_device_Api,
            tv_device_Version;
    private LinearLayout llinfo;

    private TextView tv_info;
    private String response;
    ProgressBar splashbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {

            Intent intent = getIntent();
            title = intent.getStringExtra("Title");
            type = intent.getStringExtra("Type");

        } catch (NullPointerException e) {

            Log.d("Tag", "Empty field");

        }

        getSupportActionBar().setTitle(title);

        llinfo = (LinearLayout) findViewById(R.id.ll_appinfo);
        tv_device_Brand = (TextView) findViewById(R.id.device_Brand);
        tv_device_Model = (TextView) findViewById(R.id.device_Model);
        tv_device_Api = (TextView) findViewById(R.id.device_Api);
        tv_device_Version = (TextView) findViewById(R.id.device_Version);

        tv_info = (TextView) findViewById(R.id.tv_info);
        splashbar = (ProgressBar) findViewById(R.id.infoProgress);

        if(type.contains("info")){

            llinfo.setVisibility(View.VISIBLE);

            try {

                tv_device_Brand.setText("Brand : " + CheckUserInfo.brand);
                tv_device_Model.setText("Model : " + CheckUserInfo.model);
                tv_device_Api.setText("OS API : " + CheckUserInfo.sdkVersion + ", "
                        + CheckUserInfo.release);
                tv_device_Version.setText("Current Version : "
                        + CheckUserInfo.versionCode);

            } catch (Exception ex) {

                Log.d("TAG", "Error while retreving device data ");

            }

        } else{

            try {

                splashbar.setVisibility(View.VISIBLE);

                RequestThread reqThread = new RequestThread();

                reqThread.start();

            } catch (Exception ex) {

                Log.d("TAG", "Error in request thread");
            }

        }

    }

    public class RequestThread extends Thread {
        @Override
        public void run() {

            synchronized (this) {

                try {

                    response = ServerUtilities.checkAppInfo(InfoActivity.this,
                            type, CheckUserInfo.getUserMsisdn());

                } catch (Exception ex) {

                    Log.d("TAG", "Error in request thread");
                }

            }
            handler.sendEmptyMessage(0);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            if (response != null) {

                try {

                    JSONObject respObject = new JSONObject(response);
                    String ds = respObject.getString("details");
                    tv_info.setText(Html.fromHtml(ds));
                    // just adding a tag

                } catch (Exception e) {

                }

            } else {
                Toast.makeText(InfoActivity.this,
                        "No Internet Connection, Please Try Again Later.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    };
}
