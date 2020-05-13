package com.dolj.biometrics.bioapi

/**
 * @author: dlj
 * @date: 2020/5/13
 * 添加生物识别
 */
interface IBiometricJoin {

    /**
     * 取消添加
     */
    fun cancelJoin()

    /**
     * 添加成功
     */
    fun joinComplete()

    /**
     * 添加失败
     */
    fun joinFailed()
}