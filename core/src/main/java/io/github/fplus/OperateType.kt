package io.github.fplus

// 操作类型
enum class OperateType(val value: String) {
    // 点赞 评论 转发 收藏 下载 原操作 模块菜单 暂停视频
    ORIGINAL("原操作"),
    LIKE("点赞"),
    COMMENT("评论"),
    FORWARD("转发"),
    COLLECT("收藏"),
    DOWNLOAD("下载"),
    MODULE("模块菜单"),
    INFO("视频信息");

    companion object {
        private fun fromInt(value: Int) = values().first { it.ordinal == value }

        fun fromTriggerString(value: String, triggerType: TriggerType?): OperateType? {
            triggerType ?: return null
            return fromInt(value.substring(triggerType.ordinal, triggerType.ordinal + 1).toInt())
        }

        fun fromPosition(
            x: Float, y: Float, width: Int, height: Int, value: String, isDoubleClick: Boolean
        ): OperateType? {
            val triggerType = TriggerType.judgement(x, y, width, height, isDoubleClick)
            val fromTriggerString = fromTriggerString(value, triggerType)
            if (isDoubleClick && fromTriggerString == ORIGINAL) return LIKE
            return fromTriggerString
        }
    }
}

// 触发类型
enum class TriggerType(val value: String) {
    // 左上角长按, 右上角长按, 左下角长按, 右下角长按, 双击
    LEFT_TOP("左上角长按"),
    RIGHT_TOP("右上角长按"),
    LEFT_BOTTOM("左下角长按"),
    RIGHT_BOTTOM("右下角长按"),
    DOUBLE_LEFT_TOP("左上角双击"),
    DOUBLE_RIGHT_TOP("右上角双击"),
    DOUBLE_LEFT_BOTTOM("左下角双击"),
    DOUBLE_RIGHT_BOTTOM("右下角双击");

    companion object {
        fun judgement(
            x: Float,
            y: Float,
            width: Int,
            height: Int,
            isDoubleClick: Boolean
        ): TriggerType? {
            val xRatio = x / width
            val yRatio = y / height
            return TriggerType.values()[when {
                xRatio < 0.5 && yRatio < 0.5 -> 0
                xRatio >= 0.5 && yRatio < 0.5 -> 1
                xRatio < 0.5 && yRatio >= 0.5 -> 2
                xRatio >= 0.5 && yRatio >= 0.5 -> 3
                else -> return null
            } + if (isDoubleClick) 4 else 0]
        }
    }
}