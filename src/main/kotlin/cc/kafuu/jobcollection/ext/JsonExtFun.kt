package cc.kafuu.bean

import cc.kafuu.jobcollection.ext.IToJsonObjectAble
import com.google.gson.JsonArray

public fun List<IToJsonObjectAble>.toJsonArray(): JsonArray {
    val jsonArray = JsonArray()
    for (element in this) {
        jsonArray.add(element.toJsonObject())
    }
    return jsonArray
}


