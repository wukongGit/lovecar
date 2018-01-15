package com.sunc.car.lovecar

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.kyview.interfaces.AdViewSpreadListener
import com.kyview.manager.*
import com.sunc.utils.StatusBarUtils
import com.tbruyelle.rxpermissions.RxPermissions
import rx.functions.Action1

class SplashActivity : AppCompatActivity(), AdViewSpreadListener {
    //val TAG = "ADVIEW_SplashActivity"
    override fun onAdRecieved(p0: String?) {
        //Log.d(TAG, "onAdRecieved==" + p0)
    }

    override fun onAdSpreadNotifyCallback(p0: String?, p1: ViewGroup?, p2: Int, p3: Int) {
        //Log.d(TAG, "onAdSpreadNotifyCallback==" + p0)
    }

    override fun onAdFailed(p0: String?) {
        jump()
    }

    override fun onAdDisplay(p0: String?) {
        //Log.d(TAG, "onAdDisplay==" + p0)
    }

    override fun onAdClick(p0: String?) {
        //Log.d(TAG, "onAdClick==" + p0)
    }

    override fun onAdClose(p0: String?) {
        jump()
    }

    private var isHasReadExternalPermission = false
    private var isHasWriteExternalPermission = false
    private var isHasPhoneStatePermission = false

    private var isCheckReadExternalPermission = false
    private var isCheckWriteExternalPermission = false
    private var isCheckPhoneStatePermission = false

    private var isHandleStorage = false
    private var isHandlePhone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTranslucentStatus(this, true)
        setContentView(R.layout.activity_splash)
        showPermissionCheckDialog()
    }

    override fun onResume() {
        super.onResume()
        if (isCheckReadExternalPermission && isCheckWriteExternalPermission
                && isCheckPhoneStatePermission
                && (isHandleStorage || isHandlePhone)) {
            isCheckReadExternalPermission = false
            isCheckWriteExternalPermission = false
            isCheckPhoneStatePermission = false
            isHandleStorage = false
            isHandlePhone = false
            showPermissionCheckDialog()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) false else super.onKeyDown(keyCode, event)
    }

    /**
     * onRestart()如果只使用单独竞价开屏，必须注释掉否则引起回调问题。如果和第三方一块来使用，必须打开否则有可能引起跳转问题。
     */
    override fun onRestart() {
        super.onRestart()
        // waitingOnRestart 需要自己控制
        //waitingOnRestart = true;
        jumpWhenCanClick()
    }

    var waitingOnRestart = false

    private fun jump() {
        if (!AdViewSpreadManager.hasJumped) {
            val intent = Intent()
            intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    /*
	 * 包含点击的开屏广告时会调用该方法广告
	 */
    private fun jumpWhenCanClick() {
        if ((this.hasWindowFocus() || waitingOnRestart) && !AdViewSpreadManager.hasJumped) {
            this.startActivity(Intent(this, MainActivity::class.java))
            // adSpreadManager.setAdSpreadInterface(null);
            this.finish()
        } else {
            waitingOnRestart = true
        }

    }

    private fun startUp () {
        AdViewSpreadManager.getInstance(this).request(this, App.instance.key1, this,
                findViewById<RelativeLayout>(R.id.spreadlayout), null)
    }

    private fun showPermissionCheckDialog() {
        RxPermissions.getInstance(this)
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                )
                .subscribe(Action1 { permission ->
                    when(permission.name) {
                        Manifest.permission.READ_EXTERNAL_STORAGE -> {
                            isHasReadExternalPermission = permission.granted
                            isCheckReadExternalPermission = true
                        }
                        Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                            isHasWriteExternalPermission = permission.granted
                            isCheckWriteExternalPermission = true
                        }
                        Manifest.permission.READ_PHONE_STATE -> {
                            isHasPhoneStatePermission = permission.granted
                            isCheckPhoneStatePermission = true
                        }
                    }

                    if (isCheckReadExternalPermission && isCheckWriteExternalPermission
                            && isCheckPhoneStatePermission) {
                        if (!isHasReadExternalPermission || !isHasWriteExternalPermission) {
                            showStoragePermissionAlertDialog()
                            return@Action1
                        }
                        if (!isHasPhoneStatePermission) {
                            showPhonePermissionAlertDialog()
                            return@Action1
                        }
                        startUp()
                    }
                })
    }

    private fun showStoragePermissionAlertDialog() {
        val dialog = AlertDialog.Builder(this,
                android.support.v7.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle(getString(R.string.permission_open))
                .setMessage(getString(R.string.permission_open_storage_description))
                .setPositiveButton(getString(R.string.goto_setting)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    try {
                        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
                        val pkg = "com.android.settings"
                        val cls = "com.android.settings.applications.InstalledAppDetails"
                        intent.component = ComponentName(pkg, cls)
                        intent.data = Uri.parse("package:" + packageName)
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                .setNegativeButton(getString(R.string.i_known)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
                .setCancelable(false)
                .create()
        dialog.setOnDismissListener { isHandleStorage = true }
        dialog.show()
    }

    private fun showPhonePermissionAlertDialog() {
        val dialog = AlertDialog.Builder(this,
                android.support.v7.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle(getString(R.string.permission_open))
                .setMessage(getString(R.string.permission_open_phone_state_description))
                .setPositiveButton(getString(R.string.goto_setting)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    try {
                        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
                        val pkg = "com.android.settings"
                        val cls = "com.android.settings.applications.InstalledAppDetails"
                        intent.component = ComponentName(pkg, cls)
                        intent.data = Uri.parse("package:" + packageName)
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                .setNegativeButton(getString(R.string.i_known)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    startUp()
                }
                .setCancelable(false)
                .create()
        dialog.setOnDismissListener { isHandlePhone = true }
        dialog.show()
    }

}
