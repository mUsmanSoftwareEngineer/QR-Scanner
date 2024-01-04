package scanner.app.scan.qrcode.reader.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdSize;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.adapter.ColorAdapter;
import scanner.app.scan.qrcode.reader.adapter.LogoAdapter;
import scanner.app.scan.qrcode.reader.data.constant.Constants;
import scanner.app.scan.qrcode.reader.data.preference.AppPreference;
import scanner.app.scan.qrcode.reader.data.preference.PrefKey;
import scanner.app.scan.qrcode.reader.utility.AdsManagerQ;
import scanner.app.scan.qrcode.reader.utility.CodeGenerator;
import scanner.app.scan.qrcode.reader.utility.DialogUtils;

public class QRCodeGeneratorScanner extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    public static int CODE_TYPE;
    public static String inputStr;


    Activity mActivity;
    Context mContext;
    String[] colorsTxt;
    String[] colorsRecycler;
    List<Integer> colors;
    List<Integer> colorsRecyclerList;
    List<Integer> logoRecyclerList;
    ImageView backQR;
    TextView editQR;


    Bitmap logo;
    String qr_generate;
    Bitmap bitmapAdapter;


    Bitmap templateBitmap;

    ImageView mapOpen;
    int getLocationCode = 0;

    int activityCode = 0;
    float latitude, longitude;
    Date date = null;
    Date dateEnd = null;
    Bitmap bitmapSaved;


    String geo = "", add = "", evTitle = "", eveDescription = "", eveLocation = "";
    RelativeLayout logoQR, colorQR, customQR;


    LinearLayout linearLayout1QR;
    RelativeLayout linearLayout2QR;
    Button saveQR;
    ImageView tickQR, logoBitmap;

    ColorAdapter colorAdapter;
    LogoAdapter logoAdapter;



    CardView qrCard, barCodeCardCard;
    RelativeLayout shareQrOnly;
    ImageView barCodeCard, barCodebackground, barCodeLogo;
    CardView qr_code_style;
    RelativeLayout bar_code_style;

    RelativeLayout barCodeRelative;

    FrameLayout bannerFrame;

    RelativeLayout adsRelative;


    CardView cardOfLogo;

    TextView categoryTxt;
    RelativeLayout topView;

    ImageView outputBitmap;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generate);

        initVar();
        initView();
        initFunctionality();
        initListener();


    }


    private void initVar() {
        mActivity = this;
        mContext = mActivity.getApplicationContext();
        colorsTxt = getApplicationContext().getResources().getStringArray(R.array.colors);
        colorsRecycler = getApplicationContext().getResources().getStringArray(R.array.colorsRecycler);
        colors = new ArrayList<Integer>();
        colorsRecyclerList = new ArrayList<Integer>();
        logoRecyclerList = new ArrayList<>();
    }

    private void initView() {

        outputBitmap = findViewById(R.id.outputBitmap);


        mapOpen = findViewById(R.id.open_in_map);


        saveQR = findViewById(R.id.save_btn);

        backQR = findViewById(R.id.backButtonFromQR);

        editQR = findViewById(R.id.edit);

        logoBitmap = findViewById(R.id.logoBitmap);



        qrCard = findViewById(R.id.qrCard);
        barCodeCardCard = findViewById(R.id.barcodeqrCard);
        barCodeCard = findViewById(R.id.barcodeBitmap);

        bar_code_style = findViewById(R.id.barcodeStyle);
        qr_code_style = findViewById(R.id.qr_style);

        barCodebackground = findViewById(R.id.barcodebackgroundBitmap);
        barCodeLogo = findViewById(R.id.barcodelogoBitmap);

        barCodeRelative = findViewById(R.id.barCodeRelative);
        bannerFrame = findViewById(R.id.banner_adsview);

        adsRelative = findViewById(R.id.ads_relative);


        logoQR = findViewById(R.id.add_logo);
        colorQR = findViewById(R.id.colorPalette);

        customQR = findViewById(R.id.customRel);


        linearLayout1QR = findViewById(R.id.l1QR);
        linearLayout2QR = findViewById(R.id.l2QR);


        mRecyclerView = findViewById(R.id.recycler_view);

        tickQR = findViewById(R.id.tick);


        categoryTxt = findViewById(R.id.category_txt);


        cardOfLogo = findViewById(R.id.logoCard);

        shareQrOnly = findViewById(R.id.share_qr_only);


        topView = findViewById(R.id.top_view);

    }

    private void initFunctionality() {

        try {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = mActivity.getCurrentFocus();
//If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(mActivity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {

        }


        if (!Constants.removeAds) {

            AdsManagerQ.getInstance().loadFreshBannerAd(mContext, bannerFrame, adsRelative, AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mContext, 350), getResources().getString(R.string.banner_ad_home_main_sticky_unit_id));

        } else {
            adsRelative.setVisibility(View.GONE);
        }


        initLogo();


        for (String s : colorsTxt) {
            int newColor = Color.parseColor(s);
            colors.add(newColor);
        }

        for (String s : colorsRecycler) {
            colorsRecyclerList.add(Color.parseColor(s));
        }

        Intent mIntent = getIntent();
        qr_generate = mIntent.getStringExtra("generateQR");
        CODE_TYPE = mIntent.getIntExtra("codeType", 0);
        getLocationCode = mIntent.getIntExtra("location", 0);
        activityCode = mIntent.getIntExtra("activity", 0);


        if (CODE_TYPE == 1) {


            logoQR.setVisibility(View.GONE);
            colorQR.setVisibility(View.VISIBLE);


        }


        initAdapter();


        CodeGenerator.setBLACK(Color.BLACK);
        generateCode(qr_generate);

        ArrayList<String> previousResult = AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_CREATED);

        if (CODE_TYPE == 1) {
            String barCodeStr = qr_generate;
            barCodeStr = "barCodeType:" + Constants.format + ";" + "barcode:" + qr_generate + ";";
            previousResult.add(barCodeStr);
        } else {
            previousResult.add(qr_generate);
        }

        AppPreference.getInstance(mContext).setStringArray(PrefKey.RESULT_LIST_OF_CREATED, previousResult);

        //save date of generation
        String currentDate;
        if (Locale.getDefault().equals(Locale.US)) {
            currentDate = new SimpleDateFormat("MM.dd.yyyy HH:mm").format(Calendar.getInstance().getTime());
        } else {
            currentDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
        }

        ArrayList<String> previousDate = AppPreference.getInstance(mContext).getStringArray(PrefKey.DATE_LIST_OF_CREATED);
        previousDate.add(currentDate);

        AppPreference.getInstance(mContext).setStringArray(PrefKey.DATE_LIST_OF_CREATED, previousDate);

        //saving color (standard black)
