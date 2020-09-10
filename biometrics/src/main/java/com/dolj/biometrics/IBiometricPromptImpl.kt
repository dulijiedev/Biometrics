package com.dolj.biometrics

import androidx.annotation.NonNull
import androidx.fragment.app.FragmentManager
import com.dolj.biometrics.bioapi.IBiometricApi

/**
 * @author: dlj
 * @date: 2020/5/14
 */
interface IBiometricPromptImpl {
    fun authenticate(
        @NonNull fragmentManager: FragmentManager?=null,
        @NonNull callback: IBiometricApi
    )
}