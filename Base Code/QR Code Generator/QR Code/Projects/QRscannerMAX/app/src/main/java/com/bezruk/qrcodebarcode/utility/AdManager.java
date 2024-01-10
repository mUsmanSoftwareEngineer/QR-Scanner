package com.bezruk.qrcodebarcode.utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.bezruk.qrcodebarcode.R;
import com.bezruk.qrcodebarcode.data.preference.AppPreference;
import com.bezruk.qrcodebarcode.data.preference.PrefKey;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


public class AdManager {

    private static AdManager adUtils;

    private InterstitialAd mInterstitialAd;
    private Boolean isAdsVisibility;
    String Banner, Full, BannerPr, FullPr;

    private AdManager(Context context) {
        MobileAds.initialize(context, context.getResources().getString(R.string.app_ad_id));
        isAdsVisibility = AppPreference.getInstance(context).getBoolean(PrefKey.ADS_VISIBILITY,true);
    }

    public static AdManager getInstance(Context context) {
        if (adUtils == null) {
            adUtils = new AdManager(context);
        }
        return adUtils;
    }

    public void showBannerAd(final AdView mAdView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(isAdsVisibility){
                    mAdView.setVisibility(View.VISIBLE);
                } else {
                    mAdView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                mAdView.setVisibility(View.GONE);
            }
        });
    }

    public void loadFullScreenAd(Activity activity) {
        mInterstitialAd = new InterstitialAd(activity);

        mInterstitialAd.setAdUnitId(activity.getResources().getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    public boolean showFullScreenAd() {
        if(isAdsVisibility) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                return true;
            }
        }
        return false;
    }

    public InterstitialAd getInterstitialAd() {
        return mInterstitialAd;
    }


}
