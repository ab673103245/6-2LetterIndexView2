package qianfeng.a6_2letterindexview2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class LetterIndexView extends View {

    private int currentPosition = -1;  // 记录手指按下的地方

    private Paint paint;

    private String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};


    public LetterIndexView(Context context) {
        this(context, null);
    }

    public LetterIndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(18);
//        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int perLettersHeight = getMeasuredHeight() / letters.length;

        // 绘制 字母
        for (int i = 0; i < letters.length; i++) {               // 画笔可以测量Text的宽度
            if (currentPosition == i) {
                paint.setColor(Color.RED);
            } else {
                paint.setColor(Color.BLACK);
            }
            // 精确指定坐标就不需要模式
            canvas.drawText(letters[i], (getMeasuredWidth() - paint.measureText(letters[i])) / 2, (i + 1) * perLettersHeight, paint);
        }

    }

    // 处理屏幕的操作啦！onTouchEvent()这个才是最原始的方法，setOnclick()都是从这个方法上通过接口回调封装起来的
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 我怎样知道是哪个字母被点击了呢？getY()
        float y = event.getY();
        int perLettersHeight = getMeasuredHeight() / letters.length;
        // 记录当前被按下的字母的索引，方便我设置画笔的颜色
        currentPosition = (int) y / perLettersHeight; // 因为坐标是从0开始，所以要强转为int型
        // 这里捕捉到当前触碰的字母的索引。currentPosition


        switch (event.getAction()) // 得到操作事件的常量
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: // 在屏幕上拖动
                setBackgroundColor(Color.parseColor("#11000000"));
                if (tv != null) {
                    if (currentPosition > -1 && currentPosition < letters.length) {
                        tv.setVisibility(View.VISIBLE);
                        tv.setText(letters[currentPosition]);
                        if (updateLetterIndexView != null) {
                            updateLetterIndexView.updateShowText(letters[currentPosition].charAt(0)); // 传一个字母过来干嘛？重新绘制这个字母？
                        }

                    }
                }
                break;
            case MotionEvent.ACTION_UP: // 抬起
                setBackgroundColor(Color.TRANSPARENT); // 颜色的透明度变成完全透明
                if (tv != null) {
                    tv.setVisibility(View.GONE);
                }
                break;
        }

        // 出了switch之后，重新绘制就可以了，重新调用onDraw方法重新绘制画布
        invalidate();

        return true; // true代表事件在这里处理，而不是层层往下传递，最先拿到事件的是屏幕。
        // 然后屏幕给了FragmentLayout--->RelativeLaoyut-->LetterIndexView,这里返回true，代表这个事件都在这里处理，如果不是true而是false，就会层层传递到Activity中最终处理。
    }

    private TextView tv;

    // 这个setShowText只是显示一个TextView表示你选中过这些字母，然后把它显示出来。
    public void setShowText(TextView tv) {
        this.tv = tv;
    }

    private UpdateLetterIndexView updateLetterIndexView;

    public interface UpdateLetterIndexView {
        void updateShowText(int letter); // 从Activity中传递一个position过来，更新这个position所对应的字母的画笔颜色，再重绘就可以了。
    }

    public void setUpdateLetterIndexView(UpdateLetterIndexView updateLetterIndexView)
    {
        this.updateLetterIndexView = updateLetterIndexView;
    }



    // 下面这个方法，只是更新由于ListView的滑动，而要重新绘制右边的导航栏的字母的颜色而已！！！

    // 要提供一个方法，将外面传进来的这个letter遍历加重绘
    public void updateShowText(int letter) // 传进来的这个是分组的索引，就是B，C的索引 B-->1
    {
        for(int i = 0; i < letters.length; i++)
        {
            if(letters[i].charAt(0) == letter) // B == B
            {
                currentPosition = i;  // 如果 B==B ,当前的位置就设置为i(这个i赋值给currentPosition，相当于这个i的位置被按住了，只要再调用重绘方法即可)
                invalidate();
            }
        }
    }


}
