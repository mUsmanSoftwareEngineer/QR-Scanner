package scanner.app.scan.qrcode.reader.activity;

import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.zxing.BarcodeFormat;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.data.constant.Constants;
import scanner.app.scan.qrcode.reader.data.preference.AppPreference;
import scanner.app.scan.qrcode.reader.data.preference.PrefKey;
import scanner.app.scan.qrcode.reader.utility.AdsManagerQ;
import scanner.app.scan.qrcode.reader.utility.AppUtils;


public class GenerateActivity extends AppCompatActivity implements NestedScrollView.OnScrollChangeListener, OnMapReadyCallback {

    static final int PICK_CONTACT = 1;
    final static int REQUEST_LOCATION = 199;
    private static final int TEXT = 0, CONTACT = 1, URL = 2, EMAIL = 3, WIFI = 4, BARCODE = 5, SMS = 6, LOCATION = 7, PHONE = 8, EVENT = 9,
            FACEBOOK = 10, TWITTER = 11, LINKDEIN = 12, INSTAGRAM = 13, WHATSAPP = 14;
    private static final String LOG_TAG = GenerateActivity.class.getSimpleName();

    private static int CURRENT_BTN = 0;


    private static int getCode = 0;
    public SupportMapFragment mapFragment;
    public GoogleMap map;
    public CardView cardViewMap;
    TextView appendUserName;
    String input, input2;
    int qr_category, qr_category_social, barCodeCategory;
    View v1, v2, v3, v4, v5, v6;
    LocationManager locationManager;
    boolean GpsStatus;
    int getLocation = 0;

    String startEvent = "", endEvent = "";
    String eveStartYear = "", eveStartMonth = "", eveStartDay = "", eveStartHour = "", eveStartMin = "", eveStartSec = "00";
    String eveEndYear = "", eveEndMonth = "", eveEndDay = "", eveEndHour = "", eveEndMin = "", eveEndSec = "00";
    TextView qr_cat_name;
    ImageView backFromQrCat, inforQRCat, privacyPolicyQR;
    LinearLayout removeAds;
    LinearLayout linearLayoutWifi;
    RelativeLayout hidden_network, memoRelative, eventRel, eventBTime, eventETime;
    Button w1, w2, w3;
    String type = "WEP";
    //    com.suke.widget.SwitchButton switchButton;
    SwitchCompat switchButton;
    EditText countryCode;
    MaterialSpinner materialSpinner;
    FirebaseAnalytics firebaseAnalytics;
    FrameLayout bannerFrame;

    ImageView refresh;

    String latitude, longitude;

