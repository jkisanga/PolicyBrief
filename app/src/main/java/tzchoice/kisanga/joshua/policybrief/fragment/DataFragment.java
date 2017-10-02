package tzchoice.kisanga.joshua.policybrief.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.activity.LoginActivity;
import tzchoice.kisanga.joshua.policybrief.adapter.DataCategoryAdapter;
import tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler;
import tzchoice.kisanga.joshua.policybrief.helper.SessionManager;
import tzchoice.kisanga.joshua.policybrief.model.DataCateDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment {

    private Realm realm;
    private List<DataCateDB> cateList = new ArrayList<>();
    private RecyclerView cateRecyclerView;
    private DataCategoryAdapter categoryAdapter;
    Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    private SQLiteHandler db;
    private SessionManager session;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data, container, false);


        // session manager
        session = new SessionManager(getActivity().getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        // SQLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());




        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.cat_data_swipe_refresh_layout);
        cateRecyclerView = (RecyclerView)view.findViewById(R.id.cat_data_recycler_view);


       // cateList = db.dataCateDBList();





        return  view;
    }



    private void logoutUser() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure you want to sign off?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        //Starting login activity
                        session.setLogin(false);

                        db.deleteUsers();

                        // Launching the login activity
                        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }






    private List<DataCateDB> filter(List<DataCateDB> models, String query) {
        query = query.toLowerCase();
        final List<DataCateDB> filteredModelList = new ArrayList<>();
        for (DataCateDB model : models) {

            final String text = model.getName();

            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });

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
                logoutUser();
                return true;

        }
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
