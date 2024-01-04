package scanner.app.scan.qrcode.reader.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Date;

import app.stickerwhatsapp.stickermaker.AdEvents.AdController;
import scanner.app.scan.qrcode.reader.AdEvents.AdEvent;
import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.data.constant.Constants;
import scanner.app.scan.qrcode.reader.data.preference.AppPreference;
import scanner.app.scan.qrcode.reader.data.preference.PrefKey;
import scanner.app.scan.qrcode.reader.fragment.GenerateFragment;
import scanner.app.scan.qrcode.reader.fragment.HistoryFragment;
import scanner.app.scan.qrcode.reader.fragment.ScanFragment;
import scanner.app.scan.qrcode.reader.fragment.SettingsFragment;
import scanner.app.scan.qrcode.reader.utility.AdsManagerQ;
import scanner.app.scan.qrcode.reader.utility.AppUtils;


public class MainActivity extends AppCompatActivity {

    public AdManagerInterstitialAd mAdManagerInterstitialAd;
    RelativeLayout banner_ad_layout, ads_relative;

    ImageView backButton;
    TextView fragmentName;
    int getFragmentVal = 0;
    ImageView mainInfo, mainSetting;
    LinearLayout removeAds;
    FrameLayout bannerFrame;
    TextView removeAdsTxt;
    private Activity mActivity;
    private Context mContext;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initVars();
        initViews();
        initFunctionality();
        initListeners();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.selectFromMain = true;
        Constants.isSelectingFile = false;

    }


    private void initVars() {

        mActivity = MainActivity.this;
        mContext = mActivity.getApplicationContext();


    }

    private void initViews() {
        setContentView(R.layout.activity_main);
//        mViewPager = findViewById(R.id.viewpager);
//        mViewPager.setOffscreenPageLimit(4);// Улучшение плавности перелистывания,
        // путём сохранения данных фрагментов, но фрагмент истории при этом нужно
        // обновлять вручную при перелистывании


        bottomNavigationView = findViewById(R.id.navigation);


        fragmentName = findViewById(R.id.fragmentName);
        backButton = findViewById(R.id.backButton);

        mainInfo = findViewById(R.id.infoMain);
        mainSetting = findViewById(R.id.settingsMain);


        bannerFrame = findViewById(R.id.banner_adsview);

        ads_relative = findViewById(R.id.ads_relative);

        removeAds = findViewById(R.id.remove);

        removeAdsTxt = findViewById(R.id.removeADS);

    }


    private void initFunctionality() {


        if (!Constants.removeAds) {
            removeAdsTxt.setVisibility(View.VISIBLE);
            AdsManagerQ.getInstance().loadFreshBannerAd(mContext, bannerFrame, ads_relative, AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mContext, 350), getResources().getString(R.string.banner_ad_home_main_sticky_unit_id));

        } else {
            removeAdsTxt.setVisibility(View.GONE);
            ads_relative.setVisibility(View.GONE);
            removeAds.setVisibility(View.GONE);
        }


        getFragmentVal = getIntent().getIntExtra("fragmentVal", 0);


        if (getFragmentVal == 0) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScanFragment()).commit();
            fragmentName.setText(mActivity.getResources().getString(R.string.scan));
            AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 0);
            bottomNavigationView.setSelectedItemId(R.id.nav_scan);
        } else if (getFragmentVal == 1) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GenerateFragment()).commit();
            fragmentName.setText(mActivity.getResources().getString(R.string.menu_generate));
            AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 1);
            bottomNavigationView.setSelectedItemId(R.id.nav_generate);
        } else if (getFragmentVal == 2) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryFragment()).commit();
            fragmentName.setText(mActivity.getResources().getString(R.string.history));
            AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 2);
            bottomNavigationView.setSelectedItemId(R.id.nav_history);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            fragmentName.setText(mActivity.getResources().getString(R.string.menu_setting));
            AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 4);
            bottomNavigationView.setSelectedItemId(R.id.nav_setting);
        }


        // TODO Sample banner Ad implementation

    }


    private void initListeners() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment;
                int id = item.getItemId();

                if (id == R.id.nav_scan) {
                    AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 0);