    RelativeLayout adsRelative;
    ScheduledExecutorService backgroundExecutor;
    double lati, longi;
    FrameLayout nativeFrame;
    NativeAd mNativeAd;
    TextView inputText8, inputText9, startEventTime, endEventTime;
    String countryCodeVal = "+1";
    private EditText inputText, inputText2, inputText3, inputText4, inputText5, inputText6, inputText7, inputWhatsapp;
    private ImageButton choose_btn, contactChooseFromContact, email_choose_btn;
    private TextView recipientText, messageText, password, company, jobTitle, address;
    private TextView generate_btn, decorate_btn;
    private int generate_action = 0;
    private Activity mActivity;
    private Context mContext;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        initVar();
        initView();
        initFunctionality();
        initListener();
        View currentFocus = mActivity.getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }

    }


    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     */


    @Override
    protected void onDestroy() {
        try {
            mNativeAd = AdsManagerQ.getInstance().getmNativeAd();
            Log.d("NativeDestory", "OnDestroy" + mNativeAd);
            if (mNativeAd != null) {
                mNativeAd.destroy();
            }
        } catch (Exception ignore) {

        }

        super.onDestroy();
    }

    private void initView() {

        inputText = findViewById(R.id.inputText);
        inputText2 = findViewById(R.id.inputText2);
        inputText3 = findViewById(R.id.inputText3);
        inputText4 = findViewById(R.id.inputText4);
        inputText5 = findViewById(R.id.inputText5);
        inputText6 = findViewById(R.id.inputText6);
        inputText7 = findViewById(R.id.inputText7);
        inputWhatsapp = findViewById(R.id.inputWhatsapp);

        appendUserName = findViewById(R.id.userNameText);

        v1 = findViewById(R.id.horizontalLine1);
        v2 = findViewById(R.id.horizontalLine2);
        v3 = findViewById(R.id.horizontalLine3);
        v4 = findViewById(R.id.horizontalLine4);
        v5 = findViewById(R.id.horizontalLine5);
        v6 = findViewById(R.id.horizontalLine6);

        choose_btn = findViewById(R.id.choose_btn);

        generate_btn = findViewById(R.id.generate_btn);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        cardViewMap = findViewById(R.id.cardFrag);

        backFromQrCat = findViewById(R.id.backButtonQRCat);
        inforQRCat = findViewById(R.id.infoQRCat);
        privacyPolicyQR = findViewById(R.id.privacy_policy_qr);
        qr_cat_name = findViewById(R.id.QRCat);

        recipientText = findViewById(R.id.recipientText);
        messageText = findViewById(R.id.messageText);
        password = findViewById(R.id.passwordText);
        company = findViewById(R.id.companyText);
        jobTitle = findViewById(R.id.jobText);
        address = findViewById(R.id.address);


        linearLayoutWifi = findViewById(R.id.l1);
        hidden_network = findViewById(R.id.hide);

        memoRelative = findViewById(R.id.memoRel);

        contactChooseFromContact = findViewById(R.id.contact_phone_btn);
        email_choose_btn = findViewById(R.id.email_choose_btn);
        eventRel = findViewById(R.id.eventRel);
        eventBTime = findViewById(R.id.beginTime);
        eventETime = findViewById(R.id.endTime);
        inputText8 = findViewById(R.id.inputText8);
        inputText9 = findViewById(R.id.inputText9);
        startEventTime = findViewById(R.id.inputTextStartTime);
        endEventTime = findViewById(R.id.inputTextEndTime);

        w1 = findViewById(R.id.wpawpa2);
        w2 = findViewById(R.id.wep);
        w3 = findViewById(R.id.none);

        switchButton = findViewById(R.id.switch_button);

        countryCode = findViewById(R.id.country_code_edit_txt);

        materialSpinner = findViewById(R.id.material_spinner);


        bannerFrame = findViewById(R.id.banner_adsview);

        refresh = findViewById(R.id.refresh);

        adsRelative = findViewById(R.id.ads_relative);
        nativeFrame = findViewById(R.id.fl_adplaceholder);

        removeAds = findViewById(R.id.remove);


    }

    private void initVar() {
        mActivity = this;
        mContext = mActivity.getApplicationContext();

    }

    private void initListener() {


        removeAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GenerateActivity.this, RemoveAdsActivity.class));
            }
        });

        Bundle params = new Bundle();
        Bundle paramsCreateDoing = new Bundle();

        boolean firstRunGenerate = AppPreference.getInstance(this).getBoolean(PrefKey.GenerateActivityFirstRun, true);
        if (firstRunGenerate) {
            Bundle paramsCycle = new Bundle();
            paramsCycle.putString("QRStarted", "1");

            try {
                FirebaseAnalytics.getInstance(mContext).logEvent("QRCreateCycleUnique", paramsCycle);
            } catch (Exception ignored) {

            }
            AppPreference.getInstance(this).setBoolean(PrefKey.GenerateActivityFirstRun, false);
        }


        generate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                if (generate_action == 1) {


                    switch (CURRENT_BTN) {
                        case TEXT:
                            input = inputText.getText().toString();
                            getCode = 0;
                            params.putString("create", "text");
                            paramsCreateDoing.putString("QRTYPEDOING", "TEXT");
                            break;
                        case CONTACT:
                            input = "BEGIN:VCARD\n" +
                                    "VERSION:3.0\n" +
                                    "N:" + inputText.getText().toString() + "\n" +
                                    "TEL:" + inputText2.getText().toString() + "\n" +
                                    "EMAIL:" + inputText3.getText().toString() + "\n" +
                                    "ADR:" + inputText6.getText().toString() + "\n" +
                                    "END:VCARD";
                            getCode = 0;
                            params.putString("create", "contact");
                            paramsCreateDoing.putString("QRTYPEDOING", "CONTACT");
                            break;
                        case URL:

                            input = inputText.getText().toString();
                            input = input.replace(" ", "");

                            getCode = 0;
                            params.putString("create", "url");
                            paramsCreateDoing.putString("QRTYPEDOING", "URL");
                            break;
                        case EMAIL:
                            input = "MATMSG:TO:" + inputText.getText().toString() +
                                    ";SUB:" + inputText2.getText().toString() +
                                    ";BODY:" + inputText3.getText().toString() + ";;";
                            getCode = 0;
                            params.putString("create", "email");
                            paramsCreateDoing.putString("QRTYPEDOING", "EMAIL");
                            break;

                        case WIFI:

                            if (type.equals("")) {
                                input = "WIFI:S:" + inputText.getText().toString() +
                                        ";P:" + inputText2.getText().toString() + ";H:" + switchButton.isChecked() + ";";

                            } else {
                                input = "WIFI:S:" + inputText.getText().toString() +
                                        ";T:" + type +
                                        ";P:" + inputText2.getText().toString() + ";H:" + switchButton.isChecked() + ";";

                            }
                            getCode = 0;
                            params.putString("create", "wifi");
                            paramsCreateDoing.putString("QRTYPEDOING", "WIFI");
                            break;
                        case BARCODE:
                            input = inputText.getText().toString();
                            input = input.replace(" ", "");

                            if (barCodeCategory == 11 || barCodeCategory == 12) {
                                input2 = inputText2.getText().toString();
                                input2 = input2.replace(" ", input2);
                            }


                            getCode = 1;
                            params.putString("create", "barcode");
                            paramsCreateDoing.putString("QRTYPEDOING", "BARCODE");
                            break;
                        case SMS:
                            input = "SMSTO:" + inputText.getText().toString() +
                                    ":" + inputText2.getText().toString();

                            getCode = 0;
                            params.putString("create", "sms");
                            paramsCreateDoing.putString("QRTYPEDOING", "SMS");
                            break;
                        case LOCATION:
                            String add = inputText3.getText().toString();

                            if (!inputText.getText().toString().isEmpty() && !inputText2.getText().toString().isEmpty()) {
                                if (TextUtils.isEmpty(add)) {
                                    input = "GEO: " + inputText.getText().toString() +
                                            "," + inputText2.getText().toString() + "?q=" + "";
                                } else {
                                    input = "GEO: " + inputText.getText().toString() +
                                            "," + inputText2.getText().toString() + "?q=" + inputText3.getText().toString();
                                }
                            }

                            getCode = 0;
                            getLocation = 1;
                            params.putString("create", "location");
                            paramsCreateDoing.putString("QRTYPEDOING", "LOCATION");
                            break;
                        case PHONE:
                            input = inputText.getText().toString();
                            input = "tel: " + inputText.getText().toString();
                            getCode = 0;
                            params.putString("create", "phone");
                            paramsCreateDoing.putString("QRTYPEDOING", "PHONE");
                            break;

                        case EVENT:

                            startEvent = eveStartYear + eveStartMonth + eveStartDay + "T" + eveStartHour + eveStartMin + eveStartSec;
                            endEvent = eveEndYear + eveEndMonth + eveEndDay + "T" + eveEndHour + eveEndMin + eveEndSec;
                            input = "BEGIN:VEVENT" + "\nSUMMARY:" + inputText.getText().toString() + "\nLOCATION:" + inputText2.getText().toString() + "\nDESCRIPTION:" + inputText3.getText().toString() + "\nDTSTART:" + startEvent + "\nDTEND:" + endEvent + "\nEND:VEVENT";
                            getCode = 0;
                            getLocation = 2;
                            params.putString("create", "event");
                            paramsCreateDoing.putString("QRTYPEDOING", "EVENT");
                            break;

                        case FACEBOOK:
                            getCode = 0;

                            input = inputText.getText().toString();
//                            input="facebook://user?username="+input;
                            params.putString("create", "facebook");
                            paramsCreateDoing.putString("QRTYPEDOING", "FACEBOOK");
                            break;

                        case TWITTER:
                            getCode = 0;

                            input = inputText.getText().toString();
                            params.putString("create", "twitter");
                            paramsCreateDoing.putString("QRTYPEDOING", "TWITTER");

                            break;
                        case LINKDEIN:
                            getCode = 0;

                            input = inputText.getText().toString();
                            params.putString("create", "linkdein");
                            paramsCreateDoing.putString("QRTYPEDOING", "LINKDEIN");

                            break;
                        case INSTAGRAM:
                            getCode = 0;

                            input = inputText.getText().toString();
                            params.putString("create", "instagram");
                            paramsCreateDoing.putString("QRTYPEDOING", "INSTAGRAM");

                            break;
                        case WHATSAPP:
                            getCode = 0;

                            input = inputWhatsapp.getText().toString();
                            input = "whatsapp://send?phone=" + countryCodeVal + input;
                            params.putString("create", "whatsapp");
                            paramsCreateDoing.putString("QRTYPEDOING", "WHATSAPP");
                            break;

                    }

                    String digits = inputText.getText().toString();
                    int count = 0;
                    for (int i = 0, len = digits.length(); i < len; i++) {
                        if (Character.isDigit(digits.charAt(i))) {
                            count++;
                        }
                    }

                    String digits2 = inputText2.getText().toString();
                    int count2 = 0;
                    for (int i = 0, len = digits2.length(); i < len; i++) {
                        if (Character.isDigit(digits2.charAt(i))) {
                            count2++;
                        }
                    }

                    if (CURRENT_BTN == BARCODE) {


                        try {
                            FirebaseAnalytics.getInstance(mContext).logEvent("QRCreateDoing", paramsCreateDoing);
                        } catch (Exception ignored) {

                        }


                        if (barCodeCategory == 6) {


                            if (count % 2 == 0) {
                                firebaseAnalytics.logEvent("CreateBarCode", params);
                                Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                generateQR.putExtra("generateQR", input);
                                generateQR.putExtra("codeType", getCode);
                                generateQR.putExtra("location", getLocation);
                                generateQR.putExtra("activity", 1);
                                startActivity(generateQR);
                            } else {
                                Snackbar.make(view, "Enter Even Number of digits ", Snackbar.LENGTH_LONG).show();
                            }

                        } else if (barCodeCategory == 7) {

                            if (count == 8) {


                                String substring = input.substring(input.length() - 1);

                                int lastDigit = Integer.parseInt(substring);

                                if (lastDigit == checkSum(input)) {
                                    firebaseAnalytics.logEvent("CreateBarCode", params);
                                    Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                    generateQR.putExtra("generateQR", input);
                                    generateQR.putExtra("codeType", getCode);
                                    generateQR.putExtra("location", getLocation);
                                    generateQR.putExtra("activity", 1);
                                    startActivity(generateQR);
                                } else {
                                    Snackbar.make(view, "Enter Correct Checksum Digit ", Snackbar.LENGTH_LONG).show();
                                }
                            } else if (count == 7) {
                                firebaseAnalytics.logEvent("CreateBarCode", params);
                                Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                generateQR.putExtra("generateQR", input);
                                generateQR.putExtra("codeType", getCode);
                                generateQR.putExtra("location", getLocation);
                                generateQR.putExtra("activity", 1);
                                startActivity(generateQR);
                            } else if (count < 7) {
                                Snackbar.make(view, "Enter 8 digit or 7 digit EAN-8 Number ", Snackbar.LENGTH_LONG).show();
                            }


                        } else if (barCodeCategory == 8) {

                            if (count == 13) {


                                String substring = input.substring(input.length() - 1);

                                int lastDigit = Integer.parseInt(substring);

                                if (lastDigit == calculateChecksumDigit13(input)) {
                                    firebaseAnalytics.logEvent("CreateBarCode", params);
                                    Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                    generateQR.putExtra("generateQR", input);
                                    generateQR.putExtra("codeType", getCode);
                                    generateQR.putExtra("location", getLocation);
                                    generateQR.putExtra("activity", 1);
                                    startActivity(generateQR);
                                } else {
                                    Snackbar.make(view, "Enter Correct Checksum Digit ", Snackbar.LENGTH_LONG).show();
                                }
                            } else if (count == 12) {
                                firebaseAnalytics.logEvent("CreateBarCode", params);
                                Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                generateQR.putExtra("generateQR", input);
                                generateQR.putExtra("codeType", getCode);
                                generateQR.putExtra("location", getLocation);
                                generateQR.putExtra("activity", 1);
                                startActivity(generateQR);
                            } else if (count < 12) {
                                Snackbar.make(view, "Enter 12 digit or 13 digit EAN-13 Number ", Snackbar.LENGTH_LONG).show();
                            }
                        } else if (barCodeCategory == 9) {

                            if (count == 12) {


                                String substring = input.substring(input.length() - 1);

                                int lastDigit = Integer.parseInt(substring);

                                if (lastDigit == calculateChecksumDigitUPCA(input)) {
                                    firebaseAnalytics.logEvent("CreateBarCode", params);
                                    Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                    generateQR.putExtra("generateQR", input);
                                    generateQR.putExtra("codeType", getCode);
                                    generateQR.putExtra("location", getLocation);
                                    generateQR.putExtra("activity", 1);
                                    startActivity(generateQR);
                                } else {
                                    Snackbar.make(view, "Enter Correct UPC Checksum Digit ", Snackbar.LENGTH_LONG).show();
                                }
                            } else if (count == 11) {
                                firebaseAnalytics.logEvent("CreateBarCode", params);
                                Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                generateQR.putExtra("generateQR", input);
                                generateQR.putExtra("codeType", getCode);
                                generateQR.putExtra("location", getLocation);
                                generateQR.putExtra("activity", 1);
                                startActivity(generateQR);
                            } else if (count < 11) {
                                Snackbar.make(view, "Enter Correct UPC-A Number ", Snackbar.LENGTH_LONG).show();
                            }

                        } else if (barCodeCategory == 10) {

                            if (count == 8) {

                                String firstLetter = input.substring(0, 1);

                                int firstDigit = Integer.parseInt(firstLetter);

                                if (firstDigit == 0 || firstDigit == 1) {


                                    firebaseAnalytics.logEvent("CreateBarCode", params);
                                    Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                    generateQR.putExtra("generateQR", input);
                                    generateQR.putExtra("codeType", getCode);
                                    generateQR.putExtra("location", getLocation);
                                    generateQR.putExtra("activity", 1);
                                    startActivity(generateQR);

                                } else {
                                    Snackbar.make(view, "Number System must be 0 or 1", Snackbar.LENGTH_LONG).show();
                                }

                            } else if (count == 7) {

                                String firstLetter = input.substring(0, 1);

                                int firstDigit = Integer.parseInt(firstLetter);

                                if (firstDigit == 0 || firstDigit == 1) {


                                    firebaseAnalytics.logEvent("CreateBarCode", params);
                                    Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                    generateQR.putExtra("generateQR", input);
                                    generateQR.putExtra("codeType", getCode);
                                    generateQR.putExtra("location", getLocation);
                                    generateQR.putExtra("activity", 1);
                                    startActivity(generateQR);
                                } else {
                                    Snackbar.make(view, "Number System must be 0 or 1", Snackbar.LENGTH_LONG).show();
                                }

                            } else if (count < 7) {
                                Snackbar.make(view, "Enter Correct UPC-E Number ", Snackbar.LENGTH_LONG).show();
                            }
                        } else if (barCodeCategory == 11) {


                            if (!inputText.getText().toString().isEmpty() && !inputText2.getText().toString().isEmpty() && !inputText3.getText().toString().isEmpty()) {
                                if (count2 == 12) {

                                    firebaseAnalytics.logEvent("CreateBarCode", params);
                                    Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                    generateQR.putExtra("generateQR", input2);
                                    generateQR.putExtra("codeType", getCode);
                                    generateQR.putExtra("location", getLocation);
                                    generateQR.putExtra("activity", 1);
                                    startActivity(generateQR);
                                } else if (count < 12) {
                                    Snackbar.make(view, "Enter 12 digit Product Number ", Snackbar.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(mActivity, "Empty Input", Toast.LENGTH_SHORT).show();
                            }


                        } else if (barCodeCategory == 12) {


                            if (!inputText.getText().toString().isEmpty() && !inputText2.getText().toString().isEmpty() && !inputText3.getText().toString().isEmpty()) {
                                if (count2 == 12) {

                                    firebaseAnalytics.logEvent("CreateBarCode", params);
                                    Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                    generateQR.putExtra("generateQR", input2);
                                    generateQR.putExtra("codeType", getCode);
                                    generateQR.putExtra("location", getLocation);
                                    generateQR.putExtra("activity", 1);
                                    startActivity(generateQR);
                                } else if (count < 12) {
                                    Snackbar.make(view, "Enter 12 digit Book Code ", Snackbar.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(mActivity, "Empty Input", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            firebaseAnalytics.logEvent("CreateBarCode", params);
                            Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                            generateQR.putExtra("generateQR", input);
                            generateQR.putExtra("codeType", getCode);
                            generateQR.putExtra("location", getLocation);
                            generateQR.putExtra("activity", 1);
                            startActivity(generateQR);
                        }

                    } else {
                        try {
                            FirebaseAnalytics.getInstance(mContext).logEvent("QRCreateDoing", paramsCreateDoing);
                            firebaseAnalytics.logEvent("CreateQR", params);
                            int charCount = input.length();
                            if (charCount < 300) {
                                Intent generateQR = new Intent(GenerateActivity.this, QRCodeGeneratorScanner.class);
                                generateQR.putExtra("generateQR", input);
                                generateQR.putExtra("codeType", getCode);
                                generateQR.putExtra("location", getLocation);
                                generateQR.putExtra("activity", 1);
                                startActivity(generateQR);
                            } else {
//                                Snackbar.make(view, "Characters can't be more than 300", Snackbar.LENGTH_LONG).show();
                                Toast.makeText(mActivity, "Characters can't be more than 300", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ignored) {

                        }


                    }


                } else if (generate_action == 0) {

                    Toast.makeText(mActivity, "Empty Input", Toast.LENGTH_SHORT).show();
                }
            }
        });


        w1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                w1.setBackground(getResources().getDrawable(R.drawable.round_buttons));
                w1.setTextColor(getColor(R.color.white));
                w2.setTextColor(getColor(R.color.tt_black));
                w3.setTextColor(getColor(R.color.tt_black));
                w2.setBackground(getResources().getDrawable(R.drawable.rounded_buttons_bg));
                w3.setBackground(getResources().getDrawable(R.drawable.rounded_buttons_bg));
                type = "WPA";
            }
        });

        w2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {

                w2.setBackground(getResources().getDrawable(R.drawable.round_buttons));
                w1.setBackground(getResources().getDrawable(R.drawable.rounded_buttons_bg));
                w2.setTextColor(getColor(R.color.white));
                w1.setTextColor(getColor(R.color.tt_black));
                w3.setTextColor(getColor(R.color.tt_black));
                w3.setBackground(getResources().getDrawable(R.drawable.rounded_buttons_bg));
                type = "WEP";
            }
        });

        w3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {

                w3.setBackground(getResources().getDrawable(R.drawable.round_buttons));
                w2.setBackground(getResources().getDrawable(R.drawable.rounded_buttons_bg));
                w1.setBackground(getResources().getDrawable(R.drawable.rounded_buttons_bg));
                w3.setTextColor(getColor(R.color.white));
                w2.setTextColor(getColor(R.color.tt_black));
                w1.setTextColor(getColor(R.color.tt_black));
                type = "";
            }
        });


        inputText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    generate_btn.setBackgroundResource(R.drawable.round_buttons);
                    generate_action = 1;
                } else {
                    generate_btn.setBackgroundResource(R.drawable.round_buttons);
                    generate_action = 0;


                }
            }
        });


        inputWhatsapp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() != 0) {
                    generate_btn.setBackgroundResource(R.drawable.round_buttons);
                    generate_action = 1;
                } else {
                    generate_btn.setBackgroundResource(R.drawable.round_buttons);
                    generate_action = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        countryCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    countryCodeVal = s.toString();
                } else {
                    countryCodeVal = "+1";
                    Toast.makeText(mContext, "Country Code can't be empty", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CURRENT_BTN == PHONE || CURRENT_BTN == SMS || CURRENT_BTN == CONTACT) {

                    if ((ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)) {
                        checkReadContactsPermission();
                    } else {
                        try {
                            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                            startActivityForResult(intent, PICK_CONTACT);
                        } catch (Exception e) {
                            Toast.makeText(mActivity, "Contact not found", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else if (CURRENT_BTN == EMAIL) {
                    Account[] accounts = AccountManager.get(GenerateActivity.this).getAccountsByType("com.google");
                    final String[] accountName = {""};
                    final String[] selected = {""};
                    ArrayList<String> gUsernameList = new ArrayList<String>();
                    for (Account account : accounts) {


                        gUsernameList.add(account.name);

                    }
                    gUsernameList.add("Add Account");
                    accountName[0] = gUsernameList.get(0);
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateActivity.this);
                    builder.setTitle("Choose an Account");

                    String[] selectedoption = new String[gUsernameList.size()];
                    for (int j = 0; j < gUsernameList.size(); j++) {
                        selectedoption[j] = gUsernameList.get(j);
                    }

                    int checkedItem = 0; // cow
                    builder.setSingleChoiceItems(selectedoption, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // user checked an item
                            accountName[0] = gUsernameList.get(which);

                            selected[0] = selectedoption[which];
                            //here 'which' is the position selected

                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // user clicked OK
                            if (selected[0].equals("Add Account")) {

                                Intent addAccountIntent = new Intent(Settings.ACTION_ADD_ACCOUNT);
                                addAccountIntent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, new String[]{"com.google"});
                                startActivity(addAccountIntent);
                            } else {
                                inputText.setText(accountName[0]);
                            }

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        builder.setCancelable(true);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


            }
        });

        contactChooseFromContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)) {
                    checkReadContactsPermission();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }
            }
        });

        email_choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account[] accounts = AccountManager.get(GenerateActivity.this).getAccountsByType("com.google");
                final String[] accountName = {""};
                final String[] selected = {""};
                ArrayList<String> gUsernameList = new ArrayList<String>();
                for (Account account : accounts) {

                    gUsernameList.add(account.name);

                }
                gUsernameList.add("Add Account");
                accountName[0] = gUsernameList.get(0);
                AlertDialog.Builder builder = new AlertDialog.Builder(GenerateActivity.this);
                builder.setTitle("Choose an Account");

                String[] selectedoption = new String[gUsernameList.size()];
                for (int j = 0; j < gUsernameList.size(); j++) {
                    selectedoption[j] = gUsernameList.get(j);
                }

                int checkedItem = 0; // cow
                builder.setSingleChoiceItems(selectedoption, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                        accountName[0] = gUsernameList.get(which);

                        selected[0] = selectedoption[which];
                        //here 'which' is the position selected

                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK
                        if (selected[0].equals("Add Account")) {

                            Intent addAccountIntent = new Intent(Settings.ACTION_ADD_ACCOUNT);
                            addAccountIntent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, new String[]{"com.google"});
                            startActivity(addAccountIntent);
                        } else {
                            inputText3.setText(accountName[0]);
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        inputText8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerDialog datePickerDialog = new DatePickerDialog(GenerateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                Log.d("checkDate", year + " year" + month + " month" + dayOfMonth + " day");
                                eveStartYear = String.valueOf(year);
                                eveStartMonth = String.valueOf(month + 1);
                                eveStartDay = String.valueOf(dayOfMonth);
                                inputText8.setText(eveStartDay + "/" + eveStartMonth + "/" + eveStartYear);
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.show();

            }
        });

        inputText9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerDialog datePickerDialog = new DatePickerDialog(GenerateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                eveEndYear = String.valueOf(year);
                                eveEndMonth = String.valueOf(month + 1);
                                eveEndDay = String.valueOf(dayOfMonth + 1);
                                inputText9.setText(eveEndDay + "/" + eveEndMonth + "/" + eveEndYear);
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.show();

            }
        });

        startEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        GenerateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                eveStartHour = String.valueOf(hourOfDay);
                                eveStartMin = String.valueOf(minute);
                                startEventTime.setText(eveStartHour + ":" + eveStartMin);
                            }
                        },
                        hour,
                        minute,
                        DateFormat.is24HourFormat(GenerateActivity.this)
                );
                timePickerDialog.show();
            }
        });

        endEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        GenerateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                eveEndHour = String.valueOf(hourOfDay);
                                eveEndMin = String.valueOf(minute);
                                endEventTime.setText(eveEndHour + ":" + eveEndMin);
                            }
                        },
                        hour,
                        minute,
                        DateFormat.is24HourFormat(GenerateActivity.this)
                );
                timePickerDialog.show();
            }
        });


        inforQRCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showPermissionDialog(mActivity, mContext);
            }
        });

        privacyPolicyQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(GenerateActivity.this,PrivacyPolicy.class);
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


        materialSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> inputText.setText(item));

        backFromQrCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckGpsStatus();
