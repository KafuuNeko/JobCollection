package cc.kafuu.bean

import java.sql.Time

data class UploadRecord(
    val jobId: Long,
    val studentId: Long,
    val filePath: String,
    val uploadTime: Time
)

