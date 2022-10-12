package cc.kafuu.bean

import java.sql.Time
import java.sql.Timestamp

data class JobRecord(
    val jobId: Long,
    val jobName: String,
    val startTime: Timestamp,
    val endTime: Timestamp
)