//                if (GpsStatus) {
//                    LocationRequest locationRequest = new LocationRequest();
//                    locationRequest.setInterval(10000);
//                    locationRequest.setFastestInterval(3000);
//                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    LocationServices.getFusedLocationProviderClient(GenerateActivity.this)
//                            .requestLocationUpdates(locationRequest, new LocationCallback() {
//
//                                @Override
//                                public void onLocationResult(LocationResult locationResult) {
//                                    super.onLocationResult(locationResult);
//                                    LocationServices.getFusedLocationProviderClient(getApplicationContext())
//                                            .removeLocationUpdates(this);
//                                    if (locationResult != null && locationResult.getLocations().size() > 0) {
//                                        int latestlocIndex = locationResult.getLocations().size() - 1;
//                                        double lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
//                                        double longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
//                                        inputText.setText(lati + "");
//                                        inputText2.setText(longi + "");
//
////                                    Toast.makeText(mActivity, lati + longi + "", Toast.LENGTH_SHORT).show();
//                                        Location location = new Location("providerNA");
//                                        location.setLongitude(longi);
//                                        location.setLatitude(lati);
//                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here...!!");
////                                    animateMarker(markerOptions.getIcon(),latLng,false);
//                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
//                                        if (ActivityCompat.checkSelfPermission(GenerateActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GenerateActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                            // TODO: Consider calling
//                                            //    ActivityCompat#requestPermissions
//                                            // here to request the missing permissions, and then overriding
//                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                            //                                          int[] grantResults)
//                                            // to handle the case where the user grants the permission. See the documentation
//                                            // for ActivityCompat#requestPermissions for more details.
//                                            return;
//                                        }
//
//                                        if (map != null) {
//                                            map.setMyLocationEnabled(true);
//                                            map.addMarker(markerOptions);
//                                            map.animateCamera(cameraUpdate);
//                                        }
//
//                                        if (isNetworkConnected()) {
//
//                                            try {
//                                                String address = getAddress(mContext, location.getLatitude(), location.getLongitude());
////                inputLayout3.setVisibility(View.VISIBLE);
//                                                inputText3.setVisibility(View.VISIBLE);
//                                                if (!address.isEmpty()) {
//                                                    inputText3.setText(address);
//                                                }
//
//                                                password.setVisibility(View.VISIBLE);
//                                                password.setText("Address");
//                                                v3.setVisibility(View.VISIBLE);
//                                            } catch (Exception ignored) {
//
//                                            }
//
//
//                                        } else {
////                inputLayout3.setVisibility(View.GONE);
//                                            Toast.makeText(mActivity, "Network Error", Toast.LENGTH_SHORT).show();
//                                        }
//
//                                    }
//                                }
//                            }, Looper.getMainLooper());
//
//
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();


    }

    private int calculateChecksumDigitUPCA(String code) {

        String substring = input.substring(input.length() - 1);

        int lastDigit = Integer.parseInt(substring);

        int odd_sum = 0, even_sum = 0;
        int total_sum = 0;
        for (int i = 0; i < code.length(); i++) {
            if (i % 2 != 0) {

//                Log.d("oddDigits",code.charAt(i)+" ");
                even_sum = even_sum + Integer.parseInt("" + code.charAt(i));
            } else {

//                Log.d("evenDigits",code.charAt(i)+" ");
                int i1 = Integer.parseInt("" + code.charAt(i));
                odd_sum = odd_sum + Integer.parseInt("" + code.charAt(i)) * 3;
            }
        }

        total_sum = even_sum + odd_sum;

        if (total_sum % 10 == 0) {
            return lastDigit;
        }
        return total_sum;
    }


    private int getCheckSum(String str) {

        char[] code = new char[str.length()];

        // Copy character by character into array
        for (int i = 0; i < str.length(); i++) {
            code[i] = str.charAt(i);
        }

        int odd = 0;
        int even = 0;
        for (int i = 0; i < str.length() - 1; i++) {
            int index = i + 1;
            if (index % 2 != 0)
                odd += code[i];
            else
                even += code[i];
        }
        return ((10 - ((odd + even * 3) % 10)) % 10);
    }

    private int calculateChecksumDigit13(String mMembershipId) {

        String substring = input.substring(input.length() - 1);

        int lastDigit = Integer.parseInt(substring);

        int odd_sum = 0, even_sum = 0;
        int total_sum = 0;
        for (int i = 0; i < mMembershipId.length(); i++) {
            final int i1 = Integer.parseInt("" + mMembershipId.charAt(i));
            if (i % 2 != 0) {
//                Log.d("oddDigits",mMembershipId.charAt(i)+" ");
                odd_sum = odd_sum + i1 * 3;
            } else {
//                Log.d("evenDigits",mMembershipId.charAt(i)+" ");
                even_sum = even_sum + i1;
            }
        }
        even_sum = even_sum - lastDigit;
        total_sum = even_sum + odd_sum;

        if (total_sum % 10 != 0) {
            return 10 - (total_sum % 10);
        }
        return total_sum;


    }

    public int checkSum(String str) {
//        int val=0;
//        for(int i=0;i<code.length();i++){
//            val+=((int)Integer.parseInt(code.charAt(i)+""))*((i%2==0)?1:3);
//        }
//
//        int checksum_digit = 10 - (val % 10);
//        if (checksum_digit == 10) checksum_digit = 0;


        char[] code = new char[str.length()];

        // Copy character by character into array
        for (int i = 0; i < str.length(); i++) {
            code[i] = str.charAt(i);
        }

        int sum1 = code[1] + code[3] + code[5];
        int sum2 = 3 * (code[0] + code[2] + code[4] + code[6]);

        int checksum_value = sum1 + sum2;
        int checksum_digit = 10 - (checksum_value % 10);
        if (checksum_digit == 10) checksum_digit = 0;

        return checksum_digit;


    }

