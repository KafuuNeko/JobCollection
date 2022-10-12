package cc.kafuu.servlet

import javax.servlet.http.*
import javax.servlet.annotation.*

@WebServlet(name = "version", value = ["/version"])
class Version: HttpServlet() {
    private lateinit var message: String

    override fun init() {
        message = "Version: 1.0"
    }

    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html"
        val out = response.writer
        out.println("<html><body>")
        out.println("<h1>$message</h1>")
        out.println("</body></html>")
    }

    override fun destroy() {
        servletContext?.log("")
    }
}