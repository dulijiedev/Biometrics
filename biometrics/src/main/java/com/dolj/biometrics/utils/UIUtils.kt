package com.dolj.biometrics.utils

import android.content.Context
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
}