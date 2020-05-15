package com.dolj.biometrics

import android.os.CancellationSignal
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.dolj.biometrics.bioapi.IBiometricApi

/**
 * @author: dlj
 * @date: 2020/5/14
 */
interface IBometricPromptImpl {
    fun authenticate(
        @NonNull fragmentManager: FragmentManager?=null,
        @NonNull callback: IBiometricApi
    )
}