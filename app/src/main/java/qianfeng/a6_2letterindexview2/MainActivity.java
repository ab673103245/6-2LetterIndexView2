package qianfeng.a6_2letterindexview2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<User> list;
    private MyAdapter adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        TextView tv_center = (TextView) findViewById(R.id.tv_center);
        final LetterIndexView letterIndexView = (LetterIndexView) findViewById(R.id.letterView);
        String[] usernameArray = getResources().getStringArray(R.array.arrUsernames);

        initData(usernameArray);

        adapter = new MyAdapter(list, this);
        lv.setAdapter(adapter);

        letterIndexView.setShowText(tv_center);
        letterIndexView.setUpdateLetterIndexView(new LetterIndexView.UpdateLetterIndexView() { // 就是这里有点
            @Override
            public void updateShowText(int letter) {  // 我一开始都不知道这个字母letter要来做什么，其实是拿到这个字母所在的分组的第一个item的索引,方便我设置ListView的setSelection()
                // 这里要用到Adapter里面的方法啦！
                // 获取字母在List<String>的索引位置
                //关键和核心代码是：对MyAdapter里面实现SectionIndex接口的两个方法的运用，什么时候该运用，你在适配的是List<User>,
                // 其实有用的是List<String>这个数据哦,其实你只要搞懂现在你其实是在适配这个数据就可以了！
                int positionForSection = adapter.getPositionForSection(letter);// 字母是分组B的话，就返回它的在List<String>里面的索引位置
                lv.setSelection(positionForSection);  // 设置该对应分组B所对应List<String>里面的position，这个才是和ListView的集合唯一对应的东西

            }
        });


        // 最后是ListView滑动对应右边导航栏的颜色改变，这个更简单
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 我这里返回的是分组，分组就是B，C等字母
                int sectionForPosition = adapter.getSectionForPosition(firstVisibleItem); // 这里是要找到position所在的分组，即B，C字母，再在自定义View里面对这个字母进行画笔颜色选择，再重绘。
                letterIndexView.updateShowText(sectionForPosition);


            }
        });

    }

    private void initData(String[] usernameArray) {
        list = new ArrayList<>();
        ChineseToPinyinHelper chineseToPinyinHelper = new ChineseToPinyinHelper();
        for (int i = 0; i < usernameArray.length; i++) {
            String pinyin = chineseToPinyinHelper.getPinyin(usernameArray[i]);
            String substring = pinyin.substring(0, 1).toUpperCase();// 从0开始，长度为1的子字符串，

            // 下面是拼音的首字母，还有#号的情况,字符串里面有个正则表达式匹配，先比较A-Z，再将其他的字符都统一归到#号上
            if (!substring.matches("[A-Z]")) { // 如果不是A-Z的话，统一归到#号上去处理，matches返回的是boolean值
                substring = "#";
            }
            User user = new User(usernameArray[i], pinyin, substring, R.mipmap.ic_launcher);
            list.add(user);
        }
        // 现在要给list重新排序，按照字母顺序，要用到工具类啦！
        Collections.sort(list, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getFirstLetter().equals("#")) {
                    return 1; // 如果o1是#号，就把o1作为最大的
                } else if (o2.getFirstLetter().equals("#")) {
                    return -1; // 如果o2是#号，就把o2作为最大的
                } else {
                    return o1.getFirstLetter().compareTo(o2.getFirstLetter());// 否则就按照包装类里面的自然顺序进行排序，字符串也有自然顺序
                }
            }
        });
        for (User u : list) {
            Log.d("google-my:", "initData: " + u);
        }
    }
}