//                    Constants.adLogicResultBottomBar++;
                    selectedFragment = new ScanFragment();
                    fragmentName.setText(mActivity.getResources().getString(R.string.scan));
                } else if (id == R.id.nav_generate) {
                    AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 1);
                    selectedFragment = new GenerateFragment();
                    showInterstitial(mActivity.getResources().getString(R.string.menu_generate));
                } else if (id == R.id.nav_history) {
                    AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 2);
                    selectedFragment = new HistoryFragment();
                    showInterstitial(mActivity.getResources().getString(R.string.menu_history));
                } else {
                    AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 3);
                    selectedFragment = new SettingsFragment();
                    showInterstitial(mActivity.getResources().getString(R.string.menu_setting));
                }


                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
        });

//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//                if (position == 0) {
//                    bottomNavigationView.setSelectedItemId(R.id.nav_scan);
////                    afterSplash.setVisibility(View.GONE);
//                } else if (position == 1) {
//                    bottomNavigationView.setSelectedItemId(R.id.nav_generate);
////                    afterSplash.setVisibility(View.GONE);
//                } else if (position == 2) {
//                    bottomNavigationView.setSelectedItemId(R.id.nav_template);
//                } else if (position == 3) {
//                    bottomNavigationView.setSelectedItemId(R.id.nav_history);
////                    afterSplash.setVisibility(View.GONE);
//                } else if (position == 4) {
//                    bottomNavigationView.setSelectedItemId(R.id.nav_setting);
////                    afterSplash.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                startActivity(new Intent(mContext, AfterSplash.class));

            }

        });

        mainInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showPermissionDialog(mActivity, mContext);
            }
        });

        removeAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(MainActivity.this, RemoveAds.class));
                startActivity(new Intent(MainActivity.this, RemoveAdsActivity.class));

            }
        });

        mainSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = "https://docs.google.com/document/d/1TUGt3wOVriLg3pDxz1eBDaTewzshtDNv/edit?usp=sharing&ouid=103839944119900727008&rtpof=true&sd=true";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception ignored) {

                }
            }
        });

        removeAdsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RemoveAdsActivity.class));
            }
        });
    }

    private void showInterstitial(String fragName) {


        if (Constants.timerStart) {
            try {
                Date date = new Date(System.currentTimeMillis()); //or simply new Date();

                long diff = date.getTime() - Constants.oldDate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = (hours / 24);

                if (seconds > 30) {
                    if (!Constants.removeAds) {

                        mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
                        if (mAdManagerInterstitialAd != null) {


                            mAdManagerInterstitialAd.show(MainActivity.this);
                            mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when fullscreen content is dismissed.
                                    AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));
//
                                    Constants.oldDate = new Date(System.currentTimeMillis());
//                        Constants.adLogicResultBottomBar = 3;
                                    fragmentName.setText(fragName);
                                }


                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    // Called when fullscreen content failed to show.
                                    AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));


                                    fragmentName.setText(fragName);
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                    try {
                                        Date date = new Date(System.currentTimeMillis()); //or simply new Date();
                                        long millis = date.getTime();
                                        AppPreference.getInstance(mContext).setLong(PrefKey.AdTime, millis);
                                    } catch (Exception ignored) {

                                    }

                                    AdEvent.AdAnalysisInterstitial(AdController.AdType.INTERSTITIAL,mContext);
                                }

                            });

                            mAdManagerInterstitialAd.setOnPaidEventListener(new OnPaidEventListener() {
                                @Override
                                public void onPaidEvent(@NonNull AdValue adValue) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("currency", adValue.getCurrencyCode());
                                    bundle.putString("precision", String.valueOf(adValue.getPrecisionType()));
                                    bundle.putString("valueMicros", String.valueOf(adValue.getValueMicros()));
                                    bundle.putString("network", "InterstitialAd");
                                    try {
                                        FirebaseAnalytics.getInstance(mContext).logEvent("paid_ad_impressions", bundle);

                                    } catch (Exception e) {
                                        Log.d("events", e.getMessage());
                                    }
                                }
                            });


                        } else {

                            fragmentName.setText(fragName);
                        }

//            Constants.adLogicResultBottomBar++;
                    } else {

                        fragmentName.setText(fragName);
                    }
                }

