package tzchoice.kisanga.joshua.policybrief.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.app.Config;

public class DocViewerActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    private static final String TAG = DocViewerActivity.class.getSimpleName();
    private final static int REQUEST_CODE = 42;
    public static final int PERMISSION_CODE = 42042;

    public static final String SAMPLE_FILE = "";
    public  String file_path = "";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    String pdfFileName, fileTitle;
    Integer pageNumber = 0;
    PDFView pdfView;
    BottomNavigationView navigation;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(DocViewerActivity.this, CategoryActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    Intent i = new Intent(DocViewerActivity.this, MainActivity.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_notifications:
                    shareFile();
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            file_path = bundle.getString(Config.DOC_PATH);
            fileTitle = bundle.getString(Config.FILE_TITLE);
        }
        pdfView = (PDFView) findViewById(R.id.pdfView);
        displayFromAsset(file_path);
        // navigation = (BottomNavigationView) findViewById(R.id.navigation);
      // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    public boolean onSupportNavigateUp(){
        //code it to launch an intent to the activity you want
        finish();
        return true;
    }
    private void displayFromAsset(String assetFileName) {


        pdfFileName = assetFileName;
        File file = new File(getExternalFilesDir(null) + File.separator + assetFileName);
        Uri.parse("file://" + getExternalFilesDir(null) + File.separator + assetFileName);

        Log.d(TAG, "displayFromAsset: " + Uri.parse("file://" + getExternalFilesDir(null) + File.separator + assetFileName));
        Log.d(TAG, "displayFromAsset: " + file);

        pdfView.fromUri(Uri.parse("file://" +  getExternalFilesDir(null) + File.separator + assetFileName))
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .enableDoubletap(true)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int pages, float pageWidth,
                                                    float pageHeight) {
                        pdfView.fitToWidth(pageNumber); // optionally pass page number
                    }
                })
                .load();



    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s / %s  %s", page + 1,pageCount ,  fileTitle));
    }

    private void shareFile(){

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("application/pdf");

        final File photoFile = new File(getExternalFilesDir(null) + File.separator + file_path);

        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));

        startActivity(Intent.createChooser(shareIntent, fileTitle));


    }

}