//    public void animateMarker(final Marker marker, final LatLng toPosition,
//                              final boolean hideMarker) {
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        Projection proj = map.getProjection();
//        Point startPoint = proj.toScreenLocation(marker.getPosition());
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//        final long duration = 500;
//
//        LinearInterpolator interpolator = new LinearInterpolator();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed
//                        / duration);
//                double lng = t * toPosition.longitude + (1 - t)
//                        * startLatLng.longitude;
//                double lat = t * toPosition.latitude + (1 - t)
//                        * startLatLng.latitude;
//                marker.setPosition(new LatLng(lat, lng));
//
//                if (t < 1.0) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//                } else {
//                    marker.setVisible(!hideMarker);
//                }
//            }
//        });
//    }


    //for select of contact
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {


                    try {
                        Uri contactData = data.getData();
                        Cursor c = mActivity.getContentResolver().query(contactData, null, null, null, null);
                        if (c.moveToFirst()) {


                            String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                            @SuppressLint("Range") String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                            if (hasPhone.equalsIgnoreCase("1")) {
                                Cursor phones = mActivity.getContentResolver().query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                        null, null);
                                phones.moveToFirst();
                                @SuppressLint("Range") String cNumber = phones.getString(phones.getColumnIndex("data1"));
                                //AppUtils.showToast(mContext, cNumber);

//                                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                                if (qr_category == 7 || qr_category == 5) {
                                    inputText.setText(cNumber);
                                } else if (qr_category == 1) {
                                    inputText2.setText(cNumber);
                                }

//                            if (CURRENT_BTN == PHONE) {
//                                inputText.setText(cNumber);
//                            }
                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(mActivity, e + "", Toast.LENGTH_SHORT).show();
                    }


                }
                break;


            case REQUEST_LOCATION:

                if (Activity.RESULT_OK == resultCode) {

                    CheckGpsStatus();
                    if (GpsStatus) {
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setInterval(10000);
                        locationRequest.setFastestInterval(3000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }

                        LocationServices.getFusedLocationProviderClient(GenerateActivity.this)
                                .requestLocationUpdates(locationRequest, new LocationCallback() {

                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                                .removeLocationUpdates(this);
                                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                                            int latestlocIndex = locationResult.getLocations().size() - 1;
                                            lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                                            longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
                                            latitude = String.valueOf(lati);
                                            longitude = String.valueOf(longi);
                                            inputText.setText(longitude);
                                            inputText2.setText(latitude);

                                            LocationOnMap();


                                            if (isNetworkConnected()) {
                                                try {
                                                    Location location = new Location("providerNA");
                                                    location.setLongitude(longi);
                                                    location.setLatitude(lati);
                                                    String address = getAddress(mContext, location.getLatitude(), location.getLongitude());
//                                                inputLayout3.setVisibility(View.VISIBLE);
                                                    inputText3.setText(address);
                                                } catch (Exception ignored) {

                                                }


                                            } else {
//                                                inputLayout3.setVisibility(View.GONE);
                                                Toast.makeText(mActivity, "Network Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }, Looper.getMainLooper());


//                        Toast.makeText(mContext, "GPS On", Toast.LENGTH_SHORT).show();
                    }

                }


        }
    }

    private boolean checkReadContactsPermission() {
        if ((ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_CONTACTS},
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

                if (permission.equals(Manifest.permission.READ_CONTACTS)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                            startActivityForResult(intent, PICK_CONTACT);
                        } catch (Exception e) {
                            Toast.makeText(mActivity, "Contact not found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
                    }


                } else if (permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(GenerateActivity.this, "permission granted", Toast.LENGTH_SHORT).show();

                    } else {
                        AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
                    }
                }
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }


    @SuppressLint("SetTextI18n")
    private void initFunctionality() {


        firebaseAnalytics = FirebaseAnalytics.getInstance(mContext);

        try {
            materialSpinner.setBackgroundColor(getResources().getColor(R.color.darkMainColor));
            materialSpinner.setTextColor(getResources().getColor(R.color.white));
        } catch (Exception ignored) {

        }


        Intent mIntent = getIntent();
        Intent mSocialIntent = getIntent();
        Intent barCodeIntent = getIntent();
        qr_category = mIntent.getIntExtra("qr_code_cat", 0);
//        qr_category_social = mSocialIntent.getIntExtra("qr_code_cat_social", 0);
        barCodeCategory = barCodeIntent.getIntExtra("bar_code_cat", 0);

        if (!Constants.removeAds) {

            AdsManagerQ.getInstance().loadFreshBannerAd(mContext, bannerFrame, adsRelative, AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mContext, 350), getResources().getString(R.string.banner_ad_home_main_sticky_unit_id));

            if (qr_category == 6) {
                nativeFrame.setVisibility(View.GONE);
            } else {
                new AdsManagerQ().showNativeAd(GenerateActivity.this, getResources().getString(R.string.native_ad_unit_id), nativeFrame);
            }


        } else {
            adsRelative.setVisibility(View.GONE);
            removeAds.setVisibility(View.GONE);
            nativeFrame.setVisibility(View.GONE);
        }

        if (qr_category == 0) {
            CURRENT_BTN = TEXT;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");
            choose_btn.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            inputText.setHint(R.string.type_here_text);
            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText.setLines(8);
            appendUserName.setVisibility(View.GONE);
            inputText.setVisibility(View.VISIBLE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);

//            cardViewMap.setVisibility(View.GONE);
            qr_cat_name.setText(mActivity.getResources().getString(R.string.txt_generate));
            materialSpinner.setVisibility(View.GONE);


        } else if (qr_category == 1) {

            CURRENT_BTN = CONTACT;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
            appendUserName.setVisibility(View.GONE);
            choose_btn.setVisibility(View.GONE);
            inputText.setHint("");
            inputText.setInputType(InputType.TYPE_CLASS_TEXT);
            inputText2.setHint("");
            inputText2.setInputType(InputType.TYPE_CLASS_PHONE);
            inputText3.setHint("");
            inputText3.setInputType(InputType.TYPE_CLASS_TEXT);
            inputText4.setHint("");
            inputText4.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            recipientText.setText(mActivity.getResources().getString(R.string.name));
            recipientText.setVisibility(View.VISIBLE);
            messageText.setText(mActivity.getResources().getString(R.string.phone_number));
            password.setText(mActivity.getResources().getString(R.string.email));
            messageText.setVisibility(View.VISIBLE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.VISIBLE);
            v3.setVisibility(View.VISIBLE);
            hidden_network.setVisibility(View.GONE);
            inputText5.setVisibility(View.VISIBLE);
            inputText5.setHint("");
            inputText6.setHint("");
            inputText6.setVisibility(View.VISIBLE);
            inputText7.setVisibility(View.VISIBLE);
            inputText7.setLines(4);
            memoRelative.setVisibility(View.VISIBLE);
            company.setVisibility(View.VISIBLE);
            jobTitle.setVisibility(View.VISIBLE);
            address.setVisibility(View.VISIBLE);
            contactChooseFromContact.setVisibility(View.VISIBLE);
            v4.setVisibility(View.VISIBLE);
            v5.setVisibility(View.VISIBLE);
            v6.setVisibility(View.VISIBLE);
            email_choose_btn.setVisibility(View.VISIBLE);
            eventRel.setVisibility(View.GONE);

            input = "BEGIN:VCARD\n" +
                    "VERSION:3.0\n" +
                    "N:" + inputText.getText().toString() + "\n" +
                    "TEL:" + inputText2.getText().toString() + "\n" +
                    "EMAIL:" + inputText3.getText().toString() + "\n" +
                    "ADR:" + inputText4.getText().toString() + "\n" +
                    "END:VCARD";

//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText(mActivity.getResources().getString(R.string.contact_generate));
            countryCode.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);


        } else if (qr_category == 2) {

            CURRENT_BTN = URL;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            choose_btn.setVisibility(View.GONE);
            inputText.setHint("Enter URL here");
            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText.setLines(6);
            appendUserName.setVisibility(View.GONE);
            inputText.setText("https://");
            inputText.setVisibility(View.VISIBLE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);

//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText(mActivity.getResources().getString(R.string.txt_website));
            countryCode.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);

        } else if (qr_category == 3) {

            CURRENT_BTN = WIFI;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");
            recipientText.setVisibility(View.VISIBLE);
            messageText.setVisibility(View.VISIBLE);
            recipientText.setText(mActivity.getResources().getString(R.string.network));
            messageText.setText(mActivity.getResources().getString(R.string.security));
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.GONE);
            appendUserName.setVisibility(View.GONE);
            choose_btn.setVisibility(View.GONE);
            inputText.setHint(R.string.type_here_wifi_ssid);
            inputText.setInputType(InputType.TYPE_CLASS_TEXT);
            inputText3.setHint(R.string.type_here_wifi_password);
            inputText3.setInputType(InputType.TYPE_CLASS_TEXT);
            linearLayoutWifi.setVisibility(View.VISIBLE);
            inputText.setVisibility(View.VISIBLE);
            inputText2.setVisibility(View.GONE);
            inputText2.setLines(0);
