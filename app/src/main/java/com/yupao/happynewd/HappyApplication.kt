package com.yupao.happynewd

import android.app.Application

class HappyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        initSdk(this)
    }


    /**
     *  初始化 增长营销套件SDK
     * @param application Application
     */
//    private fun initSdk(
//        application: Application,
//        activity: Activity? = null,
//        uuid: String? = null,
//        channel: String
//    ) {
//        /* 初始化SDK开始 */
//        // 第一个参数APPID: 参考2.1节获取
//        // 第二个参数CHANNEL: 填写渠道信息，请注意不能为空
//        val config = InitConfig(DEBUG_APP_ID, channel)
//        // 设置数据上送地址
//        config.setUriConfig(UriConstants.DEFAULT)
//        config.appName = DEBUG_APP_NAME
//        // 全埋点开关，true开启，false关闭
//        config.isAutoTrackEnabled = true
//        // fragment事件采集
//        config.isAutoTrackFragmentEnabled = true
//        // true:开启日志，参考4.3节设置logger，false:关闭日志
//        config.isLogEnable = true
//        config.logger = ILogger { msg, throwable ->
//            NetLogUtils.printPointerLog("message: " + msg + " - throwable: " + throwable?.message)
//        }
//        config.isAbEnable = true
//        config.picker = Picker(application, config)
//        // 打通H5埋点
//        config.isH5BridgeEnable = true
//
//        config.autoStart()
//        // 加密开关，true开启，false关闭
//        AppLog.setEncryptAndCompress(false)
//        if (activity == null) {
//            AppLog.init(application, config)
//            return
//        }
//        // activity 不为空时标识需要进行延迟初始化
//        AppLog.init(application, config, activity)
//    }

}