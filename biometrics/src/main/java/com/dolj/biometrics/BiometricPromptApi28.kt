package com.dolj.biometrics

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.dolj.biometrics.bioapi.IBiometricApi
import com.dolj.biometrics.utils.*
import java.lang.Exception
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * @author: dlj
 * @date: 2020/5/14
 * api28+
 */
object BiometricPromptApi28 : IBometricPromptImpl {

    @TargetApi(Build.VERSION_CODES.P)
    override fun authenticate(fragmentManager: FragmentManager?, callback: IBiometricApi) {
        if (UIUtils.isSupportFingerprint(UIUtils.getContext())) {
            initKey()
            initCipher9(callback)
        }
    }

    private var mKeyStore: KeyStore? = null
    @TargetApi(Build.VERSION_CODES.M)
    private fun initKey() {
        if (mKeyStore != null) return
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore")
            mKeyStore?.load(null)
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val builder = KeyGenParameterSpec.Builder(
                DEFAULT_KEY_NAME,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            keyGenerator.init(builder.build())
            keyGenerator.generateKey()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun initCipher9(callback: IBiometricApi) {
        try {
            val key = mKeyStore?.getKey(DEFAULT_KEY_NAME, null) as SecretKey
            val cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
            cipher.init(Cipher.ENCRYPT_MODE, key)
            showFingerPrintDialog9(cipher, callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun showFingerPrintDialog9(cipher: Cipher, callback: IBiometricApi) {
        val mCancellationSignal = CancellationSignal()
        val mBiometricPrompt = BiometricPrompt.Builder(UIUtils.getContext())
            .setTitle(fingerTitle)
            .setDescription(fingerDesc)
            .setSubtitle(fingerSubTitle)
            .setNegativeButton(
                fingerNegativeText,
                UIUtils.getContext().mainExecutor,
                DialogInterface.OnClickListener { _, _ ->
                    callback.onNegativeClick()
                })
            .build()

        mBiometricPrompt.authenticate(BiometricPrompt.CryptoObject(cipher),
            mCancellationSignal,
            UIUtils.getContext().mainExecutor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback.authBiometricFailedOnce()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    callback.authBiometricFailed(errorCode, errString.toString())
                }

                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                    super.onAuthenticationHelp(helpCode, helpString)
                    callback.authBiometricFailed(helpCode, helpString.toString())

                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    callback.authBiometricComplete()
                }
            }
        )
    }
}