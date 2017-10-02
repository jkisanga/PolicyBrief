package tzchoice.kisanga.joshua.policybrief.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.activity.AudioActivity;
import tzchoice.kisanga.joshua.policybrief.app.Config;
import tzchoice.kisanga.joshua.policybrief.table.RAudioCategory;

/**
 * Created by user on 8/1/2017.
 */

public class AudioCategoryAdapter extends RecyclerView.Adapter<AudioCategoryAdapter.MyViewHolder> {

    public static final String TAG = "AudioCategoryAdapter";
    private List<RAudioCategory> categories;
    Activity activity;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, desc;
        private ImageView cateIcon;
        RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.row_container) ;
            title = (TextView) view.findViewById(R.id.txt_title);
            //desc = (TextView) view.findViewById(R.id.txt_desc);
            cateIcon = (ImageView) view.findViewById(R.id.list_img);


        }
    }


    public AudioCategoryAdapter(List<RAudioCategory> categoryList, Activity activity) {
        this.activity  = activity;
        this.categories = categoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_horizontal, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final RAudioCategory category = categories.get(position);
        String path = activity.getExternalFilesDir(null) + File.separator + category.getThumbnail();
        holder.title.setText(category.getTitle());
       // holder.desc.setText(category.getDesc());

           // holder.desc.setText(category.getDesc());

            Picasso.with(activity).load(new File(path)).into(holder.cateIcon);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AudioActivity.class);
                intent.putExtra(Config.CATEGORY_ID, category.getId());
                intent.putExtra(Config.CATEGORY_NAME, category.getTitle());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public void setFilter(List<RAudioCategory> cates){
        categories = new ArrayList<>();
        categories.addAll(cates);
        notifyDataSetChanged();
    }
}
