package id.co.tirezone.tirezonemobile;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Jessica on 13-Jan-17.
 */

public class FireApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