//            Log.d("switchButton",switchButton.isChecked()+"");
//            inputText2.setClickable(false);
//            inputText2.setEnabled(false);
//            inputText2.setFocusable(false);
            inputText3.setVisibility(View.VISIBLE);
            inputText4.setVisibility(View.GONE);
            password.setVisibility(View.VISIBLE);
            v3.setVisibility(View.VISIBLE);
            hidden_network.setVisibility(View.VISIBLE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);

//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText(mActivity.getResources().getString(R.string.wifi_generate));
            countryCode.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);

        } else if (qr_category == 4) {

            CURRENT_BTN = EMAIL;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");
            recipientText.setVisibility(View.VISIBLE);
            messageText.setVisibility(View.VISIBLE);
            recipientText.setText(mActivity.getResources().getString(R.string.recipient_email));
            messageText.setText(mActivity.getResources().getString(R.string.subject));
            appendUserName.setVisibility(View.GONE);
            choose_btn.setVisibility(View.VISIBLE);
            inputText.setHint(R.string.type_here_email);
            inputText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            inputText2.setHint("");
            inputText2.setInputType(InputType.TYPE_CLASS_TEXT);
            inputText3.setHint("");
            inputText3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText3.setLines(5);
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.VISIBLE);
            password.setText(mActivity.getResources().getString(R.string.body));
            v3.setVisibility(View.VISIBLE);
            hidden_network.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);


//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText(mActivity.getResources().getString(R.string.email_generate));
            countryCode.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);

        } else if (qr_category == 5) {

            CURRENT_BTN = SMS;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");
            choose_btn.setVisibility(View.VISIBLE);
            appendUserName.setVisibility(View.GONE);
            inputText.setHint("");
            inputText.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText.setLines(3);
            inputText2.setHint("");
            inputText2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText2.setLines(6);
            inputText.setVisibility(View.VISIBLE);
            inputText2.setVisibility(View.VISIBLE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            recipientText.setVisibility(View.VISIBLE);
            messageText.setVisibility(View.VISIBLE);
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);


//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText(mActivity.getResources().getString(R.string.sms_generate));
            countryCode.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);

        } else if (qr_category == 6) {

//            bannerFrame.setVisibility(View.VISIBLE);
//            frameLayout.setVisibility(View.GONE);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            CURRENT_BTN = LOCATION;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");
//            inputText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
//            inputText2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

            choose_btn.setVisibility(View.GONE);
            inputText.setHint("");

            inputText.setClickable(false);
            inputText.setEnabled(false);
            inputText.setFocusable(false);
            inputText2.setHint("");
            appendUserName.setVisibility(View.GONE);

            recipientText.setVisibility(View.VISIBLE);
            messageText.setVisibility(View.VISIBLE);
            recipientText.setText(mActivity.getResources().getString(R.string.longitude));
            messageText.setText(mActivity.getResources().getString(R.string.latitude));

            qr_cat_name.setText(mActivity.getResources().getString(R.string.location_generate));
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
            inputText2.setClickable(false);
            inputText2.setEnabled(false);
            inputText2.setFocusable(false);
            inputText3.setClickable(false);
            inputText3.setEnabled(false);
            inputText3.setFocusable(false);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.VISIBLE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText3.setVisibility(View.VISIBLE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            countryCode.setVisibility(View.GONE);
            inputText3.setHint(mActivity.getResources().getString(R.string.enter_address));
            inputWhatsapp.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);
            password.setText(mActivity.getResources().getString(R.string.address));
            v3.setVisibility(View.VISIBLE);
//            refresh.setVisibility(View.VISIBLE);
//            relativeLayout.setVisibility(View.GONE);
            cardViewMap.setVisibility(View.VISIBLE);
//            if (mapFragment != null) {
//                mapFragment.getMapAsync(new OnMapReadyCallback() {
//                    @Override
//                    public void onMapReady(GoogleMap map) {
//                        loadLocation(map);
//                    }
//                });
//            } else {
////                Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
//            }


            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

            backgroundExecutor = Executors.newSingleThreadScheduledExecutor();

            backgroundExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    // Your code logic goes here.
                    FetchLocation();
                }
            });


        } else if (qr_category == 7) {

            CURRENT_BTN = PHONE;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");
            choose_btn.setVisibility(View.VISIBLE);
            inputText.setHint(R.string.type_here_phone);
            inputText.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText.setLines(4);
            inputText.setVisibility(View.VISIBLE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            appendUserName.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);

            inputWhatsapp.setVisibility(View.GONE);

//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText(mActivity.getResources().getString(R.string.phone_generate));
            countryCode.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);

        } else if (qr_category == 8) {
            CURRENT_BTN = EVENT;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            inputText.setHint("");
            inputText.setInputType(InputType.TYPE_CLASS_TEXT);
            inputText2.setHint("");
            inputText2.setInputType(InputType.TYPE_CLASS_TEXT);
            inputText3.setHint("");
            inputText3.setInputType(InputType.TYPE_CLASS_TEXT);
            inputText3.setLines(5);
            appendUserName.setVisibility(View.GONE);
            recipientText.setVisibility(View.VISIBLE);
            messageText.setVisibility(View.VISIBLE);
            recipientText.setText(mActivity.getResources().getString(R.string.event_name));
            messageText.setText(mActivity.getResources().getString(R.string.location));
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);

            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.VISIBLE);
            password.setText(mActivity.getResources().getString(R.string.description));
            v3.setVisibility(View.VISIBLE);
            hidden_network.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.VISIBLE);


