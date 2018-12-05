package escan.com.networkingLibraryDemo.RecyclerViewDemo.NetowrkingDemo.volleyDemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;

import escan.com.networkingLibraryDemo.R;

public class RecyclerViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        ArrayList<UserList.UserDataList> userDataLists = (ArrayList<UserList.UserDataList>) getIntent().getSerializableExtra("users");


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, userDataLists);
        recyclerView.setAdapter(adapter);

    }


}
