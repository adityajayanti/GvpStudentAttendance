package com.example.aditya.gvpstudentattendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AttendancePercent extends AppCompatActivity  implements View.OnClickListener{

    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    String dept , sem;
    Calendar myCalendar;
    EditText edittext_Datefrom , edittext_DateTo;
    DatePickerDialog.OnDateSetListener date;



    private void loadProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.StuOverallAttendanceURL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                                JSONArray jobject = new JSONArray(response);
                                JSONObject object = jobject.getJSONObject(0);

                                String SID = object.getString("sid");
                                String SNAME = object.getString("sname");
                                String SAttended = object.getString("presentCount");
                                String SHelded = object.getString("totalCount");
                                String SPer = object.getString("percentAtt");
                                Log.i("Params result",SID+SNAME+" "+SAttended+" "+SHelded+" "+SPer);

                                MainPageContents mainPageContents = new MainPageContents(SID,SNAME,SAttended,SHelded,SPer);
                                contentsList.add(mainPageContents);

                            contentsAdapter = new ContentsAdapter(AttendancePercent.this, contentsList, 4);
                            //setting adapter to recyclerview
                            recyclerView.setAdapter(contentsAdapter);
                            contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Toast.makeText(getApplicationContext(),"More Student Information",Toast.LENGTH_SHORT).show();

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cid",SharedPrefManager.getInstance(AttendancePercent.this).getUsername());
                params.put("dept",dept);
                params.put("sem",sem);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_percent);

        DataTimeHandler dataTimeHandler = new DataTimeHandler();
        getSupportActionBar().setTitle(dataTimeHandler.getActionBarDate());

        myCalendar = Calendar.getInstance();
        dept = SharedPrefManager.getInstance(this).getFid();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentsList = new ArrayList<>();
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
                params.put("college_id",SharedPrefManager.getInstance(AttendancePercent.this).getUsername());
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }





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
                startActivity(new Intent(getApplicationContext(),HomeMenu.class));
                finish();
                break;

            case R.id.hometo:
                startActivity(new Intent(getApplicationContext(), HomeMenu.class));
                finish();

        }
        return  true;

    }

    @Override
    public void onClick(View v) {

    }
}
