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
    PAUSE("暂停视频");

    companion object {
        private fun fromInt(value: Int) = values().first { it.ordinal == value }

        fun fromTriggerString(value: String, triggerType: TriggerType) =
            fromInt(value.substring(triggerType.ordinal, triggerType.ordinal + 1).toInt())
    }
}

// 触发类型
enum class TriggerType(val value: String) {
    // 左上角长按, 右上角长按, 左下角长按, 右下角长按, 双击
    LEFT_TOP("左上角长按"),
    RIGHT_TOP("右上角长按"),
    LEFT_BOTTOM("左下角长按"),
    RIGHT_BOTTOM("右下角长按"),
    DOUBLE_CLICK("双击");

    companion object {
        fun judgement(x: Float, y: Float, width: Int, height: Int): TriggerType? {
            val xRatio = x / width
            val yRatio = y / height
            return when {
                xRatio < 0.5 && yRatio < 0.5 -> LEFT_TOP
                xRatio >= 0.5 && yRatio < 0.5 -> RIGHT_TOP
                xRatio < 0.5 && yRatio >= 0.5 -> LEFT_BOTTOM
                xRatio >= 0.5 && yRatio >= 0.5 -> RIGHT_BOTTOM
                else -> null
            }
        }
    }
}