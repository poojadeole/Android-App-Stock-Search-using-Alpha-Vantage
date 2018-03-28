package com.example.poojadeole.homework9final;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

/**
 * Created by poojadeole on 11/18/17.
 */

public class Tab2History extends Fragment {

    WebView webView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.tab2history, container, false);
//        webView = (WebView) rootView.findViewById(R.id.webview);
//        return rootView;
        String symPassed = ((SendString)getActivity()).message;
        String url  = "http://www-scf.usc.edu/~deole/highstocks.php/?sym="+symPassed;
            super.onCreate(savedInstanceState);
            View rootView = inflater.inflate(R.layout.tab2history, container, false);
            //setContentView(R.layout.tab2history);
            WebView view = (WebView) rootView.findViewById(R.id.webView);
            view.getSettings().setJavaScriptEnabled(true);
            view.setWebChromeClient(new WebChromeClient());
            view.loadUrl(url);

            return rootView;

    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("http://www-scf.usc.edu/~deole/highstocks.html");
//
//    }
}
