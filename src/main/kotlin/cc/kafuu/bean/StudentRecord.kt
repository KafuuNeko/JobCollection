package cc.kafuu.bean

import cc.kafuu.ext.IToJsonObjectAble
import com.google.gson.JsonObject

data class StudentRecord(
    val studentId: Long,
    val studentName: String,
    val studentSex: String,
    val studentClass: Int,
    val studentMajor: String
) : IToJsonObjectAble {
    public override fun toJsonObject(): JsonObject {
        val record = JsonObject()
        record.addProperty("student_id", studentId)
        record.addProperty("student_name", studentName)
        record.addProperty("student_sex", studentSex)
        record.addProperty("student_class", studentClass)
        record.addProperty("student_major", studentMajor)
        return record
    }
}
