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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import scanner.app.scan.qrcode.reader.R;

public final class ActivityTipsBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView backButton;

  @NonNull
  public final TextView fragmentName;

  @NonNull
  public final RecyclerView recyclerView;

  @NonNull
  public final RelativeLayout top;

  private ActivityTipsBinding(@NonNull RelativeLayout rootView, @NonNull ImageView backButton,
      @NonNull TextView fragmentName, @NonNull RecyclerView recyclerView,
      @NonNull RelativeLayout top) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.fragmentName = fragmentName;
    this.recyclerView = recyclerView;
    this.top = top;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityTipsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityTipsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_tips, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityTipsBinding bind(@NonNull View rootView) {
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

      id = R.id.recycler_view;
      RecyclerView recyclerView = rootView.findViewById(id);
      if (recyclerView == null) {
        break missingId;
      }

      id = R.id.top;
      RelativeLayout top = rootView.findViewById(id);
      if (top == null) {
        break missingId;
      }

      return new ActivityTipsBinding((RelativeLayout) rootView, backButton, fragmentName,
          recyclerView, top);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}