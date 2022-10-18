package cc.kafuu.jobcollection.dao

import cc.kafuu.jobcollection.bean.StudentRecord
import java.sql.ResultSet

object DBTableStudents {
    public fun queryById(id: Long): StudentRecord? {
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

    public fun queryByName(name: String): List<StudentRecord> {
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

    public fun queryListOfStatistics(jobId: Long, isMissing: Boolean): List<StudentRecord> {
        val students = ArrayList<StudentRecord>()

        DBJobCollection.connect.prepareStatement(
            "SELECT * FROM students WHERE student_id ${if (isMissing) "NOT IN" else "IN"} (SELECT uploads.student_id FROM uploads WHERE uploads.job_id = ?)",
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY
        )?.use { stmt ->
            stmt.setLong(1, jobId)
            stmt.executeQuery()?.use { resultSet ->
                while (resultSet.next()) {
                    students.add(
                        StudentRecord(
                            resultSet.getLong(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getInt(4),
                            resultSet.getString(5)
                        )
                    )
                }
            }
        }

        return students;
    }

    //student_id | student_name | student_sex | student_class | student_major
    private fun addStudent(studentName: String, studentSex: String, studentClass: Int, studentMajor: String) =
        DBJobCollection.connect.prepareStatement("INSERT INTO students(student_name,student_sex,student_class,student_major) VALUES(?, ?, ?, ?)")
            ?.use { stmt ->
                stmt.setString(1, studentName)
                stmt.setString(2, studentSex)
                stmt.setInt(3, studentClass)
                stmt.setString(4, studentMajor)

                stmt.executeUpdate() > 0
            } ?: false

    private fun updateStudent(record: StudentRecord) =
        DBJobCollection.connect.prepareStatement("UPDATE students SET student_id = ? WHERE student_name = ?, student_sex = ?, student_class = ?, student_major = ?")
            ?.use { stmt ->
                stmt.setLong(1, record.studentId)
                stmt.setString(2, record.studentName)
                stmt.setString(3, record.studentSex)
                stmt.setInt(4, record.studentClass)
                stmt.setString(5, record.studentMajor)
                stmt.executeUpdate() > 0
            } ?: false

}