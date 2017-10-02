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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.adapter.AutoFitGridLayoutManager;
import tzchoice.kisanga.joshua.policybrief.adapter.CategoryAdapter;
import tzchoice.kisanga.joshua.policybrief.app.Config;
import tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler;
import tzchoice.kisanga.joshua.policybrief.pojo.Category;
import tzchoice.kisanga.joshua.policybrief.pojo.Document;
import tzchoice.kisanga.joshua.policybrief.realm.RealmController;
import tzchoice.kisanga.joshua.policybrief.retrofit.IRetrofit;
import tzchoice.kisanga.joshua.policybrief.table.RDocCategory;

public class CategoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private List<RDocCategory> cateList = new ArrayList<>();
    private RecyclerView cateRecyclerView;
    private CategoryAdapter categoryAdapter;

    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // SQLite database handler
      //  db = new SQLiteHandler(getApplicationContext());
        cateRecyclerView = (RecyclerView)findViewById(R.id.cat_recycler_view);


            setAdapter();


    }

    private void setAdapter() {
        //cateList = db.categoryList();
        cateList = RealmController.with(this).getDocCategories();
        categoryAdapter = new CategoryAdapter(cateList, this);
        categoryAdapter.notifyDataSetChanged();
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


    private List<RDocCategory> filter(List<RDocCategory> models, String query) {
        query = query.toLowerCase();
        final List<RDocCategory> filteredModelList = new ArrayList<>();
        for (RDocCategory model : models) {

             String name = model.getName();
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
        final List<RDocCategory> filteredModelList = filter(cateList, newText);

        categoryAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final MenuItem item1 = menu.findItem(R.id.action_refresh);
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
            case R.id.action_refresh:

                refreshData();
                Toast.makeText(this, "Database Updated", Toast.LENGTH_SHORT).show();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {

                db.deleteDocumentCategory();
                db.deleteDocument();
                setDocDategoryData();
                setAdapter();

    }

    //load document categories and its documents to local database
    private void setDocDategoryData() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IRetrofit service = retrofit.create(IRetrofit.class);

        try {

            Call<List<Category>> call = service.getCategories();
            call.enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Response<List<Category>> response, Retrofit retrofit) {


                    if(response.isSuccess()) {

                        List<Category>   categories = response.body();
                        for(int i =0; i < categories.size(); i++){
                            db.addDocumentCategory(categories.get(i).getId(),categories.get(i).getName(),
                                    categories.get(i).getDesc(),categories.get(i).getThumbnail());

                            String imageLink = categories.get(i).getThumbnail();

                            //call a method for download image form server
                            getDocsFromServer(imageLink);
                        }

                        //Starting fill document after loading all category
                        setRealmDocuments();

                    }else{

                    }


                }

                @Override
                public void onFailure(Throwable t) {


                }
            });
        }catch (Exception e){
        }

    }

    private void setRealmDocuments() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IRetrofit service = retrofit.create(IRetrofit.class);

        try {

            Call<List<Document>> call = service.getDocuments();
            call.enqueue(new Callback<List<Document>>() {
                @Override
                public void onResponse(Response<List<Document>> response, Retrofit retrofit) {


                    if(response.isSuccess()) {

                        List<Document>   documents = response.body();
                        for(int i = 0; i < documents.size(); i++){

                            db.addDocument(documents.get(i).getId(),documents.get(i).getCategoryId(),documents.get(i).getTitle(),
                                    documents.get(i).getSummary(),documents.get(i).getThumbnail(),documents.get(i).getFilePath(),documents.get(i).getCreatedAt());

                            String pathLinkDoc = documents.get(i).getFilePath();
                            String pathLinkImage = documents.get(i).getThumbnail();

                            //call a method for download document form server
                            getDocsFromServer(pathLinkDoc);
                            //download thumbnail and save to local
                            getDocsFromServer(pathLinkImage);


                        }


                    }else{
                    }


                }

                @Override
                public void onFailure(Throwable t) {


                }
            });
        }catch (Exception e){

        }

    }


    void getDocsFromServer(final String path) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IRetrofit service = retrofit.create(IRetrofit.class);

        Call<ResponseBody> call = service.downloadFileWithDynamicUrlSync("document/" + path);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {

                try {
                    if(response.isSuccess()) {

                        boolean FileDownloaded = writeResponseBodyToDisk(response.body(), path);


                    }else {

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private boolean writeResponseBodyToDisk(ResponseBody body, String filename) {
        try {

            // todo change the file location/name according to your needs


            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("file-download", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();



                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }



}
