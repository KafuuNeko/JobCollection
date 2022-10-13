package cc.kafuu.dao

import cc.kafuu.Application
import java.sql.Connection
import java.sql.DriverManager

object DBJobCollection {
    private var mConnect: Connection;

    val connect: Connection
        get() {
            synchronized(mConnect) {
                if (!mConnect.isValid(5)) {
                    if (!mConnect.isClosed) {
                        mConnect.close()
                    }
                    mConnect = DriverManager.getConnection("jdbc:mysql://${Config.host}/${Config.dbName}", Config.user, Config.password)
                }
            }
            return mConnect
        }

    object Config {
        val host: String
        val user: String?
        val password: String?
        val dbName: String

        init {
            val dbJson = Application.config.get("db")?.asJsonObject

            host = dbJson?.get("host")?.asString ?: "localhost"
            dbName = dbJson?.get("db_name")?.asString ?: "job_collection"

            user = dbJson?.get("user")?.asString
            password = dbJson?.get("password")?.asString
        }

    }

    init {
        Class.forName("com.mysql.cj.jdbc.Driver")
        mConnect = DriverManager.getConnection("jdbc:mysql://${Config.host}/${Config.dbName}", Config.user, Config.password)
    }

}