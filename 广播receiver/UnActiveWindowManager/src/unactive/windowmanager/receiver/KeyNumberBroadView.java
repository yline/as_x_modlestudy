package unactive.windowmanager.receiver;

import java.util.ArrayList;
import java.util.Random;

import com.unactive.windowmanager.R;
import com.yline.log.LogFileUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 单独一个随机数字键盘
 * 点击事件:
 * 1,确定按钮
 * 2,回退按钮
 * 3,数字按键
 * 
 * 屏幕适配         无
 * 数字随机         有
 * 
 * simple introduction
 */
public class KeyNumberBroadView extends LinearLayout implements View.OnClickListener
{
    private static final String TAG = "KeyNumberBroadView";
    
    private final static int MIN_SECURE_STR = 2; // 每个字符对应的 加密符号 的最小长度
    
    // 这里的数字,对应List中的第几个
    private Button mBtn0, mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8, mBtn9;
    
    private Button mBtnOk, mBtnDelete;
    
    private Random mRandom;
    
    // 对应的数字键盘中的内容
    private ArrayList<String> tempStrList = null;
    
    /** 确定按钮,监听器 */
    private onSureClickListener mSureClickListener;
    
    /** 数字按钮,监听器 */
    private onNumberClickListener mNumberClickListener;
    
    /** 回退按钮,监听器 */
    private onDeleteClickListener mDeleteClickListener;
    
    public KeyNumberBroadView(Context context)
    {
        this(context, null);
    }
    
    public KeyNumberBroadView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        initView();
        initData();
    }
    
    private void initView()
    {
        LogFileUtil.v(TAG, "initView");
        
        View containView = LayoutInflater.from(getContext()).inflate(R.layout.view_keybroad_number, null);
        
        mBtnOk = (Button)containView.findViewById(R.id.btn_ok);
        mBtnDelete = (Button)containView.findViewById(R.id.btn_delete);
        mBtn0 = (Button)containView.findViewById(R.id.btn_0);
        mBtn1 = (Button)containView.findViewById(R.id.btn_1);
        mBtn2 = (Button)containView.findViewById(R.id.btn_2);
        mBtn3 = (Button)containView.findViewById(R.id.btn_3);
        mBtn4 = (Button)containView.findViewById(R.id.btn_4);
        mBtn5 = (Button)containView.findViewById(R.id.btn_5);
        mBtn6 = (Button)containView.findViewById(R.id.btn_6);
        mBtn7 = (Button)containView.findViewById(R.id.btn_7);
        mBtn8 = (Button)containView.findViewById(R.id.btn_8);
        mBtn9 = (Button)containView.findViewById(R.id.btn_9);
        
        addView(containView);
    }
    
    private void initData()
    {
        LogFileUtil.v(TAG, "initData");
        
        // 产生 0-9 一共10个不同的字符串
        tempStrList = new ArrayList<String>();
        mRandom = new Random();
        
        String tempStr = "";
        do
        {
            tempStr = String.valueOf(mRandom.nextInt(10));
            if (!tempStrList.contains(String.valueOf(tempStr)))
            {
                tempStrList.add(tempStr);
            }
        } while (tempStrList.size() < 10);
        LogFileUtil.v(TAG, tempStrList.toString());
        
        // 设置监听时间,并且传递对应的值
        mBtn0.setOnClickListener(this);
        mBtn0.setText(tempStrList.get(0));
        mBtn0.setTag(tempStrList.get(0));
        
        mBtn1.setOnClickListener(this);
        mBtn1.setText(tempStrList.get(1));
        mBtn1.setTag(tempStrList.get(1));
        
        mBtn2.setOnClickListener(this);
        mBtn2.setText(tempStrList.get(2));
        mBtn2.setTag(tempStrList.get(2));
        
        mBtn3.setOnClickListener(this);
        mBtn3.setText(tempStrList.get(3));
        mBtn3.setTag(tempStrList.get(3));
        
        mBtn4.setOnClickListener(this);
        mBtn4.setText(tempStrList.get(4));
        mBtn4.setTag(tempStrList.get(4));
        
        mBtn5.setOnClickListener(this);
        mBtn5.setText(tempStrList.get(5));
        mBtn5.setTag(tempStrList.get(5));
        
        mBtn6.setOnClickListener(this);
        mBtn6.setText(tempStrList.get(6));
        mBtn6.setTag(tempStrList.get(6));
        
        mBtn7.setOnClickListener(this);
        mBtn7.setText(tempStrList.get(7));
        mBtn7.setTag(tempStrList.get(7));
        
        mBtn8.setOnClickListener(this);
        mBtn8.setText(tempStrList.get(8));
        mBtn8.setTag(tempStrList.get(8));
        
        mBtn9.setOnClickListener(this);
        mBtn9.setText(tempStrList.get(9));
        mBtn9.setTag(tempStrList.get(9));
        
        mBtnOk.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                setSureClick(v);
            }
        });
        
        mBtnDelete.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                setDeleteClick(v);
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        setNumberClick(v, (String)v.getTag(), mRandom.nextInt(MIN_SECURE_STR) + MIN_SECURE_STR);
    }
    
    /**
     * 确定按钮,点击,即触发;
     * @param view
     */
    private void setSureClick(View view)
    {
        if (null != mSureClickListener)
        {
            mSureClickListener.onSureClick(view);
        }
    }
    
    public void setOnSureClickListener(onSureClickListener sureClickListener)
    {
        this.mSureClickListener = sureClickListener;
    }
    
    public interface onSureClickListener
    {
        /**
         * 确定按钮,点击,即触发;
         * @param view
         */
        void onSureClick(View view);
    }
    
    /**
     * 数字按钮,点击,即触发
     * @param view
     * @param number 当前内容
     * @param randomInt 当前内容对应的随机数
     */
    private void setNumberClick(View view, String number, int randomInt)
    {
        if (null != mNumberClickListener)
        {
            mNumberClickListener.onNumberClick(view, number, randomInt);
        }
    }
    
    public void setOnNumberClickListener(onNumberClickListener numberClickListener)
    {
        this.mNumberClickListener = numberClickListener;
    }
    
    public interface onNumberClickListener
    {
        /**
         * 数字按钮,点击,即触发
         * @param view
         * @param number 当前内容
         * @param randomInt 当前内容对应的随机数
         */
        void onNumberClick(View view, String number, int randomInt);
    }
    
    /**
     * 回退按钮,点击,触发
     * @param view
     */
    private void setDeleteClick(View view)
    {
        if (null != mDeleteClickListener)
        {
            mDeleteClickListener.onDeleteClick(view);
        }
    }
    
    public void setOnDeleteClickListener(onDeleteClickListener deleteClickListener)
    {
        this.mDeleteClickListener = deleteClickListener;
    }
    
    public interface onDeleteClickListener
    {
        /**
         * 回退按钮,点击,触发
         * @param view
         */
        void onDeleteClick(View view);
    }
}
