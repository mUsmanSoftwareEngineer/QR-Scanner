package scanner.app.scan.qrcode.reader.activity;

import android.app.Application;

import java.util.Date;

import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.data.constant.Constants;
import scanner.app.scan.qrcode.reader.utility.AppOpenScript;


public class MyApp extends Application {

    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {

        super.onCreate();

        instance = this;

        String[] AD_UNIT_IDS = {getResources().getString(R.string.app_open_id),
                getResources().getString(R.string.app_open_id_2),
                getResources().getString(R.string.app_open_id_3)};
        new AppOpenScript(this, AD_UNIT_IDS);  // to open app open


//        AppOpenManagerNew appOpenManager = new AppOpenManagerNew(this);
        try {
            Constants.oldDate = new Date(System.currentTimeMillis());
        } catch (Exception ignored) {

        }

    }


}
