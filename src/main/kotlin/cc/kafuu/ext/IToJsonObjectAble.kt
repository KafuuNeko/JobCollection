package cc.kafuu.ext

import com.google.gson.JsonObject

interface IToJsonObjectAble {
    fun toJsonObject(): JsonObject
}