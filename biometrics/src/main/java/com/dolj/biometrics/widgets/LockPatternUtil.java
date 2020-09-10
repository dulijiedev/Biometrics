package com.dolj.biometrics.widgets;

import android.content.Context;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sym on 2016/1/16.
 */
public class LockPatternUtil {

    /**
     * change string value (ex: 10.0dip => 20) to int value
     *
     * @param context ctx
     * @param value 值
     * @return 大小
     */
    public static int changeSize(Context context, String value) {
        if (value.contains("dip")) {
            float dip = Float.parseFloat(value.substring(0, value.indexOf("dip")));
            return LockPatternUtil.dip2px(context, dip);
        } else if (value.contains("px")) {
            float px = Float.parseFloat(value.substring(0, value.indexOf("px")));
            return (int) px;
        } else if (value.contains("@")) {
            float px = context.getResources().getDimension(Integer.parseInt(value.replace("@", "")));
            return (int) px;
        } else {
            throw new IllegalArgumentException("can not use wrap_content " +
                    "or match_parent or fill_parent or others' illegal parameter");
        }
    }

    /**
     * dip to px
     *
     * @param context ctx
     * @param dpValue dp值
     * @return px值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * check the touch cell is or not in the circle
     *
     * @param sx sx
     * @param sy sy
     * @param r      the radius of circle
     * @param x      the x position of circle's center point
     * @param y      the y position of circle's center point
     * @param offset the offset to the frame of the circle
     *               (if offset > 0 : the offset is inside the circle; if offset < 0 : the offset is outside the circle)
     * @return circle
     */
    public static boolean checkInRound(float sx, float sy, float r, float x, float y, float offset) {
        return Math.sqrt((sx - x + offset) * (sx - x + offset) + (sy - y + offset) * (sy - y + offset)) < r;
    }

    /**
     * get distance between two points
     *
     * @param fpX first point x position
     * @param fpY first point y position
     * @param spX second point x position
     * @param spY second point y position
     * @return distance
     */
    public static float getDistanceBetweenTwoPoints(float fpX, float fpY, float spX, float spY) {
        return (float) Math.sqrt((spX - fpX) * (spX - fpX) + (spY - fpY) * (spY - fpY));
    }

    public static float getAngleLineIntersectX(float fpX, float fpY, float spX, float spY, float distance) {
        return (float) Math.toDegrees(Math.acos((spX - fpX) / distance));
    }

    public static float getAngleLineIntersectY(float fpX, float fpY, float spX, float spY, float distance) {
        return (float) Math.toDegrees(Math.acos((spY - fpY) / distance));
    }

    public static byte[] patternToHash(List<LockPatternView.Cell> pattern) {
        if (pattern == null) {
            return null;
        } else {
            int size = pattern.size();
            byte[] res = new byte[size];
            for (int i = 0; i < size; i++) {
                LockPatternView.Cell cell = pattern.get(i);
                res[i] = (byte) cell.getIndex();
            }
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-1");
                return md.digest(res);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return res;
            }
        }
    }

    /**
     * Check to see if a pattern matches the saved pattern. If no pattern
     * exists, always returns true.
     */
    public static boolean checkPattern(List<LockPatternView.Cell> pattern, byte[] bytes) {
        if (pattern == null || bytes == null) {
            return false;
        } else {
            byte[] bytes2 = patternToHash(pattern);
            return Arrays.equals(bytes, bytes2);
        }
    }

    /**
     * 将密码拼接成为字符串返回
     */
    public static String paramLockPwd(List<LockPatternView.Cell> params) {
        if (params != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.size(); i++) {
                sb.append(params.get(i).getIndex());
            }
            return sb.toString();
        } else {
            return null;
        }
    }


    /**
     * 验证两个密码是否相同
     */
    public static boolean checkPattern(List<LockPatternView.Cell> pattern, String localPwd) {
        if (pattern == null || localPwd == null || TextUtils.isEmpty(localPwd)) {
            return false;
        } else {
            String pwd2 = paramLockPwd(pattern);
            return pwd2.equals(localPwd);
        }
    }

}