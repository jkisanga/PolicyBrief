package tzchoice.kisanga.joshua.policybrief.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.szugyi.circlemenu.view.CircleImageView;
import com.szugyi.circlemenu.view.CircleLayout;

import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.Service.NotificationUtils;
import tzchoice.kisanga.joshua.policybrief.activity.AudioCategoryActivity;
import tzchoice.kisanga.joshua.policybrief.activity.CategoryActivity;
import tzchoice.kisanga.joshua.policybrief.activity.DataCategoryActivity;
import tzchoice.kisanga.joshua.policybrief.activity.VideoCategoryActivity;
import tzchoice.kisanga.joshua.policybrief.app.Config;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment implements CircleLayout.OnItemSelectedListener, CircleLayout.OnItemClickListener, CircleLayout.OnRotationFinishedListener, CircleLayout.OnCenterClickListener {

    public static final String ARG_LAYOUT = "layout";

    protected TextView selectedTextView;
    private TextView txtRegId, txtMessage;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private Button btnDocument, btnStatistic, btnAudio, btnVideo;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        btnDocument = (Button) view.findViewById(R.id.btn_document);
        btnStatistic = (Button) view.findViewById(R.id.btn_statistic);
        btnAudio = (Button) view.findViewById(R.id.btn_audio);
        btnVideo = (Button) view.findViewById(R.id.btn_video);

        //documents
        btnDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CategoryActivity.class);
                startActivity(i);
            }
        });

        //statistics
        btnStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DataCategoryActivity.class);
                startActivity(i);
            }
        });

        //audio
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AudioCategoryActivity.class);
                startActivity(i);
            }
        });


        //video
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), VideoCategoryActivity.class);
                startActivity(i);
            }
        });





       mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                Log.d(TAG, "onReceive: yes");
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    Log.d(TAG, "onReceive: yes1");
                  //  displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getActivity().getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }
            }
        };

      //  displayFirebaseRegId();

        return view;
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
//    private void displayFirebaseRegId() {
//        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        String regId = pref.getString("regId", null);
//
//        Log.e(TAG, "Firebase reg id: " + regId);
//
//        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
//    }

    @Override
    public void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getActivity().getApplicationContext());
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public void onCenterClick() {
        Toast.makeText(getActivity(), "katikati", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view) {
        String name = null;
        if (view instanceof CircleImageView) {
            name = ((CircleImageView) view).getName();
        }

        String text = getResources().getString(R.string.start_app, name);
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

        switch (view.getId()) {
            case R.id.main_calendar_image:
                // Handle calendar click

                Intent i = new Intent(getActivity(), CategoryActivity.class);
                startActivity(i);
                break;
            case R.id.main_cloud_image:
                // Handle cloud click
                Intent data = new Intent(getActivity(), DataCategoryActivity.class);
                startActivity(data);
                break;

            case R.id.main_profile_image:
                // Handle profile click
                Intent audio = new Intent(getActivity(), AudioCategoryActivity.class);
                startActivity(audio);
                break;
            case R.id.main_tap_image:
                // Handle tap click
                Intent video = new Intent(getActivity(), VideoCategoryActivity.class);
                startActivity(video);
                break;
        }
    }

    @Override
    public void onItemSelected(View view) {
        final String name;
        if (view instanceof CircleImageView) {
            name = ((CircleImageView) view).getName();
        } else {
            name = null;
        }

        selectedTextView.setText(name);

        switch (view.getId()) {
            case R.id.main_calendar_image:
                // Handle calendar selection
                break;
            case R.id.main_cloud_image:
                // Handle cloud selection
                break;

            case R.id.main_profile_image:
                // Handle profile selection
                break;
            case R.id.main_tap_image:
                // Handle tap selection
                break;
        }
    }

    @Override
    public void onRotationFinished(View view) {
        Animation animation = new RotateAnimation(0, 360, view.getWidth() / 2, view.getHeight() / 2);
        animation.setDuration(250);
        view.startAnimation(animation);
    }

    /**
     AutoFitGridLayoutManager that auto fits the cells by the column width defined.
     **/


}