//        ArrayList<String> previousColor = AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_CREATED);
//        previousColor.add(Integer.toString(Color.BLACK));
//        AppPreference.getInstance(mContext).setStringArray(PrefKey.COLOR_LIST_OF_CREATED, previousColor);


        if (getLocationCode == 1) {
            mapOpen.setVisibility(View.VISIBLE);

            if (qr_generate.contains("?")) {
                try {


                    Matcher m = Pattern.compile("geo:(.*)", Pattern.CASE_INSENSITIVE).matcher(qr_generate);
                    while (m.find()) {
                        geo = m.group(1);
                        geo = geo.substring(0, geo.indexOf("?"));


                    }
                    m = Pattern.compile("q=(.*)", Pattern.CASE_INSENSITIVE).matcher(qr_generate);
                    while (m.find()) {
                        add = m.group(1);
                        add = add.substring(0, add.indexOf(";"));

                    }
                } catch (Exception ignored) {


                }
            }

            String[] latLongArray = geo.split(",");

            String latS = latLongArray[0];
            String longS = latLongArray[1];
            latS = latS.trim();
            longS = longS.trim();

            try {
                latitude = Float.parseFloat(latS);
                longitude = Float.parseFloat(longS);
            } catch (NumberFormatException ex) { // handle your exception

            }


        } else if (getLocationCode == 2) {
            mapOpen.setVisibility(View.VISIBLE);

            mapOpen.setImageResource(R.drawable.event_scanner);
            qr_generate = qr_generate.replace("\n", ";");
            String beginEvent = "", summary = "", description = "", location = "", dtstart = "", dtend = "";
            try {


                Matcher m = Pattern.compile("BEGIN:(.*)", Pattern.CASE_INSENSITIVE).matcher(qr_generate);
                while (m.find()) {

                    beginEvent = m.group(1);
                    if (qr_generate.contains("BEGIN:") || qr_generate.contains("begin:")) {
//                        beginEvent = beginEvent.substring(0, beginEvent.indexOf("\n"));

                        beginEvent = beginEvent.substring(0, beginEvent.indexOf(";"));

                    }

                }
                m = Pattern.compile("SUMMARY:(.*)", Pattern.CASE_INSENSITIVE).matcher(qr_generate);
                while (m.find()) {
                    summary = m.group(1);
                    if (qr_generate.contains("SUMMARY:") || qr_generate.contains("summary:")) {

                        summary = summary.substring(0, summary.indexOf(";"));


                    }
                }
                m = Pattern.compile("DTSTART:(.*)", Pattern.CASE_INSENSITIVE).matcher(qr_generate);
                while (m.find()) {
                    dtstart = m.group(1);
                    if (qr_generate.contains("DTSTART:") || qr_generate.contains("dtstart:")) {

                        dtstart = dtstart.substring(0, dtstart.indexOf(";"));


                    }
                }
                m = Pattern.compile("DTEND:(.*)", Pattern.CASE_INSENSITIVE).matcher(qr_generate);
                while (m.find()) {
                    dtend = m.group(1);
                    if (qr_generate.contains("DTEND:") || qr_generate.contains("dtend:")) {

                        dtend = dtend.substring(0, dtend.indexOf(";"));


                    }
                }
                m = Pattern.compile("LOCATION:(.*)", Pattern.CASE_INSENSITIVE).matcher(qr_generate);
                while (m.find()) {
                    location = m.group(1);
                    if (qr_generate.contains("LOCATION:") || qr_generate.contains("location:")) {

                        location = location.substring(0, location.indexOf(";"));


                    }
                }
                m = Pattern.compile("DESCRIPTION:(.*)", Pattern.CASE_INSENSITIVE).matcher(qr_generate);
                while (m.find()) {
                    description = m.group(1);
                    if (qr_generate.contains("DESCRIPTION:") || qr_generate.contains("description:")) {

                        description = description.substring(0, description.indexOf(";"));


                    }
                }


            } catch (Exception ignored) {

            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

            try {
                date = formatter.parse(dtstart);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                dateEnd = formatter.parse(dtend);
            } catch (ParseException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            }
            evTitle = summary;
            eveDescription = description;
            eveLocation = location;

        } else {
            mapOpen.setVisibility(View.GONE);
        }
    }

    private void initLogo() {

        logoRecyclerList.add(R.drawable.traffic_icon);
        logoRecyclerList.add(R.drawable.add_image_icon);
        for (int i = 1; i <= 10; i++) {
            int img = getResources().getIdentifier("logo_qr_" + i, "drawable", getPackageName());
            logoRecyclerList.add(img);
        }


    }

    private void initAdapter() {


        colorAdapter = new ColorAdapter(mContext, colorsRecyclerList);
        logoAdapter = new LogoAdapter(mContext, logoRecyclerList);

        mRecyclerView.setLayoutManager((new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)));
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setHasFixedSize(true);


    }

    private void generateCode(final String input) {
        CodeGenerator codeGenerator = new CodeGenerator();
        if (CODE_TYPE == 1) {

            codeGenerator.generateBarFor(input);

        } else {
            codeGenerator.generateQRFor(input);
            Constants.finalImageEditor = 0;
        }

        codeGenerator.setResultListener(new CodeGenerator.ResultListener() {
            @Override
            public void onResult(Bitmap bitmap) {
                //((BitmapDrawable)outputBitmap.getDrawable()).getBitmap().recycle();


                if (CODE_TYPE == 1) {

                    qr_code_style.setVisibility(View.GONE);
                    bar_code_style.setVisibility(View.VISIBLE);

                    inputStr = "barcode:" + input;

                    logoQR.setVisibility(View.GONE);

                    colorQR.setVisibility(View.VISIBLE);
                    linearLayout2QR.setVisibility(View.GONE);

                    try {
                        Glide.with(mContext)
                                .load(bitmap)
                                .centerInside()
                                // resizes width to 100, preserves original height, does not respect aspect ratio
                                .into(barCodeCard);

                        bitmapSaved = bitmap;
                    } catch (Exception ignored) {

                    }


                } else {


                    qr_code_style.setVisibility(View.VISIBLE);
                    bar_code_style.setVisibility(View.GONE);
                    inputStr = input;

                    try {
                        bitmapSaved = bitmap;
                        templateBitmap = bitmap;
                        Glide.with(mContext)
                                .load(bitmap)
                                .centerInside()
                                // resizes width to 100, preserves original height, does not respect aspect ratio
                                .into(outputBitmap);

                    } catch (Exception ignored) {

                    }


                }


            }
        });
        codeGenerator.execute();
    }

    private void initListener() {


        mapOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getLocationCode == 1) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(mActivity, "No Maps Installed", Toast.LENGTH_SHORT).show();
                    }

                } else if (getLocationCode == 2) {


                    if (date == null && dateEnd == null) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_INSERT)
                                    .setData(CalendarContract.Events.CONTENT_URI)
                                    .putExtra(CalendarContract.Events.TITLE, evTitle)
                                    .putExtra(CalendarContract.Events.DESCRIPTION, eveDescription)
                                    .putExtra(CalendarContract.Events.EVENT_LOCATION, eveLocation)
                                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(mActivity, "No Maps Installed", Toast.LENGTH_SHORT).show();
                        }

                    } else if (date == null) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_INSERT)
                                    .setData(CalendarContract.Events.CONTENT_URI)
                                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, dateEnd.getTime())
                                    .putExtra(CalendarContract.Events.TITLE, evTitle)
                                    .putExtra(CalendarContract.Events.DESCRIPTION, eveDescription)
                                    .putExtra(CalendarContract.Events.EVENT_LOCATION, eveLocation)
                                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);


                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(mActivity, "No Event Installed", Toast.LENGTH_SHORT).show();
                        }
                    } else if (dateEnd == null) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_INSERT)
                                    .setData(CalendarContract.Events.CONTENT_URI)
                                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTime())
                                    .putExtra(CalendarContract.Events.TITLE, evTitle)
                                    .putExtra(CalendarContract.Events.DESCRIPTION, eveDescription)
                                    .putExtra(CalendarContract.Events.EVENT_LOCATION, eveLocation)
                                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(mActivity, "No Event Installed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_INSERT)
                                .setData(CalendarContract.Events.CONTENT_URI)
                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTime())
                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, dateEnd.getTime())
                                .putExtra(CalendarContract.Events.TITLE, evTitle)
                                .putExtra(CalendarContract.Events.DESCRIPTION, eveDescription)
                                .putExtra(CalendarContract.Events.EVENT_LOCATION, eveLocation)
                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(mActivity, "No Event Installed", Toast.LENGTH_SHORT).show();
                        }
                    }


                }

            }
        });


        saveQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Constants.finalImageEditor = 0;


                try {
                    if (CODE_TYPE == 1) {
                        captureImageQRCODE(barCodeRelative);

                    } else {
                        if (bitmapAdapter != null) {
                            captureImageQRCODE(qr_code_style);
                        } else {
                            captureImageQRCODE(shareQrOnly);
                        }

                    }


                } catch (Exception ignored) {

                }

                Intent in1 = new Intent(mContext, EditQRCode.class);
                startActivity(in1);


            }
        });

        backQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogUtils.showDialogPrompt(mActivity, null, getString(R.string.remove_customization),
                        getString(R.string.yes), getString(R.string.no), true, new DialogUtils.DialogActionListener() {
                            @Override
                            public void onPositiveClick() {

                                try {
                                    InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    View view1 = mActivity.getCurrentFocus();
//If no view currently has focus, create a new one, just so we can grab a window token from it
                                    if (view1 == null) {
                                        view1 = new View(mActivity);
                                    }
                                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                                } catch (Exception ignored) {

                                }

                                finish();
                            }


                        });


            }
        });

        colorAdapter.setClickListener(new ColorAdapter.ClickListener() {
            @Override
            public void onItemClicked(int position) {


                CodeGenerator.setBLACK(colorsRecyclerList.get(position));
                generateCode(qr_generate);


            }
        });

        logoAdapter.setClickListener(new LogoAdapter.ClickListener() {
            @Override
            public void onItemClicked(int position) {


                if (position == 0) {
                    cardOfLogo.setVisibility(View.GONE);

                    logoBitmap.setImageBitmap(null);

                    logo = null;

                } else if (position == 1) {
                    galleryImageLogo();
                } else {
                    cardOfLogo.setVisibility(View.VISIBLE);
                    logo = BitmapFactory.decodeResource(getResources(),
                            logoRecyclerList.get(position));

                    logoBitmap.setImageResource(logoRecyclerList.get(position));


                }


            }
        });

        colorQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecyclerView.setAdapter(colorAdapter);

                linearLayout1QR.setVisibility(View.GONE);
                linearLayout2QR.setVisibility(View.GONE);

                categoryTxt.setVisibility(View.VISIBLE);
                categoryTxt.setText(getResources().getString(R.string.solid_color));

                saveQR.setVisibility(View.GONE);
                tickQR.setVisibility(View.VISIBLE);


                editQR.setText(getResources().getString(R.string.solid_color));
                topView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
