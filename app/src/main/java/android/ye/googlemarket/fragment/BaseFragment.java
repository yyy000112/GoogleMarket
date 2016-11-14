package android.ye.googlemarket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.View.LoadingPager;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/9.
 */
public abstract class BaseFragment extends Fragment {

    private LoadingPager mLoadingPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLoadingPager = new LoadingPager(UIUtils.getContext()) {
            @Override
            public ResultState onLoad() {
                return BaseFragment.this.onLoad();
            }

            @Override
            public View onCreateSuccessView() {
                return BaseFragment.this.onCreateSuccessView();
            }
        };
        return mLoadingPager;
    }

    //加载成功的布局由子类来实现
    public abstract View onCreateSuccessView();

    //网络加载结束的状态由子类实现
    public abstract LoadingPager.ResultState onLoad();

    //mLoadingPager不为空时调用loadData()
    public void loadData(){
        if (mLoadingPager!=null){
            mLoadingPager.loadData();
        }
    }

    //对网络数据进行校验
    public LoadingPager.ResultState check(Object object){
        if (object!=null){
            //判断是否是
            if (object instanceof ArrayList){
                ArrayList list = (ArrayList) object;
                if (list.isEmpty()){
                    return LoadingPager.ResultState.STATE_EMPTY;
                }else {
                    return LoadingPager.ResultState.STATE_SUCCESS;
                }
            }
        }
        return LoadingPager.ResultState.STATE_ERROR;
    }
}
