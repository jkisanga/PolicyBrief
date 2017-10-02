package tzchoice.kisanga.joshua.policybrief.model;

import android.support.v4.app.Fragment;

/**
 * Created by anupamchugh on 11/02/17.
 */

public class DataModel {


    public String text;
    public int drawable;
    public String color;
    public Fragment fragment;

    public DataModel(String t, int d, String c,Fragment fragment )
    {
        text = t;
        drawable = d;
        color = c;
        fragment = fragment;
    }
}
