package tzchoice.kisanga.joshua.policybrief.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.activity.LoginActivity;
import tzchoice.kisanga.joshua.policybrief.adapter.AutoFitGridLayoutManager;
import tzchoice.kisanga.joshua.policybrief.adapter.NewsAdapter;
import tzchoice.kisanga.joshua.policybrief.adapter.SliderAdapter;
import tzchoice.kisanga.joshua.policybrief.app.Config;
import tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler;
import tzchoice.kisanga.joshua.policybrief.realm.RealmController;
import tzchoice.kisanga.joshua.policybrief.table.RNews;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private List<RNews> newsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private SQLiteHandler db;
   // SliderLayout sliderLayout ;

    HashMap<String, String> HashMapForURL;

    RecyclerView MyRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);


        newsList = RealmController.with(getActivity()).getNews();


        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (newsList.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new SliderAdapter(newsList, getActivity()));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getActivity(), 1000);
        mRecyclerView.setLayoutManager(layoutManager);



        mAdapter = new NewsAdapter(newsList, getActivity());
        mAdapter.notifyDataSetChanged();


        final SkeletonScreen skeletonScreen = Skeleton.bind(mRecyclerView)
                .adapter(mAdapter)
                .load(R.layout.item_skeleton_news)
                .show(); //default count is 10
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setAdapter(mAdapter);
                skeletonScreen.hide();
            }
        }, 3000);

        return view;
    }

//    private void init() {
//        for(String name : HashMapForURL.keySet()){
//
//            TextSliderView textSliderView = new TextSliderView(getActivity());
//
//            textSliderView
//                    .description(name)
//                    .image(HashMapForURL.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener(this);
//
//            textSliderView.bundle(new Bundle());
//
//            textSliderView.getBundle()
//                    .putString("extra",name);
//
//            sliderLayout.addSlider(textSliderView);
//        }
//        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
//
//        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//
//        sliderLayout.setCustomAnimation(new DescriptionAnimation());
//
//        sliderLayout.setDuration(5000);
//
//        sliderLayout.addOnPageChangeListener(null);
//    }




    private void message(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }



    public void addImagesUrlOnline(){

        Log.d(TAG, "AddImagesUrlOnline: "  + newsList.size());

        for(int i =1; i < 3; i++) {

            HashMapForURL = new HashMap<String, String>();
            try {
                HashMapForURL.put(newsList.get(1).getSummary(), Config.url + "document/" + newsList.get(3).getImagePath());
                HashMapForURL.put(newsList.get(2).getSummary(), Config.url + "document/" + newsList.get(2).getImagePath());
                HashMapForURL.put(newsList.get(3).getSummary(), Config.url + "document/" + newsList.get(3).getImagePath());
            } catch (Exception e){

            }

        }

    }

//    @Override
//    public void onStop() {
//
//        sliderLayout.stopAutoCycle();
//
//        super.onStop();
//    }
//
//    @Override
//    public void onSliderClick(BaseSliderView slider) {
//        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        Log.d("Slider Demo", "Page Changed: " + position);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }

//    public void AddImageUrlFormLocalRes(){
//
//        HashMapForLocalRes = new HashMap<String, Integer>();
//
//        HashMapForLocalRes.put("CupCake", R.drawable.cupcake);
//        HashMapForLocalRes.put("Donut", R.drawable.donut);
//        HashMapForLocalRes.put("Eclair", R.drawable.eclair);
//        HashMapForLocalRes.put("Froyo", R.drawable.froyo);
//        HashMapForLocalRes.put("GingerBread", R.drawable.gingerbread);
//
//    }



}
