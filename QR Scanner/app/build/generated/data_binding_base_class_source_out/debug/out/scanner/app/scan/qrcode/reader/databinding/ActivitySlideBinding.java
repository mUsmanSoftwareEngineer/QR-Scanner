// Generated by view binder compiler. Do not edit!
package scanner.app.scan.qrcode.reader.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.adapter.ClickableViewPager;

public final class ActivitySlideBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final RelativeLayout appBarLayout;

  @NonNull
  public final RelativeLayout bottomRelative;

  @NonNull
  public final DotsIndicator dotsIndicatorrr;

  @NonNull
  public final LinearLayout dotsLinear;

  @NonNull
  public final ImageView goBackk;

  @NonNull
  public final ImageView next;

  @NonNull
  public final RelativeLayout relativeLayoutSlide;

  @NonNull
  public final TextView skipeddd;

  @NonNull
  public final ClickableViewPager viewPagerSlide;

  private ActivitySlideBinding(@NonNull RelativeLayout rootView,
      @NonNull RelativeLayout appBarLayout, @NonNull RelativeLayout bottomRelative,
      @NonNull DotsIndicator dotsIndicatorrr, @NonNull LinearLayout dotsLinear,
      @NonNull ImageView goBackk, @NonNull ImageView next,
      @NonNull RelativeLayout relativeLayoutSlide, @NonNull TextView skipeddd,
      @NonNull ClickableViewPager viewPagerSlide) {
    this.rootView = rootView;
    this.appBarLayout = appBarLayout;
    this.bottomRelative = bottomRelative;
    this.dotsIndicatorrr = dotsIndicatorrr;
    this.dotsLinear = dotsLinear;
    this.goBackk = goBackk;
    this.next = next;
    this.relativeLayoutSlide = relativeLayoutSlide;
    this.skipeddd = skipeddd;
    this.viewPagerSlide = viewPagerSlide;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySlideBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySlideBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_slide, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySlideBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appBarLayout;
      RelativeLayout appBarLayout = rootView.findViewById(id);
      if (appBarLayout == null) {
        break missingId;
      }

      id = R.id.bottom_relative;
      RelativeLayout bottomRelative = rootView.findViewById(id);
      if (bottomRelative == null) {
        break missingId;
      }

      id = R.id.dots_indicatorrr;
      DotsIndicator dotsIndicatorrr = rootView.findViewById(id);
      if (dotsIndicatorrr == null) {
        break missingId;
      }

      id = R.id.dots_linear;
      LinearLayout dotsLinear = rootView.findViewById(id);
      if (dotsLinear == null) {
        break missingId;
      }

      id = R.id.goBackk;
      ImageView goBackk = rootView.findViewById(id);
      if (goBackk == null) {
        break missingId;
      }

      id = R.id.next;
      ImageView next = rootView.findViewById(id);
      if (next == null) {
        break missingId;
      }

      RelativeLayout relativeLayoutSlide = (RelativeLayout) rootView;

      id = R.id.skipeddd;
      TextView skipeddd = rootView.findViewById(id);
      if (skipeddd == null) {
        break missingId;
      }

      id = R.id.view_pager_slide;
      ClickableViewPager viewPagerSlide = rootView.findViewById(id);
      if (viewPagerSlide == null) {
        break missingId;
      }

      return new ActivitySlideBinding((RelativeLayout) rootView, appBarLayout, bottomRelative,
          dotsIndicatorrr, dotsLinear, goBackk, next, relativeLayoutSlide, skipeddd,
          viewPagerSlide);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}