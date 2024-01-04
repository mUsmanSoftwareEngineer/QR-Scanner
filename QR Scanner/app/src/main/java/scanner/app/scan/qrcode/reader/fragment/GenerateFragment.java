package scanner.app.scan.qrcode.reader.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;
import java.util.List;

import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.activity.GenerateActivity;
import scanner.app.scan.qrcode.reader.adapter.BarcodeAdapter;
import scanner.app.scan.qrcode.reader.adapter.GenerateAdapter;
import scanner.app.scan.qrcode.reader.data.preference.GenerateModel;

public class GenerateFragment extends Fragment {


    Activity activity;
    List<GenerateModel> generateModelList;
    List<GenerateModel> barcodeModelList;
    List<Integer> generateSocailList;


    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView barcodeRecyclerView;
    private GenerateAdapter adapter;
    private BarcodeAdapter barcodeAdapter;


    public GenerateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        initVar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_generate, container, false);

        initView(rootView);
        initFunctionality();
        initListener();

        return rootView;
    }

    private void initVar() {
        activity = requireActivity();
        mContext = activity.getApplicationContext();
        generateModelList = new ArrayList<>();
        barcodeModelList = new ArrayList<>();
        generateSocailList = new ArrayList<>();


    }


    private void initView(View rootView) {

        mRecyclerView = rootView.findViewById(R.id.generate_recycler);
        barcodeRecyclerView = rootView.findViewById(R.id.barcode_recycler);


    }

    private void initFunctionality() {


        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }


        addDataToGenerateAdapter();
        addDataToBarcodeAdapter();


        adapter = new GenerateAdapter(mContext, generateModelList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(adapter);


        barcodeAdapter = new BarcodeAdapter(mContext, barcodeModelList);
        barcodeRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        barcodeRecyclerView.setAdapter(barcodeAdapter);


    }


    private void addDataToBarcodeAdapter() {

        barcodeModelList.add(new GenerateModel(R.drawable.bar_code_icon2, BarcodeFormat.CODE_128 + ""));
        barcodeModelList.add(new GenerateModel(R.drawable.bar_code_icon3, BarcodeFormat.CODE_39 + ""));
        barcodeModelList.add(new GenerateModel(R.drawable.bar_code_icon4, BarcodeFormat.CODE_93 + ""));
        barcodeModelList.add(new GenerateModel(R.drawable.bar_code_icon5, BarcodeFormat.CODABAR + ""));
        barcodeModelList.add(new GenerateModel(R.drawable.bar_code_icon2, BarcodeFormat.ITF + ""));
        barcodeModelList.add(new GenerateModel(R.drawable.bar_code_icon3, BarcodeFormat.EAN_8 + ""));
        barcodeModelList.add(new GenerateModel(R.drawable.bar_code_icon4, BarcodeFormat.EAN_13 + ""));
        barcodeModelList.add(new GenerateModel(R.drawable.bar_code_icon5, BarcodeFormat.UPC_A + ""));
        barcodeModelList.add(new GenerateModel(R.drawable.bar_code_icon2, BarcodeFormat.UPC_E + ""));
        barcodeModelList.add(new GenerateModel(R.drawable.bar_code_icon3, "Product"));
        barcodeModelList.add(new GenerateModel(R.drawable.bar_code_icon4, "ISBN"));

    }


    private void addDataToGenerateAdapter() {

        generateModelList.add(new GenerateModel(R.drawable.text_scanner, activity.getResources().getString(R.string.txt_generate)));
        generateModelList.add(new GenerateModel(R.drawable.contacts_scanner, activity.getResources().getString(R.string.contact_generate)));
        generateModelList.add(new GenerateModel(R.drawable.url_scanner, activity.getResources().getString(R.string.url_generate)));
        generateModelList.add(new GenerateModel(R.drawable.wifi_scanner, activity.getResources().getString(R.string.wifi_generate)));
        generateModelList.add(new GenerateModel(R.drawable.email_scanner, activity.getResources().getString(R.string.email_generate)));
        generateModelList.add(new GenerateModel(R.drawable.sms_scanner, activity.getResources().getString(R.string.sms_generate)));
        generateModelList.add(new GenerateModel(R.drawable.location_scanner, activity.getResources().getString(R.string.location_generate)));
        generateModelList.add(new GenerateModel(R.drawable.call_scanner, activity.getResources().getString(R.string.call_generate)));
        generateModelList.add(new GenerateModel(R.drawable.event_scanner, activity.getResources().getString(R.string.event_generate)));
        generateModelList.add(new GenerateModel(R.drawable.facebook_icon, activity.getResources().getString(R.string.facebook)));
        generateModelList.add(new GenerateModel(R.drawable.twitter_icon, activity.getResources().getString(R.string.twitter)));
        generateModelList.add(new GenerateModel(R.drawable.linkdein_icon, activity.getResources().getString(R.string.linkdein)));
        generateModelList.add(new GenerateModel(R.drawable.instagram_icon, activity.getResources().getString(R.string.instagram)));
        generateModelList.add(new GenerateModel(R.drawable.whatsapp_icon, activity.getResources().getString(R.string.whatsapp)));

    }

    private void initListener() {

        adapter.setClickListener(new GenerateAdapter.ClickListener() {
            @Override
            public void onItemClicked(int position) {


                Intent intent = new Intent(getActivity(), GenerateActivity.class);
                intent.putExtra("qr_code_cat", position);
                startActivity(intent);


            }
        });


        barcodeAdapter.setClickListener(new BarcodeAdapter.ClickListener() {
            @Override
            public void onItemClicked(int position) {

                Intent intent = new Intent(getActivity(), GenerateActivity.class);
                intent.putExtra("bar_code_cat", position + 2);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
