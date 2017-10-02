package tzchoice.kisanga.joshua.policybrief.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.adapter.AudioCategoryAdapter;
import tzchoice.kisanga.joshua.policybrief.adapter.AutoFitGridLayoutManager;
import tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler;
import tzchoice.kisanga.joshua.policybrief.realm.RealmController;
import tzchoice.kisanga.joshua.policybrief.table.RAudioCategory;

public class AudioCategoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private List<RAudioCategory> cateList = new ArrayList<>();
    private RecyclerView cateRecyclerView;
    private AudioCategoryAdapter categoryAdapter;

    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cateList = RealmController.with(this).getAudioCategories();
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        cateRecyclerView = (RecyclerView)findViewById(R.id.cat_recycler_view);



        categoryAdapter = new AudioCategoryAdapter(cateList, this);

        cateRecyclerView.setAdapter(categoryAdapter);

        cateRecyclerView.setHasFixedSize(true);
        cateRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryAdapter.notifyDataSetChanged();
        cateRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 600);
        cateRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    }
    @Override
    public boolean onSupportNavigateUp(){
        //code it to launch an intent to the activity you want
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    private List<RAudioCategory> filter(List<RAudioCategory> models, String query) {
        query = query.toLowerCase();
        final List<RAudioCategory> filteredModelList = new ArrayList<>();
        for (RAudioCategory model : models) {

            String name = model.getTitle();
            String desc = model.getDesc();
            name = name.toLowerCase();
            desc = desc.toLowerCase();

            if (name.contains(query.toString()) || desc.contains(query.toString())) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<RAudioCategory> filteredModelList = filter(cateList, newText);

        categoryAdapter.setFilter(filteredModelList);
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
                        categoryAdapter.setFilter(cateList);
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

        }

        return super.onOptionsItemSelected(item);
    }

}

