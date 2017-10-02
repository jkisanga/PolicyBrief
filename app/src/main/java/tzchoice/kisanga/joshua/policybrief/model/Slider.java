package tzchoice.kisanga.joshua.policybrief.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import tzchoice.kisanga.joshua.policybrief.pojo.News;

/**
 * Created by user on 8/7/2017.
 */

public class Slider {
    @SerializedName("news")
    private ArrayList<News> newsArrayList;

    public ArrayList<News> getNewsArrayList() {
        return newsArrayList;
    }


}
