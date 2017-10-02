package tzchoice.kisanga.joshua.policybrief.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.activity.FullStoryActivity;
import tzchoice.kisanga.joshua.policybrief.app.Config;
import tzchoice.kisanga.joshua.policybrief.table.RNews;

/**
 * Created by user on 8/12/2017.
 */

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder> {


    private List<RNews> newses;

    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, date;

        public ImageView thumbnail;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            title = (TextView) view.findViewById(R.id.titleTextView);
            date = (TextView) view.findViewById(R.id.tv_date);
            thumbnail = (ImageView) view.findViewById(R.id.coverImageView);

        }
    }


    public SliderAdapter(List<RNews> newsList, Activity activity) {
        this.activity  = activity;
        this.newses = newsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slide_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final RNews news = newses.get(position);
        final String strThumbnail = news.getImagePath();
        String path = activity.getExternalFilesDir(null) + File.separator + strThumbnail;
        holder.title.setText(news.getTitle());
        holder.date.setText("Published on : " + news.getPublishedDate());
       // holder.summary.setText(news.getSummary());
       // holder.tvTime.setText("Published : " + news.getPublishedDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FullStoryActivity.class);
                intent.putExtra(Config.IMAGE_PATH, news.getImagePath());
                intent.putExtra(Config.FILE_TITLE, news.getTitle());
                intent.putExtra(Config.STORY, news.getStory());
                activity.startActivity(intent);
            }
        });

        Picasso.with(activity).load(new File(path)).into(holder.thumbnail);


    }

    @Override
    public int getItemCount() {
        return newses.size();
    }


    public void setFilter(List<RNews> docs){
        newses = new ArrayList<>();
        newses.addAll(docs);
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
