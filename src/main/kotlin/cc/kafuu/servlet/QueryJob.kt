package cc.kafuu.servlet

import cc.kafuu.dao.DBTableJobs
import cc.kafuu.utils.JsonUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "query_job", value = ["/job/query"])
class QueryJob : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        doPost(req, resp)
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp?.contentType = "application/json"
        resp?.characterEncoding = "UTF-8"
        resp?.writer?.println(queryJobs().toString())
    }

    private fun queryJobs() = try {
        val jobList = DBTableJobs.getAllJob()
        val result = JsonUtils.makeBaseResultJson(0, "查询成功")
        result.addProperty("total", jobList.size)

        val jobsJson = JsonArray()
        for (job in jobList) {
            val element = JsonObject()
            element.addProperty("job_id", job.jobId)
            element.addProperty("job_name", job.jobName)
            element.addProperty("job_start_time", job.startTime.toString())
            element.addProperty("job_end_time", job.endTime.toString())
            jobsJson.add(element)
        }

        result.add("jobs", jobsJson)

        result.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        JsonUtils.makeBaseResultJson(-1, "${e.message}")
    }


}