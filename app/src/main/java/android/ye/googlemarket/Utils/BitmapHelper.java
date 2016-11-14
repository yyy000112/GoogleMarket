package android.ye.googlemarket.Utils;

import com.lidroid.xutils.BitmapUtils;

/**
 * 设置单例模式，全局只有一个BitmapUtils，防止栈溢出
 * Created by ye on 2016/11/11.
 */
public class BitmapHelper {

    private static BitmapUtils mBitmapUtils;
    //懒汉模式
    public static BitmapUtils getBitmapUtils(){
        if (mBitmapUtils == null){
            synchronized (BitmapHelper.class){
                mBitmapUtils = new BitmapUtils(UIUtils.getContext());
            }
        }
        return mBitmapUtils;
    }
}
