package scanner.app.scan.qrcode.reader.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;

import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.activity.EntryActivity;
import scanner.app.scan.qrcode.reader.activity.SlideActivity;
import scanner.app.scan.qrcode.reader.data.constant.Constants;
import scanner.app.scan.qrcode.reader.data.preference.AppPreference;
import scanner.app.scan.qrcode.reader.data.preference.PrefKey;
import scanner.app.scan.qrcode.reader.utility.ActivityUtils;
import scanner.app.scan.qrcode.reader.utility.AppUtils;

public class SettingsFragment extends Fragment implements View.OnTouchListener {

    int s = 0;

    TextView app_version;
    String billingTag = "billingTag";
    BillingClient billingClient;
    String productKey = "app.qrcodegenerator.removeads";
    //    private LinearLayout personalize_ads;
    ImageView rate_icon, share_icon, privacy_icon, localize_icon;
    //    String productKey = "android.test.purchased";
    Activity mActivity;
    Context mContext;
    //    private SwitchButton switch_vibrate, switch_autofocus, switch_auto_url;
    private SwitchCompat switch_vibrate, switch_autofocus, switch_auto_url;
    private LinearLayout rate_us, share_app, privacy_app, localize_app, restore, intro, appPermission;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        initVar();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        initView(rootView);
        if (Constants.removeAds) {
            restore.setVisibility(View.GONE);
        }
        initListener();

        return rootView;
    }

    private void initVar() {
        mActivity = requireActivity();
        mContext = mActivity.getApplicationContext();
    }

    private void initView(View rootView) {
        switch_vibrate = rootView.findViewById(R.id.switch_vibrate);
        switch_autofocus = rootView.findViewById(R.id.switch_autofocus);
        switch_auto_url = rootView.findViewById(R.id.switch_auto_url);

        rate_us = rootView.findViewById(R.id.rate_us);
        share_app = rootView.findViewById(R.id.share_app);
        privacy_app = rootView.findViewById(R.id.privacy);
        localize_app = rootView.findViewById(R.id.localize_app);
        restore = rootView.findViewById(R.id.restorePurchase);
        intro = rootView.findViewById(R.id.intro);
        appPermission = rootView.findViewById(R.id.app_permission);


        rate_icon = rootView.findViewById(R.id.rate__icon);
        share_icon = rootView.findViewById(R.id.share_icon);
        privacy_icon = rootView.findViewById(R.id.privacy_icon);
        localize_icon = rootView.findViewById(R.id.locale_icon);

        switch_vibrate.setChecked(AppPreference.getInstance(mContext).getBoolean(PrefKey.SETTINGS_VIBRATE, true));
        switch_autofocus.setChecked(AppPreference.getInstance(mContext).getBoolean(PrefKey.SETTINGS_AUTOFOCUS, true));
        switch_auto_url.setChecked(AppPreference.getInstance(mContext).getBoolean(PrefKey.SETTINGS_AUTO_URL, false));

        app_version = rootView.findViewById(R.id.app_version);

    }

    void checkProducts() {

        billingClient = BillingClient.newBuilder(mContext).enablePendingPurchases().setListener((billingResult, list) -> {
        }).build();

        final BillingClient finalBillingClient = billingClient;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {


                Log.d(billingTag, "Billing Setup Finished" + billingResult.getResponseCode());

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    finalBillingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, (billingResult1, list) -> {

                        if (list.size() == 0) {


                        } else {
                            for (Purchase purchase : list) {
                                if (purchase.getSkus().get(0).equals(productKey)) {

                                    AppPreference.getInstance(mContext).setBoolean(PrefKey.RemoveAdsPrefs, true);
                                    ActivityUtils.getInstance().restartActivity(mActivity, EntryActivity.class, true);

                                }
                            }
                        }
                    });
                }

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

//        switch_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                Log.d("SSSSS1",String.valueOf(b));
//                AppPreference.getInstance(mContext).setBoolean(PrefKey.SETTINGS_VIBRATE, b);
//            }
//        });


        switch_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppPreference.getInstance(mContext).setBoolean(PrefKey.SETTINGS_VIBRATE, isChecked);
            }
        });

//        switch_vibrate.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                AppPreference.getInstance(mContext).setBoolean(PrefKey.SETTINGS_VIBRATE, isChecked);
//            }
//        });

//        switch_autofocus.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                AppPreference.getInstance(mContext).setBoolean(PrefKey.SETTINGS_AUTOFOCUS, isChecked);
//            }
//        });


        switch_autofocus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppPreference.getInstance(mContext).setBoolean(PrefKey.SETTINGS_AUTOFOCUS, isChecked);
            }
        });

//        switch_autofocus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                Log.d("SSSSS2",String.valueOf(b));
//                AppPreference.getInstance(mContext).setBoolean(PrefKey.SETTINGS_AUTOFOCUS, b);
//            }
//        });

//        switch_auto_url.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                AppPreference.getInstance(mContext).setBoolean(PrefKey.SETTINGS_AUTO_URL, isChecked);
//            }
//        });

        switch_auto_url.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppPreference.getInstance(mContext).setBoolean(PrefKey.SETTINGS_AUTO_URL, isChecked);
            }
        });

//        personalize_ads.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
//            @Override
//            public void onClick(View v) {
//                AppUtils.showEUPermissionDialog(mActivity,mContext);
//            }
//        });

//        switch_auto_url.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                Log.d("SSSSS3",String.valueOf(b));
//                AppPreference.getInstance(mContext).setBoolean(PrefKey.SETTINGS_AUTO_URL, b);
//            }
//        });


        rate_us.setOnTouchListener(this);
        share_app.setOnTouchListener(this);
        privacy_app.setOnTouchListener(this);
        localize_app.setOnTouchListener(this);

        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkProducts();
            }
        });

        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, SlideActivity.class);
                intent.putExtra("activityLaunch", 2);
                startActivity(intent);

            }
        });

        appPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showPermissionDialog(mActivity, mContext);
            }
        });

        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            String version = pInfo.versionName;
            app_version.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                break;
            case MotionEvent.ACTION_UP:
                if (v.getId() == R.id.rate_us) {


                    AppUtils.showSettingsRateDialog(mActivity);
                } else if (v.getId() == R.id.share_app) {

                    AppUtils.shareApp(mActivity);
                } else if (v.getId() == R.id.privacy) {

                    String url = "https://docs.google.com/document/d/1TUGt3wOVriLg3pDxz1eBDaTewzshtDNv/edit?usp=sharing&ouid=103839944119900727008&rtpof=true&sd=true";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else if (v.getId() == R.id.localize_app) {

                    s = AppPreference.getInstance(mContext).getInteger(PrefKey.languageKPos);

                    if (s == -1) {
                        s = 0;
                    }

                    AppUtils.LocalizeApp(mActivity, s, true);

                }
                break;
        }
        return true;
    }


//    @Override
//    public void setMenuVisibility(final boolean visible) {
//        super.setMenuVisibility(visible);
//        if (visible) {
//            rate_icon.setColorFilter(ContextCompat.getColor(mContext,
//                    R.color.materialcolorpicker__lightgrey));
//            share_icon.setColorFilter(ContextCompat.getColor(mContext,
//                    R.color.materialcolorpicker__lightgrey));
//        }
//    }


}
