package br.com.myfitt.log

import android.util.Log
import org.json.JSONObject

private const val TAG = "LogTool"

object LogTool {

    fun log(message: Any) {
        Log.d(TAG, message.toString())
    }

    fun log(message: String) {
        Log.d(TAG, message)
    }

    fun log(message: StringBuilder.() -> String) {
        Log.d(TAG, message(StringBuilder()))
    }

    fun log(function: String, detail: Any, vararg params: Pair<String, Any>) {
        Log.d(TAG, "$function:\n${params.toMap()}\n$detail")
    }
}