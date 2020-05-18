package com.dolj.biometrics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dolj.biometrics.bioapi.IBiometricApi
import com.dolj.biometrics.bioapi.IBiometricJoin
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSet.setOnClickListener {
            BiometricsUtils.setBiometric(this, object : IBiometricJoin {
                override fun cancelJoin() {
                    Toast.makeText(this@MainActivity, "取消设置", Toast.LENGTH_SHORT).show()
                }

                override fun joinComplete() {
                    Toast.makeText(this@MainActivity, "设置成功", Toast.LENGTH_SHORT).show()
                }

                override fun joinFailed() {
                    Toast.makeText(this@MainActivity, "设置失敗", Toast.LENGTH_SHORT).show()
                }

            })
        }

        btnCheck.setOnClickListener {
            BiometricsUtils.enterBiometric(this, supportFragmentManager, object : IBiometricApi {
                override fun authBiometricFailed(code: Int, error: String) {
                    Toast.makeText(this@MainActivity, "验证失败 $error", Toast.LENGTH_SHORT).show()
                }

                override fun authBiometricComplete() {
                    Toast.makeText(this@MainActivity, "验证成功", Toast.LENGTH_SHORT).show()
                }

                override fun authBiometricCancel() {
                    Toast.makeText(this@MainActivity, "取消验证", Toast.LENGTH_SHORT).show()
                }

                override fun authBiometricForget() {
                    super.authBiometricForget()
                    Toast.makeText(this@MainActivity, "忘記手势 xxx", Toast.LENGTH_SHORT).show()
                }

                override fun authBiometricFailedOnce() {
                    super.authBiometricFailedOnce()
                    Toast.makeText(this@MainActivity, "认证失败一次，回调", Toast.LENGTH_SHORT).show()
                }

                override fun onDismiss() {
                    super.onDismiss()
                }

                override fun onNegativeClick() {
                    super.onNegativeClick()
                    Toast.makeText(this@MainActivity, "negative 回调", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}
