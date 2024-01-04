package scanner.app.scan.qrcode.reader.activity;


import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import scanner.app.scan.qrcode.reader.utility.ResultOfTypeAndValue;

public class EditQRCode extends AppCompatActivity {

    public AdManagerInterstitialAd mAdManagerInterstitialAd;

    RelativeLayout shareRel, saveRel;
    ImageView backImg;
    Activity activity;
    TextView fixedTextView;

    String lastResult;

    ResultOfTypeAndValue resultValues;

    boolean shouldShare = false;
    boolean shouldSave = false;

    String eveStart = "", eveEnd = "";

    RelativeLayout wifiR1;
    LinearLayout wifiR2;
    TextView nameWifi, securityWifi, passwordWifi;
    int someAction = 0;
    Date date = new Date();
    Date dateEnd = new Date();

    int CODE_TYPE = 0;
    ImageView arrow, edit;
    LinearLayout hiddenView;
    CardView cardView;
    LinearLayout fixed_layout;
    ImageView fixedLayoutImage;

    ImageView loadImage;

    FrameLayout bannerFrame, nativeFrame;

    RelativeLayout adsRelative;


    FrameLayout frameLayout;
    RelativeLayout inLineBannerAds;
    Dialog dialog;


    int action_btn1, action_btn2;
    private Context mContext;
    private TextView result, result2, result3, result4, contentText;
    private ImageView resultTextIcon, resultTextIcon2, resultTextIcon3, resultTextIcon4;
    private RelativeLayout resultL, resultL1, resultL2, resultL3;

    public static AnimatorSet Swing(View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator object = ObjectAnimator.ofFloat(view, "rotation", 0, 10, -10, 6, -6, 3, -3, 0);

        animatorSet.playTogether(object);
        return animatorSet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_qrcode);

        initVars();
        initViews();
        initFunctionality();
        initListener();


    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void initListener() {


        shareRel.setOnClickListener(v -> {

            shouldShare = true;
            if (checkWritePermission()) {
                AppUtils.shareImage(EditQRCode.this, Constants.finalBitmap);

//                Intent share = new Intent(this, ShareAndCrop.class);
//                startActivity(share);

            }

        });

        saveRel.setOnClickListener(v -> {
            shouldShare = false;

            showSaveDialog();

        });

        loadImage.setOnClickListener(v -> {

            Intent intent = new Intent(mContext, QRImageActivity.class);
            startActivity(intent);

        });


        backImg.setOnClickListener(v -> {
            try {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view1 = activity.getCurrentFocus();
//If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view1 == null) {
                    view1 = new View(activity);
                }
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            } catch (Exception ignored) {

            }
            finish();
        });


        cardView.setOnClickListener(view -> {

            // If the CardView is already expanded, set its visibility
            //  to gone and change the expand less icon to expand more.
            if (hiddenView.getVisibility() == View.VISIBLE) {

                // The transition of the hiddenView is carried out
                //  by the TransitionManager class.
                // Here we use an object of the AutoTransition
                // Class to create a default transition.
                TransitionManager.beginDelayedTransition(cardView,
                        new AutoTransition());
                arrow.setImageResource(R.drawable.down_arrow_scanner);
                arrow.setRotation(0);

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        hiddenView.setVisibility(View.GONE);
                    }
                }, 200);

            }

            // If the CardView is not expanded, set its visibility
            // to visible and change the expand more icon to expand less.
            else {

                TransitionManager.beginDelayedTransition(cardView,
                        new AutoTransition());
                hiddenView.setVisibility(View.VISIBLE);
                arrow.setImageResource(R.drawable.down_arrow_scanner);
                arrow.setRotation(180);

            }
        });


        edit.setOnClickListener(v -> {

            if (resultValues.getType() == Constants.TYPE_WIFI) {
                String plainTextWifi = nameWifi.getText().toString() + "";
                if (!securityWifi.getText().toString().contains("Result")) {
                    plainTextWifi = plainTextWifi + "\n" + securityWifi.getText().toString();
                }

                if (!passwordWifi.getText().toString().contains("Result")) {
                    plainTextWifi = plainTextWifi + "\n" + passwordWifi.getText().toString();
                }


            } else {

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


    }

    private void showSaveDialog() {


        try {

            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeNew;
            dialog.setContentView(R.layout.save_dialogue);
            dialog.show();


            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            if (dialog.getWindow() != null) {
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
                dialog.getWindow().setDimAmount(0.7f); //0 for no dim to 1 for full dim
            }

            LinearLayout savePNG = dialog.findViewById(R.id.save_png);
            LinearLayout savePDF = dialog.findViewById(R.id.save_pdf);

            savePNG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shouldSave = true;
                    dialog.dismiss();
                    if (checkWritePermission()) {
                        AppUtils.saveImage(activity, QRCodeGeneratorScanner.inputStr, Constants.finalBitmap);
                    }
                }
            });

            savePDF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.isSelectingFile = true;
                    shouldSave = false;
                    dialog.dismiss();
                    if (checkWritePermission()) {

                        try {
                            if (Common.getAppPath(getApplicationContext()) != null) {
                                createPDFFile(Common.getAppPath(getApplicationContext()));
                            }
                        } catch (Exception ignored) {

                        }

                    }

                }
            });


        } catch (Exception ignored) {

        }


