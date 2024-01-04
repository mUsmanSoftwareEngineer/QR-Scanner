package scanner.app.scan.qrcode.reader.fragment;

import static android.app.Activity.RESULT_CANCELED;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.activity.CropActivity;
import scanner.app.scan.qrcode.reader.activity.HelpActivity;
import scanner.app.scan.qrcode.reader.activity.ResultActivity;
import scanner.app.scan.qrcode.reader.data.constant.Constants;
import scanner.app.scan.qrcode.reader.data.preference.AppPreference;
import scanner.app.scan.qrcode.reader.data.preference.PrefKey;
import scanner.app.scan.qrcode.reader.utility.ActivityUtils;
import scanner.app.scan.qrcode.reader.utility.AppUtils;
import scanner.app.scan.qrcode.reader.utility.ResultOfTypeAndValue;


public class ScanFragment extends Fragment implements scanner.app.scan.qrcode.reader.zxing.ZXingScannerView.ResultHandler {

    public static final int PICK_IMAGEScannerGallery = 1;
    private final boolean isAutoFocus = false;
    IndicatorSeekBar seekBarZoom;
    String resultStr, type_of_code;
    ScheduledExecutorService backgroundExecutor;
    Handler handler;
    Lock mLock = new ReentrantLock();
    Activity mActivity;
    Context mContext;
    ViewGroup contentFrame;
    scanner.app.scan.qrcode.reader.zxing.ZXingScannerView mScannerView;
    ArrayList<Integer> mSelectedIndices;
    ImageView flash, gallery, camera, help;
    boolean isFlash = false;
    boolean ActivateScanner = true;
    int camId, frontCamId, rearCamId;
    Button allowCameraUseBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        initVar();
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
        mActivity = requireActivity();
        mContext = mActivity.getApplicationContext();
//        handler = new Handler();

        //check permission and load cam();
        checkCameraPermission();

    }

    private void initView(View rootView) {
        contentFrame = rootView.findViewById(R.id.content_frame);

        flash = rootView.findViewById(R.id.flash);
        gallery = rootView.findViewById(R.id.gallery);
        camera = rootView.findViewById(R.id.camera);
        seekBarZoom = rootView.findViewById(R.id.seekBarZoom);

        allowCameraUseBtn = rootView.findViewById(R.id.allowCameraUseBtn);
        help = rootView.findViewById(R.id.help);

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


//                if(checkWritePermission()){
//                    galleryImage();
//                }


                try {
                    galleryImage();
                    mScannerView.setFlash(false);
                    flash.setImageResource(R.drawable.flash_off_scanner);
                    isFlash = false;
                } catch (Exception ignored) {

                }

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

        seekBarZoom.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onDragging(float progress) {

                try {
                    mScannerView.setZoom(progress);
                } catch (Exception ignored) {

                }


            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HelpActivity.class);
                startActivity(intent);
            }
        });


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
    public void handleResult(Result result) {
        if (ActivateScanner) {

            //save result
            try {
                type_of_code = (result.getBarcodeFormat().toString());
                if (type_of_code.contains("AZTEC") || type_of_code.contains("PDF_417") ||
                        type_of_code.contains("DATA_MATRIX") || type_of_code.contains("QR_CODE")) {
                    resultStr = result.getText();//qr_code
                    savingResults(resultStr);
                } else if (type_of_code.contains("CODE_39") || type_of_code.contains("CODE_128") || type_of_code.contains("CODABAR") || type_of_code.contains("CODE_93") || type_of_code.contains("EAN_8") || type_of_code.contains("EAN_13") || type_of_code.contains("ITF") || type_of_code.contains("UPC_A") || type_of_code.contains("UPC_E") || type_of_code.contains("UPC_EAN_EXTENSION") || type_of_code.contains("RSS_14") || type_of_code.contains("RSS_EXPANDED") || type_of_code.contains("MAXICODE")) {
                    resultStr = "barCodeType:" + type_of_code + ";" + "barcode:" + result.getText() + ";";//barcode
                    savingResults(resultStr);
                } else {
                    Toast.makeText(mActivity, "Code not found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(mActivity, "Code not found", Toast.LENGTH_SHORT).show();
            }


            backgroundExecutor = Executors.newSingleThreadScheduledExecutor();

            backgroundExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    // Your code logic goes here
                    mScannerView.resumeCameraPreview(ScanFragment.this);

                }
            }, 200, TimeUnit.MILLISECONDS);

//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
////                    mScannerView.resumeCameraPreview(ScanFragment.this);
////                    Toast.makeText(mContext, "how many times", Toast.LENGTH_SHORT).show();
//                    mScannerView.resumeCameraPreview(ScanFragment.this);
//                }
//            }, 200);
//            mScannerView.resumeCameraPreview(this);
        }
