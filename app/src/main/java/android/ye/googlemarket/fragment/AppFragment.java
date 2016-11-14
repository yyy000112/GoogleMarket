package android.ye.googlemarket.fragment;

import android.view.View;
import android.widget.ListAdapter;
import android.ye.googlemarket.Adapter.MyBaseAdapter;
import android.ye.googlemarket.Holder.AppHolder;
import android.ye.googlemarket.Holder.BaseHolder;
import android.ye.googlemarket.Http.protocol.AppProtocol;
import android.ye.googlemarket.Http.protocol.HomeProtocol;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.LogUtils;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.View.LoadingPager;
import android.ye.googlemarket.View.MyListView;
import android.ye.googlemarket.domain.AppInfo;
import android.ye.googlemarket.domain.HomeList0;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/9.
 */
public class AppFragment extends BaseFragment {

    private MyListView view;
    private ArrayList<AppInfo> data;

    @Override
    public View onCreateSuccessView() {
        view = new MyListView(UIUtils.getContext());
        view.setAdapter(new AppAdapter(data));
        return view;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        AppProtocol protocol = new AppProtocol();
        //加载第一页数据
        data =protocol.getData(0);
        return check(data);
    }

    public class AppAdapter extends MyBaseAdapter<AppInfo> {
        public AppAdapter(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public ArrayList onLoadMore() {
            AppProtocol protocol = new AppProtocol();

            //获取当前集合大小
            ArrayList<AppInfo> moreData = protocol.getData(getListSize());

            return moreData;
        }

        @Override
        public BaseHolder<AppInfo> getHolder() {
            return new AppHolder();
        }
    }
}
