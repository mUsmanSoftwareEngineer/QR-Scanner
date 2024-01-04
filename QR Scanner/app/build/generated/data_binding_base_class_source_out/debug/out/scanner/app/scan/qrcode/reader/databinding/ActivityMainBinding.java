// Generated by view binder compiler. Do not edit!
package scanner.app.scan.qrcode.reader.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import scanner.app.scan.qrcode.reader.R;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final RelativeLayout adsRelative;

  @NonNull
  public final ImageView backButton;

  @NonNull
  public final FrameLayout bannerAdsview;

  @NonNull
  public final FrameLayout fragmentContainer;

  @NonNull
  public final TextView fragmentName;

  @NonNull
  public final ImageView infoMain;

  @NonNull
  public final BottomNavigationView navigation;

  @NonNull
  public final LinearLayout remove;

  @NonNull
  public final TextView removeADS;

  @NonNull
  public final ImageView settingsMain;

  @NonNull
  public final RelativeLayout top;

  @NonNull
  public final RelativeLayout topRelative;

  private ActivityMainBinding(@NonNull RelativeLayout rootView, @NonNull RelativeLayout adsRelative,
      @NonNull ImageView backButton, @NonNull FrameLayout bannerAdsview,
      @NonNull FrameLayout fragmentContainer, @NonNull TextView fragmentName,
      @NonNull ImageView infoMain, @NonNull BottomNavigationView navigation,
      @NonNull LinearLayout remove, @NonNull TextView removeADS, @NonNull ImageView settingsMain,
      @NonNull RelativeLayout top, @NonNull RelativeLayout topRelative) {
    this.rootView = rootView;
    this.adsRelative = adsRelative;
    this.backButton = backButton;
    this.bannerAdsview = bannerAdsview;
    this.fragmentContainer = fragmentContainer;
    this.fragmentName = fragmentName;
    this.infoMain = infoMain;
    this.navigation = navigation;
    this.remove = remove;
    this.removeADS = removeADS;
    this.settingsMain = settingsMain;
    this.top = top;
    this.topRelative = topRelative;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ads_relative;
      RelativeLayout adsRelative = rootView.findViewById(id);
      if (adsRelative == null) {
        break missingId;
      }

      id = R.id.backButton;
      ImageView backButton = rootView.findViewById(id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.banner_adsview;
      FrameLayout bannerAdsview = rootView.findViewById(id);
      if (bannerAdsview == null) {
        break missingId;
      }

      id = R.id.fragment_container;
      FrameLayout fragmentContainer = rootView.findViewById(id);
      if (fragmentContainer == null) {
        break missingId;
      }

      id = R.id.fragmentName;
      TextView fragmentName = rootView.findViewById(id);
      if (fragmentName == null) {
        break missingId;
      }

      id = R.id.infoMain;
      ImageView infoMain = rootView.findViewById(id);
      if (infoMain == null) {
        break missingId;
      }

      id = R.id.navigation;
      BottomNavigationView navigation = rootView.findViewById(id);
      if (navigation == null) {
        break missingId;
      }

      id = R.id.remove;
      LinearLayout remove = rootView.findViewById(id);
      if (remove == null) {
        break missingId;
      }

      id = R.id.removeADS;
      TextView removeADS = rootView.findViewById(id);
      if (removeADS == null) {
        break missingId;
      }

      id = R.id.settingsMain;
      ImageView settingsMain = rootView.findViewById(id);
      if (settingsMain == null) {
        break missingId;
      }

      id = R.id.top;
      RelativeLayout top = rootView.findViewById(id);
      if (top == null) {
        break missingId;
      }

      id = R.id.top_relative;
      RelativeLayout topRelative = rootView.findViewById(id);
      if (topRelative == null) {
        break missingId;
      }

      return new ActivityMainBinding((RelativeLayout) rootView, adsRelative, backButton,
          bannerAdsview, fragmentContainer, fragmentName, infoMain, navigation, remove, removeADS,
          settingsMain, top, topRelative);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
