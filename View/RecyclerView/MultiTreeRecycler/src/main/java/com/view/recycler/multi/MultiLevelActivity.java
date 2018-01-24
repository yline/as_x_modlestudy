package com.view.recycler.multi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 多级列表
 *
 * @author yline 2017/12/18 -- 20:01
 * @version 1.0.0
 */
public class MultiLevelActivity extends BaseAppCompatActivity {

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, MultiLevelActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_recycler);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setBackgroundColor(ContextCompat.getColor(this, R.color.multi_level_recycler_bg));

        MultiLevelAdapter multiLevelAdapter = new MultiLevelAdapter();
        recyclerView.setAdapter(multiLevelAdapter);

        try {
            // 内容
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = getAssets().open("multi_level_data");
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, len));
            }

            // 类型
            Type type = new TypeToken<ArrayList<MultiLevelModel.ProvinceModel>>() {
            }.getType();

            List<MultiLevelModel.ProvinceModel> provinceModelList = new Gson().fromJson(stringBuilder.toString(), type);

            multiLevelAdapter.setData(provinceModelList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MultiLevelAdapter extends AbstractMultiLevelAdapter {

        @Override
        public void onBindProvinceViewHolder(RecyclerViewHolder holder, MultiLevelModel.ProvinceModel provinceModel, int position) {
            super.onBindProvinceViewHolder(holder, provinceModel, position);

            holder.setText(R.id.item_multi_level_province_tv, provinceModel.getRegion_name());
        }

        @Override
        public void onBindCityViewHolder(RecyclerViewHolder holder, MultiLevelModel.CityModel cityModel, int position) {
            super.onBindCityViewHolder(holder, cityModel, position);

            holder.setText(R.id.item_multi_level_city_tv, cityModel.getRegion_name());
        }

        @Override
        public void onBindAreaViewHolder(RecyclerViewHolder holder, MultiLevelModel.AreaModel areaModel, int position) {
            super.onBindAreaViewHolder(holder, areaModel, position);

            holder.setText(R.id.item_multi_level_area_tv, areaModel.getRegion_name());
        }
    }

    /**
     * 剩余问题
     */
    private class AbstractMultiLevelAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        private List<Object> mDataList;
        private SparseBooleanArray mProvinceStateArray;
        private SparseBooleanArray mCityStateArray;

        private AbstractMultiLevelAdapter() {
            this.mDataList = new ArrayList<>();
            this.mProvinceStateArray = new SparseBooleanArray();
            this.mCityStateArray = new SparseBooleanArray();
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == MultiLevelModel.TYPE_PROVINCE) {
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_level_province, parent, false));
            } else if (viewType == MultiLevelModel.TYPE_CITY) {
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_level_city, parent, false));
            } else {
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_level_area, parent, false));
            }
        }

        public void setData(List<MultiLevelModel.ProvinceModel> provinceModelList) {
            if (null != provinceModelList) {
                mDataList.clear();
                for (MultiLevelModel.ProvinceModel provinceModel : provinceModelList) {
                    mProvinceStateArray.put(provinceModel.hashCode(), false);
                    mDataList.add(provinceModel);
                    for (MultiLevelModel.CityModel cityModel : provinceModel.getCity()) {
                        mCityStateArray.put(cityModel.hashCode(), false);
                    }
                }
                notifyDataSetChanged();
            }
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            Object itemData = mDataList.get(position);
            if (itemData instanceof MultiLevelModel.ProvinceModel) {
                onBindProvinceViewHolder(holder, (MultiLevelModel.ProvinceModel) itemData, position);
            } else if (itemData instanceof MultiLevelModel.CityModel) {
                onBindCityViewHolder(holder, (MultiLevelModel.CityModel) itemData, position);
            } else {
                onBindAreaViewHolder(holder, (MultiLevelModel.AreaModel) itemData, position);
            }
        }

        public void onBindProvinceViewHolder(RecyclerViewHolder holder, final MultiLevelModel.ProvinceModel provinceModel, final int position) {
            // 点击 浙江
            // 打开时，打开 浙江下一级所有城市，关闭其它省份以及其他省份下的城市
            // 关闭时，关闭 浙江所有城市、地区
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int key = provinceModel.hashCode();
                    if (null != provinceModel.getCity()) {
                        if (mProvinceStateArray.get(key)) {
                            mProvinceStateArray.put(key, false);
                            mDataList.removeAll(provinceModel.getCity());

                            for (MultiLevelModel.CityModel cityModel : provinceModel.getCity()) {
                                if (mCityStateArray.get(cityModel.hashCode())) {
                                    mDataList.removeAll(cityModel.getArea());
                                }
                            }
                        } else {
                            mProvinceStateArray.put(key, true);
                            mDataList.addAll(position + 1, provinceModel.getCity());

                            // 修改状态
                            mProvinceStateArray = new SparseBooleanArray(); // 关闭其它省份
                            mCityStateArray = new SparseBooleanArray(); // 关闭其它省份下的城市
                            mProvinceStateArray.put(key, true); // 打开对应的项

                            // 修改数据，必须先添加后删减，否则顺序会乱
                            // 添加需要的数据
                            List<MultiLevelModel.CityModel> newAttachCityList = provinceModel.getCity();
                            mDataList.addAll(position + 1, newAttachCityList);

                            // 清除其它数据
                            for (Object object : new ArrayList<>(mDataList)) {
                                if (object instanceof MultiLevelModel.CityModel && !newAttachCityList.contains(object)) {
                                    mDataList.remove(object);
                                } else if (object instanceof MultiLevelModel.AreaModel) {
                                    mDataList.remove(object);
                                }
                            }
                        }

                        notifyDataSetChanged();
                    }
                }
            });
        }

        public void onBindCityViewHolder(RecyclerViewHolder holder, final MultiLevelModel.CityModel cityModel, final int position) {
            // 点击 杭州
            // 打开时，打开 杭州所有地区，关闭浙江省的其它城市
            // 关闭时，关闭 杭州地区
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int key = cityModel.hashCode();
                    if (null != cityModel.getArea()) {
                        if (mCityStateArray.get(key)) {
                            mCityStateArray.put(key, false);
                            mDataList.removeAll(cityModel.getArea());
                        } else {
                            // 修改状态
                            mCityStateArray = new SparseBooleanArray(); // 关闭其它城市
                            mCityStateArray.put(key, true); // 打开对应的项

                            // 修改数据，必须先添加后删减，否则顺序会乱
                            // 添加需要的数据
                            List<MultiLevelModel.AreaModel> newAttachAreaList = cityModel.getArea();
                            mDataList.addAll(position + 1, cityModel.getArea());

                            // 清除其它数据
                            for (Object object : new ArrayList<>(mDataList)) {
                                if (object instanceof MultiLevelModel.AreaModel && !newAttachAreaList.contains(object)) {
                                    mDataList.remove(object);
                                }
                            }
                        }
                    }

                    notifyDataSetChanged();
                }
            });
        }

        public void onBindAreaViewHolder(RecyclerViewHolder holder, final MultiLevelModel.AreaModel areaModel, int position) {
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SDKManager.toast("Click:" + areaModel.getRegion_name());
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            Object itemData = mDataList.get(position);
            if (itemData instanceof MultiLevelModel.ProvinceModel) {
                return MultiLevelModel.TYPE_PROVINCE;
            } else if (itemData instanceof MultiLevelModel.CityModel) {
                return MultiLevelModel.TYPE_CITY;
            } else {
                return MultiLevelModel.TYPE_AREA;
            }
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int viewType = getItemViewType(position);
                        if (viewType == MultiLevelModel.TYPE_PROVINCE) {
                            return gridLayoutManager.getSpanCount();
                        } else if (viewType == MultiLevelModel.TYPE_CITY) {
                            return gridLayoutManager.getSpanCount();
                        } else {
                            return spanSizeLookup.getSpanSize(position);
                        }
                    }
                });

                gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
            }
        }
    }

    private static class MultiLevelModel implements Serializable {
        private static final int TYPE_PROVINCE = 0;
        private static final int TYPE_CITY = 1;
        private static final int TYPE_AREA = 2;

        private static class ProvinceModel implements Serializable {
            private String region_id;
            private String region_name;
            private List<CityModel> city;

            public String getRegion_id() {
                return region_id;
            }

            public void setRegion_id(String region_id) {
                this.region_id = region_id;
            }

            public String getRegion_name() {
                return region_name;
            }

            public void setRegion_name(String region_name) {
                this.region_name = region_name;
            }

            public List<CityModel> getCity() {
                return city;
            }

            public void setCity(List<CityModel> city) {
                this.city = city;
            }
        }

        private static class CityModel implements Serializable {
            private String region_id;
            private String region_name;
            private boolean selected;
            private List<AreaModel> area;

            public String getRegion_id() {
                return region_id;
            }

            public void setRegion_id(String region_id) {
                this.region_id = region_id;
            }

            public String getRegion_name() {
                return region_name;
            }

            public void setRegion_name(String region_name) {
                this.region_name = region_name;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            public List<AreaModel> getArea() {
                return area;
            }

            public void setArea(List<AreaModel> area) {
                this.area = area;
            }
        }

        private static class AreaModel implements Serializable {
            private int flag;
            private String region_id;
            private String region_name;
            private boolean selected;
            private String spell;

            public int getFlag() {
                return flag;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }

            public String getRegion_id() {
                return region_id;
            }

            public void setRegion_id(String region_id) {
                this.region_id = region_id;
            }

            public String getRegion_name() {
                return region_name;
            }

            public void setRegion_name(String region_name) {
                this.region_name = region_name;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            public String getSpell() {
                return spell;
            }

            public void setSpell(String spell) {
                this.spell = spell;
            }
        }
    }
}
