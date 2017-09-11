package org.dync.giftlibrary.util;

import java.util.Random;

/**
 * Created by Administrator on 2017/5/10.
 * function：
 */

public class GiftStringUtils {


    /**
     * 判断是否是null或长度为0
     *
     * @param str 要判断字符串
     * @return 空：true
     */
    public static boolean isNullOrEmpty(String str) {
        try {
            if (str == null)
                return true;
            if (str.length() == 0)
                return true;
            if (str.isEmpty())
                return true;
            if (str.replace(" ", "").equalsIgnoreCase("null"))
                return true;
            if ("".equals(str.replace(" ", "")))
                return true;
        } catch (Exception e) {
            return true;
        }
        return false;
    }


    /**
     * 区间随机数
     *
     * @param min 区间最小值
     * @param max 区间最大值
     * @return 区间正随机数
     */
    public static int intRandom(int min, int max) {
//        Random random = new Random();
//        return random.nextInt(max) % (max - min + 1) + min;
        if (min > max) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return new Random().nextInt(max - min + 1) + min;
    }


    /**
     * @return 头像
     */
    public static int getHeanPic() {
        return GiftConstant.mbackground[intRandom(0, GiftConstant.mbackground.length - 1)];
    }

    /**
     * @return 头像
     */
    public static String getWebHeanPic() {

//        %03d%n
        String headpic = "http://www.bwgrg.com/touxiang/%03d.png";
        String headpics=String.format(headpic,intRandom(1,100));

//        String headpic = "http://www.bwgrg.com/touxiang/%s.png";
//        String headpics=String.format(headpic, String.format("%03d",intRandom(1,100)));

//        Log.e("网络头像测试", "getWebHeanPic: "+headpics );
        return headpics;
    }


}
