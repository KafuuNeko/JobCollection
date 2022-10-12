package cc.kafuu.dao

import cc.kafuu.bean.StudentRecord
import java.sql.ResultSet

object DBTableStudents {
    fun queryById(id: Long): StudentRecord? {
        return DBJobCollection.connect.prepareStatement(
            "SELECT * FROM students WHERE student_id=?",
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY
        )?.use { stmt ->
            stmt.setLong(1, id)
            stmt.executeQuery()?.use { result ->
                if (result.first()) {
                    StudentRecord(
                        result.getLong(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4),
                        result.getString(5)
                    )
                } else null
            }
        }
    }

    fun queryByName(name: String): List<StudentRecord> {
        val records = ArrayList<StudentRecord>()

        DBJobCollection.connect.prepareStatement(
            "SELECT * FROM students WHERE student_name=?",
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY
        )?.use { stmt ->
            stmt.setString(1, name)
            stmt.executeQuery()?.use { result ->
                while (result.next()) {
                    records.add(
                        StudentRecord(
                            result.getLong(1),
                            result.getString(2),
                            result.getString(3),
                            result.getInt(4),
                            result.getString(5)
                        )
                    )
                }
            }
        }

        return records
    }
}