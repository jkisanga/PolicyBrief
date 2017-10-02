package tzchoice.kisanga.joshua.policybrief.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.helper.FileOpen;
import tzchoice.kisanga.joshua.policybrief.table.RData;

/**
 * Created by user on 6/19/2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private List<RData> rawDatas;

    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, summary;
        public ImageView thumbnail;
        RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.row_container);
            title = (TextView) view.findViewById(R.id.txt_title);
            summary = (TextView) view.findViewById(R.id.txt_summary);
            thumbnail = (ImageView) view.findViewById(R.id.pdf_thumbunail);

        }
    }

    public DataAdapter(List<RData> rawDataList, Activity activity) {
        this.activity  = activity;
        this.rawDatas = rawDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final RData document = rawDatas.get(position);

        String path = activity.getExternalFilesDir(null) + File.separator + document.getThumbnail();
        holder.title.setText(document.getTitle());
        holder.summary.setText(document.getSummary());
        Picasso.with(activity).load(new File(path)).into(holder.thumbnail);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri.parse("file://" + activity.getExternalFilesDir(null) + File.separator + document.getFilePath());
                File file = new File(activity.getExternalFilesDir(null) + File.separator + document.getFilePath());
                try {
                    FileOpen.openFile(activity, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return rawDatas.size();
    }


    public void setFilter(List<RData> statistics){
        rawDatas = new ArrayList<>();
        rawDatas.addAll(statistics);
        notifyDataSetChanged();
    }


}
