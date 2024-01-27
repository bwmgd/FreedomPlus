package io.github.fplus.core.hook

import android.view.MotionEvent
import de.robv.android.xposed.XC_MethodHook
import io.github.fplus.OperateType
import io.github.fplus.TriggerType
import io.github.fplus.core.base.BaseHook
import io.github.fplus.core.config.ConfigV1
import io.github.fplus.core.helper.DexkitBuilder
import io.github.xpler.core.entity.NoneHook
import io.github.xpler.core.entity.OnBefore
import io.github.xpler.core.hookBlockRunning
import io.github.xpler.core.log.XplerLog

class HVideoPlayerHelper : BaseHook<Any>() {
    companion object {
        const val TAG = "HVideoPlayerHelper"
    }

    private val config get() = ConfigV1.get()

    override fun setTargetClass(): Class<*> {
        return DexkitBuilder.videoPlayerHelperClazz ?: NoneHook::class.java
    }

    @OnBefore
    fun methodBefore(params: XC_MethodHook.MethodHookParam, event: MotionEvent?) {
        hookBlockRunning(params) {
            if (!config.isTriggerType) {
                return
            }

            val operateType =
                OperateType.fromTriggerString(config.triggerOperateType, TriggerType.DOUBLE_CLICK)
            if (operateType != OperateType.LIKE && operateType != OperateType.ORIGINAL) {
                result = null
            }
        }.onFailure {
            XplerLog.e(it)
        }
    }
}