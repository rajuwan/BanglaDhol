package com.ebs.banglalinkbangladhol.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SDPGatewayActivity extends AppCompatActivity {

    public String selected_pack_name, res, message, title, type, url, res_sdp;
    private WebView webView;
    private String isChargeSuccess, serviceid, referenceId = "";

    TextView wait;
    AppEventsLogger logger;
    public AsyncTask<Void, Void, String> mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdp_payment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.banner_webview);
        wait = (TextView) findViewById(R.id.tv_wait);
        logger = AppEventsLogger.newLogger(getApplicationContext());

        try {

            Intent intent = getIntent();
            url = intent.getStringExtra("webUrl");
            selected_pack_name = intent.getStringExtra("pack");

            getSupportActionBar().setTitle("Bangladhol Subscription");
            ShowMap(url);

        } catch (NullPointerException e) {

            Log.d("Tag", "Empty field");
        }
    }

    public void ShowMap(String url){

        try{

            webView.setVisibility(View.VISIBLE);

            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setAllowContentAccess(true);
            //webView.loadUrl(url);
            Map<String, String> params = new HashMap<String, String>();
            params.put("Referer", "http://banglalinkdhol.com");

            webView.loadUrl(url, params);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    try{

                        view.loadUrl(url);

                        if(url.contains("isChargeSuccess")){

                            Uri uri = Uri.parse(url);
                            Set<String> paramNames = uri.getQueryParameterNames();
                            for (String key: paramNames) {

                                if(key.contains("serviceid")){

                                    serviceid = uri.getQueryParameter(key);
                                }

                                if(key.contains("isChargeSuccess")){

                                    isChargeSuccess = uri.getQueryParameter(key);
                                }

                                if(key.contains("referenceId")){

                                    referenceId = uri.getQueryParameter(key);
                                }
                            }

                            postSubStatus(serviceid, isChargeSuccess, referenceId);
                            finish();
                        }

                    } catch (Exception ex){
                        Log.d("web redirect error", ex.toString());
                    }
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    wait.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SDPGatewayActivity.this);
                    String message = "SSL Certificate error.";
                    switch (error.getPrimaryError()) {
                        case SslError.SSL_UNTRUSTED:
                            message = "The certificate authority is not trusted.";
                            break;
                        case SslError.SSL_EXPIRED:
                            message = "The certificate has expired.";
                            break;
                        case SslError.SSL_IDMISMATCH:
                            message = "The certificate Hostname mismatch.";
                            break;
                        case SslError.SSL_NOTYETVALID:
                            message = "The certificate is not yet valid.";
                            break;
                    }
                    message += " Do you want to continue anyway?";

                    builder.setTitle("SSL Certificate Error");
                    builder.setMessage(message);
                    builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.proceed();
                        }
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.cancel();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

        } catch (Exception ex){
            Log.d("ERROR IN BANNER WEBVIEW", ex.toString());
        }

    }

    private void postSubStatus(final String serviceid, final String chargestatus, final String referenceid) {

        try {

            mRegisterTask = new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    // Register on our server
                    // On server creates a new user

                    try {

                        res_sdp = ServerUtilities.updateSubStatus(CheckUserInfo.getUserMsisdn(),
                                serviceid, chargestatus, referenceid);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return res_sdp;
                }

                @Override
                protected void onPostExecute(String result) {

                    String res = "";

                    if (result != null && result.length() > 5) {

                        try {

                            JSONObject m_obj = new JSONObject(result);

                            res  = m_obj.getString("result");

                            if(res.contains("success")){

                                logCompletedRegistrationEvent(selected_pack_name);
                            }

                            Toast.makeText(SDPGatewayActivity.this,
                                    m_obj.getString("response"), Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    mRegisterTask = null;
                }

            };

            mRegisterTask.execute(null, null, null);

        } catch (Exception ex) {
            Log.d("TAG", "Error in main thread");
        }

    }

    public void logCompletedRegistrationEvent (String registrationMethod) {
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, registrationMethod);
        logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, params);
    }

}