//                saveButtonTemplate.setVisibility(View.GONE);

                {



                    if (CODE_TYPE == 1) {
                        qr_code_style.setVisibility(View.GONE);
                    } else {
                        qr_code_style.setVisibility(View.VISIBLE);
                    }

//                    saveButtonTemplate.setVisibility(View.GONE);
                    saveQR.setVisibility(View.VISIBLE);
                }

            }
        });

        logoQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecyclerView.setAdapter(logoAdapter);

                linearLayout1QR.setVisibility(View.GONE);
                linearLayout2QR.setVisibility(View.GONE);

                categoryTxt.setText(getResources().getString(R.string.logo));
                categoryTxt.setVisibility(View.VISIBLE);
                saveQR.setVisibility(View.GONE);
                tickQR.setVisibility(View.VISIBLE);


                editQR.setText(getResources().getString(R.string.logo));
                topView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);


                {

                    qr_code_style.setVisibility(View.VISIBLE);
                    saveQR.setVisibility(View.VISIBLE);
                }

            }
        });

        linearLayout2QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fancyQR = new Intent(QRCodeGeneratorScanner.this, CustomQRCode.class);
                fancyQR.putExtra(PrefKey.FancyQRStr, qr_generate);
                startActivity(fancyQR);
            }
        });

        tickQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout1QR.setVisibility(View.VISIBLE);
                linearLayout2QR.setVisibility(View.VISIBLE);

                saveQR.setVisibility(View.VISIBLE);
                tickQR.setVisibility(View.GONE);

                topView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                editQR.setText(getResources().getString(R.string.edit));

                {

                    if (CODE_TYPE == 1) {
                        linearLayout2QR.setVisibility(View.GONE);
                    } else {
                        linearLayout2QR.setVisibility(View.VISIBLE);
                    }
                    saveQR.setVisibility(View.VISIBLE);

                }

            }
        });
    }

    private void captureImageQRCODE(View v) {
        // TODO Auto-generated method stub


        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, v.getWidth(),
                v.getHeight());

        Canvas b = new Canvas(bitmap);
        v.draw(b);

        Constants.finalBitmap = bitmap;


    }

