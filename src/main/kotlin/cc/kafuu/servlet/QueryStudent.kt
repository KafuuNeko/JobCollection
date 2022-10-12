package cc.kafuu.servlet

import cc.kafuu.bean.toJsonArray
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
        val query = req?.getParameter("query")

        resp?.contentType = "application/json"
        resp?.characterEncoding = "UTF-8"

        resp?.writer?.println(
            when (query) {
                "student" -> queryUser(req.getParameter("student_name"), req.getParameter("student_id")?.toLongOrNull())
                "missing_assignments" -> queryMissingAssignmentsStudent(req.getParameter("job_id").toLongOrNull())
                else -> JsonUtils.makeBaseResultJson(-1, "结果为空")
            }
        )
    }

    /**
     * 查询用户（通过ID或名字）
     * */
    private fun queryUser(studentName: String?, studentId: Long?): JsonObject = studentId?.let { id ->
        DBTableStudents.queryById(id)?.let { studentRecord ->
            val json = JsonUtils.makeBaseResultJson(0, "查询成功")
            json.add("record", studentRecord.toJsonObject())
            json
        } ?: JsonUtils.makeBaseResultJson(-1, "无记录")
    } ?: studentName?.let {
        val json = JsonUtils.makeBaseResultJson(0, "查询成功")
        DBTableStudents.queryByName(studentName).let {
            json.addProperty("total", it.size)

            val students = JsonArray()
            it.forEach { studentRecord -> students.add(studentRecord.toJsonObject()) }

            json.add("students", students)
        }
        json

    } ?: JsonUtils.makeBaseResultJson(-1, "无记录")

    /**
     * 查询未交某个作业的用户列表
     * */
    private fun queryMissingAssignmentsStudent(jobId: Long?) = jobId?.let {id ->
        val list = DBTableStudents.queryListOfMissingAssignments(id)
        val result = JsonUtils.makeBaseResultJson(0, "查询成功")
        result.addProperty("total", list.size)
        result.add("students", list.toJsonArray())
        result
    } ?: JsonUtils.makeBaseResultJson(-1, "缺少必要参数")

}