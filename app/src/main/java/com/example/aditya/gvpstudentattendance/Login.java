package com.example.aditya.gvpstudentattendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText UserText , PassText;
    Button LogIn;
    ProgressDialog progressDialog;

    private void userLogin(){
        final String col_id = UserText.getText().toString().trim();
        final String course_name = PassText.getText().toString().trim();

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.StuLogURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                col_id,
                                                course_name
                                        );
                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(),HomeMenu.class);
                                intent.putExtra("college_id" ,col_id );
                                startActivity(intent);
                                finish();

                            }else{
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String > params = new HashMap<>();
                params.put("college_id", col_id);
                params.put("course_name" , course_name);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            //SharedPrefManager.getInstance(this).islogout();
            finish();
            startActivity(new Intent(getApplicationContext(),HomeMenu.class));
            return;
        }

        UserText = (EditText) findViewById(R.id.UserText);
        PassText = (EditText) findViewById(R.id.PassText);
        LogIn = (Button) findViewById(R.id.LogIn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        LogIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == LogIn)
            userLogin();
    }
}
