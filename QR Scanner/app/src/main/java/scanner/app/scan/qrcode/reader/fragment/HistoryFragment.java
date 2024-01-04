package scanner.app.scan.qrcode.reader.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;

import app.stickerwhatsapp.stickermaker.AdEvents.AdController;
import scanner.app.scan.qrcode.reader.AdEvents.AdEvent;
import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.activity.ResultActivity;
import scanner.app.scan.qrcode.reader.adapter.HistoryAdapter;
import scanner.app.scan.qrcode.reader.data.constant.Constants;
import scanner.app.scan.qrcode.reader.data.preference.AppPreference;
import scanner.app.scan.qrcode.reader.data.preference.PrefKey;
import scanner.app.scan.qrcode.reader.utility.ActivityUtils;
import scanner.app.scan.qrcode.reader.utility.AdsManagerQ;
import scanner.app.scan.qrcode.reader.utility.DialogUtils;

public class HistoryFragment extends Fragment {

    AdManagerInterstitialAd mAdManagerInterstitialAd;
    private Activity mActivity;
    private Context mContext;
    private TextView noResultView;
    private RecyclerView mRecyclerView;
    private ArrayList<String> arrayList = null, arrayDateList, arrayColorList;
    private HistoryAdapter adapter;
    private ImageButton deleteAll;
    private TextView scanned_btn, created_btn;
    private int current_history_btn = 0;

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        initVar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        initView(rootView);
        initFunctionality();
        initListener();

        //Обновление списка с главного активити
        /*((MainActivity) getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("REFRESHH", Integer.toString(current_history_btn));
                arrayList.clear();
                if(current_history_btn==0) {
                    arrayList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_SCANNED));
                } else {
                    arrayList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_CREATED));
                }
                Collections.reverse(arrayList);
                refreshList();
            }
        });*/

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
//        AdsManagerQ.getInstance().getAd();
//        AdsManagerQ.getInstance().createInterstitialstaticAd(mContext);
    }

    private void initVar() {
        mActivity = requireActivity();
        mContext = mActivity.getApplicationContext();
    }

    private void initView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        noResultView = rootView.findViewById(R.id.noResultView);
        deleteAll = rootView.findViewById(R.id.deleteAll);
        scanned_btn = rootView.findViewById(R.id.scanned_res_btn);
        created_btn = rootView.findViewById(R.id.created_res_btn);


    }

    public void initFunctionality() {
        arrayList = new ArrayList<>();
        arrayDateList = new ArrayList<>();
        arrayColorList = new ArrayList<>();
        adapter = new HistoryAdapter(mContext, arrayList, arrayDateList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(adapter);
        arrayList.clear();
        arrayList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_SCANNED));
        Collections.reverse(arrayList);
        arrayDateList.clear();
        arrayDateList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.DATE_LIST_OF_SCANNED));
        Collections.reverse(arrayDateList);
//        arrayColorList.clear();
//        arrayColorList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_SCANNED));
//        Collections.reverse(arrayColorList);
        refreshList();


//        AdsManagerQ.getInstance().getAd();
//        AdsManagerQ.getInstance().createInterstitialstaticAd(mContext);

//        Log.d("checkAd",mAdManagerInterstitialAd+" ");
    }


    private void initListener() {

        adapter.setClickListener(new HistoryAdapter.ClickListener() {
            @Override
            public void onDeleteClicked(final int position) {
                // AppUtils.copyToClipboard(mContext, arrayList.get(position));
                DialogUtils.showDialogPrompt(mActivity, null, getString(R.string.delete_message_item),
                        getString(R.string.yes), getString(R.string.no), true, new DialogUtils.DialogActionListener() {
                            @Override
                            public void onPositiveClick() {
                                delete(position);
                            }
                        });
            }

            @Override
            public void onItemClicked(final int position) {
//                ResultOfTypeAndValue resultValues = AppUtils.getResourceType(arrayList.get(position));
                // TODO Sample fullscreen Ad implementation
                // fullscreen ad show

//                Toast.makeText(mActivity, mAdManagerInterstitialAd+"", Toast.LENGTH_SHORT).show();

//                if(!Constants.removeAds){
//
//                    showHistoryInterstitial(position);
//                }
//                else {
//                    OpenResultFromHistory(position);
//                }
                OpenResultFromHistory(position);


            }

            @Override
            public void onItemLongClicked(final int position) {
                /*DialogUtils.showDialogPrompt(mActivity, null, getString(R.string.delete_message_item),
                        getString(R.string.yes), getString(R.string.no), true, new DialogUtils.DialogActionListener() {
                            @Override
                            public void onPositiveClick() {
                                delete(position);
                            }
                        });*/
            }

        });

        scanned_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                scanned_btn.setBackgroundColor(getResources().getColor(R.drawable.round_buttons));
//                created_btn.setBackgroundColor(getResources().getColor(R.drawable.default_rounded_buttons));
                scanned_btn.setBackgroundResource(R.drawable.round_buttons);
                created_btn.setBackgroundResource(R.drawable.history_btn_color);
                scanned_btn.setTextColor(getResources().getColor(R.color.white));
                created_btn.setTextColor(getResources().getColor(R.color.tt_black));
                current_history_btn = 0;

                arrayList.clear();
                arrayList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_SCANNED));
                Collections.reverse(arrayList);

                arrayDateList.clear();
                arrayDateList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.DATE_LIST_OF_SCANNED));
                Collections.reverse(arrayDateList);

