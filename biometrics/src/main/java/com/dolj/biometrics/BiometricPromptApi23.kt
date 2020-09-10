package com.dolj.biometrics

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.fragment.app.FragmentManager
import com.dolj.biometrics.bioapi.IBiometricApi
import com.dolj.biometrics.ui.BiometricPromptDialog
import com.dolj.biometrics.utils.DEFAULT_KEY_NAME
import com.dolj.biometrics.utils.UIUtils
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * @author: dlj
 * @date: 2020/5/14
 * @description: api23以下
 */
object BiometricPromptApi23 : IBiometricPromptImpl {

    override fun authenticate(fragmentManager: FragmentManager?, callback: IBiometricApi) {
        if (UIUtils.isSupportFingerprint(UIUtils.getContext())) {
            initKey()
            initCipher(fragmentManager, callback)
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

    @TargetApi(Build.VERSION_CODES.M)
    private fun initCipher(fragmentManager: FragmentManager?, callback: IBiometricApi) {
        try {
            val key = mKeyStore?.getKey(DEFAULT_KEY_NAME, null) as SecretKey
            val cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
            cipher.init(Cipher.ENCRYPT_MODE, key)
            showFingerPrintDialog(fragmentManager, cipher, callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showFingerPrintDialog(
        fragmentManager: FragmentManager?,
        cipher: Cipher,
        callback: IBiometricApi
    ) {
        val dialog = BiometricPromptDialog.newInstance(cipher, callback)
        if (fragmentManager != null) {
            dialog.show(fragmentManager)
        }
    }

}