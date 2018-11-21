package escan.com.networkingLibraryDemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();


    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView.LayoutManager layoutManager;

    private static ArrayList<DataModel> data;
    private static ArrayList<Integer> removed_item;

    static View.OnClickListener mOnClickListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.v(TAG, "onCreate method");


        mOnClickListner = new MyOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        data = new ArrayList<>();
        Log.v(TAG,"data size : "+data.isEmpty());
        for (int i = 0; i < DemoData.nameArray.length; i++) {
            data.add(new DataModel(
                    DemoData.nameArray[i],
                    DemoData.versionArray[i],
                    DemoData.id_[i],
                    DemoData.drawableArray[i]
            ));
        }

        removed_item = new ArrayList<>();
        Log.v(TAG,"data size : "+data.isEmpty());
        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume method");
    }


    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removedItem(v);
        }

        private void removedItem(View v) {
            int selectedPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
            String selectedName = (String) textViewName.getText();

            int selectedItemId = -1;
            for (int i = 0; i < DemoData.nameArray.length; i++) {
                if (selectedName.equals(DemoData.nameArray[i])) {
                    selectedItemId = DemoData.id_[i];
                }
            }
            removed_item.add(selectedItemId);
            data.remove(selectedPosition);
            adapter.notifyItemRemoved(selectedPosition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_item) {
            //check if any items to add
            if (removed_item.size() != 0) {
                addRemovedItemToList();
            } else {
                Toast.makeText(this, "Nothing to add", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void addRemovedItemToList() {
        int addItemAtListPosition = 3;
        data.add(addItemAtListPosition, new DataModel(
                DemoData.nameArray[removed_item.get(0)],
                DemoData.versionArray[removed_item.get(0)],
                DemoData.id_[removed_item.get(0)],
                DemoData.drawableArray[removed_item.get(0)]
        ));
        adapter.notifyItemInserted(addItemAtListPosition);
        removed_item.remove(0);
    }
}
