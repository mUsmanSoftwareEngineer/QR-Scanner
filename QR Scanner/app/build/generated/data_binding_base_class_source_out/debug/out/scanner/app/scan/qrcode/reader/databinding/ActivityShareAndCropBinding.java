// Generated by view binder compiler. Do not edit!
package scanner.app.scan.qrcode.reader.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import scanner.app.scan.qrcode.reader.R;

public final class ActivityShareAndCropBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final RelativeLayout bannerAdView;

  @NonNull
  public final FrameLayout bannerAdsFrame;

  @NonNull
  public final RelativeLayout bannerAdsRelative;

  @NonNull
  public final TextView edit;

  @NonNull
  public final ImageView editBackButtonFromQR;

  @NonNull
  public final Button shareCrop;

  @NonNull
  public final RelativeLayout top;

  private ActivityShareAndCropBinding(@NonNull RelativeLayout rootView,
      @NonNull RelativeLayout bannerAdView, @NonNull FrameLayout bannerAdsFrame,
      @NonNull RelativeLayout bannerAdsRelative, @NonNull TextView edit,
      @NonNull ImageView editBackButtonFromQR, @NonNull Button shareCrop,
      @NonNull RelativeLayout top) {
    this.rootView = rootView;
    this.bannerAdView = bannerAdView;
    this.bannerAdsFrame = bannerAdsFrame;
    this.bannerAdsRelative = bannerAdsRelative;
    this.edit = edit;
    this.editBackButtonFromQR = editBackButtonFromQR;
    this.shareCrop = shareCrop;
    this.top = top;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityShareAndCropBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityShareAndCropBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_share_and_crop, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityShareAndCropBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bannerAdView;
      RelativeLayout bannerAdView = rootView.findViewById(id);
      if (bannerAdView == null) {
        break missingId;
      }

      id = R.id.bannerAdsFrame;
      FrameLayout bannerAdsFrame = rootView.findViewById(id);
      if (bannerAdsFrame == null) {
        break missingId;
      }

      id = R.id.banner_ads_relative;
      RelativeLayout bannerAdsRelative = rootView.findViewById(id);
      if (bannerAdsRelative == null) {
        break missingId;
      }

      id = R.id.edit;
      TextView edit = rootView.findViewById(id);
      if (edit == null) {
        break missingId;
      }

      id = R.id.editBackButtonFromQR;
      ImageView editBackButtonFromQR = rootView.findViewById(id);
      if (editBackButtonFromQR == null) {
        break missingId;
      }

      id = R.id.share_crop;
      Button shareCrop = rootView.findViewById(id);
      if (shareCrop == null) {
        break missingId;
      }

      id = R.id.top;
      RelativeLayout top = rootView.findViewById(id);
      if (top == null) {
        break missingId;
      }

      return new ActivityShareAndCropBinding((RelativeLayout) rootView, bannerAdView,
          bannerAdsFrame, bannerAdsRelative, edit, editBackButtonFromQR, shareCrop, top);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}