package tk.cavinc.frozerremotecontrol.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by cav on 15.07.19.
 */

public class App extends Application{
    private static Context sContext;
    private static SharedPreferences sSharedPreferences;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getBaseContext();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(sContext);
    }

    public static Context getsContext() {
        return sContext;
    }

    public static SharedPreferences getsSharedPreferences() {
        return sSharedPreferences;
    }
}
