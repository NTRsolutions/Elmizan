package com.sismatix.Elmizan.Activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.R;

public class Chat_activity extends AppCompatActivity {
    WebView webview_chat;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activity);
        webview_chat = (WebView) findViewById(R.id.webview_chat);

        setTitle("Chat");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user_id=getIntent().getStringExtra("user_id");
        Log.e("useridd_29",""+user_id);



//wv_terms_and_condition.loadUrl("http://travel.demoproject.info/api/term_and_condition.php");

        final ProgressDialog progressBar = new ProgressDialog(Chat_activity.this);
        progressBar.setMessage("Please wait...");


       //http://elmizan.demoproject.info/chat-system/index.php?from_user_id=54&to_user_id=106

        String loginid=Login_preference.getuser_id(Chat_activity.this);
        Log.e("loginid",""+loginid);
        webview_chat.loadUrl("http://elmizan.demoproject.info/chat-system/index.php?from_user_id="+loginid+"&to_user_id="+user_id);
        webview_chat.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!progressBar.isShowing()) {
                    progressBar.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
