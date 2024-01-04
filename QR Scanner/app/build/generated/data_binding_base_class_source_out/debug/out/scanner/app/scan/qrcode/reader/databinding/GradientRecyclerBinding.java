// Generated by view binder compiler. Do not edit!
package scanner.app.scan.qrcode.reader.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import scanner.app.scan.qrcode.reader.R;

public final class GradientRecyclerBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final CardView colorCard1;

  @NonNull
  public final ImageView colorTick;

  @NonNull
  public final RelativeLayout gradient;

  private GradientRecyclerBinding(@NonNull CardView rootView, @NonNull CardView colorCard1,
      @NonNull ImageView colorTick, @NonNull RelativeLayout gradient) {
    this.rootView = rootView;
    this.colorCard1 = colorCard1;
    this.colorTick = colorTick;
    this.gradient = gradient;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static GradientRecyclerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static GradientRecyclerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.gradient_recycler, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static GradientRecyclerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      CardView colorCard1 = (CardView) rootView;

      id = R.id.color_tick;
      ImageView colorTick = rootView.findViewById(id);
      if (colorTick == null) {
        break missingId;
      }

      id = R.id.gradient;
      RelativeLayout gradient = rootView.findViewById(id);
      if (gradient == null) {
        break missingId;
      }

      return new GradientRecyclerBinding((CardView) rootView, colorCard1, colorTick, gradient);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
