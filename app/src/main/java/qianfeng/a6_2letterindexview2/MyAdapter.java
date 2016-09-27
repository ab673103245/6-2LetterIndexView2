package qianfeng.a6_2letterindexview2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class MyAdapter extends BaseAdapter implements SectionIndexer {
    private List<User> list;
    private LayoutInflater inflater;
    private Context context;

    public MyAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.lv_item,parent,false);
            holder = new ViewHolder();
            holder.tv_firstLetter = (TextView) convertView.findViewById(R.id.tv_firstLetter);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = list.get(position);
        holder.tv_username.setText(user.getUsername());

        // 判断该显示哪个TextView了
        //获取当前要显示的item所属的分组
        int sectionForPosition = getSectionForPosition(position);
        // 这个就是同一个item中要显示的字母的第一个位置   //获取该分组中第一个item的position
        int positionForSection = getPositionForSection(sectionForPosition); // 只返回同字母item的第一个索引的位置
        if(position == positionForSection)
        {
            holder.tv_firstLetter.setVisibility(View.VISIBLE);

            holder.tv_firstLetter.setText(user.getFirstLetter());
        }else
        {
            holder.tv_firstLetter.setVisibility(View.GONE);
        }



        return convertView;
    }
    class ViewHolder
    {
        TextView tv_firstLetter,tv_username;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    // 获得position通过分组的item(A--->0,B-->2) , 分组的int其实是一个char
    @Override
    public int getPositionForSection(int sectionIndex) {
        // 遍历list集合
        for (int i = 0; i < list.size(); i++) {
            // 如果传进来的char，和list里面的First匹配的话，就返回该字母的第一个位置的position
            if (list.get(i).getFirstLetter().charAt(0) == sectionIndex) { // 注意是int值和int值之间的比较
                return i; // 只返回同字母item的第一个索引的位置
            }
        }
        // 注意这里-1对ListView里面的setSelection() 方法的影响
        return -1; // 表示ListView中找不到匹配的项, ListView会停留在上一个能找到i的setSelection(int i)的位置
    }

    // 获得分组的item通过position(0-->A,1--->A,2--->B)
    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getFirstLetter().charAt(0);
    }
}
