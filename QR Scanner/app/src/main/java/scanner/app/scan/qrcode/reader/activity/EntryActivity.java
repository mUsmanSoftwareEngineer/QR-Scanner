package scanner.app.scan.qrcode.reader.activity;


import static scanner.app.scan.qrcode.reader.utility.AppUtils.changeLang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.data.constant.Constants;
import scanner.app.scan.qrcode.reader.data.preference.AppPreference;
import scanner.app.scan.qrcode.reader.data.preference.PrefKey;
import scanner.app.scan.qrcode.reader.utility.ActivityUtils;
import scanner.app.scan.qrcode.reader.utility.AdsManagerQ;


public class EntryActivity extends AppCompatActivity {


    SharedPreferences sharedpreferences;

    Activity mActivity;
    boolean checkState;
    private Context mContext;
    Date myDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mActivity = EntryActivity.this;
        mContext = mActivity.getApplicationContext();


        super.onCreate(savedInstanceState);


        LanguageAndTemplateUnlock();


        Constants.isSelectingFile = true;


        Constants.check = true;


        AdsManagerQ adsManagerQ = AdsManagerQ.getInstance();

        adsManagerQ.InterstitialAd(EntryActivity.this, getResources().getString(R.string.InterstitialOn));

//        AdsManagerQ.getInstance().rewardedAd(EntryActivity.this, getResources().getString(R.string.rewarded));

        boolean firstRun = AppPreference.getInstance(this).getBoolean(PrefKey.ActivityFirstRun, true);

        checkState = AppPreference.getInstance(this).getBoolean(PrefKey.RemoveAdsPrefs, false);


        if (checkState) {

            Constants.removeAds = true;
//            Constants.lockTemplate = 0;
        }

//        Intent startMainActivity = new Intent(EntryActivity.this, AfterSplash.class);
//        startActivity(startMainActivity);


        Log.d("firstRun", firstRun + " ");

        if (firstRun) {
            Log.d("firstRun", "inside this block ");

            try {


                ActivityUtils.getInstance().invokeActivity(EntryActivity.this, SlideActivity.class, true, 0, 0);

            } catch (Exception ignored) {

            }
        } else {

            int valFrag = AppPreference.getInstance(mContext).getInteger(PrefKey.FragmentVal);


            Intent startMainActivity = new Intent(EntryActivity.this, DashboardActivity.class);
            startMainActivity.putExtra("fragmentVal", valFrag);
            startMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startMainActivity);


        }


    }

    private void LanguageAndTemplateUnlock() {

        sharedpreferences = getSharedPreferences(PrefKey.APP_PREF_NAME,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(PrefKey.languageKey)) {

            changeLang(EntryActivity.this, AppPreference.getInstance(mContext).getString(PrefKey.languageKey));

        }

        try {


            if (!checkState) {

                if (sharedpreferences.contains(PrefKey.DateTime)) {

                    myDate = new Date(AppPreference.getInstance(mContext).getLong(PrefKey.DateTime));

                    Date date = new Date(System.currentTimeMillis()); //or simply new Date();

                    long diff = date.getTime() - myDate.getTime();
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = (hours / 24) ;





                }

            }


        } catch (Exception ignored) {

        }

    }


}

