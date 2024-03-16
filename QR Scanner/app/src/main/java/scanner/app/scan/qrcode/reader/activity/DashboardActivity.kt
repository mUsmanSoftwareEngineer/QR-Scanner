package scanner.app.scan.qrcode.reader.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.stickerwhatsapp.stickermaker.AdEvents.AdController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.OnPaidEventListener
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import scanner.app.scan.qrcode.reader.AdEvents.AdEvent
import scanner.app.scan.qrcode.reader.R
import scanner.app.scan.qrcode.reader.data.constant.Constants
import scanner.app.scan.qrcode.reader.data.preference.AppPreference
import scanner.app.scan.qrcode.reader.data.preference.PrefKey
import scanner.app.scan.qrcode.reader.fragment.QRGenerateFragment
import scanner.app.scan.qrcode.reader.fragment.QRHistoryFragment
import scanner.app.scan.qrcode.reader.fragment.QRScanFragment
import scanner.app.scan.qrcode.reader.fragment.QRSettingsFragment
import scanner.app.scan.qrcode.reader.utility.AdsManagerQ
import scanner.app.scan.qrcode.reader.utility.AppUtils
import java.util.Date

class DashboardActivity : AppCompatActivity() {
    var mAdManagerInterstitialAd: AdManagerInterstitialAd? = null
    var banner_ad_layout: RelativeLayout? = null
    var ads_relative: RelativeLayout? = null
    var backButton: ImageView? = null
    var fragmentName: TextView? = null
    var getFragmentVal = 0
    var mainInfo: ImageView? = null
    var mainSetting: ImageView? = null
    var removeAds: LinearLayout? = null
    var bannerFrame: FrameLayout? = null
    var removeAdsTxt: TextView? = null
    private var mActivity: Activity? = null
    private var mContext: Context? = null
    private var bottomNavigationView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        initVars()
        initViews()
        initFunctionality()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        Constants.selectFromMain = true
        Constants.isSelectingFile = false
    }

    private fun initVars() {
        mActivity = this@DashboardActivity
        mContext = (mActivity as DashboardActivity).applicationContext
    }

    private fun initViews() {
        setContentView(R.layout.activity_main)
        //        mViewPager = findViewById(R.id.viewpager);
//        mViewPager.setOffscreenPageLimit(4);// Улучшение плавности перелистывания,
        // путём сохранения данных фрагментов, но фрагмент истории при этом нужно
        // обновлять вручную при перелистывании
        bottomNavigationView = findViewById(R.id.navigation)
        fragmentName = findViewById(R.id.fragmentName)
        backButton = findViewById(R.id.backButton)
        mainInfo = findViewById(R.id.infoMain)
        mainSetting = findViewById(R.id.settingsMain)
        bannerFrame = findViewById(R.id.banner_adsview)
        ads_relative = findViewById(R.id.ads_relative)
        removeAds = findViewById(R.id.remove)
        removeAdsTxt = findViewById(R.id.removeADS)
    }

    private fun initFunctionality() {
        if (!Constants.removeAds) {
            removeAdsTxt!!.visibility = View.VISIBLE
            AdsManagerQ.getInstance().loadFreshBannerAd(
                mContext,
                bannerFrame,
                ads_relative,
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    mContext!!, 350
                ),
                resources.getString(R.string.banner_ad_home_main_sticky_unit_id)
            )
        } else {
            removeAdsTxt!!.visibility = View.GONE
            ads_relative!!.visibility = View.GONE
            removeAds!!.visibility = View.GONE
        }
        getFragmentVal = intent.getIntExtra("fragmentVal", 0)
        if (getFragmentVal == 0) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,
                    QRScanFragment()
                ).commit()
            fragmentName!!.text = mActivity!!.resources.getString(R.string.scan)
            AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 0)
            bottomNavigationView!!.selectedItemId = R.id.nav_scan
        } else if (getFragmentVal == 1) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,
                    QRGenerateFragment()
                ).commit()
            fragmentName!!.text = mActivity!!.resources.getString(R.string.menu_generate)
            AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 1)
            bottomNavigationView!!.selectedItemId = R.id.nav_generate
        } else if (getFragmentVal == 2) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,
                    QRHistoryFragment()
                ).commit()
            fragmentName!!.text = mActivity!!.resources.getString(R.string.history)
            AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 2)
            bottomNavigationView!!.selectedItemId = R.id.nav_history
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,
                    QRSettingsFragment()
                ).commit()
            fragmentName!!.text = mActivity!!.resources.getString(R.string.menu_setting)
            AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 4)
            bottomNavigationView!!.selectedItemId = R.id.nav_setting
        }


        // TODO Sample banner Ad implementation
    }

    private fun initListeners() {
        bottomNavigationView!!.setOnNavigationItemSelectedListener { item ->
            val selectedFragment: Fragment
            val id = item.itemId
            if (id == R.id.nav_scan) {
                AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 0)
                //                    Constants.adLogicResultBottomBar++;
                selectedFragment =
                    QRScanFragment()
                fragmentName!!.text = mActivity!!.resources.getString(R.string.scan)
            } else if (id == R.id.nav_generate) {
                AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 1)
                selectedFragment =
                    QRGenerateFragment()
                showInterstitial(mActivity!!.resources.getString(R.string.menu_generate))
            } else if (id == R.id.nav_history) {
                AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 2)
                selectedFragment =
                    QRHistoryFragment()
                showInterstitial(mActivity!!.resources.getString(R.string.menu_history))
            } else {
                AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 3)
                selectedFragment =
                    QRSettingsFragment()
                showInterstitial(mActivity!!.resources.getString(R.string.menu_setting))
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
            true
        }

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
        backButton!!.setOnClickListener { startActivity(Intent(mContext, AfterSplash::class.java)) }
        mainInfo!!.setOnClickListener { AppUtils.showPermissionDialog(mActivity, mContext) }
        removeAds!!.setOnClickListener { //                startActivity(new Intent(MainActivity.this, RemoveAds.class));
            startActivity(Intent(this@DashboardActivity, RemoveAdsActivity::class.java))
        }
        mainSetting!!.setOnClickListener {
            try {
                val url =
                    "https://docs.google.com/document/d/1TUGt3wOVriLg3pDxz1eBDaTewzshtDNv/edit?usp=sharing&ouid=103839944119900727008&rtpof=true&sd=true"
                val i = Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse(url))
                startActivity(i)
            } catch (ignored: Exception) {
            }
        }
        removeAdsTxt!!.setOnClickListener {
            startActivity(
                Intent(
                    this@DashboardActivity,
                    RemoveAdsActivity::class.java
                )
            )
        }
    }

    private fun showInterstitial(fragName: String) {
        if (Constants.timerStart) {
            try {
                val date = Date(System.currentTimeMillis()) //or simply new Date();
                val diff = date.time - Constants.oldDate.time
                val seconds = diff / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                val days = hours / 24
                if (seconds > 30) {
                    if (!Constants.removeAds) {
                        mAdManagerInterstitialAd = AdsManagerQ.getInstance().ad
                        if (mAdManagerInterstitialAd != null) {
                            mAdManagerInterstitialAd!!.show(this@DashboardActivity)
                            mAdManagerInterstitialAd!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        AdsManagerQ.getInstance().InterstitialAd(
                                            mContext,
                                            resources.getString(R.string.InterstitialOn)
                                        )
                                        //
                                        Constants.oldDate = Date(System.currentTimeMillis())
                                        //                        Constants.adLogicResultBottomBar = 3;
                                        fragmentName!!.text = fragName
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        // Called when fullscreen content failed to show.
                                        AdsManagerQ.getInstance().InterstitialAd(
                                            mContext,
                                            resources.getString(R.string.InterstitialOn)
                                        )
                                        fragmentName!!.text = fragName
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        try {
                                            val date =
                                                Date(System.currentTimeMillis()) //or simply new Date();
                                            val millis = date.time
                                            AppPreference.getInstance(mContext)
                                                .setLong(PrefKey.AdTime, millis)
                                        } catch (ignored: Exception) {
                                        }
                                        AdEvent.AdAnalysisInterstitial(
                                            AdController.AdType.INTERSTITIAL,
                                            mContext
                                        )
                                    }
                                }
                            mAdManagerInterstitialAd!!.onPaidEventListener =
                                OnPaidEventListener { adValue ->
                                    val bundle = Bundle()
                                    bundle.putString("currency", adValue.currencyCode)
                                    bundle.putString("precision", adValue.precisionType.toString())
                                    bundle.putString("valueMicros", adValue.valueMicros.toString())
                                    bundle.putString("network", "InterstitialAd")
                                    try {
                                        FirebaseAnalytics.getInstance(mContext!!)
                                            .logEvent("paid_ad_impressions", bundle)
                                    } catch (e: Exception) {
                                        Log.d("events", e.message!!)
                                    }
                                }
                        } else {
                            fragmentName!!.text = fragName
                        }

//            Constants.adLogicResultBottomBar++;
                    } else {
                        fragmentName!!.text = fragName
                    }
                }

//                            Log.d("checkTime",seconds+" "+new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(date)+":"+new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(Constants.oldDate));
                Log.d("checkTime", "$seconds ")
            } catch (ignored: Exception) {
            }
        } else {
            if (!Constants.removeAds) {
                mAdManagerInterstitialAd = AdsManagerQ.getInstance().ad
                if (mAdManagerInterstitialAd != null) {
                    mAdManagerInterstitialAd!!.show(this@DashboardActivity)
                    mAdManagerInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                AdsManagerQ.getInstance().InterstitialAd(
                                    mContext,
                                    resources.getString(R.string.InterstitialOn)
                                )
                                //
                                Constants.oldDate = Date(System.currentTimeMillis())
                                //                        Constants.adLogicResultBottomBar = 3;
                                fragmentName!!.text = fragName
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when fullscreen content failed to show.
                                AdsManagerQ.getInstance().InterstitialAd(
                                    mContext,
                                    resources.getString(R.string.InterstitialOn)
                                )
                                fragmentName!!.text = fragName
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                Constants.timerStart = true
                                try {
                                    val date =
                                        Date(System.currentTimeMillis()) //or simply new Date();
                                    val millis = date.time
                                    AppPreference.getInstance(mContext)
                                        .setLong(PrefKey.AdTime, millis)
                                } catch (ignored: Exception) {
                                }
                                AdEvent.AdAnalysisInterstitial(
                                    AdController.AdType.INTERSTITIAL,
                                    mContext
                                )
                            }
                        }
                    mAdManagerInterstitialAd!!.onPaidEventListener =
                        OnPaidEventListener { adValue ->
                            val bundle = Bundle()
                            bundle.putString("currency", adValue.currencyCode)
                            bundle.putString("precision", adValue.precisionType.toString())
                            bundle.putString("valueMicros", adValue.valueMicros.toString())
                            bundle.putString("network", "InterstitialAd")
                            try {
                                FirebaseAnalytics.getInstance(mContext!!)
                                    .logEvent("paid_ad_impressions", bundle)
                            } catch (e: Exception) {
                                Log.d("events", e.message!!)
                            }
                        }
                } else {
                    fragmentName!!.text = fragName
                }

//            Constants.adLogicResultBottomBar++;
            } else {
                fragmentName!!.text = fragName
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

    override fun onBackPressed() {
        startActivity(Intent(mContext, AfterSplash::class.java))
    }
}
