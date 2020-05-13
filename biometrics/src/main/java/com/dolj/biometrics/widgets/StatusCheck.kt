package com.dolj.biometrics.widgets

import com.dolj.biometrics.R

/**
 * Created by dlj on 2019/6/11.
 * 验证手势密码用
 */
enum class StatusCheck(val strId: Int, val colorId: Int) {
    //默认的状态
    DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
    //密码输入错误
    ERROR(R.string.gesture_error, R.color.red_f4333c),
    //密码输入正确
    CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

}