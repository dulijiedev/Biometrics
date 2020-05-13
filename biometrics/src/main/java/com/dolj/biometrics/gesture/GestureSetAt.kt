package com.dolj.biometrics.gesture

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dolj.biometrics.BiometricsUtils
import com.dolj.biometrics.R
import com.dolj.biometrics.utils.bgPrimary
import com.dolj.biometrics.utils.fontSecondary
import com.dolj.biometrics.utils.gesturePwd
import com.dolj.biometrics.widgets.LockPatternUtil
import com.dolj.biometrics.widgets.LockPatternView
import com.dolj.biometrics.widgets.StatusSet
import kotlinx.android.synthetic.main.gesture_set_at.*

/**
 * @author: dlj
 * @date: 2020/5/13
 */

class GestureSetAt : AppCompatActivity(), LockPatternView.OnPatternListener {

    private var mChosenPattern: List<LockPatternView.Cell>? = null
    private val DELAYTIME = 600L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gesture_set_at)
        tv_set_title.setTextColor(fontSecondary)
        tv_set_tip.setTextColor(fontSecondary)
        rootSet.setBackgroundColor(bgPrimary)
        lockPatternView.setOnPatternListener(this)
    }

    override fun onPatternStart() {
        lockPatternView.removePostClearPatternRunnable()
        lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT)
    }

    override fun onPatternComplete(pattern: MutableList<LockPatternView.Cell>?) {
        if (pattern.isNullOrEmpty()) {
            return
        }
        if (mChosenPattern == null && pattern.size >= 4) {
            mChosenPattern = ArrayList<LockPatternView.Cell>(pattern)
            updateStatus(StatusSet.CORRECT, pattern)
        } else if (mChosenPattern == null && pattern.size < 4) {
            updateStatus(StatusSet.LESSERROR, pattern)
        } else if (mChosenPattern != null) {
            if (mChosenPattern == pattern) {
                updateStatus(StatusSet.CONFIRMCORRECT, pattern)
            } else {
                updateStatus(StatusSet.CONFIRMERROR, pattern)
            }
        }
    }

    /**
     * 更新状态
     *
     * @param status
     * @param pattern
     */
    private fun updateStatus(status: StatusSet, pattern: List<LockPatternView.Cell>) {
        tv_set_tip.setTextColor(resources.getColor(status.colorId))
        tv_set_tip.setText(status.strId)
        when (status) {
            StatusSet.DEFAULT -> lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT)
            StatusSet.CORRECT -> lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT)
            StatusSet.LESSERROR -> lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT)
            StatusSet.CONFIRMERROR -> {
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR)
                lockPatternView.postClearPatternRunnable(DELAYTIME)
            }
            StatusSet.CONFIRMCORRECT -> {
                saveChosenPattern(pattern)
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT)
                setLockPatternSuccess()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        BiometricsUtils.callbackJoin?.cancelJoin()
    }

    /**
     * 保存手势密码
     */
    private fun saveChosenPattern(cells: List<LockPatternView.Cell>) {
        val pwd = LockPatternUtil.paramLockPwd(cells)
        gesturePwd = pwd
    }

    /**
     * 成功设置了手势密码(跳到首页)
     */
    private fun setLockPatternSuccess() {
        BiometricsUtils.callbackJoin?.joinComplete()
    }
}