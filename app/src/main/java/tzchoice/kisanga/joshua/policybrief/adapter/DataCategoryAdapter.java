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
import tzchoice.kisanga.joshua.policybrief.activity.DataActivity;
import tzchoice.kisanga.joshua.policybrief.app.Config;
import tzchoice.kisanga.joshua.policybrief.table.RDataCatefory;

/**
 * Created by user on 6/18/2017.
 */

public class DataCategoryAdapter extends RecyclerView.Adapter<DataCategoryAdapter.MyViewHolder> {

    private List<RDataCatefory> categories;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, desc;
        private ImageView cateIcon;
        RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.row_container) ;
            title = (TextView) view.findViewById(R.id.txt_title);
           // desc = (TextView) view.findViewById(R.id.txt_desc);
            cateIcon = (ImageView) view.findViewById(R.id.list_img);


        }
    }


    public DataCategoryAdapter(List<RDataCatefory> categoryList, Activity activity) {
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

        final RDataCatefory category = categories.get(position);
        String path = activity.getExternalFilesDir(null) + File.separator + category.getThumbnail();
        holder.title.setText(category.getName());
       // holder.desc.setText(category.getDesc());

            Picasso.with(activity).load(new File(path)).into(holder.cateIcon);


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DataActivity.class);
                intent.putExtra(Config.CATEGORY_ID, category.getId());
                intent.putExtra(Config.CATEGORY_NAME, category.getName());
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public void setFilter(List<RDataCatefory> cates){
        categories = new ArrayList<>();
        categories.addAll(cates);
        notifyDataSetChanged();
    }
}