//            cardViewMap.setVisibility(View.GONE);


            inputWhatsapp.setVisibility(View.GONE);
            qr_cat_name.setText(mActivity.getResources().getString(R.string.event_generate));
            countryCode.setVisibility(View.GONE);

            materialSpinner.setVisibility(View.GONE);

        } else if (qr_category == 9) {

            CURRENT_BTN = FACEBOOK;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            inputText.setHint(mActivity.getResources().getString(R.string.facebook_url));
            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText.setLines(6);
            appendUserName.setText("facebook://user?username=");
            appendUserName.setVisibility(View.GONE);
            inputText.setVisibility(View.VISIBLE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            countryCode.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);
            qr_cat_name.setText(mActivity.getResources().getString(R.string.result_facebook));
            materialSpinner.setVisibility(View.VISIBLE);
            inputText.setText("https://www.facebook.com/profile.php?id=");
            materialSpinner.setItems("https://www.facebook.com/profile.php?id=", "https://www.facebook.com/", "https://www.facebook.com/groups/");

        } else if (qr_category == 10) {
            CURRENT_BTN = TWITTER;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            inputText.setHint(R.string.twitter_url);
            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText.setLines(6);
            inputText.setVisibility(View.VISIBLE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            appendUserName.setText("https://twitter.com/");
            appendUserName.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);
            qr_cat_name.setText(mActivity.getResources().getString(R.string.result_twitter));
            countryCode.setVisibility(View.GONE);
            inputText.setText("https://twitter.com/");
            materialSpinner.setVisibility(View.VISIBLE);
            materialSpinner.setItems("https://twitter.com/");
        } else if (qr_category == 11) {
            CURRENT_BTN = LINKDEIN;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            inputText.setHint(R.string.linkdein_url);
            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText.setLines(6);
            appendUserName.setText("linkdein://user?username=");
            appendUserName.setVisibility(View.GONE);
            inputText.setVisibility(View.VISIBLE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);
            qr_cat_name.setText(mActivity.getResources().getString(R.string.result_linkdein));
            countryCode.setVisibility(View.GONE);
            inputText.setText("https://www.linkedin.com/in/");
            materialSpinner.setVisibility(View.VISIBLE);
            materialSpinner.setItems("https://www.linkedin.com/in/", "https://www.linkedin.com/feed/", "https://www.linkedin.com/company/", "https://www.linkedin.com/hiring/jobs/");
        } else if (qr_category == 12) {
            CURRENT_BTN = INSTAGRAM;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            inputText.setHint(R.string.username);
            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText.setLines(6);
            appendUserName.setText("instagram://user?username=");
            appendUserName.setVisibility(View.GONE);
            inputText.setVisibility(View.VISIBLE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);
            qr_cat_name.setText(mActivity.getResources().getString(R.string.result_instagram));
            countryCode.setVisibility(View.GONE);
            inputText.setText("https://www.instagram.com/");
            materialSpinner.setVisibility(View.VISIBLE);
            materialSpinner.setItems("https://www.instagram.com/");
        } else if (qr_category == 13) {
            CURRENT_BTN = WHATSAPP;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            inputText.setVisibility(View.GONE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            appendUserName.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            recipientText.setText(mActivity.getResources().getString(R.string.result_whatsapp));
            recipientText.setVisibility(View.VISIBLE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.VISIBLE);
            inputWhatsapp.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputWhatsapp.setLines(5);
//            cardViewMap.setVisibility(View.GONE);
            qr_cat_name.setText(mContext.getResources().getString(R.string.result_whatsapp));
            countryCode.setVisibility(View.VISIBLE);
            materialSpinner.setVisibility(View.GONE);
        }


        if (barCodeCategory == 2) {
//            Toast.makeText(mActivity, barCodeCategory+"", Toast.LENGTH_SHORT).show();
            Constants.format = BarcodeFormat.CODE_128;
            CURRENT_BTN = BARCODE;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            inputText.setHint(R.string.enter_text_without_special_characters);

            InputFilter[] editFilters = inputText.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.LengthFilter(70);
            inputText.setFilters(newFilters);
            inputText.setLines(8);
            appendUserName.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText("CODE_128");
        } else if (barCodeCategory == 3) {
//            Toast.makeText(mActivity, barCodeCategory+"", Toast.LENGTH_SHORT).show();
            Constants.format = BarcodeFormat.CODE_39;
            CURRENT_BTN = BARCODE;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);


            inputText.setHint(R.string.enter_text_in_uppercase_without_special_characters);
            inputText.setLines(8);
            InputFilter[] editFilters = inputText.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.LengthFilter(10);
            inputText.setFilters(newFilters);

            inputText.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

            appendUserName.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText("CODE_39");
        } else if (barCodeCategory == 4) {
//            Toast.makeText(mActivity, barCodeCategory+"", Toast.LENGTH_SHORT).show();
            Constants.format = BarcodeFormat.CODE_93;
            CURRENT_BTN = BARCODE;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            inputText.setHint(R.string.enter_text_in_uppercase_without_special_characters);
            inputText.setLines(8);
            InputFilter[] editFilters = inputText.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.LengthFilter(20);
            inputText.setFilters(newFilters);

            inputText.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

            appendUserName.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText("CODE_93");
        } else if (barCodeCategory == 5) {
//            Toast.makeText(mActivity, barCodeCategory+"", Toast.LENGTH_SHORT).show();
            Constants.format = BarcodeFormat.CODABAR;
            CURRENT_BTN = BARCODE;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            inputText.setLines(8);
            inputText.setText("");
            inputText.setHint(R.string.only_digits);
            InputFilter[] editFilters = inputText.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.LengthFilter(20);
            inputText.setFilters(newFilters);
            inputText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

            appendUserName.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText("CODABAR");
        } else if (barCodeCategory == 6) {
//            Toast.makeText(mActivity, barCodeCategory+"", Toast.LENGTH_SHORT).show();
            Constants.format = BarcodeFormat.ITF;
            CURRENT_BTN = BARCODE;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            inputText.setHint(R.string.only_even);
            inputText.setLines(8);
            InputFilter[] editFilters = inputText.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.LengthFilter(40);
            inputText.setFilters(newFilters);
            inputText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

            appendUserName.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText("ITF");
        } else if (barCodeCategory == 7) {
//            Toast.makeText(mActivity, barCodeCategory+"", Toast.LENGTH_SHORT).show();
            Constants.format = BarcodeFormat.EAN_8;
            CURRENT_BTN = BARCODE;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            inputText.setHint(R.string.seven_digit_with_checksum);
            inputText.setLines(8);
            InputFilter[] editFilters = inputText.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.LengthFilter(8);
            inputText.setFilters(newFilters);
            inputText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

            appendUserName.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText("EAN_8");
        } else if (barCodeCategory == 8) {
//            Toast.makeText(mActivity, barCodeCategory+"", Toast.LENGTH_SHORT).show();
            Constants.format = BarcodeFormat.EAN_13;
            CURRENT_BTN = BARCODE;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            inputText.setHint(R.string.twelve_digit_with_checksum);
            inputText.setLines(8);
            InputFilter[] editFilters = inputText.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.LengthFilter(13);
            inputText.setFilters(newFilters);
            inputText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

            appendUserName.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText("EAN_13");
        } else if (barCodeCategory == 9) {
//            Toast.makeText(mActivity, barCodeCategory+"", Toast.LENGTH_SHORT).show();
            Constants.format = BarcodeFormat.UPC_A;
            CURRENT_BTN = BARCODE;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            inputText.setHint(R.string.eleven_digit_with_checksum);
            inputText.setLines(8);
            InputFilter[] editFilters = inputText.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.LengthFilter(12);
            inputText.setFilters(newFilters);
            inputText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

            appendUserName.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText("UPC_A");
        } else if (barCodeCategory == 10) {
//            Toast.makeText(mActivity, barCodeCategory+"", Toast.LENGTH_SHORT).show();
            Constants.format = BarcodeFormat.UPC_E;
            CURRENT_BTN = BARCODE;
            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");

            choose_btn.setVisibility(View.GONE);
            inputText.setHint(R.string.seven_digit_with_checksum);
            inputText.setLines(8);
            InputFilter[] editFilters = inputText.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.LengthFilter(7);
            inputText.setFilters(newFilters);
            inputText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

            appendUserName.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            recipientText.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            hidden_network.setVisibility(View.GONE);
            inputText2.setVisibility(View.GONE);
            inputText3.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText("UPC_E");
        } else if (barCodeCategory == 11) {

            Constants.format = BarcodeFormat.EAN_13;
            CURRENT_BTN = BARCODE;

//
//            choose_btn.setVisibility(View.GONE);
//            inputText.setHint(R.string.seven_digit_with_checksum);
//            inputText.setLines(8);
//            InputFilter[] editFilters = inputText.getFilters();
//            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
//            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
//            newFilters[editFilters.length] = new InputFilter.LengthFilter(7);
//            inputText.setFilters(newFilters);
//            inputText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
//
//            appendUserName.setVisibility(View.GONE);
//            v1.setVisibility(View.GONE);
//            v2.setVisibility(View.GONE);
//            recipientText.setVisibility(View.GONE);
//            messageText.setVisibility(View.GONE);
//            linearLayoutWifi.setVisibility(View.GONE);
//            password.setVisibility(View.GONE);
//            v3.setVisibility(View.GONE);
//            hidden_network.setVisibility(View.GONE);
//            inputText2.setVisibility(View.GONE);
//            inputText3.setVisibility(View.GONE);
//            inputText4.setVisibility(View.GONE);
//            inputText5.setVisibility(View.GONE);
//            inputText6.setVisibility(View.GONE);
//            inputText7.setVisibility(View.GONE);
//            memoRelative.setVisibility(View.GONE);
//            company.setVisibility(View.GONE);
//            jobTitle.setVisibility(View.GONE);
//            address.setVisibility(View.GONE);
//            contactChooseFromContact.setVisibility(View.GONE);
//            v4.setVisibility(View.GONE);
//            v5.setVisibility(View.GONE);
//            v6.setVisibility(View.GONE);
//            email_choose_btn.setVisibility(View.GONE);
//            eventRel.setVisibility(View.GONE);
//            spinner.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);
//
//

            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");
            inputText2.setVisibility(View.VISIBLE);
            inputText3.setVisibility(View.VISIBLE);
            recipientText.setVisibility(View.VISIBLE);
            inputText2.setLines(2);
            inputText.setLines(2);
            inputText3.setLines(2);
            messageText.setVisibility(View.VISIBLE);
            recipientText.setText(mActivity.getResources().getString(R.string.product_name));
            messageText.setText(mActivity.getResources().getString(R.string.product_code));
            appendUserName.setVisibility(View.GONE);
            choose_btn.setVisibility(View.GONE);
            inputText.setHint(R.string.product_name);

            inputText2.setHint(R.string.product_code);

            inputText3.setHint(R.string.product_price);
            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText2.setInputType(InputType.TYPE_CLASS_NUMBER);
            inputText3.setInputType(InputType.TYPE_CLASS_NUMBER);

            try {
                InputFilter[] editFilters = inputText.getFilters();
                InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
                System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
                newFilters[editFilters.length] = new InputFilter.LengthFilter(40);
                inputText.setFilters(newFilters);
            } catch (Exception e) {
                inputText.setText("");
            }

            try {
                InputFilter[] editFilters1 = inputText2.getFilters();
                InputFilter[] newFilters1 = new InputFilter[editFilters1.length + 1];
                System.arraycopy(editFilters1, 0, newFilters1, 0, editFilters1.length);
                newFilters1[editFilters1.length] = new InputFilter.LengthFilter(12);
                inputText2.setFilters(newFilters1);
            } catch (Exception e) {
                inputText2.setText("");
            }

            try {
                InputFilter[] editFilters2 = inputText3.getFilters();
                InputFilter[] newFilters2 = new InputFilter[editFilters2.length + 1];
                System.arraycopy(editFilters2, 0, newFilters2, 0, editFilters2.length);
                newFilters2[editFilters2.length] = new InputFilter.LengthFilter(12);
                inputText3.setFilters(newFilters2);
            } catch (Exception e) {
                inputText3.setText("");
            }


            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.VISIBLE);
            password.setText(mActivity.getResources().getString(R.string.price));
            v3.setVisibility(View.VISIBLE);
            hidden_network.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);


