package scanner.app.scan.qrcode.reader.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.zxing.BarcodeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.stickerwhatsapp.stickermaker.AdEvents.AdController;
import scanner.app.scan.qrcode.reader.AdEvents.AdEvent;
import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.data.constant.Constants;
import scanner.app.scan.qrcode.reader.data.preference.AppPreference;
import scanner.app.scan.qrcode.reader.data.preference.PrefKey;
import scanner.app.scan.qrcode.reader.utility.AdsManagerQ;
import scanner.app.scan.qrcode.reader.utility.AppUtils;
import scanner.app.scan.qrcode.reader.utility.CodeGenerator;
import scanner.app.scan.qrcode.reader.utility.ResultOfTypeAndValue;


public class ResultActivity extends AppCompatActivity {

    ImageView resultQrCodeImage;
    ArrayList<String> arrayList, colorList;
    String lastResult;
    int colorResult = Color.BLACK;
    int resultForFragment = 0;
    int positionForHistoryFragment = 0;
    ResultOfTypeAndValue resultValues;
    //    ColorPicker cp1;
//    Button btnColor;
    boolean shouldShare = false;
    int colorRed = 0;
    int colorGreen = 0;
    int colorBlue = 0;
    String latitude = "", longitude = "", eveStart = "", eveEnd = "";
    ImageView infoResult, settingResult;
    LinearLayout removeAds;
    RelativeLayout wifiR1;
    LinearLayout wifiR2;
    TextView nameWifi, securityWifi, passwordWifi;
    int someAction = 0;
    Date date = new Date();
    Date dateEnd = new Date();
    ImageView backResult;
    ImageView decorate;
    int CODE_TYPE = 0;
    ImageView editInfo, editInfoWifi;
    ImageView copy1, copy2, copy3, copy4;
    LinearLayout storeSearch;
    String barCodeContent = "";
    //    private StartAppAd mStartUpInterstitialAd;
    ImageView barImage;
    CardView qr_layout;

    FirebaseAnalytics firebaseAnalytics;
    FrameLayout bannerFrame;

    RelativeLayout adsRelative;
    FrameLayout frameLayout;
    RelativeLayout inLineBannerAds;
    ImageView actionIcon, resultIcon, resultTextIcon, resultTextIcon2, resultTextIcon3, resultTextIcon4, searchAmazon, searchEbay;
    //    Banner banner ;
//    RelativeLayout bannerRel,banner_ad_loading;
    //private LinearLayout buttonCopy, buttonSearch, buttonShare, buttonAction;
    ImageView codeColorBtn, codeSaveBtn, codeShareBtn, resultCopyBtn,
            resultShareBtn, resultActionBtn1, resultActionBtn2, resultActionBtn3, resultActionBtn4, resultActionBtn5;
    TextView expensePreOrder;
    ImageView adsRemoveImageView;
    private Activity mActivity;
    private Context mContext;
    private TextView result, result2, result3, result4, actionText, scannedCodeType, tileOfResult, contentText;
    private LinearLayout resultL, resultL1, resultL2, resultL3;
    private Bitmap output;
    private int action_btn1, action_btn2, action_btn3, action_btn4;
    private AdManagerInterstitialAd mAdManagerInterstitialAd;

    public static AnimatorSet Swing(View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator object = ObjectAnimator.ofFloat(view, "rotation", 0, 10, -10, 6, -6, 3, -3, 0);

        animatorSet.playTogether(object);
        return animatorSet;
    }

//    @Override
//    protected void onSaveInstanceState (Bundle outState){
//        super.onSaveInstanceState(outState);
//        mStartUpInterstitialAd.onSaveInstanceState(outState);
//        startAppAd.onSaveInstanceState(outState);
//
//    }
//
//    @Override
//    protected void onRestoreInstanceState (Bundle savedInstanceState){
//        startAppAd.onRestoreInstanceState(savedInstanceState);
//        mStartUpInterstitialAd.onRestoreInstanceState(savedInstanceState);
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectAll()  // or .detectAll() for all detectable problems
//                .penaltyLog()
//                .penaltyDeath()
//                        .penaltyDialog()
//                .build());

        super.onCreate(savedInstanceState);

//        Log.d("checkOnCreate", "onCreateCalled");

        initVars();
        initViews();
        initFunctionality();
        initListeners();

        View currentFocus = mActivity.getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
    }

    private void initVars() {
        mActivity = ResultActivity.this;
        mContext = mActivity.getApplicationContext();

        //full ads load
//        AdManager.getInstance(mContext).loadFullScreenAd(mActivity);
//        AdsManagerQ.getInstance().loadbannerAd(mContext,(FrameLayout) findViewById(R.id.linear_layout_adsview));
//        else {
//            ActivityUtils.getInstance().invokeActivity(mActivity, ResultActivity.class, false, Constants.SCAN_FRAGMENT, 0);
//
//                }
    }

    private void initViews() {
        setContentView(R.layout.activity_result);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        result = findViewById(R.id.result);
        result2 = findViewById(R.id.result2);
        result3 = findViewById(R.id.result3);
        result4 = findViewById(R.id.result4);
        resultL = findViewById(R.id.result_l1);
        resultL1 = findViewById(R.id.result_l2);
        resultL2 = findViewById(R.id.result_l3);
        resultL3 = findViewById(R.id.result_l4);
        resultTextIcon = findViewById(R.id.resultTextIcon);
        resultTextIcon2 = findViewById(R.id.result2TextIcon);
        resultTextIcon3 = findViewById(R.id.result3TextIcon);
        resultTextIcon4 = findViewById(R.id.result4TextIcon);
        //actionText = (TextView) findViewById(R.id.actionText);
        scannedCodeType = findViewById(R.id.scanned_result_type_of_code);
        tileOfResult = findViewById(R.id.scanned_result_tile);
        actionIcon = findViewById(R.id.actionIcon);
        resultIcon = findViewById(R.id.resultIcon);

        contentText = findViewById(R.id.content);
//        mShimmerViewContainer=findViewById(R.id.shimmer_view_container);
       /* buttonCopy = (LinearLayout) findViewById(R.id.buttonCopy);
        buttonSearch = (LinearLayout) findViewById(R.id.buttonSearch);
        buttonShare = (LinearLayout) findViewById(R.id.buttonShare);
        buttonAction = (LinearLayout) findViewById(R.id.buttonAction);*/

        codeColorBtn = findViewById(R.id.color_of_result_qrcode_btn);
        codeSaveBtn = findViewById(R.id.save_of_result_qrcode_btn);
        codeShareBtn = findViewById(R.id.share_of_result_qrcode_btn);

        resultCopyBtn = findViewById(R.id.copy_result_btn);
        resultShareBtn = findViewById(R.id.share_result_btn);
        resultActionBtn1 = findViewById(R.id.action1_result_btn);
        resultActionBtn2 = findViewById(R.id.action2_result_btn);
        resultActionBtn3 = findViewById(R.id.action3_result_btn);
        resultActionBtn4 = findViewById(R.id.action4_result_btn);
        resultActionBtn5 = findViewById(R.id.action5_result_btn);

        resultQrCodeImage = findViewById(R.id.result_qr_code_img);
        wifiR1 = findViewById(R.id.rel1);
        wifiR2 = findViewById(R.id.rel2);
        nameWifi = findViewById(R.id.wifiName);
        securityWifi = findViewById(R.id.wifiSec);
        passwordWifi = findViewById(R.id.wifiPass);

//        cp1 = new ColorPicker(mActivity, colorRed, colorGreen, colorBlue);
//        btnColor = cp1.findViewById(R.id.okColorButton);


        backResult = findViewById(R.id.backButtonResult);

        decorate = findViewById(R.id.decorate);
        infoResult = findViewById(R.id.infoQRRes);
        settingResult = findViewById(R.id.settingsQRCreateResult);

        editInfo = findViewById(R.id.editDialog);
        editInfoWifi = findViewById(R.id.editDialogWifi);

        copy1 = findViewById(R.id.copy_1);
        copy2 = findViewById(R.id.copy_2);
        copy3 = findViewById(R.id.copy_3);
        copy4 = findViewById(R.id.copy_4);

        storeSearch = findViewById(R.id.search_on_store);

        searchAmazon = findViewById(R.id.search_on_amazon);
        searchEbay = findViewById(R.id.search_on_ebay);

        barImage = findViewById(R.id.result_bar_code_img);

        qr_layout = findViewById(R.id.qr_layout);
        frameLayout = findViewById(R.id.bannerAdsFrame);
        inLineBannerAds = findViewById(R.id.banner_ads_relative);

        bannerFrame = findViewById(R.id.banner_adsview);
        adsRelative = findViewById(R.id.ads_relative);

        removeAds = findViewById(R.id.remove);
        adsRemoveImageView = findViewById(R.id.adsRemove);


    }

    private void showInterestitial() {
        mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
        if (mAdManagerInterstitialAd != null) {

            mAdManagerInterstitialAd.show(ResultActivity.this);
            mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    try {
                        Constants.oldDate = new Date(System.currentTimeMillis());

                    } catch (Exception ignored) {

                    }
                    // Called when fullscreen content is dismissed.
//                                SaveView();
//                                initInterstitialAd();
                    AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));
                    Constants.adLogicResultBottomBar = 3;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    // Called when fullscreen content failed to show.
