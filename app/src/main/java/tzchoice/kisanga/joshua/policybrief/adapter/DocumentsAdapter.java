package tzchoice.kisanga.joshua.policybrief.adapter;

import android.app.Activity;
import android.content.pm.PackageManager;
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
import tzchoice.kisanga.joshua.policybrief.table.RDocument;

/**
 * Created by user on 5/30/2017.
 */

public class DocumentsAdapter  extends RecyclerView.Adapter<DocumentsAdapter.MyViewHolder> {


    private List<RDocument> documents;

    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, summary,docPath;
        public ImageView thumbnail;
        RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.row_container) ;


            thumbnail = (ImageView) view.findViewById(R.id.pdf_thumbunail);
            title = (TextView) view.findViewById(R.id.txt_title);
            summary = (TextView) view.findViewById(R.id.txt_summary);


        }
    }




    public DocumentsAdapter(List<RDocument> documentList, Activity activity) {
        this.activity  = activity;
        this.documents = documentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final RDocument document = documents.get(position);
       final String strThumbnail = document.getThumbnail();
        String path = activity.getExternalFilesDir(null) + File.separator + strThumbnail;
        holder.title.setText(document.getTitle());
        holder.summary.setText(document.getSummary());
        if (position % 2 == 0) {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#6f7175"));
        } else {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#0A9B88"));
        }
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

        Picasso.with(activity).load(new File(path)).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return documents.size();
    }


    public void setFilter(List<RDocument> docs){
        documents = new ArrayList<>();
        documents.addAll(docs);
        notifyDataSetChanged();
    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
