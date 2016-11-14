package android.ye.googlemarket.Utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.view.View;
import android.widget.Toast;
import android.ye.googlemarket.Globe.GoogleApplication;

/**
 * Created by ye on 2016/11/9.
 */
public class UIUtils {
    //Toast
    public static void ToastShow(String i){
        Toast.makeText(getContext(),i,Toast.LENGTH_SHORT).show();
    }

    public static Context getContext(){
        return GoogleApplication.getContext();
    }

    public static Handler getHandler(){
        return GoogleApplication.getHandler();
    }

    public static int getMainThreadId(){
        return GoogleApplication.getMainThreadId();
    }

    //加载资源文件
    //获取字符串
    public static String getString(int id){
        return getContext().getResources().getString(id);
    }
    //加载字符串数组
    public static String[] getStringArray(int id){
        return getContext().getResources().getStringArray(id);
    }

    //获取图片
    public static Drawable getDrawable(int id){
        return getContext().getResources().getDrawable(id);
    }
    //获取颜色
    public static int getColor(int id){
        return getContext().getResources().getColor(id);
    }
    //根据id获取颜色的状态选择器
    public static ColorStateList getColorStateList(int id){
        return getContext().getResources().getColorStateList(id);
    }

    //获取尺寸
    public static int getDimension(int id){
        //返回具体像素值
        return getContext().getResources().getDimensionPixelSize(id);
    }
    //dip和px的转化
    public static int dip2px(float dip){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip*density+0.5f);
    }

    //px转为dip
    public static float px2dip(int px){
        float density = getContext().getResources().getDisplayMetrics().density;
        return px/density;
    }

    //加载布局文件
    public static View inflate(int id){
        return View.inflate(getContext(),id,null);
    }
    //判断是否运行在主线程
    public static boolean isRunOnUIThread(){
        //获取当前线程,如果当前线程与主线程id相同，则运行在主线程
        int myTid = android.os.Process.myTid();
        if (myTid == getMainThreadId()){
            return true;
        }
        return false;
    }

    //运行在主线程
    public static void runOnUIThread(Runnable r){
        if (isRunOnUIThread()){
            //若已经是主线程则直接运行
            r.run();
        }else {
            getHandler().post(r);
        }
    }
}
