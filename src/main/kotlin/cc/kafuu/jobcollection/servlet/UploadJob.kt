package cc.kafuu.jobcollection.servlet

import cc.kafuu.jobcollection.Application
import cc.kafuu.jobcollection.dao.DBTableStudents
import cc.kafuu.jobcollection.dao.DBTableUploads
import cc.kafuu.jobcollection.utils.JsonUtils.makeBaseResultJson
import com.google.gson.JsonObject
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.io.path.Path

@WebServlet(name = "upload_job", value = ["/api/job/upload"])
class UploadJob : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp?.contentType = "text/html"
        resp?.writer?.println("error")
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp?.contentType = "application/json"
        resp?.characterEncoding = "UTF-8"

        req?.let {
            try {
                val factory = DiskFileItemFactory()
                //缓冲区16
                factory.sizeThreshold = 1024 * 1024 * 16

                val servletFileUpload = ServletFileUpload(factory)
                val parameterMap = servletFileUpload.parseParameterMap(req)

                resp?.writer?.println(handle(parameterMap).toString())
            } catch (e: Exception) {
                resp?.writer?.println(makeBaseResultJson(-1, "错误的提交数据"))
            }
        }
    }

    private fun Map<String, List<FileItem>>.getStringParameter(key: String) =
        this[key]?.firstOrNull()?.let {
            if (it.contentType != null || it.size > 64) {
                null
            } else {
                it.getString("UTF-8")
            }
        }

    /**
     * 处理请求数据
     * */
    private fun handle(parameterMap: Map<String, List<FileItem>>): JsonObject {
        //取普通参数
        val jobId = parameterMap.getStringParameter("JobID")?.toLongOrNull()
        val studentId = parameterMap.getStringParameter("StudentID")?.toLongOrNull()
        val name = parameterMap.getStringParameter("Name")

        //取用户上传的文件对象
        val file = parameterMap["File"]?.firstOrNull()
        val fileSuffix = file?.name?.let {
            val index = it.lastIndexOf('.')
            if (index == -1 || index + 1 >= it.length) {
                null
            } else {
                it.substring(index + 1)
            }
        }

        //校验用户提交的数据是否完整
        if (file == null || jobId == null || studentId == null || name == null) {
            return makeBaseResultJson(-1, "不完整的提交数据")
        }

        return try {
            val studentRecord = DBTableStudents.queryById(studentId) ?: throw Exception("学号不存在")

            if (name != studentRecord.studentName) {
                throw Exception("学号与姓名不匹配")
            }

            val uploadRecord = DBTableUploads.queryRecord(jobId, studentId)
            uploadRecord?.let {
                val oldFile = File(it.filePath)
                if (oldFile.exists()) {
                    if (!oldFile.delete()) {
                        throw Exception("无法删除旧记录")
                    }
                }
            }

            val jobDir = "${Application.rootDirPath}/Jobs/Uploads/$jobId"
            val filePath = "$jobDir/$studentId-${studentRecord.studentName}.${fileSuffix ?: "data"}"
            file.inputStream?.use { inputStream ->
                if (file.size > Application.uploadMaxSize) {
                    throw Exception("您上传的文件尺寸超出(${Application.uploadMaxSize.toFloat() / 1024 / 1024}M)限制")
                }

                Files.createDirectories(Path(jobDir))
                val outStream = FileOutputStream(filePath)
                outStream.use { outputStream ->
                    var bytes = inputStream.readNBytes(4096)
                    while (bytes != null && bytes.isNotEmpty()) {
                        outputStream.write(bytes)
                        bytes = inputStream.readNBytes(4096)
                    }
                }
            } ?: throw Exception("无法保存文件")

            if (!DBTableUploads.addOrUpdateRecord(jobId, studentId, filePath)) {
                throw Exception("上传记录失败")
            }

            makeBaseResultJson(0, "您已成功提交作业")
        } catch (e: Exception) {
            //e.printStackTrace()
            makeBaseResultJson(-1, "${e.message}")
        }
    }
}