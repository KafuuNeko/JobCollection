package cc.kafuu.jobcollection.servlet.administer

import cc.kafuu.jobcollection.Application
import cc.kafuu.jobcollection.utils.JsonUtils
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "login_administer", value = ["/api/administer/login"])
class LoginAdminister : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp?.contentType = "application/json"
        resp?.characterEncoding = "UTF-8"
        resp?.writer?.println(JsonUtils.makeBaseResultJson(-1, "非法请求"))
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp?.contentType = "application/json"
        resp?.characterEncoding = "UTF-8"

        val username = req?.getParameter("username")
        val password = req?.getParameter("password")

        resp?.writer?.println(
            if (username == null || password == null) {
                JsonUtils.makeBaseResultJson(-1, "参数不完整")
            } else {
                login(username, password)
            }
        )
    }

    private fun login(username: String, password: String) =
        if (Application.users[username]?.password != password) {
            JsonUtils.makeBaseResultJson(-1, "用户名或密码错误")
        } else {
            val token = Application.token.newToken()
            Application.token.put(token, "username", username)
            JsonUtils.makeBaseResultJson(0, "登录成功").apply {
                addProperty("token", token)
            }
        }

}