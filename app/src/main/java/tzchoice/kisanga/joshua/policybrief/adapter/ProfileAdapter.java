package tzchoice.kisanga.joshua.policybrief.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.app.Config;
import tzchoice.kisanga.joshua.policybrief.fragment.ProfileDetailFragment;
import tzchoice.kisanga.joshua.policybrief.table.RProfile;

/**
 * Created by user on 8/17/2017.
 */

public class ProfileAdapter  extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    private List<RProfile> profiles;

    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, name;
        public ImageView thumbnail;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.profile_list_container) ;

            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.profile_title);
            name = (TextView) view.findViewById(R.id.profile_name);


        }
    }



    public ProfileAdapter(List<RProfile> profileList, Activity activity) {
        this.activity  = activity;
        this.profiles = profileList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_profile, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final RProfile profile = profiles.get(position);

        String path = activity.getExternalFilesDir(null) + File.separator + profile.getImagePath();
        holder.title.setText(profile.getTitle());
        holder.name.setText(profile.getName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileDetailFragment profileDetailFragment= new ProfileDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Config.DOC_PATH, profile.getImagePath());
                bundle.putString(Config.TITLE, profile.getTitle());
                bundle.putString(Config.STORY, profile.getBio());
                bundle.putString(Config.EMAIL, profile.getEmail());
                bundle.putString(Config.PHONE, profile.getPhone());

                profileDetailFragment.setArguments(bundle);
                activity.getFragmentManager().beginTransaction()
                        .replace(R.id.layout_container, profileDetailFragment,"FRAGMENT")
                        .addToBackStack(null)
                        .commit();

            }
        });


            Picasso.with(activity).load(new File(path)).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }


    public void setFilter(List<RProfile> docs){
        profiles = new ArrayList<>();
        profiles.addAll(docs);
        notifyDataSetChanged();
    }

    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