//                                SaveView();
                    AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));

                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
//                                mInterstitialAd = null;
//                                initInterstitialAd();

                }

            });

//                        new Handler().postDelayed(new Runnable() {
//
//                            @Override
//                            public void run() {
////                        dialogadload.dismiss();
////                        MyUtil.dialogadload.dismiss();
//                                // TODO Auto-generated method stub
//
//
//
//                            }
//
//                        }, 1500);

            mAdManagerInterstitialAd.setOnPaidEventListener(new OnPaidEventListener() {
                @Override
                public void onPaidEvent(@NonNull AdValue adValue) {
                    Bundle bundle = new Bundle();
                    bundle.putString("currency", adValue.getCurrencyCode());
                    bundle.putString("precision", String.valueOf(adValue.getPrecisionType()));
                    bundle.putString("valueMicros", String.valueOf(adValue.getValueMicros()));
                    bundle.putString("network", "InterstitialAd");
                    try {
                        FirebaseAnalytics.getInstance(ResultActivity.this).logEvent("paid_ad_impressions", bundle);
                    } catch (Exception e) {
                        Log.d("events", e.getMessage());
                    }
                }
            });

//                        AppUtils.showAdDialog(mActivity,mContext);

        }
    }

    private void initFunctionality() {

        final ValueAnimator anim = ValueAnimator.ofFloat(1f, 1.2f);
        anim.setDuration(1000);
        anim.addUpdateListener(animation -> {
            decorate.setScaleX((Float) animation.getAnimatedValue());
            decorate.setScaleY((Float) animation.getAnimatedValue());
        });
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.start();


        if (!Constants.removeAds) {
//            AdsManagerQ.getInstance().loadbannerAd(mContext, bannerFrame, adsRelative);
//            AdsManagerQ.loadBIGadmobbannerAd(ResultActivity.this, frameLayout, inLineBannerAds, getResources().getString(R.string.banner_ad_unit_id));

            AdsManagerQ.getInstance().loadFreshBannerAd(ResultActivity.this, bannerFrame, adsRelative, AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mContext, 350), getResources().getString(R.string.banner_ad_home_main_sticky_unit_id));

            AdsManagerQ.getInstance().loadInlineFreshBannerAd(ResultActivity.this, frameLayout, inLineBannerAds, AdSize.MEDIUM_RECTANGLE, getResources().getString(R.string.banner_ad_inline_unit_id), adsRemoveImageView);


        } else {
            adsRelative.setVisibility(View.GONE);
            removeAds.setVisibility(View.GONE);
            inLineBannerAds.setVisibility(View.GONE);
        }


//        Toast.makeText(mActivity, mAdManagerInterstitialAd+"", Toast.LENGTH_SHORT).show();
//        mStartUpInterstitialAd=AdsManagerQ.getInterestitialStartUpAd();
//
//        banner.setBannerListener(new BannerListener() {
//            @Override
//            public void onReceiveAd(View view) {
//
//                bannerRel.setVisibility(View.VISIBLE);
//                banner_ad_loading.setVisibility(View.GONE);
//                banner.showBanner();
//            }
//
//            @Override
//            public void onFailedToReceiveAd(View view) {
//                bannerRel.setVisibility(View.GONE);
//                banner_ad_loading.setVisibility(View.GONE);
//                banner.hideBanner();
//
//            }
//
//            @Override
//            public void onImpression(View view) {
//
//            }
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

//        Log.d("checkTime",CropActivity.ts+"");


//        Configuration config = getResources().getConfiguration();
//        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
//            //in Right To Left layout
//            backResult.setRotation(180);
//        }

        firebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        Bundle params = new Bundle();

//        getSupportActionBar().setTitle(getString(R.string.result));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        AdsManagerQ.getInstance().loadbannerAd(mContext,(FrameLayout) findViewById(R.id.linear_layout_adsview));


//        Log.d("BarCodeType", Constants.barcodeType);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            resultForFragment = b.getInt("key");
            positionForHistoryFragment = b.getInt("position");
        }

        try {
            arrayList = AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_SCANNED);
            if (resultForFragment == Constants.HISTORY_SCAN_FRAGMENT) {


                //setResult
                arrayList = AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_SCANNED);
                Collections.reverse(arrayList);
                lastResult = arrayList.get(positionForHistoryFragment);
                //setColor
//            colorList = AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_SCANNED);
//            Collections.reverse(colorList);
//            colorResult = Integer.parseInt(colorList.get(positionForHistoryFragment));
            } else if (resultForFragment == Constants.HISTORY_GENERATE_FRAGMENT) {

                //setResult
                arrayList = AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_CREATED);
                Collections.reverse(arrayList);
                lastResult = arrayList.get(positionForHistoryFragment);
                //setColor
//            colorList = AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_CREATED);
//            Collections.reverse(colorList);
//            colorResult = Integer.parseInt(colorList.get(positionForHistoryFragment));
            } else if (resultForFragment == Constants.SCAN_FRAGMENT) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

                LayoutInflater vi = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View customLayoutpermission = vi.inflate(R.layout.scanned_dialog, null);
                AlertDialog alert = alertDialog.create();
                alert.setView(customLayoutpermission);

                ImageView ok = customLayoutpermission.findViewById(R.id.img2);

                ok.setOnClickListener(v -> {

                    alert.dismiss();

                    showInterstitial();

//                    if (!Constants.removeAds) {
//
//
//
//                        try {
//                                Date date = new Date(System.currentTimeMillis()); //or simply new Date();
//
//                                long diff = date.getTime() - Constants.oldDate.getTime();
//                                long seconds = diff / 1000;
//                                long minutes = seconds / 60;
//                                long hours = minutes / 60;
//                                long days = (hours / 24) ;
//
//                                if(seconds>40){
//                                    showInterestitial();
//                                }
//
////                            Log.d("checkTime",seconds+" "+new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(date)+":"+new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(Constants.oldDate));
//                                Log.d("checkTime",seconds+" ");
//                            }catch (Exception ignored){
//
//                            }
////                        showInterestitial();
////                        if (Constants.AdsShowCountResult % 3 == 0) {
////                            showInterestitial();
////                        }
////                        showInterestitial();
////                        if (Constants.adLogicResultBottomBar  == 2) {
////                            showInterestitial();
////                        }
////                        else {
////                            try {
////                                Date date = new Date(System.currentTimeMillis()); //or simply new Date();
////
////                                long diff = date.getTime() - Constants.oldDate.getTime();
////                                long seconds = diff / 1000;
////                                long minutes = seconds / 60;
////                                long hours = minutes / 60;
////                                long days = (hours / 24) ;
////
////                                if(seconds>40){
////                                    showInterestitial();
////                                }
////
//////                            Log.d("checkTime",seconds+" "+new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(date)+":"+new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(Constants.oldDate));
////                                Log.d("checkTime",seconds+" ");
////                            }catch (Exception ignored){
////
////                            }
////
////
////
////                        }
//////                        else if(Constants.AdsShowCountResult % 3 == 0) {
//////                            showInterestitial();
//////                        }
////
////                        Constants.AdsShowCountResult = Constants.AdsShowCountResult + 1;
//                    }


                });

                alert.setCancelable(false);
                alert.show();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80); //<-- int width=400;
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.20);
                alert.getWindow().setLayout(width, height);

                //setResult
                arrayList = AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_SCANNED);
                lastResult = arrayList.get(arrayList.size() - 1);
                //setColor
