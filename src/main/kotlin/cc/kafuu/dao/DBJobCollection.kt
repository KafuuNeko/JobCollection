package cc.kafuu.dao

import cc.kafuu.Application
import java.sql.Connection
import java.sql.DriverManager

object DBJobCollection {

    val connect: Connection

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
        connect =
            DriverManager.getConnection("jdbc:mysql://${Config.host}/${Config.dbName}", Config.user, Config.password)
        println("!!! Database connected")
    }

}