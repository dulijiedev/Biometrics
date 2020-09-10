package com.dolj.biometrics

import android.app.Application

/**
 * @author: dlj
 * @date: 2020/5/13
 */

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Biometrics.Builder(this)
            .setFontSecondary(resources.getColor(R.color.colorAccent))
            .setBgPrimary(resources.getColor(R.color.bgPrimary))
            .setDefaultColor(resources.getColor(R.color.colorAccent))
            .setSelectedColor(resources.getColor(R.color.colorPrimary))
            .setErrorColor(resources.getColor(R.color.colorAccent))
            .setThemeColor(resources.getColor(R.color.colorPrimary))
            .setForgetText(resources.getString(R.string.forget_what))
            .setTitle("指纹识别")
            .setSubtitle("使用指纹登录XXX")
            .setDescripton("请使用指纹解锁快捷登录")
            .setNegativeText("取消")
            .build()
    }
}