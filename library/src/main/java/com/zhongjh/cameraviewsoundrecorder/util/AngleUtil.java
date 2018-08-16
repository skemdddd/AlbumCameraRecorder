package com.zhongjh.cameraviewsoundrecorder.util;

/**
 * 有关角度的工具类
 * Created by zhongjh on 2018/8/10.
 */
public class AngleUtil {

    public static int getSensorAngle(float x, float y) {
        if (Math.abs(x) > Math.abs(y)) {
            // 横屏倾斜角度比较大
            if (x > 4) {
                // 左边倾斜
                return 270;
            } else if (x < -4) {
                // 右边倾斜
                return 90;
            } else {
                // 倾斜角度不够大
                return 0;
            }
        } else {
            if (y > 7) {
                // 左边倾斜
                return 0;
            } else if (y < -7) {
                // 右边倾斜
                return 180;
            } else {
                // 倾斜角度不够大
                return 0;
            }
        }
    }
}
