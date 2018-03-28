package com.example.poojadeole.homework9final;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public EditText editText;
    private SharedPreferences sharedPreferences;
    private RecyclerView favView;
    private FavAdapter favadapter;
    private Button clearButton;
    public List<String> suggest;
    public ArrayAdapter<String> aAdapter;
    TextView favChange;
    public AutoCompleteTextView autoComplete;
    private ArrayList<String> favList = new ArrayList<>();
    JSONObject obj;
    JSONObject metaobj;
    private Context context;
    private ImageButton refresh;
    Switch switchToggle;
    Spinner sortSpinner;
    Spinner orderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Stock News");
        favView = (RecyclerView)findViewById(R.id.favtable);
        clearButton = (Button) findViewById(R.id.button2);
        favView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("favorite", Context.MODE_PRIVATE);
        favChange = (TextView) findViewById(R.id.favChange);
        refresh = (ImageButton) findViewById(R.id.refresh);
        context = this;
        switchToggle = (Switch) findViewById(R.id.toggle);
        orderSpinner = (Spinner) findViewById(R.id.orderSpinner);
        sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
        ArrayAdapter orderAdapter =  ArrayAdapter.createFromResource(this,R.array.order,android.R.layout.simple_spinner_item);
        orderSpinner.setAdapter(orderAdapter);

        ArrayAdapter sortAdapter =  ArrayAdapter.createFromResource(this,R.array.sorting,android.R.layout.simple_spinner_item);
        sortSpinner.setAdapter(sortAdapter);





        autoComplete= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String)parent.getItemAtPosition(position);
                selection  = selection.split("\n")[0];
                autoComplete.setText(selection);


            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoComplete.setText("");
            }
        });

        autoComplete.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub

            }



            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("auto","auto complete called");
                String newText = s.toString();
                new getJsonAutoComplete().executethis(newText);
            }

        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        final Handler ha = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                update();
                ha.postDelayed(this, 20000);
            }
        };
        switchToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchToggle.isChecked()) {
                    ha.postDelayed(r, 20000);
                } else {
                    ha.removeCallbacks(r);
                }
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sort() {
        String sort = sortSpinner.getSelectedItem().toString();
        String order = orderSpinner.getSelectedItem().toString();
        if(favadapter!=null) {
            favadapter.sort(sort, order);
        }

    }

    public void sendMessage(View view){
        String message = autoComplete.getText().toString();
        message = message.trim();
        if(!message.equals("")) {
            Intent intent = new Intent(this, SendString.class);
            intent.putExtra("my_data", message);
            intent.putExtra("favorite", favList.contains(message));
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Please enter a Stock name or a symbol", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    private void update() {
        if(sharedPreferences!=null) {

            Map<String, ?> favoriteList = sharedPreferences.getAll();
            final List<FavInfo> favlist = new ArrayList<>();
            if(favoriteList!=null) {
                favList.clear();
                if(favadapter!=null) {
                    favadapter.clearData();
                }
                for(final Map.Entry<String, ?> entry : favoriteList.entrySet()) {


                    Log.d("Entry",entry.getKey());
                    favList.add(entry.getKey());
                    String refreshUrl = "http://demoapplication-env.us-east-2.elasticbeanstalk.com/?symbol="+entry.getKey()+"&indicator=Price";

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, refreshUrl, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Display the first 500 characters of the response string.
                                obj = response.getJSONObject("Time Series (Daily)");
                                metaobj = response.getJSONObject("Meta Data");
                                Log.d("Reply","Getting respone");
                                int count = 0;
                                String currentKey="";
                                String prevKey = "";
                                Iterator<?> keys =  obj.keys();
                                while( keys.hasNext() && count!=2){
                                    String key = (String) keys.next();
                                    if(count == 0){
                                        currentKey = key;
                                        Log.d("jsonkey",currentKey);
                                    }
                                    else if(count == 1){
                                        prevKey = key;
                                        Log.d("jsonkey",prevKey);
                                    }
                                    count++;
                                }

                                JSONObject obj1 = obj.getJSONObject(currentKey);
                                JSONObject obj2 = obj.getJSONObject(prevKey);
                                Float currclose = Float.valueOf(obj1.getString("4. close"));
                                Float prevclose = Float.valueOf(obj2.getString("4. close"));
                                Float change = ((currclose-prevclose)/prevclose)*100;
//
                                String changeno = String.format("%.2f", change);
                                Float changediff = currclose-prevclose;
                                String change2diff = String.format("%.2f", changediff);
                                String changetoadd = change2diff + "("+ changeno +"%)";
                                Log.d("Curree",changetoadd);
                                FavInfo favInfo = new FavInfo(entry.getKey(),currclose.toString(),changetoadd);
                                favlist.add(favInfo);
                                if (favadapter == null) {
                                    List<FavInfo> f = new ArrayList<>();
                                    f.add(favInfo);
                                    favadapter = new FavAdapter(context, f);
                                    favView.setAdapter(favadapter);
                                } else {
                                    favadapter.addFav(favInfo);
                                }

                                Log.d("TAGthis", "hello");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            sort();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", "Error11" + error.toString());
                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            20000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(stringRequest);


                }
                //addToAdapter(favlist);
            }
        }
    }
    public void addToAdapter(List<FavInfo> favlist){

        if (favadapter == null) {
            favadapter = new FavAdapter(this, favlist);
            favView.setAdapter(favadapter);
        } else {
            favadapter.newFavs(favlist);
        }
    }

    public void removeFav(String str) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(str);
        favList.remove(str);
        favadapter.removeFav(str);
        editor.apply();
    }

    class getJsonAutoComplete{

        public void execute(String newText) {
            String JsonURL = "http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input="+newText;
            Log.d("auto","input "+JsonURL);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, JsonURL, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("auto",response.toString());
                        JSONObject data = response;
                        JSONArray jArray = new JSONArray(data);
                        // Display the first 500 characters of the response string.
                        suggest = new ArrayList<String>();
                        for(int i=0;i<jArray.length();i++){
                            JSONObject jsonobject = jArray.getJSONObject(i);

                            suggest.add(i, Html.fromHtml("<b>"+jsonobject.getString("Symbol")+"</b>")+"\n"+jsonobject.getString("Name")+" ("+jsonobject.getString("Exchange")+")") ;
                        }


                        aAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.autocomplete,suggest);
                        autoComplete.setAdapter(aAdapter);

                        aAdapter.notifyDataSetChanged();

                        Log.d("Reply", "Getting respone");


                        Log.d("TAG", "hello");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", "Error11" + error.toString());
                        }
                    }
            );
            queue.add(stringRequest);
        }


        public void executethis(String newText){
            String JsonURL = "http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input="+newText;
            Log.d("auto","input "+JsonURL);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


            StringRequest req = new StringRequest(Request.Method.GET, JsonURL,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            try {
                                String data = response;
                                Log.d("auto","data"+data);
                                //processData(response);
                                JSONArray jArray = new JSONArray(data);
                                // Display the first 500 characters of the response string.
                                suggest = new ArrayList<String>();
                                for(int i=0;i<jArray.length();i++){
                                    JSONObject jsonobject = jArray.getJSONObject(i);

                                    suggest.add(i, Html.fromHtml("<b>"+jsonobject.getString("Symbol")+"</b>")+"\n"+jsonobject.getString("Name")+" ("+jsonobject.getString("Exchange")+")") ;
                                }


                                aAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.autocomplete,suggest);
                                autoComplete.setAdapter(aAdapter);

                                aAdapter.notifyDataSetChanged();Log.d("Reply", "Getting respone");



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // handle error response
                        }
                    }
            );
            queue.add(req);

        }



    }
}