/*        new MaterialDialog.Builder(activity)
                .title(R.string.Choose)
                .items(R.array.saveQR)
                .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {

                    if (which == 0) {

//                        Log.d("checkBitmapSize",Constants.finalBitmap.getWidth()+" \n"+Constants.finalBitmap.getHeight());

                        AppUtils.saveImage(activity, QRCodeGeneratorScanner.inputStr, Constants.finalBitmap);
                    } else if (which == 1) {
//                                       startActivity(new Intent(mContext,CustomPrintActivity.class));
//                                    generatePDF();
//                                    createMyPDF();
                        createPDFFile(Common.getAppPath(mContext));

                    }


                    return true; // allow selection
                })
                .positiveText(R.string.md_choose_label)

                .show();*/

    }


    private void createPDFFile(String path) {
        if (new File(path).exists()) {
            new File(path).delete();
        }


        try {

            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(path));

            document.open();
            document.addCreationDate();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Constants.finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
//            image.scalePercent(30);
//            image.setAlignment(Element.ALIGN_MIDDLE);
            image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            float x = (PageSize.A4.getWidth() - image.getScaledWidth()) / 2;
            float y = (PageSize.A4.getHeight() - image.getScaledHeight()) / 2;
            image.setAbsolutePosition(x, y);
            document.add(image);

//            Toast.makeText(activity, "document created", Toast.LENGTH_SHORT).show();
            document.close();

            printPDFFile();

        } catch (Exception ignored) {

        }
    }

    private void printPDFFile() {

        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        try {

            if (Common.getAppPath(getApplicationContext()) != null) {
                PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(EditQRCode.this, Common.getAppPath(getApplicationContext()));
                printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
            }


        } catch (Exception ignored) {

        }
    }

//    private void saveAndShare(final boolean shouldShare, final String input, Bitmap bitmap) {
//        if (checkWritePermission()) {
////            if (shouldShare) {
////                AppUtils.shareImage(mActivity, bitmap);
////            } else {
////                AppUtils.saveImage(mActivity, input, bitmap);
////
////            }
//            AppUtils.saveImage(activity, input, bitmap);
//
//
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSION_REQ) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {

                        if (shouldShare) {
//                            Intent share = new Intent(this, ShareAndCrop.class);
//                            startActivity(share);
                            AppUtils.shareImage(activity, Constants.finalBitmap);
                        } else {
                            if (shouldSave) {
                                AppUtils.saveImage(activity, QRCodeGeneratorScanner.inputStr, Constants.finalBitmap);
                            } else {
                                try {

                                    if (Common.getAppPath(getApplicationContext()) != null) {
                                        createPDFFile(Common.getAppPath(getApplicationContext()));
                                    }
                                } catch (Exception ignored) {

                                }

                            }
//                            showSaveDialog();

//                        AppUtils.saveImage(activity,QRCodeGeneratorScanner.inputStr,Constants.finalBitmap);
                        }

//                        saveAndShare(shouldShare, inputStr, finalBitmap);

                    } else {
                        AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
                    }
                }
//              else if (permission.equals(Manifest.permission.READ_CONTACTS)) {
////                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
//////                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//////                        startActivityForResult(intent, PICK_CONTACT);
////                    } else {
//////                        AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
////                    }
//                }
            }
        }
    }

    private boolean checkWritePermission() {
        if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.PERMISSION_REQ);
        } else {
            return true;
        }
        return false;
    }

    private void initFunctionality() {


//        AdsManagerQ.getInstance().refreshAd(activity, nativeFrame, nativeRel);

//
//        Intent mIntent = getIntent();
//        qr_generate = mIntent.getStringExtra("generateQR");
//        CODE_TYPE = mIntent.getIntExtra("codeType", 0);
//
//
//        Log.d("chelkintent",qr_generate+"");

        dialog = new Dialog(this);
        Bundle paramsCreateDoing = new Bundle();
        paramsCreateDoing.putString("QRCompleted", "1");
        Bundle paramsEndQRCode = new Bundle();
        paramsEndQRCode.putString("QRCreateCycleEnded", "1");

        Bundle paramsCreateSuccessFully = new Bundle();
        paramsCreateSuccessFully.putString("QRCodeisCreatedSuccessfully_User", "1");

        try {
            FirebaseAnalytics.getInstance(mContext).logEvent("QRCreateScreen", paramsCreateDoing);
            FirebaseAnalytics.getInstance(mContext).logEvent("QRCreateCycleEnded", paramsEndQRCode);
            FirebaseAnalytics.getInstance(mContext).logEvent("QRCodeisCreatedSuccessfully_User", paramsCreateSuccessFully);
        } catch (Exception ignored) {

        }

        boolean firstRunEdit = AppPreference.getInstance(this).getBoolean(PrefKey.EditActivityFirstRun, true);
        if (firstRunEdit) {
            Bundle paramsCycle = new Bundle();
            paramsCycle.putString("QRDone", "1");

            try {
                FirebaseAnalytics.getInstance(mContext).logEvent("QRCreateCycleUnique", paramsCycle);
            } catch (Exception ignored) {

            }
            AppPreference.getInstance(this).setBoolean(PrefKey.EditActivityFirstRun, false);
        }


        if (!Constants.removeAds) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    AdsManagerQ.getInstance().loadbannerAd(mContext, bannerFrame, adsRelative);
                    AdsManagerQ.getInstance().loadFreshBannerAd(mContext, bannerFrame, adsRelative, AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mContext, 350), getResources().getString(R.string.banner_ad_home_main_sticky_unit_id));
