package cc.kafuu

import com.google.gson.Gson
import com.google.gson.JsonObject

object Application {
    val rootDirPath: String
    val config: JsonObject
    val uploadMaxSize: Long

    init {
        val reader = javaClass.classLoader?.getResourceAsStream("app.json")?.bufferedReader()
        assert(reader != null)

        config = Gson().fromJson(reader, JsonObject::class.java)
        println("config: $config")

        val uploadConfig = config.get("upload")?.asJsonObject
        rootDirPath = uploadConfig?.get("root_dir")?.asString ?: "./JobCollection"
        uploadMaxSize = uploadConfig?.asJsonObject?.get("max_size")?.asLong ?: 33554432

    }

}