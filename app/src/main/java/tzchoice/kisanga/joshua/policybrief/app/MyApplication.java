package tzchoice.kisanga.joshua.policybrief.app;

import android.app.Application;

import io.realm.Realm;



/**
 * Created by user on 5/27/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        Realm.init(this);

    }
}
