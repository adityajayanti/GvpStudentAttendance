package com.example.aditya.gvpstudentattendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    String dayname ,cid ,aNO ,sid;
    String sem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        getSupportActionBar().setTitle(SharedPrefManager.getInstance(this).getUsername());
        sid = SharedPrefManager.getInstance(this).getUsername();
        cid = SharedPrefManager.getInstance(this).getFid();

        Intent intent =getIntent();
        aNO = intent.getStringExtra("activityno").toString();
        dayname= intent.getStringExtra("weekname").toString();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentsList = new ArrayList<>();
        //adding some items to our list
        loadsem();


    }

    private  void loadsem(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.StuSem,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                               sem = jsonObject.getString("stu_sem");
                                Log.i("Sem",sem);
                                loadProducts();

                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error Occured" , Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String > params = new HashMap<>();
                Log.i("cid",sid);
                params.put("college_id",sid);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
    private void loadProducts(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.StuTimeTableURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray timetablelist = new JSONArray(response);
                            for(int i = 0 ; i<timetablelist.length();i++){
                                JSONObject object = timetablelist.getJSONObject(i);

                                String hour = object.getString("hour");
                                String course_name = object.getString("cname");

                                MainPageContents mainPageContents = new MainPageContents(course_name,hour);
                                contentsList.add(mainPageContents);

                            }

                            contentsAdapter = new ContentsAdapter(SubjectActivity.this, contentsList,1);
                            //setting adapter to recyclerview
                            recyclerView.setAdapter(contentsAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error Occured" , Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String > params = new HashMap<>();
                Log.i("Params",sid +" "+cid+" "+sem+" "+dayname);
                params.put("college_id",sid);
                params.put("dept",cid);
                params.put("sem",sem);
                params.put("day_name",dayname);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.features_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.Logout:
                SharedPrefManager.getInstance(this).islogout();
                finish();
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
            case R.id.backto:
                if(aNO.equals("Week")) {
                    startActivity(new Intent(getApplicationContext(), WeekActivity.class));
                } else if (aNO.equals("Main2")) {
                    startActivity(new Intent(getApplicationContext(), HomeMenu.class));
                }
                break;

            case R.id.hometo:
                startActivity(new Intent(getApplicationContext(), HomeMenu.class));
                finish();


        }
        return  true;

    }
}
