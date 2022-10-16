package cc.kafuu.jobcollection.servlet

import cc.kafuu.jobcollection.dao.DBTableUploads
import cc.kafuu.jobcollection.utils.JsonUtils
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "query_upload", value = ["/api/upload/query"])
class QueryUpload : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        doPost(req, resp)
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp?.characterEncoding = "UTF-8"
        resp?.contentType = "application/json"
        
        val studentId = req?.getParameter("student_id")?.toLongOrNull()
        val jobId = req?.getParameter("job_id")?.toLongOrNull()

        resp?.writer?.println(if (studentId == null || jobId == null) {
            JsonUtils.makeBaseResultJson(-1, "参数不完整")
        } else {
            val result = JsonUtils.makeBaseResultJson(0, "查询成功")
            val record = DBTableUploads.queryRecord(jobId, studentId)

            result.addProperty("records_exist", record != null)

            record?.let {
                result.add("record", it.toJsonObject())
            }

            result
        }.toString())
    }
}