package cc.kafuu.jobcollection.ext

import com.google.gson.JsonObject

interface IToJsonObjectAble {
    fun toJsonObject(): JsonObject
}