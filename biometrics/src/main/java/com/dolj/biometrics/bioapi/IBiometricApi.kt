package com.dolj.biometrics.bioapi

/**
 * @author: dlj
 * @date: 2020/5/13
 * 生物识别验证
 */

interface IBiometricApi {

    /**
     * 验证失败
     * @param error 错误内容
     */
    fun authBiometricFailed(error: String)

    /**
     * 验证成功
     */
    fun authBiometricComplete()

    /**
     * 取消验证
     */
    fun authBiometricCancel()

    /**
     * 验证手势下面部分
     * 忘记手势/其他事件，自我配置文字及事件
     */
    fun authBiometricForget(){}
}