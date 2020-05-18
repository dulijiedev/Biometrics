# Biometrics android
 [![](https://jitpack.io/v/sdohubs/Biometrics.svg)](https://jitpack.io/#sdohubs/Biometrics)
## Example
To run the example project, clone or download, just run it on `AndroidStudio`
## Requirements
Biometrics is code by `Kotlin` and `minSdkVersion` is 19 ,If your project is lower than the minimum version, you may get an error
## How to use
#### Step 1. Add the JitPack repository to your build file
##### Add it in your root `build.gradle` at the end of repositories:

```css
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

#### **Step 2.** Add the dependency

```css
dependencies {
	implementation 'com.github.sdohubs:Biometrics:1.0.0'
}
```

#### Step 3. Deploy default text or color for gesture or Fingerprint description

```css
	Biometrics.Builder(this)
            .setFontSecondary(resources.getColor(R.color.colorAccent))
            .setBgPrimary(resources.getColor(R.color.bgPrimart))
            .setDefaultColor(resources.getColor(R.color.colorAccent))
            .setSelectedColor(resources.getColor(R.color.colorPrimary))
            .setErrorColor(resources.getColor(R.color.colorAccent))
            .setThemeColor(resources.getColor(R.color.colorPrimary))
            .setForgetText(resources.getString(R.string.forget_what))
            .setTitle("指纹识别")
            .setSubtitle("使用指纹登录XXX")
            .setDescripton("请使用指纹解锁快捷登录")
            .setNegativeText("取消")
            .build()
```

#### Step 4. Add callback for gesture set or Fingerprint

##### For gesture set:

##### because i not find the way guide to set finger ,only has gesture,after may add fingerprint recognition  guide  to set.

```css
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
```

##### For fingerprint and gesture check:

```css
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
```

### Author

2857692313@qq.com