//            colorList = AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_SCANNED);
//            Collections.reverse(colorList);
//            colorResult = Integer.parseInt(colorList.get(colorList.size() - 1));
            }
        } catch (Exception ignored) {

        }


//        Log.d("CCCC", colorList.toString());

        // TODO Sample banner Ad implementation
//        AdManager.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adViewResult));
//        Log.d("loci",lastResult);
        resultValues = AppUtils.getResourceType(lastResult);
        String finalResult = resultValues.getValue();
//        Log.d("loci1",finalResult);
//        Log.d("barCodeResult",lastResult);
        CodeGenerator.setBLACK(colorResult);
//        cp1.setColor(colorResult);


//        btnColor.setBackgroundColor(colorResult);
        //int type = AppUtils.getResourceType(lastResult);
        if (resultValues.getType() == Constants.TYPE_TEXT) {
            //buttonAction.setVisibility(View.GONE);
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_text));
            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.VISIBLE);
            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.url_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);

            result.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);
            storeSearch.setVisibility(View.GONE);
            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);
            params.putString("scan", "text");

        } else if (resultValues.getType() == Constants.TYPE_WEB) {
            //actionIcon.setImageResource(R.drawable.ic_web);
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_url));
            resultIcon.setImageResource(R.drawable.url_scanner);
            //actionText.setText(getString(R.string.action_visit));
            action_btn1 = Constants.GO_URL;
            contentText.setVisibility(View.VISIBLE);
            contentText.setText(getResources().getString(R.string.website));
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.url_scanner);
            resultTextIcon.setImageResource(R.drawable.url_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);

            result.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);
            storeSearch.setVisibility(View.GONE);
            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);
            params.putString("scan", "url");
        } else if (resultValues.getType() == Constants.TYPE_YOUTUBE) {
            //actionIcon.setImageResource(R.drawable.ic_video);
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_youtube));
//            resultIcon.setImageResource(R.drawable.ic_video);
            //actionText.setText(getString(R.string.action_youtube));
            contentText.setVisibility(View.GONE);
            action_btn1 = Constants.GO_URL;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.url_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);
            generateCode(lastResult);//for qr/barcode image

            result.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);
            storeSearch.setVisibility(View.GONE);
            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);
        } else if (resultValues.getType() == Constants.TYPE_PHONE) {
            // actionIcon.setImageResource(R.drawable.ic_call);
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_phone));
            resultIcon.setImageResource(R.drawable.call_scanner);
            contentText.setText(getResources().getString(R.string.phone_number));
            contentText.setVisibility(View.VISIBLE);
            //actionText.setText(getString(R.string.action_call));
            action_btn1 = Constants.TO_CALL;
            action_btn2 = Constants.ADD_CONTACT;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            resultTextIcon.setImageResource(R.drawable.call_scanner);
            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.call_scanner);
            resultActionBtn2.setVisibility(View.VISIBLE);
            resultActionBtn2.setImageResource(R.drawable.contacts_scanner);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);

            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

            result.setText(finalResult);

            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);
            storeSearch.setVisibility(View.GONE);
            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);
            params.putString("scan", "phone");

        } else if (resultValues.getType() == Constants.TYPE_EMAIL) {
            //actionIcon.setImageResource(R.drawable.ic_email);
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_email));
            resultIcon.setImageResource(R.drawable.email_scanner);

            resultTextIcon.setImageResource(R.drawable.email_scanner);


            //actionText.setText(getString(R.string.action_email));
            contentText.setVisibility(View.VISIBLE);
            action_btn1 = Constants.SEND_EMAIL;


            if (!Constants.emailType.isEmpty()) {
                contentText.setVisibility(View.VISIBLE);
                contentText.setText(Constants.emailType);
            }

            if (Constants.subjectType.isEmpty()) {
                resultL.setVisibility(View.GONE);
            }

            if (Constants.bodyType.isEmpty()) {
                resultL1.setVisibility(View.GONE);
            }

            if (!Constants.subjectType.isEmpty()) {

                if (Constants.subjectType.contains("%20")) {
                    Constants.subjectType = Constants.subjectType.replace("%20", " ");
                }
                result.setText(Constants.subjectType);
                resultL.setVisibility(View.VISIBLE);
            }


            if (!Constants.bodyType.isEmpty()) {

                if (Constants.bodyType.contains("%20")) {
                    Constants.bodyType = Constants.bodyType.replace("%20", " ");
                }
                result2.setText(Constants.bodyType);
                resultL1.setVisibility(View.VISIBLE);
            }

            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.email_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);

            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);
            storeSearch.setVisibility(View.GONE);

            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);
            params.putString("scan", "email");
