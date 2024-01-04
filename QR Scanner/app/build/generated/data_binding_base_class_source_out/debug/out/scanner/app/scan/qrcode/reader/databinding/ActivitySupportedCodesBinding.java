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
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import scanner.app.scan.qrcode.reader.R;

public final class ActivitySupportedCodesBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView backButton;

  @NonNull
  public final TextView fragmentName;

  @NonNull
  public final TabLayout tabs;

  @NonNull
  public final RelativeLayout top;

  @NonNull
  public final ViewPager viewpager;

  private ActivitySupportedCodesBinding(@NonNull RelativeLayout rootView,
      @NonNull ImageView backButton, @NonNull TextView fragmentName, @NonNull TabLayout tabs,
      @NonNull RelativeLayout top, @NonNull ViewPager viewpager) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.fragmentName = fragmentName;
    this.tabs = tabs;
    this.top = top;
    this.viewpager = viewpager;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySupportedCodesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySupportedCodesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_supported_codes, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySupportedCodesBinding bind(@NonNull View rootView) {
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

      id = R.id.tabs;
      TabLayout tabs = rootView.findViewById(id);
      if (tabs == null) {
        break missingId;
      }

      id = R.id.top;
      RelativeLayout top = rootView.findViewById(id);
      if (top == null) {
        break missingId;
      }

      id = R.id.viewpager;
      ViewPager viewpager = rootView.findViewById(id);
      if (viewpager == null) {
        break missingId;
      }

      return new ActivitySupportedCodesBinding((RelativeLayout) rootView, backButton, fragmentName,
          tabs, top, viewpager);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}