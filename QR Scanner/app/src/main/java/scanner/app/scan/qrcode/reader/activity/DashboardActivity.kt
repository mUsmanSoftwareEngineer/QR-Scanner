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
import scanner.app.scan.qrcode.reader.databinding.DashboardActivityBinding
import scanner.app.scan.qrcode.reader.fragment.QRGenerateFragment
import scanner.app.scan.qrcode.reader.fragment.QRHistoryFragment
import scanner.app.scan.qrcode.reader.fragment.QRScanFragment
import scanner.app.scan.qrcode.reader.fragment.QRSettingsFragment
import scanner.app.scan.qrcode.reader.utility.AdsManagerQ
import scanner.app.scan.qrcode.reader.utility.AppUtils
import java.util.Date

class DashboardActivity : AppCompatActivity() {


    var getFragmentVal = 0
    private var mActivity: Activity? = null
    private var mContext: Context? = null
    private var bottomNavigationView: BottomNavigationView? = null

    //View Binding
    private lateinit var dashboardActivityBinding: DashboardActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowFullScreen()
        dashboardActivityBinding = DashboardActivityBinding.inflate(layoutInflater)
        setContentView(dashboardActivityBinding.root)
        initVars()
        initViews()
        initFunctionality()
        initListeners()
    }

    private fun setWindowFullScreen() {
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
        //        mViewPager = findViewById(R.id.viewpager);
//        mViewPager.setOffscreenPageLimit(4);// Улучшение плавности перелистывания,
        // путём сохранения данных фрагментов, но фрагмент истории при этом нужно
        // обновлять вручную при перелистывании
        bottomNavigationView = findViewById(R.id.navigation)
    }

    private fun initFunctionality() {
        getFragmentVal = intent.getIntExtra("fragmentVal", 0)
        if (getFragmentVal == 0) {
            supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container, QRScanFragment()
                ).commit()
            AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 0)
            bottomNavigationView!!.selectedItemId = R.id.nav_scan
        } else if (getFragmentVal == 1) {
            supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container, QRGenerateFragment()
                ).commit()
            AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 1)
            bottomNavigationView!!.selectedItemId = R.id.nav_generate
        } else if (getFragmentVal == 2) {
            supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container, QRHistoryFragment()
                ).commit()
            AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 2)
            bottomNavigationView!!.selectedItemId = R.id.nav_history
        } else {
            supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container, QRSettingsFragment()
                ).commit()
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
                selectedFragment = QRScanFragment()
            } else if (id == R.id.nav_generate) {
                AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 1)
                selectedFragment = QRGenerateFragment()
            } else if (id == R.id.nav_history) {
                AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 2)
                selectedFragment = QRHistoryFragment()
            } else {
                AppPreference.getInstance(mContext).setInteger(PrefKey.FragmentVal, 3)
                selectedFragment = QRSettingsFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment).commit()
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

//       removeAds!!.setOnClickListener { //                startActivity(new Intent(MainActivity.this, RemoveAds.class));
//            startActivity(Intent(this@DashboardActivity, RemoveAdsActivity::class.java))
//        }

    }



    override fun onBackPressed() {
        startActivity(Intent(mContext, AfterSplash::class.java))
    }
}