//            result.setMovementMethod(LinkMovementMethod.getInstance());
        } else if (resultValues.getType() == Constants.TYPE_BARCODE) {


            scannedCodeType.setText(getResources().getString(R.string.scanned_type_barcode));

            resultIcon.setImageResource(R.drawable.ic_barcode_icon);
            resultTextIcon.setImageResource(R.drawable.ic_barcode_icon);


//            if(lastResult.contains("barcode:")){
//                lastResult=lastResult.replace("barcode:","");
//            }

            String barCodeType = "";
            if (lastResult.contains("barCodeType:")) {

                try {
                    Matcher m = Pattern.compile("barCodeType:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
                    while (m.find()) {
                        barCodeType = m.group(1);
                        assert barCodeType != null;
                        if (lastResult.contains("CODE_39")) {

                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (TEXT)";
                            Constants.format = BarcodeFormat.CODE_39;
                            params.putString("scan", "barcode_39");
                        } else if (lastResult.contains("CODE_128")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (TEXT)";
                            Constants.format = BarcodeFormat.CODE_128;
                            params.putString("scan", "barcode_128");
                        } else if (lastResult.contains("CODABAR")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (TEXT)";
                            Constants.format = BarcodeFormat.CODABAR;
                            params.putString("scan", "barcode_codabar");
                        } else if (lastResult.contains("CODE_93")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (TEXT)";
                            Constants.format = BarcodeFormat.CODE_93;
                            params.putString("scan", "barcode_93");
                        } else if (lastResult.contains("EAN_8")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (PRODUCT)";
                            Constants.format = BarcodeFormat.EAN_8;
                            storeSearch.setVisibility(View.VISIBLE);
                            params.putString("scan", "barcode_ean_8");

                        } else if (lastResult.contains("EAN_13")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (PRODUCT)";
                            Constants.format = BarcodeFormat.EAN_13;
                            storeSearch.setVisibility(View.VISIBLE);
                            params.putString("scan", "barcode_ean_13");
                        } else if (lastResult.contains("ITF")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (TEXT)";
                            Constants.format = BarcodeFormat.ITF;
                            params.putString("scan", "barcode_ITF");
                        } else if (lastResult.contains("UPC_A")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (PRODUCT)";
                            storeSearch.setVisibility(View.VISIBLE);
                            Constants.format = BarcodeFormat.UPC_A;
                            params.putString("scan", "barcode_UPC_A");
                        } else if (lastResult.contains("UPC_E")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (PRODUCT)";
                            storeSearch.setVisibility(View.VISIBLE);
                            Constants.format = BarcodeFormat.UPC_E;
                            params.putString("scan", "barcode_UPC_E");
                        } else if (lastResult.contains("UPC_EAN_EXTENSION")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (TEXT)";
                            Constants.format = BarcodeFormat.UPC_EAN_EXTENSION;
                            params.putString("scan", "barcode_UPC_EAN_EXTENSION");
                        } else if (lastResult.contains("RSS_14")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (TEXT)";
                            Constants.format = BarcodeFormat.CODE_128;
                            params.putString("scan", "barcode_RSS_14");
                        } else if (lastResult.contains("RSS_EXPANDED")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (TEXT)";
                            Constants.format = BarcodeFormat.CODE_128;
                            params.putString("scan", "barcode_RSS_EXPANDED");
                        } else if (lastResult.contains("MAXICODE")) {
                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
                            barCodeType = barCodeType + " (TEXT)";
                            Constants.format = BarcodeFormat.CODE_128;
                            params.putString("scan", "barcode_MAXICODE");
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(mActivity, "Incorrect Barcode Type", Toast.LENGTH_SHORT).show();
                }
            }
            tileOfResult.setText(barCodeType);

            if (lastResult.contains("barcode:")) {
                try {
                    Matcher m = Pattern.compile("barcode:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
                    while (m.find()) {
                        barCodeContent = m.group(1);
                        if (lastResult.contains("barcode:")) {
                            assert barCodeContent != null;
                            barCodeContent = barCodeContent.substring(0, barCodeType.indexOf(";"));

                        }
                    }
                } catch (Exception e) {
//                    Toast.makeText(mActivity, "Incorrect Barcode", Toast.LENGTH_SHORT).show();
                }
            }

            assert barCodeContent != null;
            barCodeContent = barCodeContent.replace(";", "");
            lastResult = barCodeContent;
            result.setText(barCodeContent);
            contentText.setVisibility(View.GONE);
            //buttonAction.setVisibility(View.GONE);
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);

            resultActionBtn5.setVisibility(View.GONE);
            action_btn1 = Constants.SEARCH_IN_WEB;
            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.url_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
            CODE_TYPE = 1;
            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);
            resultQrCodeImage.setVisibility(View.GONE);
            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.VISIBLE);
        } else if (resultValues.getType() == Constants.TYPE_WIFI) {
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_wifi));
            resultIcon.setImageResource(R.drawable.wifi_scanner);

            contentText.setVisibility(View.GONE);
            // buttonAction.setVisibility(View.GONE);
            action_btn1 = Constants.WIFI_CONNECT;


            if (!Constants.wifiName.isEmpty()) {
                nameWifi.setText(Constants.wifiName);
            }
            if (!Constants.wifiSec.isEmpty()) {
                securityWifi.setText(Constants.wifiSec);
            }
            if (!Constants.wifiPass.isEmpty()) {
                passwordWifi.setText(Constants.wifiPass);
            }

            resultL.setVisibility(View.GONE);
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);

            wifiR1.setVisibility(View.VISIBLE);
            wifiR2.setVisibility(View.VISIBLE);

            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.wifi_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);

            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);
            storeSearch.setVisibility(View.GONE);

            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);

            params.putString("scan", "wifi");

        } else if (resultValues.getType() == Constants.TYPE_SMS) {
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_sms));
//            Log.d("sms",lastResult);
//            Log.d("sms",finalResult);
            resultIcon.setImageResource(R.drawable.sms_scanner);
            resultTextIcon.setImageResource(R.drawable.call_scanner);
            //buttonAction.setVisibility(View.GONE);
//            Log.d("sms_result",finalResult);
//            String number="",smsText="";
//            if(finalResult.contains("\n")){
//                finalResult=finalResult.replace("\n",";");
//
//            }
//            if(finalResult.contains(";")){
//                String[] smsArray=finalResult.split(";");
//                number=smsArray[0];
//                smsText=smsArray[1];
//                result.setText(number);
//                result2.setText(smsText);
//            }

            String number = "";
            StringBuilder message = new StringBuilder();
            lastResult = lastResult.replace("SMSTO:", "");
            lastResult = lastResult.replace("smsto:", "");
            //scannedResult = "rrrrr";
            number = lastResult;
            if (lastResult.contains(":")) {
                String[] str = lastResult.split(":");
                number = str[0];
                if (str.length > 1) {
                    for (int i = 1; i < str.length; i++) {
                        if (message.toString().equals("")) {
                            message = new StringBuilder(str[i]);
                        } else {
                            message.append(":").append(str[i]);
                        }
                    }
                }
                //scannedResult = number + "\n" + message;
            }

            result.setText(number);
            result2.setText(message.toString());
            contentText.setText(getResources().getString(R.string.sms_to));
            contentText.setVisibility(View.VISIBLE);
            resultTextIcon2.setImageResource(R.drawable.text_scanner);
            action_btn1 = Constants.ADD_CONTACT;
            action_btn2 = Constants.SEND_SMS;
            resultL1.setVisibility(View.VISIBLE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);

            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.contacts_scanner);
            resultActionBtn2.setVisibility(View.VISIBLE);
            resultActionBtn2.setImageResource(R.drawable.sms_scanner);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);
            storeSearch.setVisibility(View.GONE);

            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);

            params.putString("scan", "sms");
        } else if (resultValues.getType() == Constants.TYPE_VCARD) {
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_vcard));
            resultIcon.setImageResource(R.drawable.contacts_scanner);
            contentText.setVisibility(View.VISIBLE);
            // buttonAction.setVisibility(View.GONE);
//            String[] contactArray=finalResult.split("\n");
//            contentText.setText(contactArray[0]);
//            result.setText(contactArray[1]);
//            result2.setText(contactArray[2]);
//            result3.setText(contactArray[3]);
            resultTextIcon.setImageResource(R.drawable.call_scanner);
            resultTextIcon2.setImageResource(R.drawable.email_scanner);
            resultTextIcon3.setImageResource(R.drawable.location_scanner);


//            Log.d("contactResult",lastResult);
//            resultL1.setVisibility(View.VISIBLE);
//            resultL2.setVisibility(View.VISIBLE);

            resultActionBtn1.setVisibility(View.GONE);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);
