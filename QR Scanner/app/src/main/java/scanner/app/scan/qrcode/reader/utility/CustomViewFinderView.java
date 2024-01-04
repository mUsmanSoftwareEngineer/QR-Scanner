package scanner.app.scan.qrcode.reader.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.zxing.core.ViewFinderView;


public class CustomViewFinderView extends ViewFinderView {

    private final Context mContext;



    public CustomViewFinderView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CustomViewFinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        init();
    }

    private void init() {
        int colorAttr;
        colorAttr = R.attr.colorPrimary;
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(colorAttr, typedValue, true);
        setSquareViewFinder(true);
        setBorderColor(typedValue.data);
        setLaserEnabled(false);

    }




}
