package android.ye.googlemarket.fragment;

import android.content.pm.PackageManager;
import android.view.View;
import android.ye.googlemarket.Adapter.MyBaseAdapter;
import android.ye.googlemarket.Holder.BaseHolder;
import android.ye.googlemarket.Holder.CategoryHolder;
import android.ye.googlemarket.Holder.TitleHolder;
import android.ye.googlemarket.Http.protocol.CategoryProtocol;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.View.LoadingPager;
import android.ye.googlemarket.View.MyListView;
import android.ye.googlemarket.domain.CategoryInfo;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

/**
 * Created by ye on 2016/11/9.
 */
public class CategoryFragment extends BaseFragment {

    private ArrayList<CategoryInfo> data;

    @Override
    public View onCreateSuccessView() {
        MyListView view = new MyListView(UIUtils.getContext());
        view.setAdapter(new CategoryAdapter(data));
        return view;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        CategoryProtocol protocol = new CategoryProtocol();
        data = protocol.getData(0);

        return check(data);
    }

    class CategoryAdapter extends MyBaseAdapter<CategoryInfo>{

        private CategoryInfo categoryInfo;

        public CategoryAdapter(ArrayList<CategoryInfo> data) {
            super(data);
        }

        @Override
        public int getInnerType(int position) {
            categoryInfo = data.get(position);
            if (categoryInfo.isTitle){
                //返回标题类型， 原来类型基础上加1
                return super.getInnerType(position)+1;
            }else {
                //返回普通类型
                return super.getInnerType(position);
            }

        }

        @Override
        public int getViewTypeCount() {
            // 在原来基础上增加一种标题类型
            return super.getViewTypeCount()+1;
        }

        @Override
        public ArrayList<CategoryInfo> onLoadMore() {
            return null;
        }

        @Override
        public BaseHolder<CategoryInfo> getHolder() {
            // 判断是标题类型还是普通分类类型, 来返回不同的holder
           if (categoryInfo.isTitle){
               return new TitleHolder();
           }
           else {
               return new CategoryHolder();
           }
        }

        //只有一页，无须更多数据，需要隐藏加载更多的布局
        @Override
        public boolean hasMore() {
            return false;
        }
    }
}
