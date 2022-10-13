package cc.kafuu.jobcollection.dao

import java.sql.ResultSet

object DBTableConfig {

    public fun read(key: String) = try {
        DBJobCollection.connect.prepareStatement(
            "SELECT `value` FROM config WHERE `key`=?",
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY
        )?.use { stmt ->
            stmt.setString(1, key)
            stmt.executeQuery()?.use {resultSet ->
                if (resultSet.first()) {
                    resultSet.getString(1)
                } else {
                    null
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

}