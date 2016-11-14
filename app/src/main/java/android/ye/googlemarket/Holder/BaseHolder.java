package android.ye.googlemarket.Holder;

import android.view.View;

/**
 * 封装Holder
 * Created by ye on 2016/11/10.
 */
public abstract class BaseHolder<T> {

    private View mRootView;
    private T data;

    public BaseHolder(){
        //加载布局, 初始化控件,设置tag
        mRootView = initView();
        mRootView.setTag(this);
    }

    //设置数据
    public void setData(T data){
        this.data = data;
        refreshView(data);
    }

    //获得数据
    public T getData(){
        return  data;
    }

    //返回item的根布局
    public View getRootView(){
        return mRootView;
    }

    //交给子类实现初始化布局
    public abstract View initView();

    //交给子类实现更新页面数据
    public abstract void refreshView(T data);

}