//    private void galleryImage() {
//
//
//        PictureSelector.create(QRCodeGeneratorScanner.this)
//                .openGallery(PictureMimeType.ofImage())
//                .isCamera(false)
//                .imageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
//                .selectionMode(PictureConfig.SINGLE)
//                .previewImage(false)
//                .forResult(PictureConfig.CHOOSE_REQUEST);
//
//    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode != RESULT_CANCELED) {
//            if (reqCode == PictureConfig.CHOOSE_REQUEST) {
//                // onResult Callback
//
//                if (data != null) {
//
//                    List<LocalMedia> result = PictureSelector.obtainMultipleResult(data);
//
//
//                    try {
//                        if (result.size() == 1) {
//
//                            Uri selectedImage = Uri.parse(result.get(0).getPath());
//
//
//                        }
//                    } catch (Exception ignored) {
//
//                    }
//
//                }
//
//
//            } else

                if (reqCode == PICK_IMAGE) {
                if (data != null) {

                    Uri selectedImage = null;
                    try {
                        selectedImage = data.getData();
                    } catch (Exception e) {
                        Toast.makeText(mActivity, "File Corrupted", Toast.LENGTH_SHORT).show();
                        return;

                    }

                    InputStream baseStream = null;
                    InputStream imageStream = null;
                    try {
                        //getting the image
                        baseStream = mActivity.getContentResolver().openInputStream(selectedImage);
                        imageStream = mActivity.getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(mContext, getResources().getString(R.string.error_file), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }

                    // ChecksumException
                    try {
                        //decoding bitmap for get width
                        Bitmap base = BitmapFactory.decodeStream(baseStream);
                        //set options for resize image
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = base.getWidth() / 1000; //get compress coef  (set image px size (compressing big image))

                        //decoding bitmap
                        cardOfLogo.setVisibility(View.VISIBLE);
                        logo = base;


                        Glide.with(mContext)
                                .asBitmap()
                                .load(base)
                                .centerCrop()
                                // resizes width to 100, preserves original height, does not respect aspect ratio
                                .into(logoBitmap);



                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }
            }
        }


    }

    private void galleryImageLogo() {


        Intent intent = new Intent(Intent.ACTION_PICK);
        //set intent type to image
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


}