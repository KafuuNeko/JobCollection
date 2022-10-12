package cc.kafuu.utils

import com.google.gson.JsonObject

object JsonUtils {
    public fun makeBaseResultJson(code: Int, message: String): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("code", code)
        jsonObject.addProperty("message", message)
        return jsonObject
    }
}