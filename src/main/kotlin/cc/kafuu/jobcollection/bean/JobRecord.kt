package cc.kafuu.jobcollection.bean

import cc.kafuu.jobcollection.ext.IToJsonObjectAble
import com.google.gson.JsonObject
import java.sql.Timestamp

data class JobRecord(
    val jobId: Long,
    val jobName: String,
    val startTime: Timestamp,
    val endTime: Timestamp
) : IToJsonObjectAble {
    public override fun toJsonObject(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("job_id", jobId)
        jsonObject.addProperty("job_name", jobName)
        jsonObject.addProperty("job_start_time", startTime.toString())
        jsonObject.addProperty("job_end_time", endTime.toString())
        return jsonObject
    }
}