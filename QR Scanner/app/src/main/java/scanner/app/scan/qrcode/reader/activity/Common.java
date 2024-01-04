package scanner.app.scan.qrcode.reader.activity;


import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

import scanner.app.scan.qrcode.reader.R;


public class Common {


    public static String getAppPath(Context context) {


        boolean success = true;
        File dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + context.getResources().getString(R.string.app_name_print));


        } else {

            dir = new File(Environment.getExternalStorageDirectory()
                    + File.separator
                    + context.getResources().getString(R.string.app_name_print)
                    + File.separator
            );

        }
        if (!dir.exists())
            success = dir.mkdirs();
        if (success) {
            return dir.getPath() + File.separator;
        }
        return null;


    }
}