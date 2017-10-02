package tzchoice.kisanga.joshua.policybrief.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.adapter.AutoFitGridLayoutManager;
import tzchoice.kisanga.joshua.policybrief.adapter.DataAdapter;
import tzchoice.kisanga.joshua.policybrief.app.Config;
import tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler;
import tzchoice.kisanga.joshua.policybrief.realm.RealmController;
import tzchoice.kisanga.joshua.policybrief.table.RData;

public class DataActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private List<RData> documentList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private Realm realm;

    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_data);

        Bundle b = getIntent().getExtras();
        int category_id = b.getInt(Config.CATEGORY_ID);
        String category_name = b.getString(Config.CATEGORY_NAME);
        toolbar.setTitle(category_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.realm = RealmController.with(this).getRealm();

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_data);



        documentList = RealmController.with(this).getStatistics(category_id);
       // documentList = db.statisticsList(category_id);

        mAdapter = new DataAdapter(documentList, this);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter.notifyDataSetChanged();
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 1000);
        mRecyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public boolean onSupportNavigateUp(){
        //code it to launch an intent to the activity you want
//        Intent intent = new Intent(DataActivity.this, TabActivity.class);
//        startActivity(intent);
        onBackPressed();
        finish();
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        mAdapter.setFilter(documentList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.home:
                onBackPressed();
                return  true;

        }


        return super.onOptionsItemSelected(item);
    }





    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }


    private List<RData> filter(List<RData> models, String query) {
        query = query.toLowerCase();
        final List<RData> filteredModelList = new ArrayList<>();
        for (RData model : models) {

             String title = model.getTitle();
             String desc = model.getSummary();
            title = title.toLowerCase();
            desc = desc.toLowerCase();

            if (title.contains(query) || desc.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<RData> filteredModelList = filter(documentList, newText);

        mAdapter.setFilter(filteredModelList);
        return true;
    }




}
