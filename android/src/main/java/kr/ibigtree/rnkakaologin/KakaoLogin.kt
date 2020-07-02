package kr.ibigtree.rnkakaologin

import android.app.Activity
import android.content.Intent
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.ViewManager
import com.kakao.auth.*
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.util.exception.KakaoException

class KakaoLoginModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ISessionCallback, ActivityEventListener {
    private var loginPromise: Promise? = null

    override fun getName(): String {
        return "KakaoLogin"
    }

    init {
        if (KakaoSDK.getAdapter() == null) {
            KakaoSDK.init(object : KakaoAdapter() {
                override fun getApplicationConfig(): IApplicationConfig {
                    return IApplicationConfig { reactContext.applicationContext }
                }
            })
        }

        reactContext.addActivityEventListener(this)
        Session.getCurrentSession().addCallback(this)
    }

    @ReactMethod
    fun login(promise: Promise) {
        loginPromise = promise

        Session.getCurrentSession().open(AuthType.KAKAO_TALK, reactContext.currentActivity)
    }

    @ReactMethod
    fun logout(promise: Promise) {

        UserManagement.getInstance().requestLogout(object: LogoutResponseCallback() {
            override fun onCompleteLogout() {
                promise.resolve(true)
            }
        })
    }

    override fun onSessionOpened() {
        val promise = loginPromise

        if (promise != null) {
            val tokenInfo = Session.getCurrentSession().tokenInfo

            val result = WritableNativeMap()

            result.putString("accessToken", tokenInfo.accessToken)
            result.putString("refreshToken", tokenInfo.refreshToken)

            promise.resolve(result)
        }
    }

    override fun onSessionOpenFailed(exception: KakaoException?) {
        val promise = loginPromise

        var errorCode = "E_UNKNOWN"

        if (exception?.errorType == KakaoException.ErrorType.CANCELED_OPERATION) {
            errorCode = "E_CANCELLED"
        }

        promise?.reject(errorCode, exception?.localizedMessage, exception)
    }


    override fun onNewIntent(intent: Intent?) {
        //
    }

    override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)
    }

    override fun onCatalystInstanceDestroy() {
        reactContext.removeActivityEventListener(this)
        Session.getCurrentSession().removeCallback(this)
        super.onCatalystInstanceDestroy()
    }
}

class KakaoLoginPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return mutableListOf(KakaoLoginModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return mutableListOf()
    }
}
