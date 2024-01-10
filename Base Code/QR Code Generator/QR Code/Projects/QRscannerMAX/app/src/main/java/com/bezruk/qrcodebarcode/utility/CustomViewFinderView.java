package com.bezruk.qrcodebarcode.utility;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.bezruk.qrcodebarcode.R;
import com.bezruk.qrcodebarcode.zxing.core.ViewFinderView;


public class CustomViewFinderView extends ViewFinderView {

    private Context mContext;


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAttr = R.attr.colorPrimary;
        } else {
            colorAttr = getResources().getIdentifier("colorPrimary", "attr", mContext.getPackageName());
        }
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(colorAttr, typedValue, true);
        setSquareViewFinder(true);
        setBorderColor(typedValue.data);
        setLaserEnabled(true);
    }

}