//            Toast.makeText(mActivity, Constants.name, Toast.LENGTH_SHORT).show();
//            Log.d("nameOfContact",Constants.name);

            if (!Constants.name.isEmpty()) {
                contentText.setText(Constants.name);


            }
            if (!Constants.tel.isEmpty()) {
//                Log.d("telephoneFromVcard",Constants.tel);
                Constants.tel = Constants.tel.replaceAll("(^[\\r\\n]+|[\\r\\n]+$)", "");
                result.setText(Constants.tel);
                resultL.setVisibility(View.VISIBLE);
            }
            if (!Constants.email.isEmpty()) {
                result2.setText(Constants.email);
                resultL1.setVisibility(View.VISIBLE);
            }
            if (!Constants.address.isEmpty()) {
//                Log.d("addressFromVcard",Constants.address);
                Constants.address = Constants.address.replaceAll("(^[\\r\\n]+|[\\r\\n]+$)", "");
                result3.setText(Constants.address);
                resultL2.setVisibility(View.VISIBLE);
            }

            String name = "";
            String org = "";
            String title = "";
            StringBuilder tel = new StringBuilder();
            String url = "";
            String email = "";
            String adr = "";
            String note = "";
            Matcher m;
            if (lastResult.contains("BEGIN:VCARD") || lastResult.contains("begin:vcard")) {
                m = Pattern.compile("TEL.*:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
                while (m.find()) {
                    tel.append("\n").append(m.group(1));
                    action_btn1 = Constants.TO_CALL;
                    action_btn2 = Constants.ADD_CONTACT;
                    resultActionBtn1.setVisibility(View.VISIBLE);
                    resultActionBtn1.setImageResource(R.drawable.call_scanner);
                    resultActionBtn2.setVisibility(View.VISIBLE);
                    resultActionBtn2.setImageResource(R.drawable.contacts_scanner);
                }
//                Log.d("TEEEE", tel);
                m = Pattern.compile("EMAIL.*:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
                while (m.find()) {
                    email = m.group(1);
                    assert email != null;
                    if (!email.equals("")) {
                        action_btn4 = Constants.SEND_EMAIL;
                        resultActionBtn4.setVisibility(View.VISIBLE);
                        resultActionBtn4.setImageResource(R.drawable.email_scanner);
                    }
                }
            } else {
                m = Pattern.compile("TEL:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
                while (m.find()) {
                    tel = new StringBuilder(Objects.requireNonNull(m.group(1)));
                    tel = new StringBuilder(tel.substring(0, tel.indexOf(";")));
                    if (!tel.toString().equals("")) {
                        action_btn1 = Constants.TO_CALL;
                        action_btn2 = Constants.ADD_CONTACT;
                        resultActionBtn1.setVisibility(View.VISIBLE);
                        resultActionBtn1.setImageResource(R.drawable.call_scanner);
                        resultActionBtn2.setVisibility(View.VISIBLE);
                        resultActionBtn2.setImageResource(R.drawable.contacts_scanner);
                    }
                }
                m = Pattern.compile("EMAIL:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
                while (m.find()) {
                    email = m.group(1);
                    assert email != null;
                    email = email.substring(0, email.indexOf(";"));
                    if (!email.equals("")) {
                        action_btn4 = Constants.SEND_EMAIL;
                        resultActionBtn4.setVisibility(View.VISIBLE);
                        resultActionBtn4.setImageResource(R.drawable.email_scanner);
                    }

                }
            }
            m = Pattern.compile("URL:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
            while (m.find()) {
                url = m.group(1);
                if (lastResult.contains("MECARD") || lastResult.contains("mecard")) {
                    assert url != null;
                    url = url.substring(0, url.indexOf(";"));
                }
                assert url != null;
                if (!url.equals("")) {
                    action_btn3 = Constants.GO_URL;
                    resultActionBtn3.setVisibility(View.VISIBLE);
                    resultActionBtn3.setImageResource(R.drawable.url_scanner);
                }


            }
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);
            storeSearch.setVisibility(View.GONE);

            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);

           /* m = Pattern.compile("TEL:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
            while (m.find()) {
                tel = m.group(1);
                if (lastResult.contains("MECARD") || lastResult.contains("mecard")) {
                    tel = tel.substring(0, tel.indexOf(";"));
                }
                if (!tel.equals("")) {
                    action_btn1 = Constants.TO_CALL;
                    action_btn2 = Constants.ADD_CONTACT;
                    resultActionBtn1.setVisibility(View.VISIBLE);
                    resultActionBtn1.setImageResource(R.drawable.ic_call_white);
                    resultActionBtn2.setVisibility(View.VISIBLE);
                    resultActionBtn2.setImageResource(R.drawable.ic_contact_white);
                }
            }
            m = Pattern.compile("EMAIL:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
            while (m.find()) {
                email = m.group(1);
                if (lastResult.contains("MECARD") || lastResult.contains("mecard")) {
                    email = email.substring(0, email.indexOf(";"));
                }
                if (!email.equals("")) {
                    action_btn4 = Constants.SEND_EMAIL;
                    resultActionBtn4.setVisibility(View.VISIBLE);
                    resultActionBtn4.setImageResource(R.drawable.ic_email_white);
                }
            }*/
            params.putString("scan", "contact");
        } else if (resultValues.getType() == Constants.TYPE_GEO) {
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_geo));
            resultIcon.setImageResource(R.drawable.location_scanner);
            someAction = 0;

            //buttonAction.setVisibility(View.GONE);
            action_btn1 = Constants.SEARCH_IN_WEB;

            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.url_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setImageResource(R.drawable.location_scanner);
            resultActionBtn5.setVisibility(View.VISIBLE);

            if (!Constants.locationAddress.isEmpty()) {
                contentText.setVisibility(View.VISIBLE);
                contentText.setText(Constants.locationAddress);
            }

            result.setText(getResources().getString(R.string.geo) + Constants.latitudeAddress + " : " + Constants.longitudeAddress);
            resultTextIcon.setImageResource(R.drawable.location_scanner);

            resultL3.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);


            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);

            storeSearch.setVisibility(View.GONE);


            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);

            params.putString("scan", "location");
        } else if (resultValues.getType() == Constants.TYPE_EVENT) {
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_event));
            resultIcon.setImageResource(R.drawable.event_scanner);
            resultTextIcon.setImageResource(R.drawable.ic_baseline_access_time_24);
            resultTextIcon2.setImageResource(R.drawable.location_scanner);
            resultTextIcon3.setImageResource(R.drawable.text_scanner);
            someAction = 1;
            //buttonAction.setVisibility(View.GONE);
            action_btn1 = Constants.SEARCH_IN_WEB;
            resultActionBtn1.setVisibility(View.GONE);
            resultActionBtn1.setImageResource(R.drawable.url_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setImageResource(R.drawable.event_scanner);
            resultActionBtn5.setVisibility(View.VISIBLE);

            if (!Constants.titleEvent.isEmpty()) {
                contentText.setText(Constants.titleEvent);
                contentText.setVisibility(View.VISIBLE);
            }

            if (!Constants.eventStartTime.isEmpty() && !Constants.eventEndTime.isEmpty()) {

                eveStart = Constants.eventStartTime;
                eveEnd = Constants.eventEndTime;
                String strDate = "2013-05-15T10:00:00-0700";
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

                try {
                    date = formatter.parse(eveStart);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    dateEnd = formatter.parse(eveEnd);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");


                result.setText(formatter.format(date) + "\n" + formatter.format(dateEnd));
                resultL.setVisibility(View.VISIBLE);


            } else if (!Constants.eventStartTime.isEmpty()) {
                eveStart = Constants.eventStartTime;

                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

                try {
                    date = formatter.parse(eveStart);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                result.setText(formatter.format(date));
                resultL.setVisibility(View.VISIBLE);
            } else if (!Constants.eventEndTime.isEmpty()) {

                eveEnd = Constants.eventEndTime;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

                try {
                    dateEnd = formatter.parse(eveEnd);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

                result.setText(formatter.format(eveEnd));
                resultL.setVisibility(View.VISIBLE);
            }

            if (!Constants.eventLocation.isEmpty()) {
                result2.setText(Constants.eventLocation);
                resultL1.setVisibility(View.VISIBLE);
            }

            if (!Constants.eventDescription.isEmpty()) {
                result3.setText(Constants.eventDescription);
                resultL2.setVisibility(View.VISIBLE);
            }

            resultTextIcon2.setVisibility(View.VISIBLE);
            resultTextIcon3.setVisibility(View.VISIBLE);
            resultL3.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);

            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);

            storeSearch.setVisibility(View.GONE);

            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);
            params.putString("scan", "event");
        } else if (resultValues.getType() == Constants.TYPE_FACEBOOK) {
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_facebook));
            resultIcon.setImageResource(R.drawable.facebook_icon);
            resultTextIcon.setImageResource(R.drawable.facebook_icon);

            if (finalResult.contains("https://www.facebook.com/profile.php?id=")) {
                contentText.setText(getResources().getString(R.string.username_only));
            } else if (finalResult.contains("https://www.facebook.com/groups/")) {
                contentText.setText(getResources().getString(R.string.group));
            } else if (finalResult.contains("https://www.facebook.com/")) {
                contentText.setText(getResources().getString(R.string.page));
            }
