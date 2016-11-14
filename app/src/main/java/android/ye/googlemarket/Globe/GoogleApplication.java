package android.ye.googlemarket.Globe;

import android.app.Application;
import android.content.Context;
import android.os.*;

/**
 * 进行全局初始化
 * Created by ye on 2016/11/9.
 */
public class GoogleApplication extends Application {

    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();

    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }


}
