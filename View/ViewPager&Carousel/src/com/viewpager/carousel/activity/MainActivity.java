package com.viewpager.carousel.activity;

import com.viewpager.carousel.R;
import com.viewpager.carousel.fragment.CarouselPagerFragment;
import com.viewpager.carousel.fragment.CarouselPagerFragmentParams;
import com.yline.base.BaseFragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends BaseFragmentActivity implements CarouselPagerFragment.onPagerClickListener
{
    private FragmentManager fragmentManager = getSupportFragmentManager();
    
    private CarouselPagerFragment carouselFragment;
    
    private ListView listView;
    
    private int[] mImageSrc = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5,};
    
    private static final String[] strs =
        new String[] {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "nineth", "ten"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        initView();
        initData();
    }
    
    private void initView()
    {
        listView = (ListView)findViewById(R.id.show_listview);
    }
    
    private void initData()
    {
        CarouselPagerFragmentParams params = new CarouselPagerFragmentParams();
        params.setResInt(mImageSrc);
        
        params.setViewLayout(getScreenWidth(getApplicationContext()), getScreenWidth(getApplicationContext()) / 2);
        params.setRecycleSetting(true, true, true);
        params.setPointStartPosition(0);
        
        carouselFragment = new CarouselPagerFragment(params);
        
        fragmentManager.beginTransaction().add(R.id.fragment_carousepager, carouselFragment).commit();
        
        // listView计算高度就可以了
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs));
    }
    
    /** 获取屏幕宽度 */
    private int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    
    @Override
    public void onPagerClicked(View v, int position)
    {
        Toast.makeText(getApplicationContext(), "position = " + position, Toast.LENGTH_SHORT).show();
    }
    
}