//                arrayColorList.clear();
//                arrayColorList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_SCANNED));
//                Collections.reverse(arrayColorList);

                refreshList();
            }
        });

        created_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                scanned_btn.setBackgroundColor(getResources().getColor(R.drawable.round_buttons));
//                created_btn.setBackgroundColor(getResources().getColor(R.c));
                scanned_btn.setBackgroundResource(R.drawable.history_btn_color);
                created_btn.setBackgroundResource(R.drawable.round_buttons);
                scanned_btn.setTextColor(getResources().getColor(R.color.tt_black));
                created_btn.setTextColor(getResources().getColor(R.color.white));
                current_history_btn = 1;

                arrayList.clear();
                arrayList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_CREATED));
                Collections.reverse(arrayList);

                arrayDateList.clear();
                arrayDateList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.DATE_LIST_OF_CREATED));
                Collections.reverse(arrayDateList);

                arrayColorList.clear();
                arrayColorList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_CREATED));
                Collections.reverse(arrayColorList);

                refreshList();
            }
        });


        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showDialogPrompt(mActivity, null, getString(R.string.delete_message_all),
                        getString(R.string.yes), getString(R.string.no), true, new DialogUtils.DialogActionListener() {
                            @Override
                            public void onPositiveClick() {
                                deleteAll();
                            }
                        });
            }
        });
    }

    public void showHistoryInterstitial(int position) {

        mAdManagerInterstitialAd = AdsManagerQ.getInstance().getAd();
        if (mAdManagerInterstitialAd != null) {


            mAdManagerInterstitialAd.show(requireActivity());
            mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {

                    Constants.adLogicResultBottomBar = 3;
                    // Called when fullscreen content is dismissed.
                    AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));
//                            Toast.makeText(mActivity, mAdManagerInterstitialAd+"ad dismissed", Toast.LENGTH_SHORT).show();
                    OpenResultFromHistory(position);

                }


                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    // Called when fullscreen content failed to show.
//                            Toast.makeText(mActivity, mAdManagerInterstitialAd+"ad failed to show", Toast.LENGTH_SHORT).show();
                    AdsManagerQ.getInstance().InterstitialAd(mContext, getResources().getString(R.string.InterstitialOn));
                    OpenResultFromHistory(position);

                }

                @Override
                public void onAdShowedFullScreenContent() {
//                            AdsManagerQ.getInstance().createInterstitialstaticAd(mContext);
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.

                    AdEvent.AdAnalysisInterstitial(AdController.AdType.INTERSTITIAL,mContext);
                }

            });

            mAdManagerInterstitialAd.setOnPaidEventListener(new OnPaidEventListener() {
                @Override
                public void onPaidEvent(@NonNull AdValue adValue) {

                    try {
                        Bundle bundle = new Bundle();
                        bundle.putString("currency", adValue.getCurrencyCode());
                        bundle.putString("precision", String.valueOf(adValue.getPrecisionType()));
                        bundle.putString("valueMicros", String.valueOf(adValue.getValueMicros()));
                        FirebaseAnalytics.getInstance(mContext).logEvent("paid_ad_impressions", bundle);
                    } catch (Exception e) {
//                                    Log.d("events", e.getMessage());
                    }
                }
            });
