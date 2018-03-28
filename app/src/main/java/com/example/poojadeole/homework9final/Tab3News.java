package com.example.poojadeole.homework9final;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by poojadeole on 11/18/17.
 */

public class Tab3News extends Fragment {
    private RecyclerView newsView;
    private NewsAdapter newsadapter;
    String myxmlResponse;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context mycontext;
        mycontext = this.getActivity();
        Log.d("Making request again","hello");
        View layout = inflater.inflate(R.layout.tab3news, container, false);
        newsView = (RecyclerView) layout.findViewById(R.id.newstable);
        String symPassed = ((SendString)getActivity()).message;
        //    Log.d("Symbol",symPassed);
        String XmlURL = "http://demoapplication-env.us-east-2.elasticbeanstalk.com/?symbol="+symPassed+"&indicator=XML";


//
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest req = new StringRequest(Request.Method.GET, XmlURL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //processData(response);

                            myxmlResponse = response;

                            newsView.setHasFixedSize(true);
                            //newsView.setItemAnimator(new DefaultItemAnimator());
                            newsView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            newsView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
                            newsadapter = new NewsAdapter(getActivity(),getData());
                            newsView.setAdapter(newsadapter);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle error response
                        Toast.makeText(mycontext,"Error while fetching API", Toast.LENGTH_SHORT).show();
                        Log.e("Volley", "Error11" + error.toString());
                    }
                }
        );
        queue.add(req);


        return layout;
    }



    public List<NewsInfo> getData() throws IOException, XmlPullParserException{
        int newscounter = 0;
        StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
        List<StackOverflowXmlParser.Entry> entries = null;
        List<NewsInfo> newsdata = new ArrayList<>();
        List<String> ntitles = new ArrayList<>();
        List<String> nauthors = new ArrayList<>();
        List<String> nlinks = new ArrayList<>();
        List<String> ndates = new ArrayList<>();
        Log.d("XMLSymbol",myxmlResponse);
        InputStream stream = new ByteArrayInputStream(myxmlResponse.getBytes(StandardCharsets.UTF_8.name()));
        entries = stackOverflowXmlParser.parse(stream);
        for (StackOverflowXmlParser.Entry entry : entries){
            if(entry.link.startsWith("https://seekingalpha.com/article/")){
                nauthors.add(entry.author);
                ntitles.add(entry.title);
                String replacedStr = entry.date.replace("-0500", "EDT");
                nlinks.add(entry.link);
                ndates.add(replacedStr);
                Log.d("Entry",entry.title);
                Log.d("Entry",entry.author);
                Log.d("Link Entry",entry.link);
            }

        }

        for(int i=0; i< nauthors.size(); i++){
            if(newscounter < 5){
                NewsInfo currentnews = new NewsInfo();
                currentnews.nauthor = "Author: "+nauthors.get(i);
                currentnews.ntitle = ntitles.get(i);
                currentnews.nlink = nlinks.get(i);

                currentnews.ndate = ndates.get(i);
                newsdata.add(currentnews);
                newscounter++;
            }
        }
        return newsdata;
    }



//    public void processData(String xmlResponse) throws IOException, XmlPullParserException {
//
//    }


}


