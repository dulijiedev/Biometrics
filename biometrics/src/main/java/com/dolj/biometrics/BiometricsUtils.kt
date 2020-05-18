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

        if (builder.getFontSecondary() != null) {
            fontSecondary = builder.getFontSecondary() as Int
        }

        if (builder.getBgPrimary() != null) {
            bgPrimary = builder.getBgPrimary() as Int
        }

        if (builder.getDefaultColor() != null) {
            defaultPattern = builder.getDefaultColor() as Int
        }

        if (builder.getSelectedColor() != null) {
            selectedPattern = builder.getSelectedColor() as Int
        }

        if (builder.getErrorColor() != null) {
            errorPattern = builder.getErrorColor() as Int
        }

        if (builder.getThemeColor() != null) {
            themeColors = builder.getThemeColor() as Int
        }

        if (builder.getForgetText() != null) {
            forgetText = builder.getForgetText() as String
        }
        /***********************************************/
        if (builder.getTitle() != null) {
            fingerTitle = builder.getTitle() as String
        }

        if (builder.getSubtitle() != null) {
            fingerSubTitle = builder.getSubtitle() as String
        }

        if (builder.getNegativeText() != null) {
            fingerNegativeText = builder.getNegativeText() as String
        }

        if (builder.getDescripton() != null) {
            fingerDesc = builder.getDescripton() as String
        }
    }

    open class Builder(val context: Context) {
        init {
            UIUtils.init(context)
        }

        private var colorFontSecondary: Int? = null
        private var colorBgPrimary: Int? = null
        private var colorDefaultPattern: Int? = null
        private var colorSelectedPattern: Int? = null
        private var colorErrorPattern: Int? = null
        private var colorThemeAccent: Int? = null
        private var forgetText: String? = null

        fun setFontSecondary(color: Int): Builder {
            this.colorFontSecondary = color
            return this
        }

        fun getFontSecondary(): Int? {
            return colorFontSecondary
        }

        fun setBgPrimary(color: Int): Builder {
            this.colorBgPrimary = color
            return this
        }

        fun getBgPrimary(): Int? {
            return colorBgPrimary
        }

        fun setDefaultColor(color: Int): Builder {
            this.colorDefaultPattern = color
            return this
        }

        fun getDefaultColor(): Int? {
            return colorDefaultPattern
        }

        fun setSelectedColor(color: Int): Builder {
            this.colorSelectedPattern = color
            return this
        }

        fun getSelectedColor(): Int? {
            return colorSelectedPattern
        }

        fun setErrorColor(color: Int): Builder {
            this.colorErrorPattern = color
            return this
        }

        fun getErrorColor(): Int? {
            return colorErrorPattern
        }

        fun setThemeColor(color: Int): Builder {
            this.colorThemeAccent = color
            return this
        }

        fun getThemeColor(): Int? {
            return colorThemeAccent
        }

        fun setForgetText(text: String): Builder {
            this.forgetText = text
            return this
        }

        fun getForgetText(): String? {
            return forgetText
        }

        /*****************************/
        private var title: String? = null
        private var subTitle: String? = null
        private var negativeText: String? = null
        private var description: String? = null

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun getTitle(): String? {
            return title
        }

        fun setSubtitle(subTitle: String): Builder {
            this.subTitle = subTitle
            return this
        }

        fun getSubtitle(): String? {
            return subTitle
        }

        fun setDescripton(description: String): Builder {
            this.description = description
            return this
        }


        fun getDescripton(): String? {
            return description
        }

        fun setNegativeText(negativeText: String): Builder {
            this.negativeText = negativeText
            return this
        }

        fun getNegativeText(): String? {
            return negativeText
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


