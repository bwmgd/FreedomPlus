package io.github.fplus.core.config

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.freegang.extension.child
import com.freegang.extension.getStringOrDefault
import com.freegang.extension.parseJSON
import com.freegang.extension.storageRootFile
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKV.LibLoader
import io.github.webdav.WebDav
import org.json.JSONObject
import java.io.File

class ConfigV1 private constructor() {
    data class Version(
        var versionName: String = "", // 版本名称
        var versionCode: Long = 0L, // 版本代码
        var dyVersionName: String = "", // 抖音版本名称
        var dyVersionCode: Long = 0L, // 抖音版本代码
    )

    companion object {
        private val mmkv by lazy { MMKV.defaultMMKV() }

        private val config by lazy { ConfigV1() }

        fun getFreedomDir(context: Context): File {
            return context.applicationContext.storageRootFile
                .child(Environment.DIRECTORY_DOWNLOADS)
                .child("Freedom")
        }

        fun getConfigDir(context: Context): File {
            // return getFreedomDir(context).child(".config")
            return context.filesDir.child("fplus")
        }

        fun initialize(context: Context, libLoader: LibLoader? = null) {
            MMKV.initialize(context, getConfigDir(context).absolutePath, libLoader)
        }

        fun clear(context: Context) {
            clearMMKV()
            getFreedomDir(context).deleteRecursively()
        }

        fun clearMMKV() {
            mmkv.clearAll()
        }

        fun get() = config
    }