//            cardViewMap.setVisibility(View.GONE);

            qr_cat_name.setText(mActivity.getResources().getString(R.string.email));
            countryCode.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);

            qr_cat_name.setText("Product");
        } else if (barCodeCategory == 12) {

            Constants.format = BarcodeFormat.EAN_13;
            CURRENT_BTN = BARCODE;

            inputText.setText("");
            inputText2.setText("");
            inputText3.setText("");
            inputText4.setText("");
            inputText2.setVisibility(View.VISIBLE);
            inputText3.setVisibility(View.VISIBLE);
            recipientText.setVisibility(View.VISIBLE);
            inputText2.setLines(2);
            inputText.setLines(2);
            inputText3.setLines(2);
            messageText.setVisibility(View.VISIBLE);
            recipientText.setText(mActivity.getResources().getString(R.string.book_name_1));
            messageText.setText(mActivity.getResources().getString(R.string.book_code_1));
            appendUserName.setVisibility(View.GONE);
            choose_btn.setVisibility(View.GONE);
            inputText.setHint(R.string.book_name);

            inputText2.setHint(R.string.book_code);

            inputText3.setHint(R.string.book_price);
            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            inputText3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

            try {
                InputFilter[] editFilters = inputText.getFilters();
                InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
                System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
                newFilters[editFilters.length] = new InputFilter.LengthFilter(40);
                inputText.setFilters(newFilters);
            } catch (Exception exception) {

            }

            try {
                InputFilter[] editFilters1 = inputText2.getFilters();
                InputFilter[] newFilters1 = new InputFilter[editFilters1.length + 1];
                System.arraycopy(editFilters1, 0, newFilters1, 0, editFilters1.length);
                newFilters1[editFilters1.length] = new InputFilter.LengthFilter(12);
                inputText2.setFilters(newFilters1);
            } catch (Exception exception) {

            }

            try {

                InputFilter[] editFilters2 = inputText3.getFilters();
                InputFilter[] newFilters2 = new InputFilter[editFilters2.length + 1];
                System.arraycopy(editFilters2, 0, newFilters2, 0, editFilters2.length);
                newFilters2[editFilters2.length] = new InputFilter.LengthFilter(12);
                inputText3.setFilters(newFilters2);
            } catch (Exception exception) {

            }


            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
            linearLayoutWifi.setVisibility(View.GONE);
            password.setVisibility(View.VISIBLE);
            password.setText(mActivity.getResources().getString(R.string.book_price_1));
            v3.setVisibility(View.VISIBLE);
            hidden_network.setVisibility(View.GONE);
            inputText4.setVisibility(View.GONE);
            inputText5.setVisibility(View.GONE);
            inputText6.setVisibility(View.GONE);
            inputText7.setVisibility(View.GONE);
            memoRelative.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            jobTitle.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            contactChooseFromContact.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            email_choose_btn.setVisibility(View.GONE);
            eventRel.setVisibility(View.GONE);


//            cardViewMap.setVisibility(View.GONE);

//            qr_cat_name.setText(mActivity.getResources().getString(R.string.book_price_1));
            countryCode.setVisibility(View.GONE);
            inputWhatsapp.setVisibility(View.GONE);
            materialSpinner.setVisibility(View.GONE);
            qr_cat_name.setText("Product");

        }

