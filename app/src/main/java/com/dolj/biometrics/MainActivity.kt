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
            BiometricsUtils.setBiometric(this,object:IBiometricJoin{
                override fun cancelJoin() {
                    Toast.makeText(this@MainActivity,"取消設置",Toast.LENGTH_SHORT).show()
                }

                override fun joinComplete() {
                    Toast.makeText(this@MainActivity,"設置成功",Toast.LENGTH_SHORT).show()
                }

                override fun joinFailed() {
                    Toast.makeText(this@MainActivity,"設置失敗",Toast.LENGTH_SHORT).show()
                }

            })
        }

        btnCheck.setOnClickListener {
            BiometricsUtils.enterBiometric(this,object:IBiometricApi{
                override fun authBiometricFailed(error: String) {
                    Toast.makeText(this@MainActivity,"验证失败 $error",Toast.LENGTH_SHORT).show()
                }

                override fun authBiometricComplete() {
                    Toast.makeText(this@MainActivity,"验证成功",Toast.LENGTH_SHORT).show()
                }

                override fun authBiometricCancel() {
                    Toast.makeText(this@MainActivity,"取消验证",Toast.LENGTH_SHORT).show()
                }

                override fun authBiometricForget() {
                    super.authBiometricForget()
                    Toast.makeText(this@MainActivity,"忘記手势 xxx",Toast.LENGTH_SHORT).show()
                }

            })
        }

    }
}