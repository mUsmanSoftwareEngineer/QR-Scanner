package com.bezruk.qrcodebarcode.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bezruk.qrcodebarcode.R;
import com.bezruk.qrcodebarcode.adapter.MainPagerAdapter;
import com.bezruk.qrcodebarcode.data.preference.AppPreference;
import com.bezruk.qrcodebarcode.data.preference.PrefKey;
import com.bezruk.qrcodebarcode.utility.AdManager;
import com.bezruk.qrcodebarcode.utility.AppUtils;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Activity mActivity;
    private Context mContext;

    AlertDialog.Builder ad;

    private ViewPager mViewPager;
    private ArrayList<String> mFragmentItems;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVars();
        initViews();
        initFunctionality();
        initListeners();

    }

    private void initVars() {
        mActivity = MainActivity.this;
        mContext = mActivity.getApplicationContext();
        mFragmentItems = new ArrayList<>();
    }

    private void initViews() {
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);// Улучшение плавности перелистывания,
        // путём сохранения данных фрагментов, но фрагмент истории при этом нужно
        // обновлять вручную при перелистывании

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
    }

    private void initFunctionality() {
        setUpViewPager();

        // TODO Sample banner Ad implementation
        AdManager.getInstance(mContext).showBannerAd((AdView)findViewById(R.id.adViewMain));
    }

    private void initListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_scan:
                        mViewPager.setCurrentItem(0, true);
                        break;
                    case R.id.nav_generate:
                        mViewPager.setCurrentItem(1, true);
                        break;
                    case R.id.nav_history:
                        //обновление списка истории при перелистывании
                        /*if(getFragmentRefreshListener()!=null){
                            getFragmentRefreshListener().onRefresh();
                        }*/
                        mViewPager.setCurrentItem(2, true);
                        break;
                    case R.id.nav_setting:
                        mViewPager.setCurrentItem(3, true);
                        break;
                }
                return true;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_scan);
                } else if(position == 1) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_generate);
                } else if(position == 2) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_history);
                } else if(position == 3) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_setting);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    public void setUpViewPager() {

        mFragmentItems.add(getString(R.string.menu_scan));
        mFragmentItems.add(getString(R.string.menu_generate));
        mFragmentItems.add(getString(R.string.menu_history));
        mFragmentItems.add(getString(R.string.menu_setting));

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragmentItems);
        mViewPager.setAdapter(mainPagerAdapter);
        mainPagerAdapter.notifyDataSetChanged();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Constants.PERMISSION_REQ) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.CAMERA)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        allowCameraUseBtn.setVisibility(View.GONE);
                        setUpViewPager();
                    } else {
                        AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
                        allowCameraUseBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

    }

    private boolean checkCameraPermission() {
        if ((ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA},
                    Constants.CAMERA_PERMISSION_REQ);
        } else {
            setUpViewPager();
            return true;
        }
        return false;
    }*/



    @Override
    public void onBackPressed() {
        if(AppPreference.getInstance(mContext).getInteger(PrefKey.RATE_DIALOG_VALUE)==-1){
            if((int)(Math.random()*2)==1){
                AppUtils.showRateDialog(mActivity);
            } else {
                AppUtils.tapToExit(mActivity);
            }

        } else {
            AppUtils.tapToExit(mActivity);
        }
    }

}
