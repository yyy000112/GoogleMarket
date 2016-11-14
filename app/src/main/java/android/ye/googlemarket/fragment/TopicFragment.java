package android.ye.googlemarket.fragment;

import android.view.View;
import android.ye.googlemarket.Adapter.MyBaseAdapter;
import android.ye.googlemarket.Holder.BaseHolder;
import android.ye.googlemarket.Holder.TopicHolder;
import android.ye.googlemarket.Http.protocol.AppProtocol;
import android.ye.googlemarket.Http.protocol.TopicProtocol;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.View.LoadingPager;
import android.ye.googlemarket.View.MyListView;
import android.ye.googlemarket.domain.TopicInfo;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/9.
 */
public class TopicFragment extends BaseFragment {

    private ArrayList<TopicInfo> data;
    @Override
    public View onCreateSuccessView() {
        MyListView view = new MyListView(UIUtils.getContext());
        view.setAdapter(new TopicAdapter(data));
        return view;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        TopicProtocol protocol = new TopicProtocol();
        //加载第一页数据
        data =protocol.getData(0);
        return check(data);
    }

    private class TopicAdapter extends MyBaseAdapter<TopicInfo> {
        public TopicAdapter(ArrayList<TopicInfo> data) {
            super(data);
        }

        @Override
        public ArrayList<TopicInfo> onLoadMore() {
            TopicProtocol protocol = new TopicProtocol();
            ArrayList<TopicInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }

        @Override
        public BaseHolder<TopicInfo> getHolder() {
            return new TopicHolder();
        }
    }
}
