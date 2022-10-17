package cc.kafuu.jobcollection.servlet.administer

import cc.kafuu.jobcollection.Application
import cc.kafuu.jobcollection.utils.JsonUtils
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "login_administer", value = ["/api/administer/login"])
class LoginAdminister: HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        super.doGet(req, resp)
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        super.doPost(req, resp)

        val username = req?.getParameter("username")
        val password = req?.getParameter("password")
        if (username == null || password == null) {
            resp?.writer?.println(JsonUtils.makeBaseResultJson(-1, "参数不完整").toString())
            return
        }

        if (Application.users[username]?.password != password) {
            resp?.writer?.println(JsonUtils.makeBaseResultJson(-1, "用户名或密码错误").toString())
            return
        }

        val token = Application.token.newToken()
        Application.token.put(token, "username", username)

        resp?.writer?.println(JsonUtils.makeBaseResultJson(0, "登录成功").apply {
            addProperty("token", token)
        }.toString())

    }
}