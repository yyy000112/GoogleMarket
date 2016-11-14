package android.ye.googlemarket.fragment;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.ye.googlemarket.Fly.ShakeListener;
import android.ye.googlemarket.Fly.StellarMap;
import android.ye.googlemarket.Http.protocol.RecommendProtocol;
import android.ye.googlemarket.Utils.UIUtils;
import android.ye.googlemarket.View.LoadingPager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ye on 2016/11/9.
 */
public class RecommendFragment extends BaseFragment {

    private ArrayList<String> data;

    @Override
    public View onCreateSuccessView() {

        final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        stellarMap.setAdapter(new RecommendAdapter());
        // 随机方式, 将控件划分为9行6列的的格子, 然后在格子中随机展示
        stellarMap.setRegularity(6, 9);
        //内边距
        int padding = UIUtils.dip2px(10);
        stellarMap.setPadding(padding, padding, padding, padding);
        //设置默认页，第一页数据
        stellarMap.setGroup(0,true);

        ShakeListener shake = new ShakeListener(UIUtils.getContext());
        shake.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                // 跳到下一页数据
                stellarMap.zoomIn();
            }
        });
        return stellarMap;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        RecommendProtocol protocol = new RecommendProtocol();
        data = protocol.getData(0);

        return check(data);
    }

     class RecommendAdapter implements StellarMap.Adapter {
         @Override
         public int getGroupCount() {
             return 2;
         }

         @Override
         public int getCount(int group) {
             int count = data.size()/getGroupCount();
             if (group == count -1){
                 // 最后一页, 将除不尽,余下来的数量追加在最后一页, 保证数据完整不丢失
                 count += data.size()%getGroupCount();
             }
             return count;
         }

         @Override
         public View getView(int group, int position, View convertView) {
            // 因为position每组都会从0开始计数, 所以需要将前面几组数据的个数加起来,才能确定当前组获取数据的角标位置
             position += getCount(group-1);
             final String keyword =data.get(position);
             TextView view = new TextView(UIUtils.getContext());
             view.setText(keyword);

             Random random = new Random();
             //字体随机大小
             int size = 16 + random.nextInt(10);
             //COMPLEX_UNIT_SP值文字大小的单位
             view.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
             //字体颜色随机，r g b, 0-255 -> 30-230, 颜色值不能太小或太大, 从而避免整体颜色过亮或者过暗
             int r = 30 + random.nextInt(200);
             int g = 30 + random.nextInt(200);
             int b = 30 + random.nextInt(200);
             view.setTextColor(Color.rgb(r,g,b));

             view.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     UIUtils.ToastShow(keyword);
                 }
             });
             return view;
         }


         @Override
         public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            if (isZoomIn){
                // 往下滑加载上一页
                if (group>0){
                    group--;
                }else {
                    // 跳到最后一页
                    group = getGroupCount()-1;
                }
            }else {
                if (group<getGroupCount()-1){
                    group++;
                }else {
                    // 跳到第一页
                    group = 0;
                }

            }
             return group;
         }
     }
}