//        if (qr_category_social == 1) {
//
//            CURRENT_BTN = FACEBOOK;
//            inputText.setText("");
//            inputText2.setText("");
//            inputText3.setText("");
//            inputText4.setText("");
//
//            choose_btn.setVisibility(View.GONE);
//            v1.setVisibility(View.GONE);
//            v2.setVisibility(View.GONE);
//            inputText.setHint(mActivity.getResources().getString(R.string.facebook_url));
//            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
//            inputText.setLines(6);
//            appendUserName.setText("facebook://user?username=");
//            appendUserName.setVisibility(View.GONE);
//            inputText.setVisibility(View.VISIBLE);
//            inputText2.setVisibility(View.GONE);
//            inputText3.setVisibility(View.GONE);
//            inputText4.setVisibility(View.GONE);
//            inputText5.setVisibility(View.GONE);
//            inputText6.setVisibility(View.GONE);
//            inputText7.setVisibility(View.GONE);
//            memoRelative.setVisibility(View.GONE);
//            company.setVisibility(View.GONE);
//            jobTitle.setVisibility(View.GONE);
//            address.setVisibility(View.GONE);
//            recipientText.setVisibility(View.GONE);
//            messageText.setVisibility(View.GONE);
//            linearLayoutWifi.setVisibility(View.GONE);
//            password.setVisibility(View.GONE);
//            v3.setVisibility(View.GONE);
//            hidden_network.setVisibility(View.GONE);
//            email_choose_btn.setVisibility(View.GONE);
//            contactChooseFromContact.setVisibility(View.GONE);
//            v4.setVisibility(View.GONE);
//            v5.setVisibility(View.GONE);
//            v6.setVisibility(View.GONE);
//            eventRel.setVisibility(View.GONE);
//            ccp.setVisibility(View.GONE);
//            inputWhatsapp.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);
//            qr_cat_name.setText(mActivity.getResources().getString(R.string.result_facebook));
//            spinner.setVisibility(View.VISIBLE);
//            inputText.setText("https://www.facebook.com/profile.php?id=");
//            spinner.setItems("https://www.facebook.com/profile.php?id=", "https://www.facebook.com/", "https://www.facebook.com/groups/");
//
//
//        } else if (qr_category_social == 2) {
//            CURRENT_BTN = TWITTER;
//            inputText.setText("");
//            inputText2.setText("");
//            inputText3.setText("");
//            inputText4.setText("");
//
//            choose_btn.setVisibility(View.GONE);
//            v1.setVisibility(View.GONE);
//            v2.setVisibility(View.GONE);
//            inputText.setHint(R.string.twitter_url);
//            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
//            inputText.setLines(6);
//            inputText.setVisibility(View.VISIBLE);
//            inputText2.setVisibility(View.GONE);
//            inputText3.setVisibility(View.GONE);
//            inputText4.setVisibility(View.GONE);
//            inputText5.setVisibility(View.GONE);
//            inputText6.setVisibility(View.GONE);
//            inputText7.setVisibility(View.GONE);
//            memoRelative.setVisibility(View.GONE);
//            appendUserName.setText("https://twitter.com/");
//            appendUserName.setVisibility(View.GONE);
//            company.setVisibility(View.GONE);
//            jobTitle.setVisibility(View.GONE);
//            address.setVisibility(View.GONE);
//            recipientText.setVisibility(View.GONE);
//            messageText.setVisibility(View.GONE);
//            linearLayoutWifi.setVisibility(View.GONE);
//            password.setVisibility(View.GONE);
//            v3.setVisibility(View.GONE);
//            hidden_network.setVisibility(View.GONE);
//            email_choose_btn.setVisibility(View.GONE);
//            contactChooseFromContact.setVisibility(View.GONE);
//            v4.setVisibility(View.GONE);
//            v5.setVisibility(View.GONE);
//            v6.setVisibility(View.GONE);
//            eventRel.setVisibility(View.GONE);
//            inputWhatsapp.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);
//            qr_cat_name.setText(mActivity.getResources().getString(R.string.result_twitter));
//            ccp.setVisibility(View.GONE);
//            inputText.setText("https://twitter.com/");
//            spinner.setVisibility(View.VISIBLE);
//            spinner.setItems("https://twitter.com/");
//        } else if (qr_category_social == 3) {
//            CURRENT_BTN = LINKDEIN;
//            inputText.setText("");
//            inputText2.setText("");
//            inputText3.setText("");
//            inputText4.setText("");
//
//            choose_btn.setVisibility(View.GONE);
//            v1.setVisibility(View.GONE);
//            v2.setVisibility(View.GONE);
//            inputText.setHint(R.string.linkdein_url);
//            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
//            inputText.setLines(6);
//            appendUserName.setText("linkdein://user?username=");
//            appendUserName.setVisibility(View.GONE);
//            inputText.setVisibility(View.VISIBLE);
//            inputText2.setVisibility(View.GONE);
//            inputText3.setVisibility(View.GONE);
//            inputText4.setVisibility(View.GONE);
//            inputText5.setVisibility(View.GONE);
//            inputText6.setVisibility(View.GONE);
//            inputText7.setVisibility(View.GONE);
//            memoRelative.setVisibility(View.GONE);
//            company.setVisibility(View.GONE);
//            jobTitle.setVisibility(View.GONE);
//            address.setVisibility(View.GONE);
//            recipientText.setVisibility(View.GONE);
//            messageText.setVisibility(View.GONE);
//            linearLayoutWifi.setVisibility(View.GONE);
//            password.setVisibility(View.GONE);
//            v3.setVisibility(View.GONE);
//            hidden_network.setVisibility(View.GONE);
//            email_choose_btn.setVisibility(View.GONE);
//            contactChooseFromContact.setVisibility(View.GONE);
//            v4.setVisibility(View.GONE);
//            v5.setVisibility(View.GONE);
//            v6.setVisibility(View.GONE);
//            eventRel.setVisibility(View.GONE);
//            inputWhatsapp.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);
//            qr_cat_name.setText(mActivity.getResources().getString(R.string.result_linkdein));
//            ccp.setVisibility(View.GONE);
//            inputText.setText("https://www.linkedin.com/in/");
//            spinner.setVisibility(View.VISIBLE);
//            spinner.setItems("https://www.linkedin.com/in/", "https://www.linkedin.com/feed/", "https://www.linkedin.com/company/", "https://www.linkedin.com/hiring/jobs/");
//        } else if (qr_category_social == 4) {
//            CURRENT_BTN = INSTAGRAM;
//            inputText.setText("");
//            inputText2.setText("");
//            inputText3.setText("");
//            inputText4.setText("");
//
//            choose_btn.setVisibility(View.GONE);
//            v1.setVisibility(View.GONE);
//            v2.setVisibility(View.GONE);
//            inputText.setHint(R.string.username);
//            inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
//            inputText.setLines(6);
//            appendUserName.setText("instagram://user?username=");
//            appendUserName.setVisibility(View.GONE);
//            inputText.setVisibility(View.VISIBLE);
//            inputText2.setVisibility(View.GONE);
//            inputText3.setVisibility(View.GONE);
//            inputText4.setVisibility(View.GONE);
//            inputText5.setVisibility(View.GONE);
//            inputText6.setVisibility(View.GONE);
//            inputText7.setVisibility(View.GONE);
//            memoRelative.setVisibility(View.GONE);
//            company.setVisibility(View.GONE);
//            jobTitle.setVisibility(View.GONE);
//            address.setVisibility(View.GONE);
//            recipientText.setVisibility(View.GONE);
//            messageText.setVisibility(View.GONE);
//            linearLayoutWifi.setVisibility(View.GONE);
//            password.setVisibility(View.GONE);
//            v3.setVisibility(View.GONE);
//            hidden_network.setVisibility(View.GONE);
//            email_choose_btn.setVisibility(View.GONE);
//            contactChooseFromContact.setVisibility(View.GONE);
//            v4.setVisibility(View.GONE);
//            v5.setVisibility(View.GONE);
//            v6.setVisibility(View.GONE);
//            eventRel.setVisibility(View.GONE);
//            inputWhatsapp.setVisibility(View.GONE);
//            cardViewMap.setVisibility(View.GONE);
//            qr_cat_name.setText(mActivity.getResources().getString(R.string.result_instagram));
//            ccp.setVisibility(View.GONE);
//            inputText.setText("https://www.instagram.com/");
//            spinner.setVisibility(View.VISIBLE);
//            spinner.setItems("https://www.instagram.com/");
//        } else if (qr_category_social == 5) {
//
//            CURRENT_BTN = WHATSAPP;
//            inputText.setText("");
//            inputText2.setText("");
//            inputText3.setText("");
//            inputText4.setText("");
//
//            choose_btn.setVisibility(View.GONE);
//            v1.setVisibility(View.GONE);
//            v2.setVisibility(View.GONE);
//            inputText.setVisibility(View.GONE);
//            inputText2.setVisibility(View.GONE);
//            inputText3.setVisibility(View.GONE);
//            inputText4.setVisibility(View.GONE);
//            inputText5.setVisibility(View.GONE);
//            inputText6.setVisibility(View.GONE);
//            inputText7.setVisibility(View.GONE);
//            memoRelative.setVisibility(View.GONE);
//            company.setVisibility(View.GONE);
//            appendUserName.setVisibility(View.GONE);
//            jobTitle.setVisibility(View.GONE);
//            address.setVisibility(View.GONE);
//            recipientText.setText(mActivity.getResources().getString(R.string.result_whatsapp));
//            recipientText.setVisibility(View.VISIBLE);
//            messageText.setVisibility(View.GONE);
//            linearLayoutWifi.setVisibility(View.GONE);
//            password.setVisibility(View.GONE);
//            v3.setVisibility(View.GONE);
//            hidden_network.setVisibility(View.GONE);
//            email_choose_btn.setVisibility(View.GONE);
//            contactChooseFromContact.setVisibility(View.GONE);
//            v4.setVisibility(View.GONE);
//            v5.setVisibility(View.GONE);
//            v6.setVisibility(View.GONE);
//            eventRel.setVisibility(View.GONE);
//            inputWhatsapp.setVisibility(View.VISIBLE);
//            inputWhatsapp.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
//            inputWhatsapp.setLines(5);
//            cardViewMap.setVisibility(View.GONE);
//            qr_cat_name.setText(mContext.getResources().getString(R.string.result_whatsapp));
//            ccp.setVisibility(View.VISIBLE);
//            spinner.setVisibility(View.GONE);
//        }


    }

    private void FetchLocation() {


        loadLocation();
        CheckGpsStatus();
        if (GpsStatus) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.getFusedLocationProviderClient(GenerateActivity.this)
                    .requestLocationUpdates(locationRequest, new LocationCallback() {

                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                    .removeLocationUpdates(this);
                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                int latestlocIndex = locationResult.getLocations().size() - 1;
                                lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                                longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
                                latitude = String.valueOf(lati);
                                longitude = String.valueOf(longi);
                                inputText.setText(longitude);
                                inputText2.setText(latitude);

                                LocationOnMap();

                                if (isNetworkConnected()) {

                                    try {
                                        Location location = new Location("providerNA");
                                        location.setLongitude(longi);
                                        location.setLatitude(lati);
                                        String address = getAddress(mContext, location.getLatitude(), location.getLongitude());
                                        inputText3.setVisibility(View.VISIBLE);
                                        inputText3.setText(address);
                                        password.setVisibility(View.VISIBLE);
                                        password.setText(mActivity.getResources().getString(R.string.address));
                                        v3.setVisibility(View.VISIBLE);
                                        refresh.setVisibility(View.GONE);
                                    } catch (Exception ignored) {

                                    }

                                } else {

                                    Toast.makeText(mActivity, "Network Error", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }, Looper.getMainLooper());


        }

    }

    protected void loadLocation() {

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        CheckGpsStatus();
                        if (GpsStatus) {
                            LocationRequest locationRequest = new LocationRequest();
                            locationRequest.setInterval(10000);
                            locationRequest.setFastestInterval(3000);
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                            if (ActivityCompat.checkSelfPermission(GenerateActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GenerateActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            LocationServices.getFusedLocationProviderClient(GenerateActivity.this)
                                    .requestLocationUpdates(locationRequest, new LocationCallback() {

                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                                            super.onLocationResult(locationResult);
                                            LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                                    .removeLocationUpdates(this);
                                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                                int latestlocIndex = locationResult.getLocations().size() - 1;
                                                lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                                                longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
                                                latitude = String.valueOf(lati);
                                                longitude = String.valueOf(longi);
                                                inputText.setText(longitude);
                                                inputText2.setText(latitude);
                                                LocationOnMap();
                                                if (isNetworkConnected()) {

                                                    try {
                                                        Location location = new Location("providerNA");
                                                        location.setLongitude(longi);
                                                        location.setLatitude(lati);

                                                        String address = getAddress(mContext, location.getLatitude(), location.getLongitude());
//                                                        inputLayout3.setVisibility(View.VISIBLE);

                                                        inputText3.setText(address);
                                                    } catch (Exception ignored) {

                                                    }


                                                } else {
                                                    Toast.makeText(mActivity, "Network Error", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }, Looper.getMainLooper());


                        } else {
                            enableLoc();
                        }

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError dexterError) {
//                        Log.e("Dexter", "There was an error: " + dexterError.toString());
                    }
                })
                .check();


    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(GenerateActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                            Toast.makeText(mActivity, "Location Error", Toast.LENGTH_SHORT).show();
//                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(mActivity, REQUEST_LOCATION);

//                                finish();
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                    }
                }
            });
        }
    }

    public void CheckGpsStatus() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {

        }

    }

    public String getAddress(Context ctx, double lat, double lng) {
        String fullAdd = "";
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                fullAdd = address.getAddressLine(0);


                // if you want only city or pin code use following code //
           /* String Location = address.getLocality();
            String zip = address.getPostalCode();
            String Country = address.getCountryName(); */
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fullAdd;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        try {
//            Log.d("Scroll","scrolling");
            View currentFocus = getCurrentFocus();
            if (currentFocus != null) {
                currentFocus.clearFocus();
            }
        } catch (Exception ignored) {

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (backgroundExecutor != null) {
            backgroundExecutor.shutdown();
        }
    }

    void LocationOnMap() {
        if (map != null) {
            LatLng TutorialsPoint = new LatLng(lati, longi);

            // Add a marker in Sydney and move the camera
            MarkerOptions markerOptions = new MarkerOptions().position(TutorialsPoint).title("You are here...!!");
//                                    animateMarker(markerOptions.getIcon(),latLng,false);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(TutorialsPoint, 17);


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);
            map.addMarker(markerOptions);
            map.animateCamera(cameraUpdate);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


    }
}