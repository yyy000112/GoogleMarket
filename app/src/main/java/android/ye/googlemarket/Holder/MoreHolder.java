package android.ye.googlemarket.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.UIUtils;

/**
 * Created by ye on 2016/11/10.
 */
public class MoreHolder extends BaseHolder<Integer> {

    //加载更多布局中的三种状况：1，加载更多 2，加载更多失败 3，没有更多数据加载
    public static final int STATE_MORE_MORE = 1;
    public static final int STATE_MORE_ERROE = 2;
    public static final int STATE_MORE_NONE = 3;


    private ProgressBar pbLoadMore;
    private LinearLayout llLoadMore;
    private TextView tvError;


    public MoreHolder(boolean hasMore) {
        //如果有更多数据,状态为STATE_MORE_MORE,否则为STATE_MORE_NONE,将此状态传递给父类的data
        setData(hasMore?STATE_MORE_MORE:STATE_MORE_NONE);
    }

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_more);
        llLoadMore = (LinearLayout) view.findViewById(R.id.ll_load_more);
        tvError = (TextView) view.findViewById(R.id.tv_load_error);
        return view;
    }

    @Override
    public void refreshView(Integer data) {
        switch (data){
            case STATE_MORE_MORE:
                llLoadMore.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.GONE);
                break;
            case STATE_MORE_ERROE:
                llLoadMore.setVisibility(View.GONE);
                tvError.setVisibility(View.VISIBLE);
                break;
            case STATE_MORE_NONE:
                llLoadMore.setVisibility(View.GONE);
                tvError.setVisibility(View.GONE);
                break;
        }
    }
}
