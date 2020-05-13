package com.dolj.biometrics


import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

/**
 * @author: dlj
 * @date: 2020/5/12
 * @description 获取设备相关信息
 */

/**
 * Return the version name of device's system.
 *
 * @return the version name of device's system
 */
fun getSDKVersionName(): String {
    return android.os.Build.VERSION.RELEASE
}

/**
 * Return version code of device's system.
 *
 * @return version code of device's system
 */
fun getSDKVersionCode(): Int {
    return android.os.Build.VERSION.SDK_INT
}

/**
 * Return the android id of device.
 *
 * @return the android id of device
 */
@SuppressLint("HardwareIds")
fun getAndroidID(context: Context): String {
    val id = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
    return id ?: ""
}

