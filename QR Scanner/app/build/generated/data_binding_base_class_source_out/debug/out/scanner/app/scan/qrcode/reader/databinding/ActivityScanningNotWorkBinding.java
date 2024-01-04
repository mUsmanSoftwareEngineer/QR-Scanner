// Generated by view binder compiler. Do not edit!
package scanner.app.scan.qrcode.reader.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class ActivityScanningNotWorkBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView backButton;

  @NonNull
  public final TextView fragmentName;

  @NonNull
  public final TextView mainText;

  @NonNull
  public final ImageView scanImg;

  @NonNull
  public final TextView text;

  @NonNull
  public final RelativeLayout top;

  private ActivityScanningNotWorkBinding(@NonNull RelativeLayout rootView,
      @NonNull ImageView backButton, @NonNull TextView fragmentName, @NonNull TextView mainText,
      @NonNull ImageView scanImg, @NonNull TextView text, @NonNull RelativeLayout top) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.fragmentName = fragmentName;
    this.mainText = mainText;
    this.scanImg = scanImg;
    this.text = text;
    this.top = top;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityScanningNotWorkBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityScanningNotWorkBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_scanning_not_work, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityScanningNotWorkBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backButton;
      ImageView backButton = rootView.findViewById(id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.fragmentName;
      TextView fragmentName = rootView.findViewById(id);
      if (fragmentName == null) {
        break missingId;
      }

      id = R.id.mainText;
      TextView mainText = rootView.findViewById(id);
      if (mainText == null) {
        break missingId;
      }

      id = R.id.scan_img;
      ImageView scanImg = rootView.findViewById(id);
      if (scanImg == null) {
        break missingId;
      }

      id = R.id.text;
      TextView text = rootView.findViewById(id);
      if (text == null) {
        break missingId;
      }

      id = R.id.top;
      RelativeLayout top = rootView.findViewById(id);
      if (top == null) {
        break missingId;
      }

      return new ActivityScanningNotWorkBinding((RelativeLayout) rootView, backButton, fragmentName,
          mainText, scanImg, text, top);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
