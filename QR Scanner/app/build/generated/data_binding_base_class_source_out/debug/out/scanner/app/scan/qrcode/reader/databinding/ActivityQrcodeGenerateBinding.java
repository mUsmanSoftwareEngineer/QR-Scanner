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
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import scanner.app.scan.qrcode.reader.R;

public final class ActivityQrcodeGenerateBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final RelativeLayout adsRelative;

  @NonNull
  public final ImageView backButtonFromQR;

  @NonNull
  public final FrameLayout bannerAdsview;

  @NonNull
  public final RelativeLayout barCodeRelative;

  @NonNull
  public final ImageView barcodeBitmap;

  @NonNull
  public final RelativeLayout barcodeStyle;

  @NonNull
  public final ImageView barcodebackgroundBitmap;

  @NonNull
  public final ImageView barcodelogoBitmap;

  @NonNull
  public final CardView barcodeqrCard;

  @NonNull
  public final TextView edit;

  @NonNull
  public final ImageView logoBitmap;

  @NonNull
  public final CardView logoCard;

  @NonNull
  public final RelativeLayout nextRelative;

  @NonNull
  public final ImageView openInMap;

  @NonNull
  public final ImageView outputBitmap;

  @NonNull
  public final CardView qrCard;

  @NonNull
  public final CardView qrStyle;

  @NonNull
  public final Button saveBtn;

  @NonNull
  public final RelativeLayout shareQrOnly;

  @NonNull
  public final RelativeLayout top;

  @NonNull
  public final RelativeLayout topRelative;

  private ActivityQrcodeGenerateBinding(@NonNull RelativeLayout rootView,
      @NonNull RelativeLayout adsRelative, @NonNull ImageView backButtonFromQR,
      @NonNull FrameLayout bannerAdsview, @NonNull RelativeLayout barCodeRelative,
      @NonNull ImageView barcodeBitmap, @NonNull RelativeLayout barcodeStyle,
      @NonNull ImageView barcodebackgroundBitmap, @NonNull ImageView barcodelogoBitmap,
      @NonNull CardView barcodeqrCard, @NonNull TextView edit, @NonNull ImageView logoBitmap,
      @NonNull CardView logoCard, @NonNull RelativeLayout nextRelative,
      @NonNull ImageView openInMap, @NonNull ImageView outputBitmap, @NonNull CardView qrCard,
      @NonNull CardView qrStyle, @NonNull Button saveBtn, @NonNull RelativeLayout shareQrOnly,
      @NonNull RelativeLayout top, @NonNull RelativeLayout topRelative) {
    this.rootView = rootView;
    this.adsRelative = adsRelative;
    this.backButtonFromQR = backButtonFromQR;
    this.bannerAdsview = bannerAdsview;
    this.barCodeRelative = barCodeRelative;
    this.barcodeBitmap = barcodeBitmap;
    this.barcodeStyle = barcodeStyle;
    this.barcodebackgroundBitmap = barcodebackgroundBitmap;
    this.barcodelogoBitmap = barcodelogoBitmap;
    this.barcodeqrCard = barcodeqrCard;
    this.edit = edit;
    this.logoBitmap = logoBitmap;
    this.logoCard = logoCard;
    this.nextRelative = nextRelative;
    this.openInMap = openInMap;
    this.outputBitmap = outputBitmap;
    this.qrCard = qrCard;
    this.qrStyle = qrStyle;
    this.saveBtn = saveBtn;
    this.shareQrOnly = shareQrOnly;
    this.top = top;
    this.topRelative = topRelative;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityQrcodeGenerateBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityQrcodeGenerateBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_qrcode_generate, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityQrcodeGenerateBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ads_relative;
      RelativeLayout adsRelative = rootView.findViewById(id);
      if (adsRelative == null) {
        break missingId;
      }

      id = R.id.backButtonFromQR;
      ImageView backButtonFromQR = rootView.findViewById(id);
      if (backButtonFromQR == null) {
        break missingId;
      }

      id = R.id.banner_adsview;
      FrameLayout bannerAdsview = rootView.findViewById(id);
      if (bannerAdsview == null) {
        break missingId;
      }

      id = R.id.barCodeRelative;
      RelativeLayout barCodeRelative = rootView.findViewById(id);
      if (barCodeRelative == null) {
        break missingId;
      }

      id = R.id.barcodeBitmap;
      ImageView barcodeBitmap = rootView.findViewById(id);
      if (barcodeBitmap == null) {
        break missingId;
      }

      id = R.id.barcodeStyle;
      RelativeLayout barcodeStyle = rootView.findViewById(id);
      if (barcodeStyle == null) {
        break missingId;
      }

      id = R.id.barcodebackgroundBitmap;
      ImageView barcodebackgroundBitmap = rootView.findViewById(id);
      if (barcodebackgroundBitmap == null) {
        break missingId;
      }

      id = R.id.barcodelogoBitmap;
      ImageView barcodelogoBitmap = rootView.findViewById(id);
      if (barcodelogoBitmap == null) {
        break missingId;
      }

      id = R.id.barcodeqrCard;
      CardView barcodeqrCard = rootView.findViewById(id);
      if (barcodeqrCard == null) {
        break missingId;
      }

      id = R.id.edit;
      TextView edit = rootView.findViewById(id);
      if (edit == null) {
        break missingId;
      }

      id = R.id.logoBitmap;
      ImageView logoBitmap = rootView.findViewById(id);
      if (logoBitmap == null) {
        break missingId;
      }

      id = R.id.logoCard;
      CardView logoCard = rootView.findViewById(id);
      if (logoCard == null) {
        break missingId;
      }

      id = R.id.nextRelative;
      RelativeLayout nextRelative = rootView.findViewById(id);
      if (nextRelative == null) {
        break missingId;
      }

      id = R.id.open_in_map;
      ImageView openInMap = rootView.findViewById(id);
      if (openInMap == null) {
        break missingId;
      }

      id = R.id.outputBitmap;
      ImageView outputBitmap = rootView.findViewById(id);
      if (outputBitmap == null) {
        break missingId;
      }

      id = R.id.qrCard;
      CardView qrCard = rootView.findViewById(id);
      if (qrCard == null) {
        break missingId;
      }

      id = R.id.qr_style;
      CardView qrStyle = rootView.findViewById(id);
      if (qrStyle == null) {
        break missingId;
      }

      id = R.id.save_btn;
      Button saveBtn = rootView.findViewById(id);
      if (saveBtn == null) {
        break missingId;
      }

      id = R.id.share_qr_only;
      RelativeLayout shareQrOnly = rootView.findViewById(id);
      if (shareQrOnly == null) {
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

      return new ActivityQrcodeGenerateBinding((RelativeLayout) rootView, adsRelative,
          backButtonFromQR, bannerAdsview, barCodeRelative, barcodeBitmap, barcodeStyle,
          barcodebackgroundBitmap, barcodelogoBitmap, barcodeqrCard, edit, logoBitmap, logoCard,
          nextRelative, openInMap, outputBitmap, qrCard, qrStyle, saveBtn, shareQrOnly, top,
          topRelative);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}