package cc.kafuu.jobcollection

import cc.kafuu.jobcollection.bean.User
import cc.kafuu.jobcollection.bean.UserGroup
import cc.kafuu.jobcollection.utils.BytesUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import mu.KotlinLogging
import java.security.MessageDigest
import java.util.*

object Application {
    private val mLogger = KotlinLogging.logger {}

    val rootDirPath: String
    val config: JsonObject
    val uploadMaxSize: Long

    val token = TokenManage()
    private val mTokenGcTimer = Timer()

    val users = kotlin.collections.HashMap<String, User>()
    val userGroups = kotlin.collections.HashMap<String, UserGroup>()

    //读app.json配置
    init {
        val reader = javaClass.classLoader?.getResourceAsStream("app.json")?.bufferedReader()
        assert(reader != null)

        config = Gson().fromJson(reader, JsonObject::class.java)

        val uploadConfig = config["upload"]?.asJsonObject
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

    init {
        val reader = javaClass.classLoader?.getResourceAsStream("users.json")?.bufferedReader()
        assert(reader != null)

        //save sha1(job_collection::password@password)
        val usersJson = Gson().fromJson(reader, JsonObject::class.java)
        for (user in usersJson["users"].asJsonArray) {
            val username = user.asJsonObject["username"].asString
            val password = BytesUtils.bytesToHexString(
                MessageDigest.getInstance("SHA-1")
                    .digest("job_collection::user@$username/${user.asJsonObject["password"].asString}".toByteArray())
            )
            User(
                username,
                password,
                user.asJsonObject["group"].asString
            ).apply {
                users[name] = this
            }

            mLogger.info("Read user: $username/$password")
        }
    }

}