package tzchoice.kisanga.joshua.policybrief.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.app.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileDetailFragment extends Fragment {

TextView txtStory,txtTitle, txtPhone, txtEmail;
    ImageView image;
    String story, phone, email, title, imagePath;
    public ProfileDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            story = bundle.getString(Config.STORY);
            phone = bundle.getString(Config.PHONE);
            email = bundle.getString(Config.EMAIL);
            title = bundle.getString(Config.TITLE);
            imagePath = bundle.getString(Config.DOC_PATH);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_detail, container, false);
        txtStory = (TextView) view.findViewById(R.id.tv_story);
        txtTitle = (TextView) view.findViewById(R.id.tv_title);
        txtEmail = (TextView) view.findViewById(R.id.tv_email);
        txtPhone = (TextView) view.findViewById(R.id.tv_phone);

        String path = getActivity().getExternalFilesDir(null) + File.separator + imagePath;
        image = (ImageView) view.findViewById(R.id.image_news);
        Picasso.with(getActivity()).load(new File(path)).into(image);
        txtStory.setText(story);
        txtTitle.setText("TITLE : " + title);
        txtEmail.setText("EMAIL : " + email);
        txtPhone.setText("PHONE : " + phone);
        return  view;
    }

}
