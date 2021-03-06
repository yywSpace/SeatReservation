package com.yywspace.module_base.util;

public class MapUtil {
    /**
     * 此方法计算两个经纬度之间的距离
     *
     * @param lat1 经度1
     * @param lon1 纬度1
     * @param lat2 经度2
     * @param lon2 纬度2
     * @return 两个经纬度之间的距离
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 地球半径
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // 单位转换成米
        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }
}
