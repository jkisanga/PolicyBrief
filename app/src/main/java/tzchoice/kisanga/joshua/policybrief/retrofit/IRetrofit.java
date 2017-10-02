package tzchoice.kisanga.joshua.policybrief.retrofit;

import com.squareup.okhttp.ResponseBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Url;
import tzchoice.kisanga.joshua.policybrief.model.User;
import tzchoice.kisanga.joshua.policybrief.pojo.Audio;
import tzchoice.kisanga.joshua.policybrief.pojo.AudioCategory;
import tzchoice.kisanga.joshua.policybrief.pojo.Category;
import tzchoice.kisanga.joshua.policybrief.pojo.DataCategory;
import tzchoice.kisanga.joshua.policybrief.pojo.Document;
import tzchoice.kisanga.joshua.policybrief.pojo.News;
import tzchoice.kisanga.joshua.policybrief.pojo.Profile;
import tzchoice.kisanga.joshua.policybrief.pojo.Statistic;
import tzchoice.kisanga.joshua.policybrief.pojo.Video;
import tzchoice.kisanga.joshua.policybrief.pojo.VideoCategory;

/**
 * Created by user on 5/27/2017.
 */

public interface IRetrofit {

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("audio_categories")
    Call<List<AudioCategory>> getAudioCategories();

    @GET("audios")
    Call<List<Audio>> getAudios();

    @GET("video_categories")
    Call<List<VideoCategory>> getVideoCategories();

    @GET("videos")
    Call<List<Video>> getVideo();



    @GET("data_categories")
    Call<List<DataCategory>> getDataCategories();


    @GET("raw_datas")
    Call<List<Statistic>> getDatas();


    @GET("documents")
    Call<List<Document>> getDocuments();

    @POST("device_users")
    Call<User> postLogin(@Body User user);

//    @PUT("device_users/update/{id}")
//    Call<User> changePIN(@Path("user_id,") int user_id, @Body User user);

    @GET("change_pin/{user_id}/{pin}")
    Call<User> changePIN(@Path("user_id") int user_id, @Path("pin") int pin);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);


    @GET("news")
    Call<List<News>> getNews();

    @GET("profiles")
    Call<List<Profile>> getProfiles();
}
