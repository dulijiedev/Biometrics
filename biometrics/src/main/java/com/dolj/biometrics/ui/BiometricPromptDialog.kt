package com.dolj.biometrics.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dolj.biometrics.R
import com.dolj.biometrics.bioapi.IBiometricApi
import com.dolj.biometrics.utils.fingerNegativeText
import com.dolj.biometrics.utils.fingerSubTitle
import com.dolj.biometrics.utils.fingerTitle
import javax.crypto.Cipher

/**
 * @author: dlj
 * @date: 2020/5/14
 * @description: 添加认证弹窗
 */
class BiometricPromptDialog private constructor(
    val cipher: Cipher,
    val callback: IBiometricApi?
) :
    DialogFragment() {

    companion object {
        const val STATE_NORMAL = 1
        const val STATE_FAILED = 2
        const val STATE_ERROR = 3
        const val STATE_SUCCEED = 4

        fun newInstance(cipher: Cipher, callback: IBiometricApi): BiometricPromptDialog {
            return BiometricPromptDialog(cipher, callback)
        }
    }

    private var fingerprintManager: FingerprintManagerCompat? = null
    private var mCancellationSignal: CancellationSignal? = null

    private var mStateTv: TextView? = null
    private var mTitleTv: TextView? = null
    private var mSubtitleTv: TextView? = null
    private var mPositiveBtn: TextView? = null
    private var mNegativeBtn: TextView? = null
    private var mActivity: Activity? = null
    /**
     * 标识是否是用户主动取消的认证。
     */
    private var isSelfCancelled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fingerprintManager = FingerprintManagerCompat.from(this.requireContext())
    }

    override fun onResume() {
        super.onResume()
        startListening(cipher)
    }

    override fun onPause() {
        super.onPause()
        stopListening()
    }

    override fun onStop() {
        super.onStop()
        stopListening()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupWindow(dialog?.window)
    }

    private fun setupWindow(window: Window?) {
        window?.apply {
            val lp = window.attributes
            lp.gravity = Gravity.CENTER
            lp.dimAmount = 0f
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            window.attributes = lp
            window.setBackgroundDrawableResource(R.color.bg_biometric_prompt_dialog)
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_biometric_prompt_dg, container)

        val rootView = view.findViewById<RelativeLayout>(R.id.root_view)
        rootView.isClickable = false

        mTitleTv = view.findViewById<TextView>(R.id.tvTitle)
        mTitleTv?.text = fingerTitle
        mSubtitleTv = view.findViewById<TextView>(R.id.tvSubtitle)
        mSubtitleTv?.text = fingerSubTitle
        mStateTv = view.findViewById(R.id.state_tv)
        mNegativeBtn = view.findViewById(R.id.negative_btn)

        if (fingerNegativeText.isNotBlank()) {
            mNegativeBtn?.text = fingerNegativeText
        }

        mNegativeBtn?.setOnClickListener {
            callback?.onNegativeClick()
            stopListening()
            dismiss()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (dialog.window != null) {
            dialog.window?.setBackgroundDrawableResource(R.color.bg_biometric_prompt_dialog)
        }
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback?.onDismiss()
    }


    /**
     * 设置不同状态显示内容
     */
    fun setState(state: Int) {
        when (state) {
            STATE_NORMAL -> {
                mStateTv?.setTextColor(ContextCompat.getColor(mActivity!!, R.color.text_quaternary))
                mStateTv?.text = mActivity?.getString(R.string.biometric_dialog_state_normal)
                mNegativeBtn?.visibility = View.VISIBLE
                mPositiveBtn?.visibility = View.VISIBLE
            }
            STATE_FAILED -> {
                mStateTv?.setTextColor(ContextCompat.getColor(mActivity!!, R.color.text_red))
                mStateTv?.text = mActivity?.getString(R.string.biometric_dialog_state_failed)
                mNegativeBtn?.visibility = View.VISIBLE
                mPositiveBtn?.visibility = View.VISIBLE
            }
            STATE_ERROR -> {
                mStateTv?.setTextColor(ContextCompat.getColor(mActivity!!, R.color.text_red))
                mStateTv?.text = mActivity?.getString(R.string.biometric_dialog_state_error)
                mNegativeBtn?.visibility = View.VISIBLE
                mPositiveBtn?.visibility = View.VISIBLE
            }
            STATE_SUCCEED -> {
                mStateTv?.setTextColor(ContextCompat.getColor(mActivity!!, R.color.text_green))
                mStateTv?.text = mActivity?.getString(R.string.biometric_dialog_state_succeeded)
                mNegativeBtn?.visibility = View.VISIBLE
                mPositiveBtn?.visibility = View.VISIBLE

                mStateTv?.postDelayed({ dismiss() }, 500)
            }
            else -> {
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
    }

    private fun startListening(cipher: Cipher) {
        isSelfCancelled = false
        mCancellationSignal = CancellationSignal()
        fingerprintManager?.authenticate(
            FingerprintManagerCompat.CryptoObject(cipher),
            0,
            mCancellationSignal,
            object : FingerprintManagerCompat.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    if (!isSelfCancelled) {
                        setState(STATE_ERROR)
                        callback?.authBiometricFailed(errorCode, errString.toString())
                        if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                            dismiss()
                        }
                    }
                }

                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                    setState(STATE_FAILED)
                    callback?.authBiometricFailed(helpCode, helpString.toString())
                }

                override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                    setState(STATE_SUCCEED)
                    callback?.authBiometricComplete()
                    dismiss()
                }

                override fun onAuthenticationFailed() {
                    setState(STATE_FAILED)
                    callback?.authBiometricFailedOnce()
                }
            }, null
        )
    }

    private fun stopListening() {
        if (mCancellationSignal != null) {
            mCancellationSignal?.cancel()
            mCancellationSignal = null
            isSelfCancelled = true
        }
    }

    fun show(manager: FragmentManager) {
        show(manager, "BiometricPromptApi23")
    }

}