//                            Log.d("checkTime",seconds+" "+new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(date)+":"+new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(Constants.oldDate));
                Log.d("checkTime", seconds + " ");
            } catch (Exception ignored) {

            }
        } else {
            if (!Constants.removeAds) {

                mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
                if (mAdManagerInterstitialAd != null) {


                    mAdManagerInterstitialAd.show(MainActivity.this);
                    mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));
//
                            Constants.oldDate = new Date(System.currentTimeMillis());
//                        Constants.adLogicResultBottomBar = 3;
                            fragmentName.setText(fragName);
                        }


                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            // Called when fullscreen content failed to show.
                            AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));


                            fragmentName.setText(fragName);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            Constants.timerStart = true;
                            try {
                                Date date = new Date(System.currentTimeMillis()); //or simply new Date();
                                long millis = date.getTime();
                                AppPreference.getInstance(mContext).setLong(PrefKey.AdTime, millis);
                            } catch (Exception ignored) {

                            }

                            AdEvent.AdAnalysisInterstitial(AdController.AdType.INTERSTITIAL,mContext);
                        }

                    });

                    mAdManagerInterstitialAd.setOnPaidEventListener(new OnPaidEventListener() {
                        @Override
                        public void onPaidEvent(@NonNull AdValue adValue) {
                            Bundle bundle = new Bundle();
                            bundle.putString("currency", adValue.getCurrencyCode());
                            bundle.putString("precision", String.valueOf(adValue.getPrecisionType()));
                            bundle.putString("valueMicros", String.valueOf(adValue.getValueMicros()));
                            bundle.putString("network", "InterstitialAd");
                            try {
                                FirebaseAnalytics.getInstance(mContext).logEvent("paid_ad_impressions", bundle);

                            } catch (Exception e) {
                                Log.d("events", e.getMessage());
                            }
                        }
                    });


                } else {

                    fragmentName.setText(fragName);
                }

//            Constants.adLogicResultBottomBar++;
            } else {

                fragmentName.setText(fragName);
            }
        }


//        if (!Constants.removeAds && Constants.adLogicResultBottomBar == 2) {
//
//            mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
//            if (mAdManagerInterstitialAd != null) {
//
//
//                mAdManagerInterstitialAd.show(MainActivity.this);
//                mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                    @Override
//                    public void onAdDismissedFullScreenContent() {
//                        // Called when fullscreen content is dismissed.
//                        AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));
////
//                        Constants.oldDate = new Date(System.currentTimeMillis());
//                        Constants.adLogicResultBottomBar = 3;
//                        fragmentName.setText(fragName);
//                    }
//
//
//                    @Override
//                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                        // Called when fullscreen content failed to show.
//                        AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));
//
//
//                        fragmentName.setText(fragName);
//                    }
//
//                    @Override
//                    public void onAdShowedFullScreenContent() {
//                        // Called when fullscreen content is shown.
//                        // Make sure to set your reference to null so you don't
//                        // show it a second time.
//                        try {
//                            Date date = new Date(System.currentTimeMillis()); //or simply new Date();
//                            long millis = date.getTime();
//                            AppPreference.getInstance(mContext).setLong(PrefKey.AdTime, millis);
//                        } catch (Exception ignored) {
//
//                        }
//                    }
//
//                });
//
//                mAdManagerInterstitialAd.setOnPaidEventListener(new OnPaidEventListener() {
//                    @Override
//                    public void onPaidEvent(@NonNull AdValue adValue) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("currency", adValue.getCurrencyCode());
//                        bundle.putString("precision", String.valueOf(adValue.getPrecisionType()));
//                        bundle.putString("valueMicros", String.valueOf(adValue.getValueMicros()));
//                        bundle.putString("network", "InterstitialAd");
//                        try {
//                            FirebaseAnalytics.getInstance(mContext).logEvent("paid_ad_impressions", bundle);
//
//                        } catch (Exception e) {
//                            Log.d("events", e.getMessage());
//                        }
//                    }
//                });
//
//
//            } else {
//
//                fragmentName.setText(fragName);
//            }
//
////            Constants.adLogicResultBottomBar++;
//        } else {
//
//            fragmentName.setText(fragName);
//        }
    }


    @Override
    public void onBackPressed() {


        startActivity(new Intent(mContext, AfterSplash.class));

    }


}
