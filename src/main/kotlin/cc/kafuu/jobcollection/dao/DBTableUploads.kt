package cc.kafuu.jobcollection.dao

import cc.kafuu.jobcollection.bean.UploadRecord
import java.sql.ResultSet

object DBTableUploads {
    /**
     * 查询上传记录
     * @param jobId 作业ID
     * @param studentId 学生ID
     *
     * @return 上传记录对象，如果未查到则返回空(null)
     * */
    public fun queryRecord(jobId: Long, studentId: Long): UploadRecord? {
        return DBJobCollection.connect.prepareStatement(
            "SELECT * FROM uploads WHERE job_id=? AND student_id=?",
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY
        )?.use { stmt ->
            stmt.setLong(1, jobId)
            stmt.setLong(2, studentId)
            stmt.executeQuery()?.use {
                if (!it.first()) {
                    null
                } else {
                    UploadRecord(it.getLong(1), it.getLong(2), it.getString(3), it.getTime(4))
                }
            }
        }
    }

    /**
     * 添加一个上传记录
     * @param jobId 作业ID
     * @param studentId 学生ID
     * @param filePath 上传的作业文件保存路径
     *
     * @return 成功修改返回true，失败返回false
     * */
    public fun addRecord(jobId: Long, studentId: Long, filePath: String): Boolean {
        return try {
            DBJobCollection.connect.prepareStatement("INSERT INTO uploads(job_id, student_id, file_path, upload_time) VALUES(?, ?, ?, now())")
                ?.use {
                    it.setLong(1, jobId)
                    it.setLong(2, studentId)
                    it.setString(3, filePath)
                    it.executeUpdate() > 0
                } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 更新已上传的记录
     * @param jobId 作业ID
     * @param studentId 学生ID
     * @param filePath 作业文件保存路径
     *
     * @return 成功修改返回true，失败返回false
     * */
    public fun updateRecord(jobId: Long, studentId: Long, filePath: String): Boolean {
        return try {
            DBJobCollection.connect.prepareStatement("UPDATE uploads SET file_path=?, upload_time=now() WHERE job_id=? AND student_id=?")
                ?.use {
                    it.setString(1, filePath)
                    it.setLong(2, jobId)
                    it.setLong(3, studentId)
                    it.executeUpdate() > 0
                } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    public fun addOrUpdateRecord(jobId: Long, studentId: Long, filePath: String): Boolean {
        return if (queryRecord(jobId, studentId) == null) {
            addRecord(jobId, studentId, filePath)
        } else {
            updateRecord(jobId, studentId, filePath)
        }
    }
}