//                    AdsManagerQ.getInstance().loadFreshBannerAd(mContext, frameLayout, inLineBannerAds, AdSize.MEDIUM_RECTANGLE, getResources().getString(R.string.banner_ad_unit_id_inline));

//                    AdsManagerQ.loadBIGadmobbannerAd(mContext, frameLayout, inLineBannerAds, getResources().getString(R.string.banner_ad_unit_id));
                }
            }, 10);

            showInterstitial();
//            showInterestitial();
//            if (Constants.AdsShowEditCount % 2 == 0) {
//
//                showInterestitial();
//            }
//
//            Constants.AdsShowEditCount = Constants.AdsShowEditCount + 1;


//            if(Constants.AdsShowCount % 3 == 0){
//
////                nativeCard.setVisibility(View.GONE);
//                rateUsCard.setVisibility(View.VISIBLE);
//                inLineBannerAds.setVisibility(View.GONE);
//            }
//            else {
////                nativeCard.setVisibility(View.VISIBLE);
//                rateUsCard.setVisibility(View.GONE);
//                inLineBannerAds.setVisibility(View.VISIBLE);
//            }
//            Constants.AdsShowCount=Constants.AdsShowCount+1;
        } else {
            inLineBannerAds.setVisibility(View.GONE);
            adsRelative.setVisibility(View.GONE);
//            nativeCard.setVisibility(View.GONE);
//            rateUsCard.setVisibility(View.GONE);
        }


        int appLaunchCount = AppPreference.getInstance(mContext).getIntegerDialogue(PrefKey.RATE_DIALOG_VALUE, 0);


//        Log.d("checkAppLaunchCount", appLaunchCount + "");

        AppPreference.getInstance(mContext).setInteger(PrefKey.RATE_DIALOG_VALUE, appLaunchCount + 1);
//        appLaunchCount++;
//        } else {
//            showInterestitial();
//        }

//        Log.d("CheckAdsCount",Constants.AdsShowCount+" count");


//        showRateApp();
//        appLaunchCount = appLaunchCount + 1;
//        AppPreference.getInstance(mContext).setInteger(PrefKey.RATE_DIALOG_VALUE, appLaunchCount);


//                        dialogadload.dismiss();
//                        MyUtil.dialogadload.dismiss();
        // TODO Auto-generated method stub


//                    if(Constants.adLogic==1){
//                        startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC,new AdEventListener() {
//
//                            @Override
//                            public void onReceiveAd(Ad ad) {
////                                    Toast.makeText(mActivity, "Interestitial Loaded", Toast.LENGTH_SHORT).show();
//
//                                Log.d("checkAdsEdit","adRecieved");
//
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if(startAppAd.isReady()){
//
//                                            startAppAd.showAd(new AdDisplayListener() {
//                                                @Override
//                                                public void adHidden(Ad ad) {
//                                                    Constants.adLogic=2;
//
//                                                }
//
//                                                @Override
//                                                public void adDisplayed(Ad ad) {
//
//                                                    Constants.adLogic=2;
//                                                }
//
//                                                @Override
//                                                public void adClicked(Ad ad) {
//
//                                                }
//
//                                                @Override
//                                                public void adNotDisplayed(Ad ad) {
//                                                    Constants.adLogic=2;
//                                                }
//                                            });
//
//                                        }
//                                    }
//                                },1500);
//                                AppUtils.showAdDialog(activity,mContext);
//
//
//
//                            }
//
//                            @Override
//                            public void onFailedToReceiveAd(Ad ad) {
//                                Constants.adLogic=2;
//
////                                    Toast.makeText(mActivity, "Interestitial Not Loaded", Toast.LENGTH_SHORT).show();
//                            }
//                        });
////                        Constants.adLogic=1;
//                    }
//                    else if(Constants.adLogic==2){
////                                startAppAd.loadAd(StartAppAd.AdMode.OFFERWALL);
////                                startAppAd.showAd();
//                        Constants.adLogic=1;
//                                if (mAdManagerInterstitialAd != null) {
//
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mAdManagerInterstitialAd.show(Objects.requireNonNull(EditQRCode.this));
//                                            mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                                @Override
//                                                public void onAdDismissedFullScreenContent() {
//                                                    // Called when fullscreen content is dismissed.
////                                SaveView();
////                                initInterstitialAd();
//                                                    AdsManagerQ.getInstance().createInterstitialstaticAd(mContext);
//
//                                                }
//
//                                                @Override
//                                                public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                                    // Called when fullscreen content failed to show.
////                                SaveView();
//                                                }
//
//                                                @Override
//                                                public void onAdShowedFullScreenContent() {
//                                                    // Called when fullscreen content is shown.
//                                                    // Make sure to set your reference to null so you don't
//                                                    // show it a second time.
////                                mInterstitialAd = null;
////                                initInterstitialAd();
//
//                                                }
//
//                                            });
//
//                                        }
//                                    },1500);
//                                    AppUtils.showAdDialog(activity,mContext);
//
//
//                                }
//
//
//
//
//                    }


//        banner=AdsManagerQ.getInstance().getStartUpBanner();

//        Log.d("checkBanner",Constants.Banner+"");

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
//
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

        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            //in Right To Left layout
            backImg.setRotation(180);
        }

