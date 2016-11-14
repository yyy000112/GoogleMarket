package android.ye.googlemarket.fragment;

import android.view.View;

import android.ye.googlemarket.Adapter.MyBaseAdapter;
import android.ye.googlemarket.Holder.BaseHolder;

import android.ye.googlemarket.Holder.GameHolder;
import android.ye.googlemarket.Http.protocol.AppProtocol;
import android.ye.googlemarket.Http.protocol.GameProtocol;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.View.LoadingPager;
import android.ye.googlemarket.View.MyListView;

import android.ye.googlemarket.domain.AppInfo;
import android.ye.googlemarket.domain.GameInfo;

import java.util.ArrayList;

/**
 * Created by ye on 2016/11/9.
 */
public class GameFragment extends BaseFragment {

    private MyListView view;
    private ArrayList<GameInfo> data;
    @Override
    public View onCreateSuccessView() {
        view = new MyListView(UIUtils.getContext());
        view.setAdapter(new GameAdapter(data));
        return view;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        GameProtocol protocol = new GameProtocol();
        //加载第一页数据
        data =protocol.getData(0);
        return check(data);
    }

    public class GameAdapter extends MyBaseAdapter<GameInfo> {

        public GameAdapter(ArrayList<GameInfo> data) {
            super(data);
        }

        @Override
        public ArrayList<GameInfo> onLoadMore() {
            GameProtocol protocol = new GameProtocol();

            //获取当前集合大小
            ArrayList<GameInfo> moreData = protocol.getData(getListSize());

            return moreData;
        }

        @Override
        public BaseHolder<GameInfo> getHolder() {
            return new GameHolder();
        }
    }
}
