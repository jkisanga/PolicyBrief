package tzchoice.kisanga.joshua.policybrief.fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.adapter.AudioCategoryAdapter;
import tzchoice.kisanga.joshua.policybrief.adapter.CategoryAdapter;
import tzchoice.kisanga.joshua.policybrief.adapter.DataCategoryAdapter;
import tzchoice.kisanga.joshua.policybrief.adapter.VideoCategoryAdapter;
import tzchoice.kisanga.joshua.policybrief.app.Config;
import tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler;
import tzchoice.kisanga.joshua.policybrief.pojo.Audio;
import tzchoice.kisanga.joshua.policybrief.pojo.AudioCategory;
import tzchoice.kisanga.joshua.policybrief.pojo.Category;
import tzchoice.kisanga.joshua.policybrief.pojo.DataCategory;
import tzchoice.kisanga.joshua.policybrief.pojo.Document;
import tzchoice.kisanga.joshua.policybrief.pojo.Statistic;
import tzchoice.kisanga.joshua.policybrief.pojo.Video;
import tzchoice.kisanga.joshua.policybrief.pojo.VideoCategory;
import tzchoice.kisanga.joshua.policybrief.realm.RealmController;
import tzchoice.kisanga.joshua.policybrief.retrofit.IRetrofit;
import tzchoice.kisanga.joshua.policybrief.table.RAudioCategory;
import tzchoice.kisanga.joshua.policybrief.table.RData;
import tzchoice.kisanga.joshua.policybrief.table.RDataCatefory;
import tzchoice.kisanga.joshua.policybrief.table.RDocCategory;
import tzchoice.kisanga.joshua.policybrief.table.RDocument;
import tzchoice.kisanga.joshua.policybrief.table.RVideo;
import tzchoice.kisanga.joshua.policybrief.table.RVideoCategory;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentFragment extends Fragment {

    private List<RDocCategory> cateList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private SQLiteHandler db;
    private Realm realm;


    private List<RDataCatefory> dataCategoryList = new ArrayList<>();

    private List<RAudioCategory> audioCategoryList = new ArrayList<>();

    private List<RVideoCategory> videoCategoryList = new ArrayList<>();

    private DataCategoryAdapter dataCategoryAdapter;

    private AudioCategoryAdapter audioCategoryAdapter;

    private VideoCategoryAdapter videoCategoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // SQLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());
        //cateList = db.categoryList();
        //get realm instance
        this.realm = RealmController.with(this).getRealm();
        if(isOnline()) {
            setRealmDocDategoryData();
        }
            cateList = RealmController.with(getActivity()).getDocCategories();

        dataCategoryList = RealmController.with(getActivity()).getDataCategories();

        audioCategoryList = RealmController.with(getActivity()).getAudioCategories();
        videoCategoryList = RealmController.with(getActivity()).getVideoCategories();


        categoryAdapter = new CategoryAdapter(cateList, getActivity());
        dataCategoryAdapter = new DataCategoryAdapter(dataCategoryList, getActivity());
        audioCategoryAdapter = new AudioCategoryAdapter(audioCategoryList, getActivity());

        videoCategoryAdapter = new VideoCategoryAdapter(videoCategoryList, getActivity());
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_document, container, false);

        MultiSnapRecyclerView firstRecyclerView = (MultiSnapRecyclerView)view.findViewById(R.id.first_recycler_view);
        LinearLayoutManager firstManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        firstRecyclerView.setLayoutManager(firstManager);
        firstRecyclerView.setAdapter(categoryAdapter);

        MultiSnapRecyclerView secondRecyclerView =(MultiSnapRecyclerView) view.findViewById(R.id.second_recycler_view);
        LinearLayoutManager secondManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        secondRecyclerView.setLayoutManager(secondManager);
        secondRecyclerView.setAdapter(dataCategoryAdapter);

        MultiSnapRecyclerView thirdRecyclerView = (MultiSnapRecyclerView) view.findViewById(R.id.third_recycler_view);
        LinearLayoutManager thirdManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        thirdRecyclerView.setLayoutManager(thirdManager);
        thirdRecyclerView.setAdapter(audioCategoryAdapter);

        MultiSnapRecyclerView fourthRecyclerView = (MultiSnapRecyclerView) view.findViewById(R.id.forth_recycler_view);
        LinearLayoutManager fourthManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        fourthRecyclerView.setLayoutManager(fourthManager);
        fourthRecyclerView.setAdapter(videoCategoryAdapter);


        return view;
    }



    //loading data form server to local db
    private void setRealmDocuments() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url)
                .client(client)
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

                            RDocument rDocument = new RDocument();
                            rDocument.setId(documents.get(i).getId());
                            rDocument.setCategoryId(documents.get(i).getCategoryId());
                            rDocument.setTitle(documents.get(i).getTitle());
                            rDocument.setSummary(documents.get(i).getSummary());
                            rDocument.setThumbnail(documents.get(i).getThumbnail());
                            rDocument.setFilePath(documents.get(i).getFilePath());
                            rDocument.setCreatedAt(documents.get(i).getCreatedAt());

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(rDocument);
                            realm.commitTransaction();

                            //  db.addDocument(documents.get(i).getId(),documents.get(i).getCategoryId(),documents.get(i).getTitle(), documents.get(i).getSummary(),documents.get(i).getThumbnail(),documents.get(i).getFilePath(),documents.get(i).getCreatedAt());

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

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url).client(client)
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


            File futureStudioIconFile = new File(getActivity().getExternalFilesDir(null) + File.separator + filename);

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

    //load document categories and its documents to local database
    private void setRealmDocDategoryData() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url).client(client)
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
                            RDocCategory rDocCategory = new RDocCategory();
                            rDocCategory.setId(categories.get(i).getId());
                            rDocCategory.setName(categories.get(i).getName());
                            rDocCategory.setDesc(categories.get(i).getDesc());
                            rDocCategory.setThumbnail(categories.get(i).getThumbnail());
                            rDocCategory.setCreatedAt(categories.get(i).getCreatedAt());

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(rDocCategory);
                            realm.commitTransaction();

                            //db.addDocumentCategory(categories.get(i).getId(),categories.get(i).getName(), categories.get(i).getDesc(),categories.get(i).getThumbnail());

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

    private void setRealmDataCategoryData() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IRetrofit service = retrofit.create(IRetrofit.class);

        try {

            Call<List<DataCategory>> call = service.getDataCategories();
            call.enqueue(new Callback<List<DataCategory>>() {
                @Override
                public void onResponse(Response<List<DataCategory>> response, Retrofit retrofit) {


                    if(response.isSuccess()) {

                        List<DataCategory>   categories = response.body();
                        for(int i =0; i < categories.size(); i++){
                            RDataCatefory rDataCatefory = new RDataCatefory();
                            rDataCatefory.setId(categories.get(i).getId());
                            rDataCatefory.setName(categories.get(i).getName());
                            rDataCatefory.setDesc(categories.get(i).getDesc());
                            rDataCatefory.setThumbnail(categories.get(i).getThumbnail());

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(rDataCatefory);
                            realm.commitTransaction();

                            // db.addDataCategory(categories.get(i).getId(), categories.get(i).getName(), categories.get(i).getDesc(), categories.get(i).getThumbnail());
                        }

                        //Starting fill document after loading all category
                        setRealmDatas();

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
    //loading data form server to local db

    private void setRealmDatas() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IRetrofit service = retrofit.create(IRetrofit.class);

        try {

            Call<List<Statistic>> call = service.getDatas();
            call.enqueue(new Callback<List<Statistic>>() {
                @Override
                public void onResponse(Response<List<Statistic>> response, Retrofit retrofit) {


                    if (response.isSuccess()) {

                        List<Statistic> documents = response.body();
                        for (int i = 0; i < documents.size(); i++) {

                            RData rData = new RData();
                            rData.setId(documents.get(i).getId());
                            rData.setDataCategoryId(documents.get(i).getDataCategoryId());
                            rData.setTitle(documents.get(i).getTitle());
                            rData.setSummary(documents.get(i).getSummary());
                            rData.setThumbnail(documents.get(i).getThumbnail());
                            rData.setFilePath(documents.get(i).getFilePath());
                            rData.setCreatedAt(documents.get(i).getCreatedAt());

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(rData);
                            realm.commitTransaction();


                            // db.addStatistics(documents.get(i).getId(), documents.get(i).getDataCategoryId(), documents.get(i).getTitle(), documents.get(i).getSummary(), documents.get(i).getThumbnail(), documents.get(i).getFilePath(), documents.get(i).getCreatedAt());

                            String pathLinkDoc = documents.get(i).getFilePath();
                            String pathLinkImage = documents.get(i).getThumbnail();

                            //call a method for download document form server
                            getDocsFromServer(pathLinkDoc);
                            //download thumbnail and save to local
                            getDocsFromServer(pathLinkImage);


                        }


                    } else {
                    }


                }

                @Override
                public void onFailure(Throwable t) {


                }
            });
        } catch (Exception e) {

        }

    }

    //load document categories and its documents to local database
    private void downloadAudioCatgoryToSqlite() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IRetrofit service = retrofit.create(IRetrofit.class);

        try {

            Call<List<AudioCategory>> call = service.getAudioCategories();
            call.enqueue(new Callback<List<AudioCategory>>() {
                @Override
                public void onResponse(Response<List<AudioCategory>> response, Retrofit retrofit) {


                    if(response.isSuccess()) {

                        List<AudioCategory>   categories = response.body();
                        for(int i = 0; i < categories.size(); i++){

                            RAudioCategory rAudioCategory = new RAudioCategory();
                            rAudioCategory.setId(categories.get(i).getId());
                            rAudioCategory.setTitle(categories.get(i).getTitle());
                            rAudioCategory.setDesc(categories.get(i).getDesc());
                            rAudioCategory.setThumbnail(categories.get(i).getThumbnail());


                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(rAudioCategory);
                            realm.commitTransaction();

                            //db.addAudioCategory(categories.get(i).getId(),categories.get(i).getTitle(), categories.get(i).getDesc(),categories.get(i).getThumbnail());

                            String imageLink = categories.get(i).getThumbnail();

                            //call a method for download image form server
                            getDocsFromServer(imageLink);
                        }

                        //Starting fill audio
                        downloadAdiosToSqlite();

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

    private void downloadAdiosToSqlite() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IRetrofit service = retrofit.create(IRetrofit.class);

        try {

            Call<List<Audio>> call = service.getAudios();
            call.enqueue(new Callback<List<Audio>>() {
                @Override
                public void onResponse(Response<List<Audio>> response, Retrofit retrofit) {


                    if (response.isSuccess()) {

                        List<Audio> documents = response.body();
                        for (int i = 0; i < documents.size(); i++) {

                            db.addAudio(documents.get(i).getId(),
                                    documents.get(i).getAudioCategoryId(), documents.get(i).getTitle(),
                                    documents.get(i).getDesc(), documents.get(i).getThumbnail(), documents.get(i).getFilePath(), documents.get(i).getCreatedAt());
                            String pathLinkDoc = documents.get(i).getFilePath();
                            String pathLinkImage = documents.get(i).getThumbnail();

                            //call a method for download document form server
                            getDocsFromServer(pathLinkDoc);
                            //download thumbnail and save to local
                            getDocsFromServer(pathLinkImage);


                        }


                    } else {
                    }


                }

                @Override
                public void onFailure(Throwable t) {


                }
            });
        } catch (Exception e) {

        }

    }

    //load document categories and its documents to local database
    private void downloadVideoCatgoryToSqlite() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IRetrofit service = retrofit.create(IRetrofit.class);

        try {

            Call<List<VideoCategory>> call = service.getVideoCategories();
            call.enqueue(new Callback<List<VideoCategory>>() {
                @Override
                public void onResponse(Response<List<VideoCategory>> response, Retrofit retrofit) {


                    if(response.isSuccess()) {

                        List<VideoCategory>   categories = response.body();
                        for(int i = 0; i < categories.size(); i++){

                            RVideoCategory rVideoCategory = new RVideoCategory();
                            rVideoCategory.setId(categories.get(i).getId());
                            rVideoCategory.setTitle(categories.get(i).getTitle());
                            rVideoCategory.setDesc(categories.get(i).getDesc());
                            rVideoCategory.setThumbnail(categories.get(i).getThumbnail());

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(rVideoCategory);
                            realm.commitTransaction();

                            //db.addVideoCategory(categories.get(i).getId(),categories.get(i).getTitle(), categories.get(i).getDesc(),categories.get(i).getThumbnail());

                            String imageLink = categories.get(i).getThumbnail();

                            //call a method for download image form server
                            getDocsFromServer(imageLink);
                        }

                        //Starting fill audio
                        downloadVideoToSqlite();

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

    private void downloadVideoToSqlite() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IRetrofit service = retrofit.create(IRetrofit.class);

        try {

            Call<List<Video>> call = service.getVideo();
            call.enqueue(new Callback<List<Video>>() {
                @Override
                public void onResponse(Response<List<Video>> response, Retrofit retrofit) {


                    if (response.isSuccess()) {

                        List<Video> documents = response.body();
                        for (int i = 0; i < documents.size(); i++) {

                            RVideo rVideo = new RVideo();
                            rVideo.setId(documents.get(i).getId());
                            rVideo.setVideoCategoryId(documents.get(i).getVideoCategoryId());
                            rVideo.setTitle(documents.get(i).getTitle());
                            rVideo.setDesc(documents.get(i).getDesc());
                            rVideo.setThumbnail(documents.get(i).getThumbnail());
                            rVideo.setFilePath(documents.get(i).getFilePath());
                            rVideo.setCreatedAt(documents.get(i).getCreatedAt());

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(rVideo);
                            realm.commitTransaction();
                            // db.addVideo(documents.get(i).getId(), documents.get(i).getVideoCategoryId(), documents.get(i).getTitle(), documents.get(i).getDesc(), documents.get(i).getThumbnail(), documents.get(i).getFilePath(), documents.get(i).getCreatedAt());

                            String pathLinkDoc = documents.get(i).getFilePath();
                            String pathLinkImage = documents.get(i).getThumbnail();

                            //call a method for download document form server
                            getDocsFromServer(pathLinkDoc);
                            //download thumbnail and save to local
                            getDocsFromServer(pathLinkImage);


                        }


                    } else {
                    }


                }

                @Override
                public void onFailure(Throwable t) {


                }
            });
        } catch (Exception e) {

        }

    }

    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

}
