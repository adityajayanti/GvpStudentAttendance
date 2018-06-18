package com.example.aditya.gvpstudentattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AttendanceMenu extends AppCompatActivity {

    RecyclerView recyclerView;
    List<MainPageContents> contentsList;
    ContentsAdapter contentsAdapter;
    DataTimeHandler dataTimeHandler;
    public void PageOne(View view){

        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_menu);


        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(getApplicationContext(),Login.class));
        }
        dataTimeHandler = new DataTimeHandler();
        getSupportActionBar().setTitle(dataTimeHandler.getActionBarDate());
        //Toast.makeText(getApplicationContext(),"Welcome "+SharedPrefManager.getInstance(this).getUsername(),Toast.LENGTH_LONG).show();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contentsList = new ArrayList<>();
        //adding some items to our list

        contentsList.add(
                new MainPageContents(
                        "Overall Report",
                        "Overall attendance of students.",
                        R.drawable.overall));


        contentsAdapter = new ContentsAdapter(this, contentsList,0);

        //setting adapter to recyclerview
        recyclerView.setAdapter(contentsAdapter);

        contentsAdapter.setItemClickListener(new ContentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
              if(position == 0){
                    Toast.makeText(getApplicationContext(), contentsList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AttendancePercent.class));
                }
            }
        });


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
                startActivity(new Intent(getApplicationContext(), HomeMenu.class));
                break;

            case R.id.hometo:
                startActivity(new Intent(getApplicationContext(), HomeMenu.class));
                finish();

        }
        return  true;
    }
}
