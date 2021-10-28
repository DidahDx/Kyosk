package com.github.didahdx.kyosk.common

import androidx.annotation.Nullable
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import org.jetbrains.annotations.NotNull
import timber.log.Timber

/**
 * Created by Daniel Didah on 10/20/21.
 */
class TimberLoggingTree : Timber.DebugTree() {

    init {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(1) // (Optional) How many method line to show. Default 2
            .methodOffset(5) // Set methodOffset to 5 in order to hide internal method calls
            .tag("")
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        Logger.log(priority, "-$tag", message, t)
    }

    @Nullable
    override fun createStackElementTag(@NotNull element: StackTraceElement): String {
        return String.format(
            "Class:%s: Line: %s, Method: %s",
            super.createStackElementTag(element),
            element.lineNumber,
            element.methodName
        )
    }
}