//        else {
//            mScannerView.resumeCameraPreview(this);
//        }
    }

    private void initializeScanner() {

//        mScannerView = new app.scanqrcode.qrcodegenerator.zxing.ZXingScannerView(mActivity);
//        mScannerView.setSquareViewFinder(true);
//        mScannerView.setResultHandler((app.scanqrcode.qrcodegenerator.zxing.ZXingScannerView.ResultHandler) this);

        try {
            mScannerView = new scanner.app.scan.qrcode.reader.zxing.ZXingScannerView(getActivity());
            mScannerView.setResultHandler(this);
        } catch (Exception ignored) {

        }

//        mScannerView.setAutoFocus(true);

        loadCams();
        camId = rearCamId;

        activateScanner();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == Constants.PERMISSION_REQ) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.CAMERA)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        allowCameraUseBtn.setVisibility(View.GONE);
//                        checkCameraPermission();
                        initializeScanner();
                    } else {
                        AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
                        allowCameraUseBtn.setVisibility(View.VISIBLE);
                    }
                }

//                else if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
//
//                        galleryImage();
//
////                        saveAndShare(shouldShare, inputStr, finalBitmap);
//
//                    } else {
//                        AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
//                    }
//                }

            }
        }

//        if (requestCode == PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE) {
//            for (int grantResult : grantResults) {
//                if (grantResult == PackageManager.PERMISSION_GRANTED) {
//
//                    PictureFileUtils.deleteCacheDirFile(requireContext(), PictureMimeType.ofImage());
//                } else {
//                    Toast.makeText(getContext(),
//                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
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
            mScannerView.stopCamera();
        } catch (Exception ignored) {
//            Log.d("checkException",ignored.toString());
        }
        try {
            if (mScannerView != null) {

                if (mScannerView.getParent() != null) {
                    ((ViewGroup) mScannerView.getParent()).removeView(mScannerView); // to prevent crush on re adding view
                }

                try { //for slow devices and old android versions, remove transition animation when changing cameras and remove view
                    contentFrame.setLayoutTransition(null);
                    contentFrame.removeAllViews();
                } catch (Exception ignored) {

                }

                try {
                    contentFrame.addView(mScannerView);
                } catch (Exception ignored) {

                }


                try {
                    mScannerView.startCamera(camId);


                /*zXingScannerView.setFlash(isFlash);
                zXingScannerView.setAutoFocus(isAutoFocus);*/
                } catch (Exception e) {
                    Toast.makeText(mContext, getResources().getString(R.string.error_restart_app), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<>();
            for (int i = 0; i < scanner.app.scan.qrcode.reader.zxing.ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for (int index : mSelectedIndices) {
            formats.add(scanner.app.scan.qrcode.reader.zxing.ZXingScannerView.ALL_FORMATS.get(index));
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d("callbacks","onResume called");
        if (mLock.tryLock()) {
            try {
                initializeScanner();
                activateScanner();
            } finally {
                mLock.unlock();
            }
        }

    }


    @Override
    public void onPause() {
        super.onPause();
//        Log.d("callbacks", "onPause called");

        try {
//            if(handler!=null){
//                handler.removeCallbacks(null);
//            }
            if (backgroundExecutor != null) {
                backgroundExecutor.shutdown();
            }

            if (mScannerView != null) {
                mScannerView.stopCamera();
                seekBarZoom.setProgress(0);
                flash.setImageResource(R.drawable.flash_off_scanner);
                mScannerView.setFlash(false);
                isFlash = false;
            }

        } catch (Exception e) {
//            Toast.makeText(mContext,e.toString(),Toast.LENGTH_LONG).show();
//            Log.d("checkExc",e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (mScannerView != null) {
            if (visible) {
                mScannerView.setFlash(isFlash);
                ActivateScanner = true;
            } else {
                mScannerView.setFlash(false);
                ActivateScanner = false;
            }
        }
    }

    public void toggleFlash() {
        if (isFlash) {
            isFlash = false;
            flash.setImageResource(R.drawable.flash_off_scanner);
        } else {
            isFlash = true;
            flash.setImageResource(R.drawable.flash_on_scanner);
        }
//        AppPreference.getInstance(mContext).setBoolean(PrefKey.FLASH, isFlash);
        if (mScannerView != null) {
            mScannerView.setFlash(isFlash);
        }

    }

    private void galleryImage() {


        Intent intent = new Intent(Intent.ACTION_PICK);
        //set intent type to image
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGEScannerGallery);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

//        try {
//            PictureSelector.create(ScanFragment.this)
//                    .openGallery(PictureMimeType.ofImage())
//                    .isCamera(false)
//                    .imageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
//                    .selectionMode(PictureConfig.SINGLE)
//                    .previewImage(false)
//                    .compress(true)
//                    .forResult(PictureConfig.CHOOSE_REQUEST);
//        } catch (Exception ignored) {
//
//        }

//        Intent intent = new Intent(Intent.ACTION_PICK);
//        //set intent type to image
//        intent.setType("image/*");
////        galleryActivityResultLauncher.launch(intent);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


    }

    //For gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode != RESULT_CANCELED) {
//            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
//                // onResult Callback
//
//
//                if (imageReturnedIntent != null) {
//
//                    List<LocalMedia> result = PictureSelector.obtainMultipleResult(imageReturnedIntent);
//
//
//                    try {
//                        if (result.size() == 1) {
////                    Toast.makeText(mActivity, result.get(0).getPath()+"", Toast.LENGTH_SHORT).show();
//                            Uri selectedImage = Uri.parse(result.get(0).getPath());
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                InputStream baseStream;
//                                InputStream imageStream;
//                                try {
////                            Toast.makeText(mActivity, selectedImage+"", Toast.LENGTH_SHORT).show();
//                                    //getting the image
//                                    baseStream = mContext.getContentResolver().openInputStream(selectedImage);
//                                    imageStream = mContext.getContentResolver().openInputStream(selectedImage);
//                                } catch (FileNotFoundException e) {
//                                    Toast.makeText(mContext, getResources().getString(R.string.error_file), Toast.LENGTH_SHORT).show();
//                                    e.printStackTrace();
//                                    return;
//                                }
//
//                                // ChecksumException
//                                try {
////                        Bitmap myBitmap = BitmapFactory.decodeFile(result.get(0).getPath());
//                                    //decoding bitmap for get width
//                                    Bitmap bMap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), selectedImage);
//                                    Bitmap base = BitmapFactory.decodeStream(baseStream);
//                                    //set options for resize image
//                                    BitmapFactory.Options options = new BitmapFactory.Options();
//                                    options.inSampleSize = base.getWidth() / 1000; //get compress coef  (set image px size (compressing big image))
//
//                                    //decoding bitmap
//                                    bMap = BitmapFactory.decodeStream(imageStream, null, options); //get image with options
//
//                                    Constants.galleryBitmap = bMap;
//
//                                    startActivity(new Intent(mContext, CropActivity.class));
//
//
//                                    result.clear();
//
//                                } catch (Exception e) {
//                                    Toast.makeText(mContext, getResources().getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                try {
//
//                                    Constants.galleryBitmap = BitmapFactory.decodeFile(result.get(0).getPath());
//
//                                    startActivity(new Intent(mContext, CropActivity.class));
//
//
//                                    result.clear();
//
//
//                                } catch (Exception e) {
//                                    Toast.makeText(mContext, getResources().getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        }
//                    } catch (Exception ignored) {
//
//
//                    }
//
//                }
//
//
//            } else

                if (requestCode == PICK_IMAGEScannerGallery) {
                if (imageReturnedIntent != null) {

                    Uri selectedImage = null;
                    try {
                        selectedImage = imageReturnedIntent.getData();
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
                        Bitmap bMap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), selectedImage);

                        //set options for resize image


                        //decoding bitmap
                        bMap = BitmapFactory.decodeStream(imageStream, null, options); //get image with options

                        Constants.galleryBitmap = bMap;

                        startActivity(new Intent(mContext, CropActivity.class));


                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }
            }


        }

//        switch (requestCode) {
//            case PICK_IMAGE:
//                if (resultCode == RESULT_OK) {
//                    //doing some uri parsing
//                    if(imageReturnedIntent!=null){
//                        Uri selectedImage=null;
//                        try{
//                            selectedImage = imageReturnedIntent.getData();
//                        }
//                        catch (Exception e){
//                            Toast.makeText(mActivity, "File Corrupted", Toast.LENGTH_SHORT).show();
//                        }
//
//                        InputStream baseStream = null;
//                        InputStream imageStream = null;
//                        try {
//                            //getting the image
//                            baseStream = mActivity.getContentResolver().openInputStream(selectedImage);
//                            imageStream = mActivity.getContentResolver().openInputStream(selectedImage);
//                        } catch (FileNotFoundException e) {
//                            Toast.makeText(mContext, getResources().getString(R.string.error_file), Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                            return;
//                        }
//
//                        // ChecksumException
//                        try {
//                            //decoding bitmap for get width
//                            Bitmap base = BitmapFactory.decodeStream(baseStream);
//                            //set options for resize image
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inSampleSize = base.getWidth() / 1000; //get compress coef  (set image px size (compressing big image))
//
//                            //decoding bitmap
//                            Bitmap bMap = BitmapFactory.decodeStream(imageStream, null, options); //get image with options
//
//                            //Convert Bitmap to ByteArray
////                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
////                        bMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
////                        byte[] byteArray = stream.toByteArray();
//
////                        Reader reader = new MultiFormatReader();// use this otherwise
////
////                        Map<DecodeHintType, Object> tmpHintsMap = new EnumMap<>(DecodeHintType.class);
////                        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); //thorough search Qrcode
////
////                        //rotate image if nothing found
////                        Result result = null;
////                        for (int i = 0; i < 8; i++) {
////                            BinaryBitmap bitmap = binaryBitmapFromJpegData(byteArray, i * 45);
////                            try {
////                                //get result
////                                result = reader.decode(bitmap, tmpHintsMap);
////                                break;
////                            } catch (NotFoundException e) {
////                                if (i == 7) {
////                                    Toast.makeText(mContext, getResources()
////                                            .getString(R.string.error_nothing_find), Toast.LENGTH_SHORT).show();
////                                }
////                            }
////                        }
//
//                            Constants.galleryBitmap=bMap;
//
//                            startActivity(new Intent(mContext, CropActivity.class));
//                            //save result
////                        type_of_code = (result.getBarcodeFormat().toString());
////                        if (type_of_code.contains("AZTEC") || type_of_code.contains("PDF_417") ||
////                                type_of_code.contains("DATA_MATRIX") || type_of_code.contains("QR_CODE")) {
////                            resultStr = result.getText();//qr_code
////                        } else {
////                            resultStr = "barcode:" + result.getText();//barcode
////                        }
////                        savingResults(resultStr);
//
//                        } catch (Exception e) {
////                        Toast.makeText(mContext, getResources().getString(R.string.error_nothing_find), Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                }
//
//
//        }
    }

//    private BinaryBitmap binaryBitmapFromJpegData(byte[] data, int rotation) {
//        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//
//        if (rotation != 0) {
//            bitmap = rotateBitmap(bitmap, rotation);
//        }
//
//        int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
//        bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//        LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
//
//        return new BinaryBitmap(new HybridBinarizer(source));
//    }
//
//    public static Bitmap rotateBitmap(Bitmap source, float angle) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
//    }


    void savingResults(final String resultStr) {
        // boolean isCopy = AppPreference.getInstance(mContext).getBoolean(PrefKey.SETTINGS_COPY, false);
        // boolean isSound = AppPreference.getInstance(mContext).getBoolean(PrefKey.SETTINGS_SOUND, false);
        boolean isVibrate = AppPreference.getInstance(mContext).getBoolean(PrefKey.SETTINGS_VIBRATE, true);
        final boolean isAutoOpenURL = AppPreference.getInstance(mContext).getBoolean(PrefKey.SETTINGS_AUTO_URL, false);

        try {
            isFlash = false;
            mScannerView.setFlash(false);
            flash.setImageResource(R.drawable.flash_off_scanner);
        } catch (Exception ignored) {

        }

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

//        AppUtils.showScannedDialog(mActivity);


        ActivityUtils.getInstance().invokeActivity(mActivity, ResultActivity.class, false, Constants.SCAN_FRAGMENT, 0);
//        ActivityUtils.getInstance().invokeActivity(mActivity, ResultActivity.class, false, Constants.SCAN_FRAGMENT, 0);
        //Auto Open URL
//        autoOpenUrl(isAutoOpenURL, resultStr);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                alert.dismiss();
//                ActivityUtils.getInstance().invokeActivity(mActivity, ResultActivity.class, false, Constants.SCAN_FRAGMENT, 0);
//            }
//        },500);
        // TODO Sample fullscreen Ad implementation
        // fullscreen ad show
//        if(AdManager.getInstance(mContext).showFullScreenAd(mActivity)){
//
//            AdManager.getInstance(mContext).getInterstitialAd().setFullScreenContentCallback(new FullScreenContentCallback() {
//                @Override
//                public void onAdFailedToShowFullScreenContent(AdError adError) {
//                    super.onAdFailedToShowFullScreenContent(adError);
//                }
//
//                @Override
//                public void onAdShowedFullScreenContent() {
//                    super.onAdShowedFullScreenContent();
//                }
//
//                @Override
//                public void onAdDismissedFullScreenContent() {
//                    super.onAdDismissedFullScreenContent();
//                    AdManager.getInstance(mContext).loadFullScreenAd(mActivity);
//                    ActivityUtils.getInstance().invokeActivity(mActivity, ResultActivity.class, false, Constants.SCAN_FRAGMENT, 0);
//                    //Auto Open URL
//                    autoOpenUrl(isAutoOpenURL, resultStr);
//                }
//
//                @Override
//                public void onAdImpression() {
//                    super.onAdImpression();
//                }
//
//                @Override
//                public void onAdClicked() {
//                    super.onAdClicked();
//                }
//            });
////            AdManager.getInstance(mContext).getInterstitialAd().setAdListener(new AdListener() {
////                @Override
////                public void onAdClosed() {
////                    super.onAdClosed();
////                    //full ads load
////                    AdManager.getInstance(mContext).loadFullScreenAd(mActivity);
////                    ActivityUtils.getInstance().invokeActivity(mActivity, ResultActivity.class, false, Constants.SCAN_FRAGMENT, 0);
////                    //Auto Open URL
////                    autoOpenUrl(isAutoOpenURL, resultStr);
////                }
////            });
//        } else {
//            ActivityUtils.getInstance().invokeActivity(mActivity, ResultActivity.class, false, Constants.SCAN_FRAGMENT, 0);
//            //Auto Open URL
//            autoOpenUrl(isAutoOpenURL, resultStr);
//        }

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
            Uri content_url = Uri.parse(resultValues.getValue());
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

        if (mScannerView != null) {
            mScannerView.setFlash(isFlash);
        }
//        flash.setImageResource(R.drawable.ic_flash_icon);
//        AppPreference.getInstance(mContext).setBoolean(PrefKey.FLASH, false);
//        isFlash = false;
        activateScanner();
    }

    private void initConfigs() {

        flash.setImageResource(R.drawable.flash_off_scanner);
        if (isFlash) {
            flash.setImageResource(R.drawable.flash_on_scanner);
        } else {
            flash.setImageResource(R.drawable.flash_off_scanner);
        }
       /* if (isAutoFocus) {
            focus.setImageResource(R.drawable.ic_focus_off);
        } else {
            focus.setImageResource(R.drawable.ic_focus_on);
        }*/
    }

    private void loadCams() {

        try {
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
        } catch (Exception e) {
            Toast.makeText(mActivity, "Error Occured", Toast.LENGTH_SHORT).show();
        }

    }


}


