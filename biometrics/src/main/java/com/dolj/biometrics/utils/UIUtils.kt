package com.dolj.biometrics.utils

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import java.lang.NullPointerException

/**
 * @author: dlj
 * @date: 2020/5/13
 */

object UIUtils {
    private var scontext: Context? = null

    fun init(context: Context) {
        this.scontext = context
    }

    fun getContext(): Context {
        if (scontext != null) return scontext as Context
        throw NullPointerException("未设置Context")
    }

    fun isSupportFingerprint(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(context, "您的系统版本过低，暂不支持指纹功能!", Toast.LENGTH_SHORT).show()
            return false
        } else {
            val keyguardManager = context.getSystemService(KeyguardManager::class.java)
            val fingerprintManager = FingerprintManagerCompat.from(context)
            if (!fingerprintManager!!.isHardwareDetected) {
                Toast.makeText(context, "您的手机不支持指纹功能！", Toast.LENGTH_SHORT).show()
                return false
            } else if (!keyguardManager!!.isKeyguardSecure) {
                Toast.makeText(context, "请开启您手机的指纹验证功能！", Toast.LENGTH_SHORT).show()
                return false
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(context, "请您在系统设置中至少添加一个指纹!", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    fun isSupportFingerprint2(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < 23) {
            return false
        } else {
            val keyguardManager = context.getSystemService(KeyguardManager::class.java)
            val fingerprintManager = FingerprintManagerCompat.from(context)
            if (!fingerprintManager!!.isHardwareDetected) {
                return false
            } else if (!keyguardManager!!.isKeyguardSecure) {
                return false
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                return false
            }
        }
        return true
    }

    fun isAboveApi28(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    fun isAboveApi23(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}