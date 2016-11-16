package android.ye.googlemarket.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.ye.googlemarket.Activity.HomeDetailActivity;
import android.ye.googlemarket.Holder.BaseHolder;
import android.ye.googlemarket.Adapter.MyBaseAdapter;
import android.ye.googlemarket.Holder.HomeHeaderHolder;
import android.ye.googlemarket.Holder.HomeHolder;
import android.ye.googlemarket.Http.protocol.HomeProtocol;
import android.ye.googlemarket.Utils.LogUtils;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.View.LoadingPager;
import android.ye.googlemarket.View.MyListView;
import android.ye.googlemarket.domain.AppDetail;


import java.util.ArrayList;

/**
 * Created by ye on 2016/11/9.
 */
public class HomeFragment extends BaseFragment {

    private MyListView llHome;
    //private ArrayList<HomeList0.List> data;
    private ArrayList<String> pictureList;
    private ArrayList<AppDetail> data;

    //运行在主线程，可以随意更新UI
    @Override
    public View onCreateSuccessView() {

        llHome = new MyListView(UIUtils.getContext());

        //listview中加载滚动新闻的头布局
        HomeHeaderHolder header = new HomeHeaderHolder();
        llHome.addHeaderView(header.getRootView());

        llHome.setAdapter(new HomeAdapter(data));

        if (pictureList!=null){
            header.setData(pictureList);

        }
        llHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //去掉头布局
                AppDetail list = data.get(position-1);
                if (list !=null){
                    Intent intent = new Intent(UIUtils.getContext(),HomeDetailActivity.class);
                    intent.putExtra("packageName",list.packageName);
                    startActivity(intent);
                }

            }
        });


        return llHome;
    }

    //运行在子线程，不必要new一个Thread
    @Override
    public LoadingPager.ResultState onLoad() {
        HomeProtocol protocol = new HomeProtocol();
        //加载第一页数据
        data = protocol.getData(0);

        pictureList = protocol.getPictureList();

        return check(data);
    }

    class HomeAdapter extends MyBaseAdapter<AppDetail>{

        public HomeAdapter(ArrayList<AppDetail> data) {
            super(data);
        }



        @Override
        public ArrayList<AppDetail> onLoadMore() {
           /* //在子线程调用
            ArrayList<HomeList0.List> moreData = new ArrayList<HomeList0.List>();
            for (int i =0;i<20;i++){
                moreData.add("这是第" + i + "个测试数据");
            }
            SystemClock.sleep(3000);*/
            HomeProtocol protocol = new HomeProtocol();

            //获取当前集合大小
           // ArrayList<HomeList0.List> moreData = protocol.getData(getListSize());

            ArrayList<AppDetail> moreData = protocol.getData(getListSize());
            return moreData;
        }

        @Override
        public BaseHolder<AppDetail> getHolder() {
           return  new HomeHolder();
        }
    }

}
