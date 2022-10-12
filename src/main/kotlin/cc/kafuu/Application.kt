package cc.kafuu

import com.google.gson.Gson
import com.google.gson.JsonObject

object Application {
    lateinit var rootDirPath: String
    lateinit var config: JsonObject
    init {
        val reader = javaClass.classLoader?.getResourceAsStream("app.json")?.bufferedReader()
        assert(reader != null)

        config = Gson().fromJson(reader, JsonObject::class.java)
        println("config: $config")
        rootDirPath = config.get("upload")?.asJsonObject?.get("root_dir")?.asString ?: "./JobCollection"
    }

}