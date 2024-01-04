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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import scanner.app.scan.qrcode.reader.R;

public final class ActivityCustomQrcodeBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final RelativeLayout adLayout;

  @NonNull
  public final RelativeLayout adsTxt;

  @NonNull
  public final FrameLayout bannerAdsFrame;

  @NonNull
  public final TextView creatingText;

  @NonNull
  public final RecyclerView dotsRecyclerView;

  @NonNull
  public final TextView edit;

  @NonNull
  public final ImageView editBackButtonFromQR;

  @NonNull
  public final RecyclerView eyesRecyclerView;

  @NonNull
  public final RecyclerView gradientRecyclerView;

  @NonNull
  public final ImageView ivQrcode;

  @NonNull
  public final Button shareCustom;

  @NonNull
  public final RelativeLayout spinner;

  @NonNull
  public final RelativeLayout top;

  private ActivityCustomQrcodeBinding(@NonNull RelativeLayout rootView,
      @NonNull RelativeLayout adLayout, @NonNull RelativeLayout adsTxt,
      @NonNull FrameLayout bannerAdsFrame, @NonNull TextView creatingText,
      @NonNull RecyclerView dotsRecyclerView, @NonNull TextView edit,
      @NonNull ImageView editBackButtonFromQR, @NonNull RecyclerView eyesRecyclerView,
      @NonNull RecyclerView gradientRecyclerView, @NonNull ImageView ivQrcode,
      @NonNull Button shareCustom, @NonNull RelativeLayout spinner, @NonNull RelativeLayout top) {
    this.rootView = rootView;
    this.adLayout = adLayout;
    this.adsTxt = adsTxt;
    this.bannerAdsFrame = bannerAdsFrame;
    this.creatingText = creatingText;
    this.dotsRecyclerView = dotsRecyclerView;
    this.edit = edit;
    this.editBackButtonFromQR = editBackButtonFromQR;
    this.eyesRecyclerView = eyesRecyclerView;
    this.gradientRecyclerView = gradientRecyclerView;
    this.ivQrcode = ivQrcode;
    this.shareCustom = shareCustom;
    this.spinner = spinner;
    this.top = top;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCustomQrcodeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCustomQrcodeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_custom_qrcode, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCustomQrcodeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.adLayout;
      RelativeLayout adLayout = rootView.findViewById(id);
      if (adLayout == null) {
        break missingId;
      }

      id = R.id.ads_txt;
      RelativeLayout adsTxt = rootView.findViewById(id);
      if (adsTxt == null) {
        break missingId;
      }

      id = R.id.bannerAdsFrame;
      FrameLayout bannerAdsFrame = rootView.findViewById(id);
      if (bannerAdsFrame == null) {
        break missingId;
      }

      id = R.id.creatingText;
      TextView creatingText = rootView.findViewById(id);
      if (creatingText == null) {
        break missingId;
      }

      id = R.id.dotsRecyclerView;
      RecyclerView dotsRecyclerView = rootView.findViewById(id);
      if (dotsRecyclerView == null) {
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

      id = R.id.eyesRecyclerView;
      RecyclerView eyesRecyclerView = rootView.findViewById(id);
      if (eyesRecyclerView == null) {
        break missingId;
      }

      id = R.id.gradientRecyclerView;
      RecyclerView gradientRecyclerView = rootView.findViewById(id);
      if (gradientRecyclerView == null) {
        break missingId;
      }

      id = R.id.iv_qrcode;
      ImageView ivQrcode = rootView.findViewById(id);
      if (ivQrcode == null) {
        break missingId;
      }

      id = R.id.share_Custom;
      Button shareCustom = rootView.findViewById(id);
      if (shareCustom == null) {
        break missingId;
      }

      id = R.id.spinner;
      RelativeLayout spinner = rootView.findViewById(id);
      if (spinner == null) {
        break missingId;
      }

      id = R.id.top;
      RelativeLayout top = rootView.findViewById(id);
      if (top == null) {
        break missingId;
      }

      return new ActivityCustomQrcodeBinding((RelativeLayout) rootView, adLayout, adsTxt,
          bannerAdsFrame, creatingText, dotsRecyclerView, edit, editBackButtonFromQR,
          eyesRecyclerView, gradientRecyclerView, ivQrcode, shareCustom, spinner, top);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
