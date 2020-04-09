package com.yline.address.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yline.address.R;

/**
 * 最多支持4个连续
 *
 * @author yline 2020-04-08 -- 22:44
 */
public class RegionTabView extends RelativeLayout {
    private TextView mOneTextView;
    private TextView mTwoTextView;
    private TextView mThreeTextView;
    private TextView mFourTextView;
    private View mOneLine;
    private View mTwoLine;
    private View mThreeLine;
    private View mFourLine;

    public RegionTabView(Context context) {
        this(context, null);
    }

    public RegionTabView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_region_tab, this);

        mOneTextView = findViewById(R.id.view_region_tab_one);
        mTwoTextView = findViewById(R.id.view_region_tab_two);
        mThreeTextView = findViewById(R.id.view_region_tab_three);
        mFourTextView = findViewById(R.id.view_region_tab_four);

        mOneLine = findViewById(R.id.view_region_tab_one_line);
        mTwoLine = findViewById(R.id.view_region_tab_two_line);
        mThreeLine = findViewById(R.id.view_region_tab_three_line);
        mFourLine = findViewById(R.id.view_region_tab_four_line);
    }

    /**
     * 初始状态，开始选择省份
     */
    public void selectOneState(String province) {
        mOneTextView.setTextColor(getContext().getResources().getColor(R.color.ff5000));
        mOneTextView.setText(getCheckTitle(province));
        mOneLine.setVisibility(VISIBLE);

        mTwoTextView.setText("");
        mTwoLine.setVisibility(GONE);

        mThreeTextView.setText("");
        mThreeLine.setVisibility(GONE);

        mFourTextView.setText("");
        mFourLine.setVisibility(GONE);
    }

    /**
     * 已经选择了省份，开始选择城市
     *
     * @param province 省份
     */
    public void selectTwoState(String province, String city) {
        mOneTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
        mOneTextView.setText(province);
        mOneLine.setVisibility(GONE);

        mTwoTextView.setTextColor(getContext().getResources().getColor(R.color.ff5000));
        mTwoTextView.setText(getCheckTitle(city));
        mTwoLine.setVisibility(VISIBLE);

        mThreeTextView.setText("");
        mThreeLine.setVisibility(GONE);

        mFourTextView.setText("");
        mFourLine.setVisibility(GONE);
    }

    /**
     * 已经选择了省份、城市，开始选择地区
     *
     * @param province 省份
     * @param city     城市
     * @param area     地区
     */
    public void selectThreeState(String province, String city, String area) {
        mOneTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
        mOneTextView.setText(province);
        mOneLine.setVisibility(GONE);

        mTwoTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
        mTwoTextView.setText(city);
        mTwoLine.setVisibility(GONE);

        mThreeTextView.setTextColor(getContext().getResources().getColor(R.color.ff5000));
        mThreeTextView.setText(getCheckTitle(area));
        mThreeLine.setVisibility(VISIBLE);
    }

    /**
     * 已经选择了省份、城市、地区，开始选择街道
     *
     * @param province 省份
     * @param city     城市
     * @param area     地区
     */
    public void selectFourState(String province, String city, String area, String street) {
        mOneTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
        mOneTextView.setText(province);
        mOneLine.setVisibility(GONE);

        mTwoTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
        mTwoTextView.setText(city);
        mTwoLine.setVisibility(GONE);

        mThreeTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
        mThreeTextView.setText(area);
        mThreeLine.setVisibility(GONE);

        mFourTextView.setTextColor(getContext().getResources().getColor(R.color.ff5000));
        mFourTextView.setText(getCheckTitle(street));
        mFourLine.setVisibility(VISIBLE);
    }

    private String getCheckTitle(String title) {
        return TextUtils.isEmpty(title) ? "请选择" : title;
    }

    public void setOnOneClickListener(final OnClickListener listener) {
        mOneTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOneTextView.setTextColor(getContext().getResources().getColor(R.color.ff5000));
                mOneLine.setVisibility(VISIBLE);

                mTwoTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mTwoLine.setVisibility(GONE);

                mThreeTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mThreeLine.setVisibility(GONE);

                mFourTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mFourLine.setVisibility(GONE);

                if (null != listener) {
                    listener.onClick(v);
                }
            }
        });
    }

    public void setOnTwoClickListener(final OnClickListener listener) {
        mTwoTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOneTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mOneLine.setVisibility(GONE);

                mTwoTextView.setTextColor(getContext().getResources().getColor(R.color.ff5000));
                mTwoLine.setVisibility(VISIBLE);

                mThreeTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mThreeLine.setVisibility(GONE);

                mFourTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mFourLine.setVisibility(GONE);

                if (null != listener) {
                    listener.onClick(v);
                }
            }
        });
    }

    public void setOnThreeClickListener(final OnClickListener listener) {
        mThreeTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOneTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mOneLine.setVisibility(GONE);

                mTwoTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mTwoLine.setVisibility(GONE);

                mThreeTextView.setTextColor(getContext().getResources().getColor(R.color.ff5000));
                mThreeLine.setVisibility(VISIBLE);

                mFourTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mFourLine.setVisibility(GONE);

                if (null != listener) {
                    listener.onClick(v);
                }
            }
        });
    }

    public void setOnFourClickListener(final OnClickListener listener) {
        mFourTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOneTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mOneLine.setVisibility(GONE);

                mTwoTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mTwoLine.setVisibility(GONE);

                mThreeTextView.setTextColor(getContext().getResources().getColor(R.color.v333333));
                mThreeLine.setVisibility(GONE);

                mFourTextView.setTextColor(getContext().getResources().getColor(R.color.ff5000));
                mFourLine.setVisibility(VISIBLE);

                if (null != listener) {
                    listener.onClick(v);
                }
            }
        });
    }
}