//            contentText.setText("Username");
            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.VISIBLE);
            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.url_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);

            result.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);

            storeSearch.setVisibility(View.GONE);

            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);
            params.putString("scan", "facebook");
        } else if (resultValues.getType() == Constants.TYPE_TWITTER) {
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_twitter));
            resultIcon.setImageResource(R.drawable.twitter_icon);
            resultTextIcon.setImageResource(R.drawable.twitter_icon);
            contentText.setText(getResources().getString(R.string.username_only));
            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.VISIBLE);
            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.url_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);

            result.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);

            storeSearch.setVisibility(View.GONE);

            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);
            params.putString("scan", "twitter");
        } else if (resultValues.getType() == Constants.TYPE_LINKDEIN) {
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_linkdein));
            resultIcon.setImageResource(R.drawable.linkdein_icon);
            resultTextIcon.setImageResource(R.drawable.linkdein_icon);
            if (finalResult.contains("https://www.linkedin.com/in/")) {
                contentText.setText(getResources().getString(R.string.profile));
            } else if (finalResult.contains("https://www.linkedin.com/feed/")) {
                contentText.setText(getResources().getString(R.string.feed));
            } else if (finalResult.contains("https://www.linkedin.com/company/")) {
                contentText.setText(getResources().getString(R.string.company));
            } else if (finalResult.contains("https://www.linkedin.com/hiring/jobs/")) {
                contentText.setText(getResources().getString(R.string.job));
            }
//            contentText.setText("Username");
            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.VISIBLE);
            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.url_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);

            result.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);

            storeSearch.setVisibility(View.GONE);

            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);
            params.putString("scan", "linkdien");
        } else if (resultValues.getType() == Constants.TYPE_WHATSAPP) {
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_whatsapp));
            resultIcon.setImageResource(R.drawable.whatsapp_icon);
            resultTextIcon.setImageResource(R.drawable.whatsapp_icon);
            contentText.setText(getResources().getString(R.string.whatsapp));
            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.GONE);
            resultActionBtn1.setVisibility(View.GONE);
            resultActionBtn1.setImageResource(R.drawable.url_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);

            result.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);

            storeSearch.setVisibility(View.GONE);

            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);
            params.putString("scan", "whatsapp");
        } else if (resultValues.getType() == Constants.TYPE_INSTAGRAM) {
            scannedCodeType.setText(getResources().getString(R.string.scanned_type_qrcode));
            tileOfResult.setText(getResources().getString(R.string.result_instagram));
            resultIcon.setImageResource(R.drawable.instagram_icon);
            resultTextIcon.setImageResource(R.drawable.instagram_icon);
            contentText.setText(getResources().getString(R.string.username_only));
            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.VISIBLE);
            resultActionBtn1.setVisibility(View.VISIBLE);
            resultActionBtn1.setImageResource(R.drawable.url_scanner);
            resultActionBtn2.setVisibility(View.GONE);
            resultActionBtn3.setVisibility(View.GONE);
            resultActionBtn4.setVisibility(View.GONE);
            resultActionBtn5.setVisibility(View.GONE);

            result.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
            editInfoWifi.setVisibility(View.GONE);
            editInfo.setVisibility(View.GONE);

            storeSearch.setVisibility(View.GONE);

            qr_layout.setVisibility(View.VISIBLE);
            barImage.setVisibility(View.GONE);
            params.putString("scan", "instagram");
        }

        firebaseAnalytics.logEvent("ScanCode", params);

        generateCode(lastResult);//for qr/barcode image
////        if(resultValues.getType() == Constants.)
//        result.setText(finalResult);
//        result.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private void showInterstitial() {


        if(Constants.timerStart){
            try {
                Date date = new Date(System.currentTimeMillis()); //or simply new Date();

                long diff = date.getTime() - Constants.oldDate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = (hours / 24) ;

                if(seconds>30){
                    if (!Constants.removeAds) {

                        mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
                        if (mAdManagerInterstitialAd != null) {


                            mAdManagerInterstitialAd.show(ResultActivity.this);
                            mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when fullscreen content is dismissed.
                                    AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));
//
                                    Constants.oldDate = new Date(System.currentTimeMillis());
//                        Constants.adLogicResultBottomBar = 3;

                                }


                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    // Called when fullscreen content failed to show.
                                    AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));



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


                        }

//            Constants.adLogicResultBottomBar++;
                    } else {


                    }
                }

//                            Log.d("checkTime",seconds+" "+new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(date)+":"+new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(Constants.oldDate));
                Log.d("checkTime",seconds+" ");
            }catch (Exception ignored){

            }
        }else {
            if (!Constants.removeAds) {

                mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
                if (mAdManagerInterstitialAd != null) {


                    mAdManagerInterstitialAd.show(ResultActivity.this);
                    mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));
//
                            Constants.oldDate = new Date(System.currentTimeMillis());
//                        Constants.adLogicResultBottomBar = 3;

                        }


                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            // Called when fullscreen content failed to show.
                            AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));



                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            Constants.timerStart=true;
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


                }

//            Constants.adLogicResultBottomBar++;
            } else {


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

    private void initListeners() {

        removeAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ResultActivity.this, RemoveAdsActivity.class));
            }
        });

        adsRemoveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, RemoveAdsActivity.class));
            }
        });