//        Log.d("imageName007",QRCodeGeneratorScanner.inputStr);
//        bmp2 = (Bitmap) getIntent().getParcelableExtra("image");
//        result.setImageBitmap(bmp2);
//        if(getIntent().hasExtra("byteArray")) {
//
//            Bitmap b = BitmapFactory.decodeByteArray(
//                    getIntent().getByteArrayExtra("byteArray"),0,getIntent()
//                            .getByteArrayExtra("byteArray").length);
//            bmp2=b;
//
//        }
//        Log.d("inputStr",QRCodeGeneratorScanner.inputStr);


//        mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
////        AdsManagerQ.getInstance().getAd();
//        if (mAdManagerInterstitialAd != null) {
//            mAdManagerInterstitialAd.show(EditQRCode.this);
//            Log.d("7247","showed;");
////            adManager.createInterstitialstaticAd(activity);
//            AdsManagerQ.getInstance().createInterstitialstaticAd(mContext);
//            mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                @Override
//                public void onAdDismissedFullScreenContent() {
////                    (mActivity).startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
//
//                    super.onAdDismissedFullScreenContent();
//                }
//            });
//        }


        loadImage.setImageBitmap(Constants.finalBitmap);


        lastResult = QRCodeGeneratorScanner.inputStr;
//        Log.d("checkLast007",lastResult);
        resultValues = AppUtils.getResourceType(QRCodeGeneratorScanner.inputStr);
        String finalResult = resultValues.getValue();


        if (resultValues.getType() == Constants.TYPE_TEXT) {
            //buttonAction.setVisibility(View.GONE);


            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.VISIBLE);
