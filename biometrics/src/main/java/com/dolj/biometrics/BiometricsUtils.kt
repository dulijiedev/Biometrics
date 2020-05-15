package com.dolj.biometrics

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.dolj.biometrics.bioapi.IBiometricApi
import com.dolj.biometrics.bioapi.IBiometricJoin
import com.dolj.biometrics.gesture.GestureCheckAt
import com.dolj.biometrics.gesture.GestureSetAt
import com.dolj.biometrics.utils.*

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
    fun enterBiometric(
        context: Context,
        fragmentManager: FragmentManager,
        callback: IBiometricApi
    ) {
        this.callbackEnter = callback
        when {
            UIUtils.isAboveApi28() && UIUtils.isSupportFingerprint2(context) -> {
                //>=api28 使用指纹或者人脸识别
                BiometricPromptApi28.authenticate(null, callback)
            }
            UIUtils.isAboveApi23() && UIUtils.isSupportFingerprint2(context) -> {
                //api23 - api 28  使用指纹
                BiometricPromptApi23.authenticate(fragmentManager, callback)
            }
            else -> {
                //小于api23 使用九宫格验证
                context.startActivity(Intent(context, GestureCheckAt::class.java))
            }
        }
    }

    /**
     * 添加生物识别或手势
     */
    fun setBiometric(context: Context, callback: IBiometricJoin) {
        this.callbackJoin = callback
        context.startActivity(Intent(context, GestureSetAt::class.java))
//        when {
//            UIUtils.isAboveApi28() && UIUtils.isSupportFingerprint2(context) -> {
//                //>=api28 使用指纹或者人脸识别
//
//            }
//            UIUtils.isAboveApi23() && UIUtils.isSupportFingerprint2(context) -> {
//                //api23 - api 28  使用指纹
//            }
//            else -> {
//                //小于api23 使用九宫格验证
//                context.startActivity(Intent(context, GestureSetAt::class.java))
//            }
//        }
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
        /***********************************************/
        if (builder.title != null) {
            fingerTitle = builder.title as String
        }

        if (builder.subTitle != null) {
            fingerSubTitle = builder.subTitle as String
        }

        if (builder.negativeText != null) {
            fingerNegativeText = builder.negativeText as String
        }

        if (builder.description != null) {
            fingerDesc = builder.description as String
        }
    }

    open class Builder(val context: Context) {
        init {
            UIUtils.init(context)
        }

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

        /*****************************/
        var title: String? = null
        var subTitle: String? = null
        var negativeText: String? = null
        var description: String? = null

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setSubtitle(subTitle: String): Builder {
            this.subTitle = subTitle
            return this
        }

        fun setDescripton(description: String): Builder {
            this.description = description
            return this
        }

        fun setNegativeText(negativeText: String): Builder {
            this.negativeText = negativeText
            return this
        }

        fun build(): Biometrics {
            when {
                UIUtils.isSupportFingerprint2(context) -> {
                    when {

                        title == null -> {
                            Toast.makeText(context, "请设置title", Toast.LENGTH_SHORT).show()
                            throw RuntimeException("请设置title")
                        }
                        subTitle == null -> {
                            Toast.makeText(context, "请设置subTitle", Toast.LENGTH_SHORT).show()
                            throw RuntimeException("请设置subTitle")
                        }
                        negativeText == null -> {
                            Toast.makeText(context, "请设置negativeText", Toast.LENGTH_SHORT).show()
                            throw RuntimeException("请设置negativeText")
                        }
                        description == null -> {
                            Toast.makeText(context, "请设置认证描述", Toast.LENGTH_SHORT).show()
                            throw RuntimeException("请设置认证描述")
                        }
                    }
                }
                else -> {
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
                }

            }
            return Biometrics(this)
        }
    }

}


