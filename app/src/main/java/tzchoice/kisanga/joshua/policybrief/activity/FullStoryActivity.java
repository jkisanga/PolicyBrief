package tzchoice.kisanga.joshua.policybrief.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.app.Config;

public class FullStoryActivity extends AppCompatActivity {

    String story, title,imagePath, datePubished, summary;
    ImageView imageNews;
    TextView txtTitle, txtStoty, txtDate, txtSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            imagePath = bundle.getString(Config.IMAGE_PATH);
            title = bundle.getString(Config.FILE_TITLE);
            story = bundle.getString(Config.STORY);
        }
        toolbar.setTitle(title);
        String path = this.getExternalFilesDir(null) + File.separator + imagePath;
        imageNews = (ImageView) findViewById(R.id.image_news);
        Picasso.with(this).load(new File(path)).into(imageNews);
        txtTitle = (TextView) findViewById(R.id.tv_title);
        txtStoty = (TextView) findViewById(R.id.tv_story);
        txtDate = (TextView) findViewById(R.id.tv_date);
        txtSummary = (TextView) findViewById(R.id.tv_date);

        txtStoty.setText(story);
        txtTitle.setText(title);

    }
    @Override
    public boolean onSupportNavigateUp(){
        //code it to launch an intent to the activity you want
//        Intent intent = new Intent(FullStoryActivity.this, TabActivity.class);
//        startActivity(intent);
        onBackPressed();
        finish();
        return true;
    }

}
