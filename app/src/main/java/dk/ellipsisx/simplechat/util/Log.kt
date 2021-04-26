package dk.ellipsisx.simplechat.util

import java.io.PrintWriter
import java.io.StringWriter

object Log {

    val TAG = "app"
    val DELIM = "\r\n   "
    private var silent = false

    fun setSilent(silent: Boolean) {
        Log.silent = silent
    }

    fun d(msg: String) {
        if (silent) return
        android.util.Log.d(TAG, "[DEBUG]" + prefix() + DELIM + msg)
    }

    fun d(msg: String, t: Throwable) {
        if (silent) return
        android.util.Log.d(TAG, "[DEBUG]" + prefix() + DELIM + msg, t)
    }

    fun i(msg: String) {
        if (silent) return
        android.util.Log.i(TAG, "[INFO]" + prefix() + DELIM + msg)
    }

    fun i(msg: String, t: Throwable) {
        if (silent) return
        android.util.Log.i(TAG, "[INFO]" + prefix() + DELIM + msg, t)
    }

    fun w(msg: String) {
        if (silent) return
        android.util.Log.w(TAG, "[WARN]" + prefix() + DELIM + msg)
    }

    fun w(msg: String, t: Throwable) {
        if (silent) return
        android.util.Log.w(TAG, "[WARN]" + prefix() + DELIM + msg, t)
    }

    fun e(msg: String) {
        if (silent) return
        android.util.Log.e(TAG, "[ERROR]" + prefix() + DELIM + msg)
    }

    fun e(msg: String, t: Throwable) {
        if (silent) return
        android.util.Log.e(TAG, "[ERROR]" + prefix() + DELIM + msg, t)
    }

    private fun prefix(): String {
        val elements = Thread.currentThread().stackTrace
        var doNext = false
        var doNextNext = false
        for (s in elements) {
            if (doNext) {
                val classname = s.className
                var index = classname.lastIndexOf('.') + 1
                if (index >= classname.length) {
                    index = 0
                }
                return "[" + classname.substring(index) + "." + s.methodName + "]"
            }
            if (doNextNext) {
                doNext = true
            } else {
                doNextNext = s.methodName == "prefix"
            }
        }
        return ""
    }

    /**
     * Includes exception message and stacktrace, and calls recursively on 'cause' throwable.
     * @param throwable
     * @return
     */
    fun toString(throwable: Throwable): String {
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        throwable.printStackTrace(printWriter)
        printWriter.flush()
        return writer.toString()
    }
}