//        codeColorBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    cp1.show();
//                } catch (Exception e) {
//                    AppUtils.showToast(mContext, "An unexpected error has occurred");
//                }
//
//            }
//        });
//
//        cp1.setCallback(new ColorPickerCallback() {
//            @Override
//            public void onColorChosen(@ColorInt int color) {
//                CodeGenerator.setBLACK(cp1.getColor());
//                String inputeForColor;
//                if (lastResult.contains("barcode:")) {
//                    inputeForColor = lastResult.replace("barcode:", "");
//                } else {
//                    inputeForColor = lastResult;
//                }
//                generateCode(lastResult);
//                //saving color
//                if (resultForFragment == Constants.HISTORY_SCAN_FRAGMENT) {
//                    ArrayList<String> previousColor = AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_SCANNED);
//                    Collections.reverse(previousColor);
//                    previousColor.set(positionForHistoryFragment, Integer.toString(cp1.getColor()));
//                    Collections.reverse(previousColor);
//                    AppPreference.getInstance(mContext).setStringArray(PrefKey.COLOR_LIST_OF_SCANNED, previousColor);
//
//                } else if (resultForFragment == Constants.HISTORY_GENERATE_FRAGMENT) {
//                    ArrayList<String> previousColor = AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_CREATED);
//                    Collections.reverse(previousColor);
//                    previousColor.set(positionForHistoryFragment, Integer.toString(cp1.getColor()));
//                    Collections.reverse(previousColor);
//                    AppPreference.getInstance(mContext).setStringArray(PrefKey.COLOR_LIST_OF_CREATED, previousColor);
//
//                } else if (resultForFragment == Constants.SCAN_FRAGMENT) {
//                    ArrayList<String> previousColor = AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_SCANNED);
//                    Collections.reverse(previousColor);
//                    previousColor.set((colorList.size() - 1), Integer.toString(cp1.getColor()));
//                    Collections.reverse(previousColor);
//                    AppPreference.getInstance(mContext).setStringArray(PrefKey.COLOR_LIST_OF_SCANNED, previousColor);
//                }
//                // If the auto-dismiss option is not enable (disabled as default) you have to manually dimiss the dialog
//                cp1.dismiss();
//            }
//        });

        codeSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shouldShare = false;
                saveAndShare(shouldShare, "scanQrcode", output);
            }
        });

        codeShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shouldShare = true;
                saveAndShare(shouldShare, "scanQRCode", output);
            }
        });

        resultCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.copyToClipboard(mContext, lastResult);
            }
        });

        resultShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AppUtils.share(mActivity, result.getText().toString());
                String plainText = result.getText().toString() + "";
                if (!result2.getText().toString().contains("Result")) {
                    plainText = plainText + "\n" + result2.getText().toString();
                }

                if (!result3.getText().toString().contains("Result")) {
                    plainText = plainText + "\n" + result3.getText().toString();
                }

                if (!result4.getText().toString().contains("Result")) {
                    plainText = plainText + "\n" + result4.getText().toString();
                }
                AppUtils.share(mActivity, plainText);

            }
        });

        resultActionBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ResultOfTypeAndValue resultValues = AppUtils.getResourceType(lastResult);
                if (resultValues.getType() == Constants.TYPE_WIFI) {
//                    AppUtils.executeAction(mActivity, nameWifi.getText().toString()+securityWifi.getText().toString()+passwordWifi.getText().toString() , lastResult, resultValues.getType(), action_btn1);
//                    Toast.makeText(mActivity, "wifi go wifi", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                } else {
                    AppUtils.executeAction(mActivity, result.getText().toString(), lastResult, resultValues.getType(), action_btn1);
                }


            }
        });

        resultActionBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ResultOfTypeAndValue resultValues = AppUtils.getResourceType(lastResult);
                AppUtils.executeAction(mActivity, result.getText().toString(), lastResult, resultValues.getType(), action_btn2);
            }
        });

        resultActionBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ResultOfTypeAndValue resultValues = AppUtils.getResourceType(lastResult);
                AppUtils.executeAction(mActivity, result.getText().toString(), lastResult, resultValues.getType(), action_btn3);
            }
        });

        resultActionBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ResultOfTypeAndValue resultValues = AppUtils.getResourceType(lastResult);
                AppUtils.executeAction(mActivity, result.getText().toString(), lastResult, resultValues.getType(), action_btn4);
            }
        });


        resultActionBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (someAction == 0) {

                    try {
                        float lat = Float.parseFloat(Constants.latitudeAddress);
                        float longi = Float.parseFloat(Constants.longitudeAddress);
                        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, longi);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(mActivity, "An unexpected error has been occurred", Toast.LENGTH_SHORT).show();
                    }

                } else if (someAction == 1) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_INSERT)
                                .setData(CalendarContract.Events.CONTENT_URI)
                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTime())
                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, dateEnd.getTime())
                                .putExtra(CalendarContract.Events.TITLE, Constants.titleEvent)
                                .putExtra(CalendarContract.Events.DESCRIPTION, Constants.eventDescription)
                                .putExtra(CalendarContract.Events.EVENT_LOCATION, Constants.eventLocation)
                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

                        startActivity(intent);
                    } catch (ActivityNotFoundException ignored) {

                    }

                }

            }
        });

        contentText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        result.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        result2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result2.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        result3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result3.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        result4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result4.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        copy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();

            }
        });

        copy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result2.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        copy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result3.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        copy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result4.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        decorate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(CODE_TYPE==1){
//                    Constants.finalImageEditor=1;
//                }
//                else{
//                    Constants.finalImageEditor=0;
//                }


//                mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
//
//                if (mAdManagerInterstitialAd != null) {
//
//                    new Handler().postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
////                        dialogadload.dismiss();
////                        MyUtil.dialogadload.dismiss();
//                            // TODO Auto-generated method stub
//
//                            mAdManagerInterstitialAd.show(Objects.requireNonNull(ResultActivity.this));
//                            mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                @Override
//                                public void onAdDismissedFullScreenContent() {
//                                    // Called when fullscreen content is dismissed.
////                                SaveView();
////                                initInterstitialAd();
//                                    AdsManagerQ.getInstance().createInterstitialstaticAd(mContext);
//                                    Intent generateQR=new Intent(ResultActivity.this,QRCodeGeneratorScanner.class);
//                                    generateQR.putExtra("generateQR",lastResult);
//                                    generateQR.putExtra("codeType",CODE_TYPE);
//                                    startActivity(generateQR);
//
//                                }
//
//                                @Override
//                                public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                    // Called when fullscreen content failed to show.
////                                SaveView();
//                                }
//
//                                @Override
//                                public void onAdShowedFullScreenContent() {
//                                    // Called when fullscreen content is shown.
//                                    // Make sure to set your reference to null so you don't
//                                    // show it a second time.
////                                mInterstitialAd = null;
////                                initInterstitialAd();
//
//                                }
//
//                            });
//
////                            Constants.adLogicResult=1;
//
//
//
//                        }
//
//                    }, 1500);
//                    AppUtils.showAdDialog(mActivity,mContext);
//
//                }
//
//                else {
//                    Constants.adLogicResult=1;
//                    Intent generateQR=new Intent(ResultActivity.this,QRCodeGeneratorScanner.class);
//                    generateQR.putExtra("generateQR",lastResult);
//                    generateQR.putExtra("codeType",CODE_TYPE);
//                    startActivity(generateQR);
//                }

//                Constants.adLogicResult = 1;
                Intent generateQR = new Intent(ResultActivity.this, QRCodeGeneratorScanner.class);
                generateQR.putExtra("generateQR", lastResult);
                generateQR.putExtra("codeType", CODE_TYPE);
                startActivity(generateQR);

//                if (Constants.adLogicResult == 1) {
////                                        new Handler().postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
//////                            Log.d("checkIntersetitial1",startAppAd.isReady()+"");
////
////
////
////
////                        }
////                    },1500);
////                    AppUtils.showAdDialog(mActivity,mContext);
//                    SplashActivity.startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC,new AdEventListener() {
//                        @Override
//                        public void onReceiveAd(Ad ad) {
//                            Log.d("checkAds","ad recieved");
////                                    Toast.makeText(mActivity, "Interestitial Loaded", Toast.LENGTH_SHORT).show();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if(SplashActivity.startAppAd.isReady()){
//                                        Log.d("checkAds1","ad ready");
//
//                                        SplashActivity.startAppAd.showAd(new AdDisplayListener() {
//
//
//
//                                            @Override
//                                            public void adHidden(Ad ad) {
//                                                Constants.adLogicResult=2;
//                                                Log.d("checkAds2","ad hidden");
//                                                Intent generateQR=new Intent(ResultActivity.this,QRCodeGeneratorScanner.class);
//                                                generateQR.putExtra("generateQR",lastResult);
//                                                generateQR.putExtra("codeType",CODE_TYPE);
//                                                startActivity(generateQR);
//                                            }
//
//                                            @Override
//                                            public void adDisplayed(Ad ad) {
//                                                Log.d("checkAds3","ad displayed");
//                                                Constants.adLogicResult=2;
//                                            }
//
//                                            @Override
//                                            public void adClicked(Ad ad) {
//
//                                            }
//
//                                            @Override
//                                            public void adNotDisplayed(Ad ad) {
//                                                Log.d("checkAds4","ad not displayed");
//                                                Constants.adLogicResult=2;
//                                                Intent generateQR=new Intent(ResultActivity.this,QRCodeGeneratorScanner.class);
//                                                generateQR.putExtra("generateQR",lastResult);
//                                                generateQR.putExtra("codeType",CODE_TYPE);
//                                                startActivity(generateQR);
//                                            }
//                                        });
//
//
////                                        Log.d("checkIntersetitial",startAppAd.isReady()+"");
//
//                                    }
//                                    else {
//                                        Log.d("checkAds5","ad not ready");
//                                        Intent generateQR=new Intent(ResultActivity.this,QRCodeGeneratorScanner.class);
//                                        generateQR.putExtra("generateQR",lastResult);
//                                        generateQR.putExtra("codeType",CODE_TYPE);
//                                        startActivity(generateQR);
//                                    }
//                                }
//                            },1500);
//                            AppUtils.showAdDialog(mActivity,mContext);
//
//
//                        }
//
//                        @Override
//                        public void onFailedToReceiveAd(Ad ad) {
//                            Log.d("checkAds6","ad failed");
//                            Constants.adLogicResult=2;
//                            Intent generateQR=new Intent(ResultActivity.this,QRCodeGeneratorScanner.class);
//                            generateQR.putExtra("generateQR",lastResult);
//                            generateQR.putExtra("codeType",CODE_TYPE);
//                            startActivity(generateQR);
////                                    Toast.makeText(mActivity, "Interestitial Not Loaded", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                else if(Constants.adLogicResult==2){
//
//                    if (mAdManagerInterstitialAd != null) {
//
//                        new Handler().postDelayed(new Runnable() {
//
//                            @Override
//                            public void run() {
////                        dialogadload.dismiss();
////                        MyUtil.dialogadload.dismiss();
//                                // TODO Auto-generated method stub
//
//                                mAdManagerInterstitialAd.show(Objects.requireNonNull(ResultActivity.this));
//                                mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                    @Override
//                                    public void onAdDismissedFullScreenContent() {
//                                        // Called when fullscreen content is dismissed.
////                                SaveView();
////                                initInterstitialAd();
//                                        AdsManagerQ.getInstance().createInterstitialstaticAd(mContext);
//                                        Intent generateQR=new Intent(ResultActivity.this,QRCodeGeneratorScanner.class);
//                                        generateQR.putExtra("generateQR",lastResult);
//                                        generateQR.putExtra("codeType",CODE_TYPE);
//                                        startActivity(generateQR);
//
//                                    }
//
//                                    @Override
//                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                        // Called when fullscreen content failed to show.
////                                SaveView();
//                                    }
//
//                                    @Override
//                                    public void onAdShowedFullScreenContent() {
//                                        // Called when fullscreen content is shown.
//                                        // Make sure to set your reference to null so you don't
//                                        // show it a second time.
////                                mInterstitialAd = null;
////                                initInterstitialAd();
//
//                                    }
//
//                                });
//
//                                Constants.adLogicResult=1;
//
//
//
//                            }
//
//                        }, 1500);
//                        AppUtils.showAdDialog(mActivity,mContext);
//
//                    }
//
//                    else {
//                        Constants.adLogicResult=1;
//                        Intent generateQR=new Intent(ResultActivity.this,QRCodeGeneratorScanner.class);
//                        generateQR.putExtra("generateQR",lastResult);
//                        generateQR.putExtra("codeType",CODE_TYPE);
//                        startActivity(generateQR);
//                    }
//
////                    if(mStartUpInterstitialAd!=null){
////                        Toast.makeText(mActivity, "Not Null", Toast.LENGTH_SHORT).show();
////
////                        startAppAd.showAd(new AdDisplayListener() {
////                            @Override
////                            public void adHidden(Ad ad) {
////
////                            }
////
////                            @Override
////                            public void adDisplayed(Ad ad) {
////                                    Constants.adLogicResult=1;
////                            }
////
////                            @Override
////                            public void adClicked(Ad ad) {
////
////                            }
////
////                            @Override
////                            public void adNotDisplayed(Ad ad) {
////
////                            }
////                        });
////                    }
//
////                    startAppAd.loadAd(StartAppAd.AdMode.FULLPAGE);
//
//
//                }


            }
        });

        qr_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, QRImageActivity.class);
                startActivity(intent);
