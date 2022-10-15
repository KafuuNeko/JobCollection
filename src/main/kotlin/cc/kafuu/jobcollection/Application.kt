package cc.kafuu.jobcollection

import cc.kafuu.jobcollection.bean.Token
import cc.kafuu.jobcollection.utils.Heap
import com.google.gson.Gson
import com.google.gson.JsonObject
import mu.KotlinLogging
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

object Application {
    private val mLogger = KotlinLogging.logger {}

    val rootDirPath: String
    val config: JsonObject
    val uploadMaxSize: Long

    val token = TokenManage()
    private val mTokenGcTimer = Timer()

    //读app.json配置
    init {
        val reader = javaClass.classLoader?.getResourceAsStream("app.json")?.bufferedReader()
        assert(reader != null)

        config = Gson().fromJson(reader, JsonObject::class.java)

        val uploadConfig = config.get("upload")?.asJsonObject
        rootDirPath = uploadConfig?.get("root_dir")?.asString ?: "./JobCollection"
        uploadMaxSize = uploadConfig?.asJsonObject?.get("max_size")?.asLong ?: 33554432

        mLogger.info("RootDirPath: $rootDirPath")
        mLogger.info("UploadMaxSize: $uploadMaxSize")

        mLogger.info("Read config completed")
    }

    //初始化Token回收计时器
    init {
        mTokenGcTimer.schedule(object : TimerTask() {
            override fun run() {
                val count = token.recoveryFailureToken()
                if (count > 0) {
                    mLogger.info("Recovery failure token: $count")
                }
            }
        }, 1000, 1000)
    }



}