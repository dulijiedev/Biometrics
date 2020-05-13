package com.dolj.biometrics

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.dolj.biometrics.bioapi.IBiometricApi
import com.dolj.biometrics.bioapi.IBiometricJoin
import com.dolj.biometrics.gesture.GestureCheckAt
import com.dolj.biometrics.gesture.GestureSetAt
import com.dolj.biometrics.utils.*
import java.lang.RuntimeException

/**
 * @author: dlj
 * @date: 2020/5/12
 * @description: 在不同版本适配不通功能
 */

object BiometricsUtils {

    var callbackEnter: IBiometricApi? = null

    var callbackJoin: IBiometricJoin? = null

    /**
     * 进入生物识别或者手势验证
     */
    fun enterBiometric(context: Context, callback: IBiometricApi) {
        this.callbackEnter = callback
        when {
            getSDKVersionCode() < android.os.Build.VERSION_CODES.M -> {
                //小于api23 使用九宫格验证
                context.startActivity(Intent(context, GestureCheckAt::class.java))
            }
            getSDKVersionCode() >= android.os.Build.VERSION_CODES.M && getSDKVersionCode() < android.os.Build.VERSION_CODES.P -> {
                //api23 - api 28  使用指纹
                context.startActivity(Intent(context, GestureCheckAt::class.java))
            }
            getSDKVersionCode() >= android.os.Build.VERSION_CODES.P -> {
                //>=api28 使用指纹或者人脸识别
                context.startActivity(Intent(context, GestureCheckAt::class.java))
            }
        }
    }

    /**
     * 添加生物识别或手势
     */
    fun setBiometric(context: Context, callback: IBiometricJoin) {
        this.callbackJoin = callback
        when {
            getSDKVersionCode() < android.os.Build.VERSION_CODES.M -> {
                //小于api23 使用九宫格验证
                context.startActivity(Intent(context, GestureSetAt::class.java))
            }
            getSDKVersionCode() >= android.os.Build.VERSION_CODES.M && getSDKVersionCode() < android.os.Build.VERSION_CODES.P -> {
                //api23 - api 28  使用指纹
                context.startActivity(Intent(context, GestureSetAt::class.java))
            }
            getSDKVersionCode() >= android.os.Build.VERSION_CODES.P -> {
                //>=api28 使用指纹或者人脸识别
                context.startActivity(Intent(context, GestureSetAt::class.java))
            }
        }
    }
}


/**
 * 手势相关颜色
 */
class Biometrics(builder: Builder) {

    init {
        if (builder.colorFontSecondary != null) {
            fontSecondary = builder.colorFontSecondary as Int
        }

        if (builder.colorBgPrimary != null) {
            bgPrimary = builder.colorBgPrimary as Int
        }

        if (builder.colorDefaultPattern != null) {
            defaultPattern = builder.colorDefaultPattern as Int
        }

        if (builder.colorSelectedPattern != null) {
            selectedPattern = builder.colorSelectedPattern as Int
        }

        if (builder.colorErrorPattern != null) {
            errorPattern = builder.colorErrorPattern as Int
        }


        if (builder.colorThemeAccent != null) {
            themeColors = builder.colorThemeAccent as Int
        }

        if (builder.forgetText != null) {
            forgetText = builder.forgetText as String
        }
    }

    open class Builder(val context: Context) {


        var colorFontSecondary: Int? = null
        var colorBgPrimary: Int? = null
        var colorDefaultPattern: Int? = null
        var colorSelectedPattern: Int? = null
        var colorErrorPattern: Int? = null
        var colorThemeAccent: Int? = null
        var forgetText: String? = null

        fun setFontSecondary(color: Int): Builder {
            this.colorFontSecondary = color
            return this
        }

        fun setBgPrimary(color: Int): Builder {
            this.colorBgPrimary = color
            return this
        }

        fun setDefaultColor(color: Int): Builder {
            this.colorDefaultPattern = color
            return this
        }

        fun setSelectedColor(color: Int): Builder {
            this.colorSelectedPattern = color
            return this
        }

        fun setErrorColor(color: Int): Builder {
            this.colorErrorPattern = color
            return this
        }

        fun setThemeColor(color: Int): Builder {
            this.colorThemeAccent = color
            return this
        }

        fun setForgetText(text: String): Builder {
            this.forgetText = text
            return this
        }

        fun build(): Biometrics {
            when {
                colorFontSecondary == null -> {
                    Toast.makeText(context, "请设置字体颜色", Toast.LENGTH_SHORT).show()
                    throw RuntimeException("请设置字体颜色")
                }
                colorBgPrimary == null -> {
                    Toast.makeText(context, "请设置背景色", Toast.LENGTH_SHORT).show()
                    throw RuntimeException("请设置背景色")
                }
                colorDefaultPattern == null -> {
                    Toast.makeText(context, "请设置手势默认颜色", Toast.LENGTH_SHORT).show()
                    throw RuntimeException("请设置手势默认颜色")
                }
                colorSelectedPattern == null -> {
                    Toast.makeText(context, "请设置手势选中颜色", Toast.LENGTH_SHORT).show()
                    throw RuntimeException("请设置手势选中颜色")
                }
                colorErrorPattern == null -> {
                    Toast.makeText(context, "请设置验证出错颜色", Toast.LENGTH_SHORT).show()
                    throw RuntimeException("请设置验证出错颜色")
                }
                colorThemeAccent == null -> {
                    Toast.makeText(context, "请设置主题颜色", Toast.LENGTH_SHORT).show()
                    throw RuntimeException("请设置主题颜色")
                }
            }
            UIUtils.init(context)
            return Biometrics(this)
        }
    }

}

