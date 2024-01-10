package com.bezruk.qrcodebarcode.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bezruk.qrcodebarcode.R;
import com.bezruk.qrcodebarcode.activity.ResultActivity;
import com.bezruk.qrcodebarcode.data.constant.Constants;
import com.bezruk.qrcodebarcode.data.preference.AppPreference;
import com.bezruk.qrcodebarcode.data.preference.PrefKey;
import com.bezruk.qrcodebarcode.utility.ActivityUtils;
import com.bezruk.qrcodebarcode.utility.AdManager;
import com.bezruk.qrcodebarcode.utility.AppUtils;
import com.bezruk.qrcodebarcode.utility.CustomScannerView;
import com.bezruk.qrcodebarcode.utility.ResultOfTypeAndValue;
import com.bezruk.qrcodebarcode.zxing.ZXingScannerView;
import com.google.android.gms.ads.AdListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler  {

    private Activity mActivity;
    private Context mContext;

    private ViewGroup contentFrame;
    private CustomScannerView zXingScannerView;
    private ArrayList<Integer> mSelectedIndices;
    private ImageView flash, gallery, camera;
    private boolean isFlash = false;
    private boolean isAutoFocus = false;
    private boolean ActivateScanner = true;
    private int camId, frontCamId, rearCamId;
    MediaPlayer beepSound;
    private Button allowCameraUseBtn;


    public static final int PICK_IMAGE = 1;
    String resultStr, type_of_code;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
        Log.d("CRRRRE", "create;");

        setupFormats();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);

        initView(rootView);
        initListener();

        return rootView;
    }

    private void initVar() {
        mActivity = getActivity();
        mContext = mActivity.getApplicationContext();

        isFlash = AppPreference.getInstance(mContext).getBoolean(PrefKey.FLASH, false); // flash off by default

       //check permission and load cam();
        checkCameraPermission();

        //full ads load
        AdManager.getInstance(mContext).loadFullScreenAd(mActivity);
    }

    private void initView(View rootView) {
        contentFrame = rootView.findViewById(R.id.content_frame);

        flash = rootView.findViewById(R.id.flash);
        gallery = rootView.findViewById(R.id.gallery);
        camera = rootView.findViewById(R.id.camera);

        allowCameraUseBtn = rootView.findViewById(R.id.allowCameraUseBtn);
        initConfigs();

    }


    private void initListener() {

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFlash();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryImage();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCamera();
            }
        });

        allowCameraUseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });
    }

    @Override
    public void handleResult(Result result) {
        if (ActivateScanner) {

            //save result
            type_of_code = (result.getBarcodeFormat().toString());
            if (type_of_code.contains("AZTEC") || type_of_code.contains("PDF_417") ||
                    type_of_code.contains("DATA_MATRIX") || type_of_code.contains("QR_CODE")) {
                resultStr = result.getText();//qr_code
            } else {
                resultStr = "barcode:" + result.getText();//barcode
            }
            savingResults(resultStr);

            zXingScannerView.resumeCameraPreview(this);
        } //else zXingScannerView.resumeCameraPreview(this);
    }

    private void initializeScanner(){
        zXingScannerView = new CustomScannerView(mActivity);
        zXingScannerView.setSquareViewFinder(true);
        zXingScannerView.setResultHandler(this);

        loadCams();
        camId = rearCamId;

        activateScanner();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Constants.PERMISSION_REQ) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.CAMERA)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        allowCameraUseBtn.setVisibility(View.GONE);
                        checkCameraPermission();
                    } else {
                        AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
                        allowCameraUseBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private void checkCameraPermission() {
        if ((ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
           requestPermissions(new String[]{Manifest.permission.CAMERA},
                    Constants.PERMISSION_REQ);
        } else {
            initializeScanner();
        }
    }

    private void activateScanner() {
        try {
            zXingScannerView.stopCamera();
        } catch (Exception ignored) {
        }
        try {
            if (zXingScannerView != null) {

                if (zXingScannerView.getParent() != null) {
                    ((ViewGroup) zXingScannerView.getParent()).removeView(zXingScannerView); // to prevent crush on re adding view
                }

                try { //for slow devices and old android versions, remove transition animation when changing cameras and remove view
                    contentFrame.setLayoutTransition(null);
                    contentFrame.removeAllViews();
                } catch (Exception ignored) {
                }
                contentFrame.addView(zXingScannerView);

                try {
                    zXingScannerView.startCamera(camId);
                /*zXingScannerView.setFlash(isFlash);
                zXingScannerView.setAutoFocus(isAutoFocus);*/
                } catch (Exception e) {
                    Toast.makeText(mContext, getResources().getString(R.string.error_restart_app), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<>();
            for (int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for (int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if (zXingScannerView != null) {
            zXingScannerView.setFormats(formats);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activateScanner();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (zXingScannerView != null) {
                zXingScannerView.stopCamera();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (zXingScannerView != null) {
            if (visible) {
                zXingScannerView.setFlash(isFlash);
                ActivateScanner = true;
            } else {
                zXingScannerView.setFlash(false);
                ActivateScanner = false;
            }
        }
    }

    private void toggleFlash() {
        if (isFlash) {
            isFlash = false;
            flash.setImageResource(R.drawable.ic_flash_on);
        } else {
            isFlash = true;
            flash.setImageResource(R.drawable.ic_flash_off);
        }
        AppPreference.getInstance(mContext).setBoolean(PrefKey.FLASH, isFlash);
        zXingScannerView.setFlash(isFlash);
    }

    private void galleryImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    //For gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    //doing some uri parsing
                    Uri selectedImage = imageReturnedIntent.getData();
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
                        Bitmap bMap = BitmapFactory.decodeStream(imageStream, null, options); //get image with options

                        //Convert Bitmap to ByteArray
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        Reader reader = new MultiFormatReader();// use this otherwise

                        Map<DecodeHintType, Object> tmpHintsMap = new EnumMap<>(DecodeHintType.class);
                        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); //thorough search Qrcode

                        //rotate image if nothing found
                        Result result = null;
                        for (int i = 0; i < 8; i++) {
                            BinaryBitmap bitmap = binaryBitmapFromJpegData(byteArray, i * 45);
                            try {
                                //get result
                                result = reader.decode(bitmap, tmpHintsMap);
                                break;
                            } catch (NotFoundException e) {
                                if (i == 7) {
                                    Toast.makeText(mContext, getResources()
                                            .getString(R.string.error_nothing_find), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        //save result
                        type_of_code = (result.getBarcodeFormat().toString());
                        if (type_of_code.contains("AZTEC") || type_of_code.contains("PDF_417") ||
                                type_of_code.contains("DATA_MATRIX") || type_of_code.contains("QR_CODE")) {
                            resultStr = result.getText();//qr_code
                        } else {
                            resultStr = "barcode:" + result.getText();//barcode
                        }
                        savingResults(resultStr);

                    } catch (ChecksumException e) {
                        Toast.makeText(mContext, getResources().getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (FormatException e) {
                        Toast.makeText(mContext, getResources().getString(R.string.error_format), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (Exception e) {
                        Toast.makeText(mContext, getResources().getString(R.string.error_nothing_find), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
        }
    }

    private BinaryBitmap binaryBitmapFromJpegData(byte[] data, int rotation) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        if (rotation != 0) {
            bitmap = rotateBitmap(bitmap, rotation);
        }

        int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);

        return new BinaryBitmap(new HybridBinarizer(source));
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    void savingResults(final String resultStr) {
       // boolean isCopy = AppPreference.getInstance(mContext).getBoolean(PrefKey.SETTINGS_COPY, false);
       // boolean isSound = AppPreference.getInstance(mContext).getBoolean(PrefKey.SETTINGS_SOUND, false);
        boolean isVibrate = AppPreference.getInstance(mContext).getBoolean(PrefKey.SETTINGS_VIBRATE, true);
        final boolean isAutoOpenURL = AppPreference.getInstance(mContext).getBoolean(PrefKey.SETTINGS_AUTO_URL, false);

        //vibrate
        vibrate(isVibrate);
        //sound
       // sound(isSound);

        //saving result
        ArrayList<String> previousResult = AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_SCANNED);
        previousResult.add(this.resultStr);
        AppPreference.getInstance(mContext).setStringArray(PrefKey.RESULT_LIST_OF_SCANNED, previousResult);

        //Copy to clipboard
       // copyToClipboard(isCopy);

        //save date of scan
        String currentDate;
        if (Locale.getDefault().equals(Locale.US)) {
            currentDate = new SimpleDateFormat("MM.dd.yyyy HH:mm").format(Calendar.getInstance().getTime());
        } else {
            currentDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
        }
        ArrayList<String> previousDate = AppPreference.getInstance(mContext).getStringArray(PrefKey.DATE_LIST_OF_SCANNED);
        previousDate.add(currentDate);
        AppPreference.getInstance(mContext).setStringArray(PrefKey.DATE_LIST_OF_SCANNED, previousDate);

        //saving color (standart black)
        ArrayList<String> previousColor = AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_SCANNED);
        previousColor.add(Integer.toString(Color.BLACK));
        AppPreference.getInstance(mContext).setStringArray(PrefKey.COLOR_LIST_OF_SCANNED, previousColor);

        // TODO Sample fullscreen Ad implementation
        // fullscreen ad show
        if(AdManager.getInstance(mContext).showFullScreenAd()){
            AdManager.getInstance(mContext).getInterstitialAd().setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    //full ads load
                    AdManager.getInstance(mContext).loadFullScreenAd(mActivity);
                    ActivityUtils.getInstance().invokeActivity(mActivity, ResultActivity.class, false, Constants.SCAN_FRAGMENT, 0);
                    //Auto Open URL
                    autoOpenUrl(isAutoOpenURL, resultStr);
                }
            });
        } else {
            ActivityUtils.getInstance().invokeActivity(mActivity, ResultActivity.class, false, Constants.SCAN_FRAGMENT, 0);
            //Auto Open URL
            autoOpenUrl(isAutoOpenURL, resultStr);
        }
    }

    private void vibrate(boolean isVibrate) {
        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (isVibrate) {
            assert v != null;
            v.vibrate(100);
        }
    }

 /*   private void sound(boolean isSound) {
        if (isSound) {
            beepSound = MediaPlayer.create(mActivity, R.raw.beep); //For beep sound
            beepSound.start();
        }
    }*/

   /* private void copyToClipboard(boolean isCopy) {
        //Copy to clipboard
        if (isCopy) {
            ResultOfTypeAndValue resultValues = AppUtils.getResourceType(this.resultStr,mActivity);
            String finalResult = resultValues.getValue().toString();
            AppUtils.copyToClipboard(mContext, finalResult);
        }
    }*/

    private void autoOpenUrl(boolean isAutoOpenURL, String resultStr) {
        //Auto Open URL
        ResultOfTypeAndValue resultValues = AppUtils.getResourceType(resultStr);
        if (resultValues.getType() == Constants.TYPE_WEB && isAutoOpenURL) {
            //If this result get from the QR code is an URL, open the browse.
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            android.net.Uri content_url = android.net.Uri.parse(resultValues.getValue());
            intent.setData(content_url);
            startActivity(intent);
        }
    }

    private void toggleCamera() {
        if (camId == rearCamId) {
            camId = frontCamId;
        } else {
            camId = rearCamId;
        }
        flash.setImageResource(R.drawable.ic_flash_on);
        AppPreference.getInstance(mContext).setBoolean(PrefKey.FLASH, false);
        isFlash = false;
        activateScanner();
    }

    private void initConfigs() {
        if (isFlash) {
            flash.setImageResource(R.drawable.ic_flash_off);
        } else {
            flash.setImageResource(R.drawable.ic_flash_on);
        }
       /* if (isAutoFocus) {
            focus.setImageResource(R.drawable.ic_focus_off);
        } else {
            focus.setImageResource(R.drawable.ic_focus_on);
        }*/
    }

    private void loadCams() {
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontCamId = i;
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                rearCamId = i;
            }
        }
        AppPreference.getInstance(mContext).setInteger(PrefKey.CAM_ID, rearCamId);
    }

}
