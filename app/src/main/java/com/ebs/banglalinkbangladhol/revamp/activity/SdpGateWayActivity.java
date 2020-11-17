package com.ebs.banglalinkbangladhol.revamp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ebs.banglalinkbangladhol.R;

public class SdpGateWayActivity extends AppCompatActivity {

    public String res, message, title, type, url, res_sdp;
    private WebView webView;
    private String isChargeSuccess, serviceid, referenceId = "";

    TextView wait, pageTitle;
    ImageView backBtn;
    //SessionManager sessionManager;

    public AsyncTask<Void, Void, String> mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdp_gate_way);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = findViewById(R.id.sdp_webview);
        wait = findViewById(R.id.tv_wait);
        backBtn = findViewById(R.id.backBtn);
        pageTitle = findViewById(R.id.pageTitle);

        //sessionManager = new SessionManager(getApplicationContext());

        pageTitle.setText("Islamic Portal Subscription");

        try {

            Intent intent = getIntent();
            url = intent.getStringExtra("webUrl");

            //getSupportActionBar().setTitle("Islamic Portal Subscription");
            //ShowMap(url);

        } catch (NullPointerException e) {

            Log.d("Tag", "Empty field");
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Toast.makeText(getApplicationContext(), "url> $url", Toast.LENGTH_SHORT).show();
    }
}
