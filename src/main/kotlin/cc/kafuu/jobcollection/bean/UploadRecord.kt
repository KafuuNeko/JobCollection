package cc.kafuu.jobcollection.bean

import cc.kafuu.jobcollection.ext.IToJsonObjectAble
import com.google.gson.JsonObject
import java.sql.Time

data class UploadRecord(
    val jobId: Long,
    val studentId: Long,
    val filePath: String,
    val uploadTime: Time
) : IToJsonObjectAble {
    override fun toJsonObject(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("job_id", jobId)
        jsonObject.addProperty("student_id", studentId)
        //jsonObject.addProperty("file_path", filePath)
        jsonObject.addProperty("upload_time", uploadTime.time)
        return jsonObject
    }
}

