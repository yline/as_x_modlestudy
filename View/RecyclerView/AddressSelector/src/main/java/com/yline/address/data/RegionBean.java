package com.yline.address.data;

import java.util.List;

/**
 *
 * @author yline 2020-04-09 -- 08:37
 */
public class RegionBean {

    public String provinceId;
    public String provinceName;
    public List<City> citys;

    public static class City {
        public String cityId;
        public String cityName;
        public List<Area> areas;

        public static class Area {
            public String areaName;
            public String id;
        }
    }
}
