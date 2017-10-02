package tzchoice.kisanga.joshua.policybrief.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler;

import static tzchoice.kisanga.joshua.policybrief.R.id.imageView;

public class SplashActivity extends Activity {
    private SQLiteHandler db;
    TextView txtWelcome, txtDate, txtGvt, txtAgriculture;
    ImageView imgWazamini, gavLogo;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        // SQLite database handler

        linearLayout = (LinearLayout) findViewById(R.id.linear);

        db = new SQLiteHandler(getApplicationContext());
        txtWelcome = (TextView) findViewById(R.id.txt_welcome);
        txtDate = (TextView) findViewById(R.id.txt_date);

        txtGvt = (TextView) findViewById(R.id.txt_gvt);
        txtAgriculture = (TextView) findViewById(R.id.txt_agriculture1);
        imgWazamini = (ImageView) findViewById(R.id.wazamini);
        gavLogo = (ImageView) findViewById(imageView);

        txtWelcome.setText("Welcome to the Ag.Policy App");
        txtDate.setText(date);

        final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.logo);
        final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.tz);
        final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.agr);
        final Animation animation_4 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.logo2);

        gavLogo.startAnimation(animation_1);

        animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtGvt.setText(getResources().getString(R.string.govt));
                txtGvt.startAnimation(animation_2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });





        animation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtAgriculture.setText(getResources().getString(R.string.agriculture1));
                txtAgriculture.startAnimation(animation_3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation_3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgWazamini.setImageResource(R.drawable.wazamini);
                imgWazamini.startAnimation(animation_4);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation_4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                finish();
                Intent i = new Intent(getBaseContext(),TabActivity.class);
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
