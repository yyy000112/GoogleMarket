package android.ye.googlemarket.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.ye.googlemarket.Holder.BaseHolder;
import android.ye.googlemarket.Holder.MoreHolder;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.UIUtils;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;

/**
 * 将BeseAdapter进行封装
 * Created by ye on 2016/11/10.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    //设定加载的两种类型标识，必须从0开始写，否则无法运行
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_MORE = 0;

    private ArrayList<T> data;
    private BaseHolder holder;

    public MyBaseAdapter(ArrayList<T> data){
        this.data = data;
    }
    @Override
    public int getCount() {
        //+1表示增加更多布局的加载
        return data.size()+1;
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //返回类型个数
    @Override
    public int getViewTypeCount() {
        //返回两种类型：普通布局+加载更多布局
        return 2;
    }

    //返回类型
    @Override
    public int getItemViewType(int position) {
        if (position == getCount()-1){
            return TYPE_MORE;
        }else {

           return getInnerType(position);
        }
    }

    //可以重写此方法来返回更多其他的布局类型
    public int getInnerType(int position) {
        return TYPE_NORMAL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            //加载控件，初始化findViewById，打标记
            if (getItemViewType(position) == TYPE_MORE){
               //加载更多类型
                holder = new MoreHolder(hasMore());
            }else {
                holder =getHolder();
            }
        }else {
           holder = (BaseHolder) convertView.getTag();
        }
        //刷新页面
        if (getItemViewType(position)!=TYPE_MORE){
            holder.setData(getItem(position));
        }else {
            //加载更多布局
            MoreHolder moreHolder = (MoreHolder) holder;
          if (moreHolder.getData() == MoreHolder.STATE_MORE_MORE){
              loadMore(moreHolder);
          }
        }
        return holder.getRootView();
    }

    //添加是否在加载更多标记
    private boolean isLoadMore = false;
    //加载更多数据
    public void loadMore(final MoreHolder holder) {
        if (!isLoadMore){
            isLoadMore = true;
            new Thread(){
                @Override
                public void run() {
                    //由子类实现更多数据
                    final ArrayList<T> moreData = onLoadMore();
                    //运行在主线程更新UI
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (moreData!=null){
                                //分页加载
                                if (moreData.size() < 20 ){
                                    holder.setData(MoreHolder.STATE_MORE_NONE);
                                    UIUtils.ToastShow("数据加载完咯");
                                }else {
                                    holder.setData(MoreHolder.STATE_MORE_ERROE);
                                }
                                data.addAll(moreData);
                                //更新页面
                                MyBaseAdapter.this.notifyDataSetChanged();
                            }else {
                                //加载失败
                                holder.setData(MoreHolder.STATE_MORE_ERROE);
                            }
                            isLoadMore = false;
                        }
                    });

                }
            }.start();
        }

    }

    //由子类实现更多数据
    public abstract ArrayList<T> onLoadMore();

    //是否可用加载更多数据
    public boolean hasMore(){
        return true;
    }

    // 返回当前页面的holder对象, 必须子类实现
    public abstract BaseHolder<T> getHolder();

    //获取当前集合大小
    public int getListSize(){

        return data.size();
    }
}
