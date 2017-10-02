package tzchoice.kisanga.joshua.policybrief.realm;


import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;
import tzchoice.kisanga.joshua.policybrief.table.RAudio;
import tzchoice.kisanga.joshua.policybrief.table.RAudioCategory;
import tzchoice.kisanga.joshua.policybrief.table.RData;
import tzchoice.kisanga.joshua.policybrief.table.RDataCatefory;
import tzchoice.kisanga.joshua.policybrief.table.RDocCategory;
import tzchoice.kisanga.joshua.policybrief.table.RDocument;
import tzchoice.kisanga.joshua.policybrief.table.RNews;
import tzchoice.kisanga.joshua.policybrief.table.RProfile;
import tzchoice.kisanga.joshua.policybrief.table.RVideo;
import tzchoice.kisanga.joshua.policybrief.table.RVideoCategory;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }



    //find all objects in the RDocCategory.class
    public RealmResults<RDocCategory> getDocCategories() {

        return realm.where(RDocCategory.class).findAll();
    }

    public RealmResults<RDataCatefory> getDataCategories() {

        return realm.where(RDataCatefory.class).findAll();
    }

    //find all objects in the RDocCategory.class
    public RealmResults<RNews> getNews() {

        return realm.where(RNews.class).findAll();
    }
 //find all objects in the RDocCategory.class
    public RealmResults<RProfile> getProfiles() {

        return realm.where(RProfile.class).findAll();
    }

    //query a single item with the given id
    public RealmResults<RDocument> getDocuments(int id) {

        return realm.where(RDocument.class).equalTo("categoryId", id)
        .findAll();
    }

    //query a single item with the given id
    public RealmResults<RData> getStatistics(int id) {

        return realm.where(RData.class).equalTo("dataCategoryId", id)
        .findAll();
    }

    //query a single item with the given id
    public RealmResults<RAudio> getAudios(int id) {

        return realm.where(RAudio.class).equalTo("audioCategoryId", id)
        .findAll();
    }
    //query a single item with the given id
    public RealmResults<RVideo> getVideos(int id) {

        return realm.where(RVideo.class).equalTo("videoCategoryId", id)
        .findAll();
    }

    //query a single item with the given id
    public RealmResults<RAudioCategory> getAudioCategories() {

        return realm.where(RAudioCategory.class).findAll();
    }

//query a single item with the given id
    public RealmResults<RVideoCategory> getVideoCategories() {

        return realm.where(RVideoCategory.class).findAll();
    }



    //query example
    public RealmResults<RDocCategory> queryedDocCategoy() {

        return realm.where(RDocCategory.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}
