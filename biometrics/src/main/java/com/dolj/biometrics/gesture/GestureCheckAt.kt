package com.dolj.biometrics.gesture

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dolj.biometrics.BiometricsUtils
import com.dolj.biometrics.R
import com.dolj.biometrics.utils.bgPrimary
import com.dolj.biometrics.utils.fontSecondary
import com.dolj.biometrics.utils.forgetText
import com.dolj.biometrics.utils.gesturePwd
import com.dolj.biometrics.widgets.LockPatternUtil
import com.dolj.biometrics.widgets.LockPatternView
import com.dolj.biometrics.widgets.StatusCheck
import kotlinx.android.synthetic.main.gesture_check_at.*

/**
 * @author: dlj
 * @date: 2020/5/13
 * @description 验证手势
 */
class GestureCheckAt : AppCompatActivity(), LockPatternView.OnPatternListener {

    private val DELAYTIME = 600L
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gesture_check_at)
//        tv_username.setTextColor(fontSecondary)
        tv_check_tips.setTextColor(fontSecondary)
        rootCheck.setBackgroundColor(bgPrimary)
        tv_forget_gesture.setTextColor(fontSecondary)
        tv_forget_gesture.text = forgetText

        tv_forget_gesture.setOnClickListener {
            BiometricsUtils.callbackEnter?.authBiometricForget()
        }
        lockPatternView.setOnPatternListener(this)
        updateStatus(StatusCheck.DEFAULT)
    }

    override fun onPatternStart() {
        lockPatternView.removePostClearPatternRunnable()
    }

    override fun onPatternComplete(cells: MutableList<LockPatternView.Cell>?) {
        if (cells != null) {
            if (LockPatternUtil.checkPattern(cells, gesturePwd)) {
                updateStatus(StatusCheck.CORRECT)
            } else {
                updateStatus(StatusCheck.ERROR)
            }
        }
    }

    /**
     * 更新状态
     *
     * @param status
     */
    private fun updateStatus(status: StatusCheck) {
        tv_check_tips.setText(status.strId)
        tv_check_tips.setTextColor(resources.getColor(status.colorId))
        when (status) {
            StatusCheck.DEFAULT -> lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT)
            StatusCheck.ERROR -> {
                count++
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR)
                lockPatternView.postClearPatternRunnable(DELAYTIME)
                if (count >= 5) {
//                    Toast.makeText(this, R.string.check_error_over_time, Toast.LENGTH_SHORT).show()
                    gesturePwd = ""
                    window.setWindowAnimations(0)
                    //验证出错后跳转到
                    BiometricsUtils.callbackEnter?.authBiometricFailed(-1,resources.getString(R.string.check_error_over_time))
                    finish()
                }
            }
            StatusCheck.CORRECT -> {
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT)
                //收拾验证成功回调跳转
                BiometricsUtils.callbackEnter?.authBiometricComplete()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //取消验证
        BiometricsUtils.callbackEnter?.authBiometricCancel()
    }
}