//            AppUtils.showAdDialog(activity, mContext);


        } else {
            OpenResultFromHistory(position);
        }


    }


    private void delete(int position) {
        if (current_history_btn == 0) {
            AppPreference.getInstance(mContext).setStringArray(PrefKey.RESULT_LIST_OF_SCANNED, null);
            arrayList.remove(position);
            Collections.reverse(arrayList);
            AppPreference.getInstance(mContext).setStringArray(PrefKey.RESULT_LIST_OF_SCANNED, arrayList);
            Collections.reverse(arrayList);

            AppPreference.getInstance(mContext).setStringArray(PrefKey.DATE_LIST_OF_SCANNED, null);
            arrayDateList.remove(position);
            Collections.reverse(arrayDateList);
            AppPreference.getInstance(mContext).setStringArray(PrefKey.DATE_LIST_OF_SCANNED, arrayDateList);

//            AppPreference.getInstance(mContext).setStringArray(PrefKey.COLOR_LIST_OF_SCANNED, null);
//            arrayColorList.remove(position);
//            Collections.reverse(arrayColorList);
//            AppPreference.getInstance(mContext).setStringArray(PrefKey.COLOR_LIST_OF_SCANNED, arrayColorList);
//            Collections.reverse(arrayColorList);

        } else {
            AppPreference.getInstance(mContext).setStringArray(PrefKey.RESULT_LIST_OF_CREATED, null);
            arrayList.remove(position);
            Collections.reverse(arrayList);
            AppPreference.getInstance(mContext).setStringArray(PrefKey.RESULT_LIST_OF_CREATED, arrayList);
            Collections.reverse(arrayList);

            AppPreference.getInstance(mContext).setStringArray(PrefKey.DATE_LIST_OF_CREATED, null);
            arrayDateList.remove(position);
            Collections.reverse(arrayDateList);
            AppPreference.getInstance(mContext).setStringArray(PrefKey.DATE_LIST_OF_CREATED, arrayDateList);

//            AppPreference.getInstance(mContext).setStringArray(PrefKey.COLOR_LIST_OF_CREATED, null);
//            arrayColorList.remove(position);
//            Collections.reverse(arrayColorList);
//            AppPreference.getInstance(mContext).setStringArray(PrefKey.COLOR_LIST_OF_CREATED, arrayColorList);
//            Collections.reverse(arrayColorList);

        }
        Collections.reverse(arrayDateList);
        refreshList();
    }

    private void deleteAll() {
        if (current_history_btn == 0) {
            AppPreference.getInstance(mContext).setStringArray(PrefKey.RESULT_LIST_OF_SCANNED, null);
            arrayList.clear();

            AppPreference.getInstance(mContext).setStringArray(PrefKey.DATE_LIST_OF_SCANNED, null);
            arrayDateList.clear();

            AppPreference.getInstance(mContext).setStringArray(PrefKey.COLOR_LIST_OF_SCANNED, null);

        } else {
            AppPreference.getInstance(mContext).setStringArray(PrefKey.RESULT_LIST_OF_CREATED, null);
            arrayList.clear();

            AppPreference.getInstance(mContext).setStringArray(PrefKey.DATE_LIST_OF_CREATED, null);
            arrayDateList.clear();

            AppPreference.getInstance(mContext).setStringArray(PrefKey.COLOR_LIST_OF_CREATED, null);

        }
        arrayColorList.clear();
        refreshList();
    }

    public void refreshList() {
        if (arrayList.isEmpty()) {
            noResultView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            deleteAll.setVisibility(View.GONE);
        } else {
            noResultView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            deleteAll.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }


//    @Override
//    public void setMenuVisibility(final boolean visible) {
//        super.setMenuVisibility(visible);
//        if (visible) {
//            arrayList=new ArrayList<>();
//            arrayDateList=new ArrayList<>();
//            arrayColorList=new ArrayList<>();
//            arrayList.clear();
//            arrayDateList.clear();
//            if (current_history_btn == 0) {
//                arrayList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_SCANNED));
//                arrayDateList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.DATE_LIST_OF_SCANNED));
//                arrayColorList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_SCANNED));
//            } else {
//                arrayList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST_OF_CREATED));
//                arrayDateList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.DATE_LIST_OF_CREATED));
//                arrayColorList.addAll(AppPreference.getInstance(mContext).getStringArray(PrefKey.COLOR_LIST_OF_CREATED));
//            }
//            Collections.reverse(arrayList);
//            Collections.reverse(arrayDateList);
//            Collections.reverse(arrayColorList);
//            refreshList();
//        }
//    }

    public void OpenResultFromHistory(int position) {
        if (current_history_btn == 0) {

            ActivityUtils.getInstance().invokeActivity(mActivity, ResultActivity.class, false, Constants.HISTORY_SCAN_FRAGMENT, position);
        } else {
            ActivityUtils.getInstance().invokeActivity(mActivity, ResultActivity.class, false, Constants.HISTORY_GENERATE_FRAGMENT, position);
        }
    }

}
