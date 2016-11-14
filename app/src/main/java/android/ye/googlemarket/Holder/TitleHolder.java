package android.ye.googlemarket.Holder;

import android.view.View;
import android.widget.TextView;
import android.ye.googlemarket.R;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.domain.CategoryInfo;

/**
 * 分类模块标题holder
 * Created by ye on 2016/11/13.
 */
public class TitleHolder extends BaseHolder<CategoryInfo> {

    private TextView tvTitle;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_title);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void refreshView(CategoryInfo data) {
        tvTitle.setText(data.title);
    }
}