    /// 视频/图文/音乐下载
    var isDownload: Boolean = false
        get() {
            field = mmkv.getBoolean("isDownload", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isDownload", value)
            field = value
        }

    /// 按视频创作者单独创建文件夹
    var ownerDir: Boolean = false
        get() {
            field = mmkv.getBoolean("ownerDir", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("ownerDir", value)
            field = value
        }

    /// 通知栏下载
    var notificationDownload: Boolean = false
        get() {
            field = mmkv.getBoolean("notificationDownload", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("notificationDownload", value)
            field = value
        }

    /// 复制链接时弹出下载
    var copyLinkDownload: Boolean = false
        get() {
            field = mmkv.getBoolean("copyLinkDownload", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("copyLinkDownload", value)
            field = value
        }

    /// 视频编码 [Auto, H265, H264]
    var videoCoding: String = "H265"
        get() {
            field = mmkv.getString("videoCoding", "H265")!!
            return field
        }
        set(value) {
            mmkv.putString("videoCoding", value)
            field = value
        }

    /// 表情包/评论区视频、图片保存
    var isEmojiDownload: Boolean = false
        get() {
            field = mmkv.getBoolean("isEmojiDownload", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isEmojiDownload", value)
            field = value
        }

    /// 震动反馈
    var vibrate: Boolean = false
        get() {
            field = mmkv.getBoolean("vibrate", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("vibrate", value)
            field = value
        }

    /// 首页控件半透明
    var isTranslucent: Boolean = false
        get() {
            field = mmkv.getBoolean("isTranslucent", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isTranslucent", value)
            field = value
        }

    /// 首页控件透明度 [顶部导航, 视频控件, 视频控件右侧, 底部导航]
    var translucentValue: List<Int> = listOf(50, 50, 50, 50)
        get() {
            field = mmkv.getString("translucentValue", "50, 50, 50, 50")!!
                .split(",")
                .map { it.trim().toInt() }

            return if (field.size == 3) {
                listOf(field[0], field[1], field[1], field[2])
            } else {
                field
            }
        }
        set(value) {
            mmkv.putString("translucentValue", value.joinToString())
            field = value
        }

    /// 双击屏幕响应类型
    var isTriggerType: Boolean = false
        get() {
            field = mmkv.getBoolean("isTriggerType", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isTriggerType", value)
            field = value
        }

    /// "00000" 代表:长按,双击(左上,右上,左下,右下) 内容为OperateType(0~8)
    var triggerOperateType: String = "00000000"
        get() {
            field = mmkv.getString("triggerOperate", "00000000") ?: "00000000"
            return field
        }
        set(value) {
            mmkv.putString("triggerOperate", value)
            field = value
        }

    /// 视频时长超过10分钟提示
    var isLongtimeVideoToast: Boolean = false
        get() {
            field = mmkv.getBoolean("isLongtimeVideoToast", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isLongtimeVideoToast", value)
            field = value
        }

    /// 隐藏顶部tab
    var isHideTopTab: Boolean = false
        get() {
            field = mmkv.getBoolean("isHideTopTab", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isHideTopTab", value)
            field = value
        }

    /// 隐藏顶部tab包含的关键字, 逗号隔开
    var hideTopTabKeywords: String = "经验, 探索, 商城"
        get() {
            field = mmkv.getString("hideTopTabKeywords", "经验, 探索, 商城")!!
            return field
        }
        set(value) {
            mmkv.putString("hideTopTabKeywords", value)
            field = value
        }

    /// 隐藏底部tab
    var isHideBottomTab: Boolean = false
        get() {
            field = mmkv.getBoolean("isHideBottomTab", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isHideBottomTab", value)
            field = value
        }

    /// 隐藏底部tab包含的关键字, 逗号隔开
    var hideBottomTabKeywords: String = "商城, 朋友, 消息"
        get() {
            field = mmkv.getString("hideBottomTabKeywords", "商城, 朋友, 消息")!!
            return field
        }
        set(value) {
            mmkv.putString("hideBottomTabKeywords", value)
            field = value
        }

    /// 隐藏底部加号按钮
    var isHidePhotoButton: Boolean = false
        get() {
            field = mmkv.getBoolean("issHidePhotoButton", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("issHidePhotoButton", value)
            field = value
        }

    /// 底部加号按钮拍摄按钮状态: 0=显示占位按钮[允许拍摄]; 1=显示占位按钮[不允许拍摄]; 2=隐藏占位按钮[不显示]
    var photoButtonType: Int = 2
        get() {
            field = mmkv.getInt("photoButtonType", 2)
            return field
        }
        set(value) {
            mmkv.putInt("photoButtonType", value)
            field = value
        }

    /// 手势误触复确认
    var isPreventAccidentalTouch: Boolean = false
        get() {
            field = mmkv.getBoolean("isPreventAccidentalTouch", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isPreventAccidentalTouch", value)
            field = value
        }

    /// 视频右侧控件栏
    var isVideoOptionBarFilter: Boolean = false
        get() {
            field = mmkv.getBoolean("isVideoOptionBarFilter", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isVideoOptionBarFilter", value)
            field = value
        }

    /// 右侧控件栏类型关键字
    val videoOptionBarFilterTypes = setOf("头像", "喜欢", "评论", "收藏", "分享", "少推荐", "音乐")

    /// 右侧控件栏隐藏关键字
    var videoOptionBarFilterKeywords: String = "少推荐"
        get() {
            field = mmkv.getString("videoOptionBarFilterKeywords", "少推荐")!!
            return field
        }
        set(value) {
            mmkv.putString("videoOptionBarFilterKeywords", value)
            field = value
        }

    /// 视频过滤
    var isVideoFilter: Boolean = false
        get() {
            field = mmkv.getBoolean("isVideoFilter", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isVideoFilter", value)
            field = value
        }

    /// 视频类型关键字
    val videoFilterTypes = setOf("直播", "广告", "图文", "长视频", "推荐卡片", "推荐商家", "空文案")

    /// 视频过滤关键字
    var videoFilterKeywords: String = "直播, #生日, 广告, 买, 优惠"
        get() {
            field = mmkv.getString("videoFilterKeywords", "直播, #生日, 广告, 买, 优惠")!!
            return field
        }
        set(value) {
            mmkv.putString("videoFilterKeywords", value)
            field = value
        }

    /// 弹窗过滤
    var isDialogFilter: Boolean = false
        get() {
            field = mmkv.getBoolean("isDialogFilter", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isDialogFilter", value)
            field = value
        }

    /// 弹窗关闭提示
    var dialogDismissTips: Boolean = false
        get() {
            field = mmkv.getBoolean("dialogDismissTips", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("dialogDismissTips", value)
            field = value
        }

    /// 弹窗过滤关键字
    var dialogFilterKeywords: String = "现在安装, 立即升级"
        get() {
            field = mmkv.getString("dialogFilterKeywords", "现在安装, 立即升级")!!
            return field
        }
        set(value) {
            mmkv.putString("dialogFilterKeywords", value)
            field = value
        }

    /// 清爽模式
    var isNeatMode: Boolean = true
        get() {
            field = mmkv.getBoolean("isNeatMode", true)
            return field
        }
        set(value) {
            mmkv.putBoolean("isNeatMode", value)
            field = value
        }

    /// 自动连播
    var isAutoPlay: Boolean = false
        get() {
            field = mmkv.getBoolean("isAutoPlay", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isAutoPlay", value)
            field = value
        }

    /// 首页添加连播按钮
    var addAutoPlayButton: Boolean = false
        get() {
            field = mmkv.getBoolean("addAutoPlayButton", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("addAutoPlayButton", value)
            field = value
        }

    /// 启动时默认开启连播
    var defaultAutoPlay: Boolean = false
        get() {
            field = mmkv.getBoolean("defaultAutoPlay", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("defaultAutoPlay", value)
            field = value
        }

    /// 当前是否处于清爽模式
    var neatModeState: Boolean = false
        get() {
            field = mmkv.getBoolean("neatModeState", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("neatModeState", value)
            field = value
        }

    /// 移除悬浮挑战/评论贴纸
    var isRemoveSticker: Boolean = false
        get() {
            field = mmkv.getBoolean("isRemoveSticker", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isRemoveSticker", value)
            field = value
        }

    /// 移除底部播放控制栏
    var isRemoveBottomCtrlBar: Boolean = false
        get() {
            field = mmkv.getBoolean("isRemoveBottomCtrlBar", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isRemoveBottomCtrlBar", value)
            field = value
        }

    /// 消息防撤回
    var isPreventRecalled: Boolean = false
        get() {
            field = mmkv.getBoolean("isPreventRecalled", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isPreventRecalled", value)
            field = value
        }

    /// 撤回其他设置
    var preventRecalledOtherSetting: List<Boolean> = listOf(false)
        get() {
            field = mmkv.getString("preventRecalledOtherSetting", "false")!!
                .split(",")
                .map { it.trim().toBoolean() }
            return field
        }
        set(value) {
            mmkv.putString("preventRecalledOtherSetting", value.joinToString())
            field = value
        }

    /// 全屏沉浸式
    var isImmersive: Boolean = false
        get() {
            return mmkv.getBoolean("immersive", false)
        }
        set(value) {
            mmkv.putBoolean("immersive", value)
            field = value
        }

    // 系统隐藏项(状态栏、导航栏)
    var systemControllerValue: List<Boolean> = listOf(false, false)
        get() {
            field = mmkv.getString("systemControllerValue", "false, false")!!
                .split(",")
                .map { it.trim().toBoolean() }
            return field
        }
        set(value) {
            mmkv.putString("systemControllerValue", value.joinToString())
            field = value
        }

    // 评论区颜色模式
    var isCommentColorMode: Boolean = false
        get() {
            return mmkv.getBoolean("isCommentColorMode", false)
        }
        set(value) {
            mmkv.putBoolean("isCommentColorMode", value)
            field = value
        }

    // 评论区颜色模式 [0: 浅色模式、1: 深色模式、2: 跟随系统]
    var commentColorMode: Int = 0
        get() {
            return mmkv.getInt("commentColorMode", 0)
        }
        set(value) {
            mmkv.putInt("commentColorMode", value)
            field = value
        }

    /// 是否开启WebDav
    var isWebDav: Boolean = false
        get() {
            field = mmkv.getBoolean("isWebDav", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isWebDav", value)
            field = value
        }

    /// WebDav 配置
    var webDavConfig: WebDav.Config = WebDav.Config()
        get() {
            return WebDav.Config(
                mmkv.getString("webDavHost", "")!!,
                mmkv.getString("webDavUsername", "")!!,
                mmkv.getString("webDavPassword", "")!!,
            )
        }
        set(value) {
            field = value
            mmkv.putString("webDavHost", field.host)
            mmkv.putString("webDavUsername", field.username)
            mmkv.putString("webDavPassword", field.password)
        }

    /// WebDav 历史
    val webDavConfigList: List<WebDav.Config>
        get() {
            val set = mmkv.getStringSet("webDavHistory", emptySet())!!
            return set.map {
                val json = it.parseJSON()
                WebDav.Config(
                    host = json.getStringOrDefault("host"),
                    username = json.getStringOrDefault("username"),
                    password = json.getStringOrDefault("password"),
                )
            }
        }

    ///
    fun addWebDavConfig(config: WebDav.Config) {
        val set = mmkv.getStringSet("webDavHistory", mutableSetOf())!!
        set.add(config.toJson())
        mmkv.putStringSet("webDavHistory", set)
    }

    ///
    fun removeWebDavConfig(config: WebDav.Config) {
        val set = mmkv.getStringSet("webDavHistory", mutableSetOf())!!
        set.remove(config.toJson())
        mmkv.putStringSet("webDavHistory", set)
    }

    /// 定时退出
    var isTimedExit: Boolean = false
        get() {
            field = mmkv.getBoolean("isTimedExit", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isTimedExit", value)
            field = value
        }

    /// 定时退出 [运行时间, 空闲时间]
    var timedShutdownValue: List<Int> = listOf(10, 3)
        get() {
            field = mmkv.getString("timedShutdownValue", "10, 3")!!
                .split(",")
                .map { it.trim().toInt() }
            return field
        }
        set(value) {
            mmkv.putString("timedShutdownValue", value.joinToString())
            field = value
        }

    /// 保留应用后台
    var keepAppBackend: Boolean = false
        get() {
            field = mmkv.getBoolean("keepAppBackend", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("keepAppBackend", value)
            field = value
        }

    /// 崩溃容错
    var isCrashTolerance: Boolean = false
        get() {
            field = mmkv.getBoolean("isCrashTolerance", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isCrashTolerance", value)
            field = value
        }

    /// 去插件化
    var isDisablePlugin: Boolean = false
        get() {
            field = mmkv.getBoolean("isDisablePlugin", false)
            return field
        }
        set(value) {
            mmkv.putBoolean("isDisablePlugin", value)
            field = value
        }

    /// 版本信息
    var versionConfig: Version = Version()
        get() {
            return Version(
                mmkv.getString("versionName", "")!!,
                mmkv.getLong("versionCode", 0L),
                mmkv.getString("dyVersionName", "")!!,
                mmkv.getLong("dyVersionCode", 0L),
            )
        }
        set(value) {
            field = value
            mmkv.putString("versionName", value.versionName)
            mmkv.putLong("versionCode", value.versionCode)
            mmkv.putString("dyVersionName", value.dyVersionName)
            mmkv.putLong("dyVersionCode", value.dyVersionCode)
        }

    var is32BitTips: Boolean = true
        get() {
            field = mmkv.getBoolean("is32BitTips", true)
            return field
        }
        set(value) {
            field = value
            mmkv.putBoolean("is32BitTips", value)
        }

    var dexkitCache: JSONObject = JSONObject()
        get() {
            return mmkv.getString("dexkitCache", "")!!.parseJSON()
        }
        set(value) {
            field = value
            mmkv.putString("dexkitCache", value.toString())
        }
}
