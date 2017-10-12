package tzchoice.kisanga.joshua.policybrief.adapter;

import android.app.Activity;
import android.graphics.Color;
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
import tzchoice.kisanga.joshua.policybrief.table.RAudio;

/**
 * Created by user on 8/1/2017.
 */

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MyViewHolder> {


    private List<RAudio> documents;

    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, summary;
        public ImageView thumbnail;
        RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.audio_row_container) ;


            thumbnail = (ImageView) view.findViewById(R.id.pdf_thumbunail);
            title = (TextView) view.findViewById(R.id.txt_title);
            summary = (TextView) view.findViewById(R.id.txt_summary);


        }
    }




    public AudioAdapter(List<RAudio> documentList, Activity activity) {
        this.activity  = activity;
        this.documents = documentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final RAudio document = documents.get(position);
        final String strThumbnail = document.getThumbnail();
        if (position % 2 == 0) {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#6f7175"));
        } else {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#0A9B88"));
        }
        String path = activity.getExternalFilesDir(null) + File.separator + strThumbnail;
        final String audioPath = activity.getExternalFilesDir(null) + File.separator + document.getFilePath();
        holder.title.setText(document.getTitle());
        holder.summary.setText(document.getDesc());
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

//                Intent intent = new Intent();
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(file), "audio/*");
//                activity.startActivity(intent);

            }
        });

        Picasso.with(activity).load(new File(path)).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return documents.size();
    }


    public void setFilter(List<RAudio> docs){
        documents = new ArrayList<>();
        documents.addAll(docs);
        notifyDataSetChanged();
    }


}