//            resultActionBtn1.setVisibility(View.VISIBLE);
//            resultActionBtn1.setImageResource(R.drawable.ic_url_icon);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);

            result.setText(finalResult);
            fixedTextView.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

        } else if (resultValues.getType() == Constants.TYPE_WEB) {
            //actionIcon.setImageResource(R.drawable.ic_web);


            //actionText.setText(getString(R.string.action_visit));
            action_btn1 = Constants.GO_URL;
            contentText.setVisibility(View.VISIBLE);
            contentText.setText("Website");
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
//            resultActionBtn1.setVisibility(View.VISIBLE);
//            resultActionBtn1.setImageResource(R.drawable.ic_url_icon);
            resultTextIcon.setImageResource(R.drawable.url_scanner);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);
            fixedLayoutImage.setImageResource(R.drawable.url_scanner);

            result.setText(finalResult);
            fixedTextView.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
        } else if (resultValues.getType() == Constants.TYPE_YOUTUBE) {
            //actionIcon.setImageResource(R.drawable.ic_video);


            //actionText.setText(getString(R.string.action_youtube));
            contentText.setVisibility(View.GONE);
            action_btn1 = Constants.GO_URL;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
//            resultActionBtn1.setVisibility(View.VISIBLE);
//            resultActionBtn1.setImageResource(R.drawable.ic_web_white);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);
            //for qr/barcode image

            result.setText(finalResult);
            fixedTextView.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
        } else if (resultValues.getType() == Constants.TYPE_PHONE) {
            // actionIcon.setImageResource(R.drawable.ic_call);


            contentText.setText("Phone number");
            contentText.setVisibility(View.VISIBLE);
            //actionText.setText(getString(R.string.action_call));
            action_btn1 = Constants.TO_CALL;
            action_btn2 = Constants.ADD_CONTACT;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            resultTextIcon.setImageResource(R.drawable.call_scanner);
//            resultActionBtn1.setVisibility(View.VISIBLE);
//            resultActionBtn1.setImageResource(R.drawable.ic_call_icon);
//            resultActionBtn2.setVisibility(View.VISIBLE);
//            resultActionBtn2.setImageResource(R.drawable.ic_contact_icon);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);
            fixedLayoutImage.setImageResource(R.drawable.call_scanner);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

            result.setText(finalResult);
            fixedTextView.setText(finalResult);

        } else if (resultValues.getType() == Constants.TYPE_EMAIL) {
            //actionIcon.setImageResource(R.drawable.ic_email);


            resultTextIcon.setImageResource(R.drawable.email_scanner);
            fixedLayoutImage.setImageResource(R.drawable.email_scanner);

            //actionText.setText(getString(R.string.action_email));
            contentText.setVisibility(View.VISIBLE);
            action_btn1 = Constants.SEND_EMAIL;


            if (!Constants.emailType.isEmpty()) {
                contentText.setVisibility(View.VISIBLE);
                contentText.setText(Constants.emailType);
                fixedTextView.setText(Constants.emailType);
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

//            resultActionBtn1.setVisibility(View.VISIBLE);
//            resultActionBtn1.setImageResource(R.drawable.ic_email_icon);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);

            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

//            result.setMovementMethod(LinkMovementMethod.getInstance());
        } else if (resultValues.getType() == Constants.TYPE_BARCODE) {


            resultTextIcon.setImageResource(R.drawable.ic_barcode_icon);
//            Log.d("bar",lastResult);
            fixedLayoutImage.setImageResource(R.drawable.ic_barcode_icon);
            String barCodeContent = "";
            if (lastResult.contains("barcode:")) {
                try {
                    Matcher m = Pattern.compile("barcode:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
                    while (m.find()) {
                        barCodeContent = m.group(1);
                        if (lastResult.contains("barcode:")) {
                            assert barCodeContent != null;
                            barCodeContent = barCodeContent.substring(0, barCodeContent.indexOf(";"));

                        }
                    }
                } catch (Exception e) {
//                    Toast.makeText(activity, "Incorrect Barcode", Toast.LENGTH_SHORT).show();
                }
            }

//            String barCodeType="";

//            if(lastResult.contains("barCodeType:")){
//
//                try {
//                    Matcher m = Pattern.compile("barCodeType:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
//                    while (m.find()) {
//                        barCodeType = m.group(1);
//                        if(lastResult.contains("CODE_39")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (TEXT)";
//                        }
//                        else if(lastResult.contains("CODE_128")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (TEXT)";
//                        }
//                        else if(lastResult.contains("CODABAR")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (TEXT)";
//                        }
//                        else if(lastResult.contains("CODE_93")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (TEXT)";
//                        }
//                        else if(lastResult.contains("EAN_8")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (PRODUCT)";
//
//
//                        }
//                        else if(lastResult.contains("EAN_13")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (PRODUCT)";
//
//                        }
//                        else if(lastResult.contains("ITF")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (TEXT)";
//                        }
//                        else if(lastResult.contains("UPC_A")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (PRODUCT)";
//
//                        }
//                        else if(lastResult.contains("UPC_E")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (PRODUCT)";
//
//                        }
//                        else if(lastResult.contains("UPC_EAN_EXTENSION")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (TEXT)";
//                        }
//                        else if(lastResult.contains("RSS_14")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (TEXT)";
//                        }
//                        else if(lastResult.contains("RSS_EXPANDED")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (TEXT)";
//                        }
//                        else if(lastResult.contains("MAXICODE")){
//                            barCodeType = barCodeType.substring(0, barCodeType.indexOf(";"));
//                            barCodeType=barCodeType + " (TEXT)";
//                        }
//                    }
//                }catch (Exception e){
//                    Toast.makeText(activity, "Incorrect Barcode Type", Toast.LENGTH_SHORT).show();
//                }
//            }
//            tileOfResult.setText(barCodeType);

//            if(lastResult.contains("barcode:")){
//                try {
//                    Matcher m = Pattern.compile("barcode:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
//                    while (m.find()) {
//                        barCodeContent = m.group(1);
//                        if(lastResult.contains("barcode:")){
//                            barCodeContent = barCodeContent.substring(0, barCodeType.indexOf(";"));
//                        }
//                    }
//                }catch (Exception e){
////                    Toast.makeText(mActivity, "Incorrect Barcode", Toast.LENGTH_SHORT).show();
//                }
//            }

            result.setText(barCodeContent);
            fixedTextView.setText(barCodeContent);
            contentText.setVisibility(View.GONE);
            //buttonAction.setVisibility(View.GONE);
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
//
//            resultActionBtn5.setVisibility(View.GONE);
//            action_btn1 = Constants.SEARCH_IN_WEB;
//            resultActionBtn1.setVisibility(View.VISIBLE);
//            resultActionBtn1.setImageResource(R.drawable.ic_url_icon);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
            CODE_TYPE = 1;
        } else if (resultValues.getType() == Constants.TYPE_WIFI) {


            contentText.setVisibility(View.GONE);
            // buttonAction.setVisibility(View.GONE);
            action_btn1 = Constants.WIFI_CONNECT;

            if (!Constants.wifiName.isEmpty()) {
                nameWifi.setText(Constants.wifiName);
                fixedTextView.setText(Constants.wifiName);
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

//            resultActionBtn1.setVisibility(View.VISIBLE);
//            resultActionBtn1.setImageResource(R.drawable.ic_wifi_icon);
            fixedLayoutImage.setImageResource(R.drawable.wifi_scanner);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);
        } else if (resultValues.getType() == Constants.TYPE_SMS) {


//            Log.d("sms",lastResult);
//            Log.d("sms",finalResult);

            resultTextIcon.setImageResource(R.drawable.call_scanner);
            fixedLayoutImage.setImageResource(R.drawable.sms_scanner);
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

            String number = "", message = "";
            lastResult = lastResult.replace("SMSTO:", "");
            lastResult = lastResult.replace("smsto:", "");
            //scannedResult = "rrrrr";
            number = lastResult;
            if (lastResult.contains(":")) {
                String[] str = lastResult.split(":");
                number = str[0];
                if (str.length > 1) {
                    for (int i = 1; i < str.length; i++) {
                        if (message.equals("")) {
                            message = str[i];
                        } else {
                            message = message + ":" + str[i];
                        }
                    }
                }
                //scannedResult = number + "\n" + message;
            }

            result.setText(number);
            fixedTextView.setText(number);
            result2.setText(message);
            contentText.setText("Sms to");
            contentText.setVisibility(View.VISIBLE);
            resultTextIcon2.setImageResource(R.drawable.text_scanner);
//            action_btn1 = Constants.ADD_CONTACT;
//            action_btn2 = Constants.SEND_SMS;
            resultL1.setVisibility(View.VISIBLE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);

//            resultActionBtn1.setVisibility(View.VISIBLE);
//            resultActionBtn1.setImageResource(R.drawable.ic_contact_icon);
//            resultActionBtn2.setVisibility(View.VISIBLE);
//            resultActionBtn2.setImageResource(R.drawable.ic_sms_icon);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
        } else if (resultValues.getType() == Constants.TYPE_VCARD) {


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
            fixedLayoutImage.setImageResource(R.drawable.contacts_scanner);

//            Log.d("contactResult",lastResult);
//            resultL1.setVisibility(View.VISIBLE);
//            resultL2.setVisibility(View.VISIBLE);

//            resultActionBtn1.setVisibility(View.GONE);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);
//            Toast.makeText(mActivity, Constants.name, Toast.LENGTH_SHORT).show();
//            Log.d("nameOfContact",Constants.name);

            if (!Constants.name.isEmpty()) {
                contentText.setText(Constants.name);
                fixedTextView.setText(Constants.name);

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
                    //                        action_btn1 = Constants.TO_CALL;
//                        action_btn2 = Constants.ADD_CONTACT;
//                        resultActionBtn1.setVisibility(View.VISIBLE);
//                        resultActionBtn1.setImageResource(R.drawable.ic_call_icon);
//                        resultActionBtn2.setVisibility(View.VISIBLE);
//                        resultActionBtn2.setImageResource(R.drawable.ic_contact_icon);
                }
//                Log.d("TEEEE", tel);
                m = Pattern.compile("EMAIL.*:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
                while (m.find()) {
                    email = m.group(1);
                    assert email != null;
//                    if (!email.equals("")) {
////                        action_btn4 = Constants.SEND_EMAIL;
////                        resultActionBtn4.setVisibility(View.VISIBLE);
////                        resultActionBtn4.setImageResource(R.drawable.ic_email_icon);
//                    }
                }
            } else {
                m = Pattern.compile("TEL:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
                while (m.find()) {
                    tel = new StringBuilder(Objects.requireNonNull(m.group(1)));
                    tel = new StringBuilder(tel.substring(0, tel.indexOf(";")));
//                    if (!tel.equals("")) {
////                        action_btn1 = Constants.TO_CALL;
////                        action_btn2 = Constants.ADD_CONTACT;
////                        resultActionBtn1.setVisibility(View.VISIBLE);
////                        resultActionBtn1.setImageResource(R.drawable.ic_call_icon);
////                        resultActionBtn2.setVisibility(View.VISIBLE);
////                        resultActionBtn2.setImageResource(R.drawable.ic_contact_icon);
//                    }
                }
                m = Pattern.compile("EMAIL:(.*)", Pattern.CASE_INSENSITIVE).matcher(lastResult);
                while (m.find()) {
                    email = m.group(1);
                    assert email != null;
                    email = email.substring(0, email.indexOf(";"));
//                    if (!email.equals("")) {
////                        action_btn4 = Constants.SEND_EMAIL;
////                        resultActionBtn4.setVisibility(View.VISIBLE);
////                        resultActionBtn4.setImageResource(R.drawable.ic_email_icon);
//                    }

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
//                if (!url.equals("")) {
////                    action_btn3 = Constants.GO_URL;
////                    resultActionBtn3.setVisibility(View.VISIBLE);
////                    resultActionBtn3.setImageResource(R.drawable.ic_url_icon);
//                }


            }
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

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

        } else if (resultValues.getType() == Constants.TYPE_GEO) {


            someAction = 0;

            //buttonAction.setVisibility(View.GONE);
//            action_btn1 = Constants.SEARCH_IN_WEB;
//
//            resultActionBtn1.setVisibility(View.VISIBLE);
//            resultActionBtn1.setImageResource(R.drawable.ic_url_icon);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setImageResource(R.drawable.ic_location_icon);
//            resultActionBtn5.setVisibility(View.VISIBLE);

            contentText.setVisibility(View.VISIBLE);
            contentText.setText(Constants.locationAddress);
            fixedTextView.setText(Constants.locationAddress);

            result.setText("Geo: " + Constants.latitudeAddress + " : " + Constants.longitudeAddress);
            resultTextIcon.setImageResource(R.drawable.location_scanner);
            fixedLayoutImage.setImageResource(R.drawable.location_scanner);
            resultL3.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);

        } else if (resultValues.getType() == Constants.TYPE_EVENT) {


            resultTextIcon.setImageResource(R.drawable.ic_baseline_access_time_24);
            resultTextIcon2.setImageResource(R.drawable.location_scanner);
            resultTextIcon3.setImageResource(R.drawable.text_scanner);
            fixedLayoutImage.setImageResource(R.drawable.event_scanner);
            someAction = 1;
            //buttonAction.setVisibility(View.GONE);
//            action_btn1 = Constants.SEARCH_IN_WEB;
//            resultActionBtn1.setVisibility(View.VISIBLE);
//            resultActionBtn1.setImageResource(R.drawable.ic_url_icon);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setImageResource(R.drawable.ic_event_icon);
//            resultActionBtn5.setVisibility(View.VISIBLE);

            if (!Constants.titleEvent.isEmpty()) {
                contentText.setText(Constants.titleEvent);
                contentText.setVisibility(View.VISIBLE);
                fixedTextView.setText(Constants.titleEvent);
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

        } else if (resultValues.getType() == Constants.TYPE_FACEBOOK) {


//            Log.d("FinalResultValue",finalResult+"");
            resultTextIcon.setImageResource(R.drawable.facebook_icon);

            if (finalResult.contains("https://www.facebook.com/profile.php?id=")) {
                contentText.setText("Username");
            } else if (finalResult.contains("https://www.facebook.com/groups/")) {
                contentText.setText("Group");
            } else if (finalResult.contains("https://www.facebook.com/")) {
                contentText.setText("Page");
            }


            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.VISIBLE);
            fixedLayoutImage.setImageResource(R.drawable.facebook_icon);
//            resultActionBtn1.setVisibility(View.GONE);
//            resultActionBtn1.setImageResource(R.drawable.ic_url_icon);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);
            result.setText(finalResult);
            fixedTextView.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
        } else if (resultValues.getType() == Constants.TYPE_TWITTER) {


            resultTextIcon.setImageResource(R.drawable.twitter_icon);
            fixedLayoutImage.setImageResource(R.drawable.twitter_icon);
            contentText.setText("Username");
            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.VISIBLE);
//            resultActionBtn1.setVisibility(View.GONE);
//            resultActionBtn1.setImageResource(R.drawable.ic_url_icon);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);
            result.setText(finalResult);
            fixedTextView.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
        } else if (resultValues.getType() == Constants.TYPE_LINKDEIN) {


            if (finalResult.contains("https://www.linkedin.com/in/")) {
                contentText.setText("Profile");
            } else if (finalResult.contains("https://www.linkedin.com/feed/")) {
                contentText.setText("Feed");
            } else if (finalResult.contains("https://www.linkedin.com/company/")) {
                contentText.setText("Company");
            } else if (finalResult.contains("https://www.linkedin.com/hiring/jobs/")) {
                contentText.setText("Job");
            }

            resultTextIcon.setImageResource(R.drawable.linkdein_icon);
            fixedLayoutImage.setImageResource(R.drawable.linkdein_icon);
//            contentText.setText("Username");
            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.VISIBLE);
//            resultActionBtn1.setVisibility(View.GONE);
//            resultActionBtn1.setImageResource(R.drawable.ic_url_icon);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);
            result.setText(finalResult);
            fixedTextView.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
        } else if (resultValues.getType() == Constants.TYPE_WHATSAPP) {


            resultTextIcon.setImageResource(R.drawable.whatsapp_icon);
            fixedLayoutImage.setImageResource(R.drawable.whatsapp_icon);
            contentText.setText("Whatsapp");
//            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.GONE);
//            resultActionBtn1.setVisibility(View.GONE);
//            resultActionBtn1.setImageResource(R.drawable.ic_url_icon);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);
            result.setText(finalResult);
            fixedTextView.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
        } else if (resultValues.getType() == Constants.TYPE_INSTAGRAM) {


            resultTextIcon.setImageResource(R.drawable.instagram_icon);
            fixedLayoutImage.setImageResource(R.drawable.instagram_icon);
            contentText.setText("Username");
//            action_btn1 = Constants.SEARCH_IN_WEB;
            resultL1.setVisibility(View.GONE);
            resultL2.setVisibility(View.GONE);
            resultL3.setVisibility(View.GONE);
            contentText.setVisibility(View.VISIBLE);
//            resultActionBtn1.setVisibility(View.GONE);
//            resultActionBtn1.setImageResource(R.drawable.ic_url_icon);
//            resultActionBtn2.setVisibility(View.GONE);
//            resultActionBtn3.setVisibility(View.GONE);
//            resultActionBtn4.setVisibility(View.GONE);
//            resultActionBtn5.setVisibility(View.GONE);
            result.setText(finalResult);
            fixedTextView.setText(finalResult);
            wifiR1.setVisibility(View.GONE);
            wifiR2.setVisibility(View.GONE);
        }
    }

    private void initViews() {

//        resultImg = findViewById(R.id.finalImage);
        shareRel = findViewById(R.id.shareCode);
//        back = findViewById(R.id.editBackButtonFromQR);
        saveRel = findViewById(R.id.saveCode);

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

        cardView = findViewById(R.id.base_cardview);
        arrow = findViewById(R.id.arrow_button);
        hiddenView = findViewById(R.id.hidden_view);


        contentText = findViewById(R.id.content);

        fixedTextView = findViewById(R.id.fixedDropTextView);
        fixedLayoutImage = findViewById(R.id.fixedLayoutImage);
        fixed_layout = findViewById(R.id.fixed_layout);


        adsRelative = findViewById(R.id.ads_relative);


        wifiR1 = findViewById(R.id.rel3);
        wifiR2 = findViewById(R.id.rel4);
        nameWifi = findViewById(R.id.wifiName);
        securityWifi = findViewById(R.id.wifiSec);
        passwordWifi = findViewById(R.id.wifiPass);


        edit = findViewById(R.id.edit_button);

        bannerFrame = findViewById(R.id.banner_adsview);
        nativeFrame = findViewById(R.id.fl_adplaceholder);


        loadImage = findViewById(R.id.qr_img);

        backImg = findViewById(R.id.back_img);


        frameLayout = findViewById(R.id.bannerAdsFrame);
        inLineBannerAds = findViewById(R.id.banner_ads_relative);


    }

    private void showInterstitial() {


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


                            mAdManagerInterstitialAd.show(EditQRCode.this);
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
                Log.d("checkTime", seconds + " ");
            } catch (Exception ignored) {

            }
        } else {
            if (!Constants.removeAds) {

                mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
                if (mAdManagerInterstitialAd != null) {


                    mAdManagerInterstitialAd.show(EditQRCode.this);
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
                            Constants.timerStart = true;
                            try {
                                Date date = new Date(System.currentTimeMillis()); //or simply new Date();
                                long millis = date.getTime();
                                AppPreference.getInstance(mContext).setLong(PrefKey.AdTime, millis);
                            } catch (Exception ignored) {

                            }
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

//    private void showInterestitial() {
//        mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
//        if (mAdManagerInterstitialAd != null) {
//
//            mAdManagerInterstitialAd.show(EditQRCode.this);
//            mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                @Override
//                public void onAdDismissedFullScreenContent() {
//                    // Called when fullscreen content is dismissed.
//
//                    AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));
//                    showSaveDialog();
//
//                }
//
//                @Override
//                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                    // Called when fullscreen content failed to show.
//                    AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));
//
//                }
//
//                @Override
//                public void onAdShowedFullScreenContent() {
//                    // Called when fullscreen content is shown.
//                    // Make sure to set your reference to null so you don't
//                    // show it a second time.
//
//
//                }
//
//            });
//
//
//            mAdManagerInterstitialAd.setOnPaidEventListener(new OnPaidEventListener() {
//                @Override
//                public void onPaidEvent(@NonNull AdValue adValue) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("currency", adValue.getCurrencyCode());
//                    bundle.putString("precision", String.valueOf(adValue.getPrecisionType()));
//                    bundle.putString("valueMicros", String.valueOf(adValue.getValueMicros()));
//                    bundle.putString("network", "InterstitialAd");
//                    try {
//                        FirebaseAnalytics.getInstance(EditQRCode.this).logEvent("paid_ad_impressions", bundle);
//                    } catch (Exception e) {
////                        Log.d("events", e.getMessage());
//                    }
//                }
//            });
//
//        } else {
//            showSaveDialog();
//        }
//    }


    public void dismissProgressDialog() {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();

        }

    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void initVars() {

        activity = this;
        mContext = activity.getApplicationContext();
//        reviewManager = ReviewManagerFactory.create(this);
//        AdManager.getInstance(mContext).showBannerAd((AdView)findViewById(R.id.adViewMain));
//        AdsManagerQ.getInstance().loadbannerAd(mContext,(FrameLayout) findViewById(R.id.linear_layout_adsview));
    }

//    public void showRateApp() {
//        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
//        request.addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // Getting the ReviewInfo object
////                Toast.makeText(activity, "In App Review", Toast.LENGTH_SHORT).show();
//                ReviewInfo reviewInfo = task.getResult();
//
//                Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
//                flow.addOnCompleteListener(task1 -> {
//                    // The flow has finished. The API does not indicate whether the user
//                    // reviewed or not, or even whether the review dialog was shown.
//                });
//
//
//            }
//
//        });
//    }

}