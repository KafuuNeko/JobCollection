package cc.kafuu.servlet

import cc.kafuu.dao.DBTableStudents
import cc.kafuu.utils.JsonUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "query_student", value = ["/student/query"])
class QueryStudent : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        doPost(req, resp)
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val studentId = req?.getParameter("StudentID")?.toLongOrNull()
        val studentName = req?.getParameter("StudentName")

        val result = studentId?.let { id ->
            DBTableStudents.queryById(id)?.let { studentRecord ->
                val json = JsonUtils.makeBaseResultJson(0, "查询成功")

                val record = JsonObject()
                record.addProperty("student_id", id)
                record.addProperty("student_name", studentRecord.studentName)
                record.addProperty("student_sex", studentRecord.studentSex)
                record.addProperty("student_class", studentRecord.studentClass)
                record.addProperty("student_major", studentRecord.studentMajor)

                json.add("record", record)
                json
            } ?: JsonUtils.makeBaseResultJson(-1, "无记录")
        } ?: studentName?.let {
            val json = JsonUtils.makeBaseResultJson(0, "查询成功")
            DBTableStudents.queryByName(studentName).let {
                json.addProperty("total", it.size)

                val students = JsonArray()
                it.forEach {studentRecord ->
                    val record = JsonObject()
                    record.addProperty("student_id", studentRecord.studentId)
                    record.addProperty("student_name", studentRecord.studentName)
                    record.addProperty("student_sex", studentRecord.studentSex)
                    record.addProperty("student_class", studentRecord.studentClass)
                    record.addProperty("student_major", studentRecord.studentMajor)
                    students.add(record)
                }

                json.add("students", students)
            }
            json

        } ?: JsonUtils.makeBaseResultJson(-1, "无记录")

        resp?.contentType = "application/json"
        resp?.characterEncoding = "UTF-8"
        resp?.writer?.println(result.toString())
    }
}