//                Toast.makeText(mActivity, "qr layout clicked", Toast.LENGTH_SHORT).show();
            }
        });


        nameWifi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result4.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        passwordWifi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result4.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        securityWifi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = result4.getText().toString();
                ClipData clip = ClipData.newPlainText("", getString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copy to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        editInfo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String plainText = result.getText().toString() + "";
                if (!result2.getText().toString().contains("Result")) {
                    plainText = plainText + "\n" + result2.getText().toString();
                }

                if (!result3.getText().toString().contains("Result")) {
                    plainText = plainText + "\n" + result3.getText().toString();
                }

                if (!result4.getText().toString().contains("Result")) {
                    plainText = plainText + "\n" + result4.getText().toString();
                }


            }
        });

        editInfoWifi.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String plainText = nameWifi.getText().toString() + "";
                if (!securityWifi.getText().toString().contains("Result")) {
                    plainText = plainText + "\n" + securityWifi.getText().toString();
                }

                if (!passwordWifi.getText().toString().contains("Result")) {
                    plainText = plainText + "\n" + passwordWifi.getText().toString();
                }


            }
        });

        /*buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.copyToClipboard(mContext, result.getText().toString());
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.searchInWeb(mActivity, result.getText().toString());
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.share(mActivity, result.getText().toString());
            }
        });

        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResultOfTypeAndValue resultValues = AppUtils.getResourceType(lastResult);
                AppUtils.executeAction(mActivity, result.getText().toString(), resultValues.getType());
            }
        });*/

        backResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        infoResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showPermissionDialog(mActivity, mContext);
            }
        });

        settingResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(ResultActivity.this,PrivacyPolicy.class);
//                startActivity(i);
                try {
                    String url = "https://docs.google.com/document/d/1TUGt3wOVriLg3pDxz1eBDaTewzshtDNv/edit?usp=sharing&ouid=103839944119900727008&rtpof=true&sd=true";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException ignored) {

                }

            }
        });

        searchAmazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = "https://www.amazon.com/s?k=" + barCodeContent;
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception ignored) {

                }

            }
        });

        searchEbay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = "https://www.ebay.com/sch/i.html?_nkw=" + barCodeContent;
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception ignored) {

                }

            }
        });


    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    private void generateCode(final String input) {
        CodeGenerator codeGenerator = new CodeGenerator();
        if (resultValues.getType() == Constants.TYPE_BARCODE) {
//            String dummyInput="1234567890128";
//            codeGenerator.generateBarFor(dummyInput);
//            Log.d("codeGenerateInfo",input);
            codeGenerator.generateBarFor(input);
        } else {
//            Toast.makeText(mActivity, "createQR", Toast.LENGTH_SHORT).show();
            codeGenerator.generateQRFor(input);
        }
        codeGenerator.setResultListener(new CodeGenerator.ResultListener() {
            @Override
            public void onResult(Bitmap bitmap) {
                //((BitmapDrawable)outputBitmap.getDrawable()).getBitmap().recycle();
                output = bitmap;
                Constants.finalBitmap = bitmap;
                if (resultValues.getType() == Constants.TYPE_BARCODE) {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(bitmap)
                            .into(barImage);

//                    barImage.setImageBitmap(bitmap);
                    resultQrCodeImage.setVisibility(View.GONE);
                    barImage.setVisibility(View.VISIBLE);
                    Constants.finalImageEditor = 1;
                } else {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(bitmap)
                            .into(resultQrCodeImage);
//                    resultQrCodeImage.setImageBitmap(bitmap);
                    resultQrCodeImage.setVisibility(View.VISIBLE);
                    barImage.setVisibility(View.GONE);
                }

            }
        });
        codeGenerator.execute();
    }

    private void saveAndShare(final boolean shouldShare, final String input, Bitmap bitmap) {
        if (checkWritePermission()) {
            if (shouldShare) {
                AppUtils.shareImage(mActivity, bitmap);
            } else {
                AppUtils.saveImage(mActivity, input, bitmap);
            }
        }
    }


    private boolean checkWritePermission() {
        if ((ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.PERMISSION_REQ);
        } else {
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSION_REQ) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        saveAndShare(shouldShare, lastResult, output);
                    } else {
                        AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
                    }
                }
            }
        }
    }
}

