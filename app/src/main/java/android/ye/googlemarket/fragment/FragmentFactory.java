package android.ye.googlemarket.fragment;

import java.util.HashMap;

/**
 * fragment工厂
 * Created by ye on 2016/11/9.
 */
public class FragmentFactory {

    private static HashMap<Integer,BaseFragment> mFragmentMap = new HashMap<Integer,BaseFragment>();

    public static BaseFragment createFragment(int pos){
        //先从集合中取，如果没有则新建一个
        BaseFragment fragment = mFragmentMap.get(pos);
        if (fragment == null){
            switch (pos){
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new AppFragment();
                    break;
                case 2:
                    fragment = new GameFragment();
                    break;
                case 3:
                    fragment = new TopicFragment();
                    break;
                case 4:
                    fragment = new RecommendFragment();
                    break;
                case 5:
                    fragment = new CategoryFragment();
                    break;
                case 6:
                    fragment = new HotFragment();
                    break;
            }
            //保存在集合中
            mFragmentMap.put(pos,fragment);
        }
        return fragment;
    }
}
