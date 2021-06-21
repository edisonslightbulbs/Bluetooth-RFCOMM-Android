package edisonslightbulbs.traceless;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class Utils {

    public static void toast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
