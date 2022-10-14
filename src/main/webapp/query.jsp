<%@ page import="cc.kafuu.jobcollection.dao.DBTableStudents" %>
<%@ page import="cc.kafuu.jobcollection.bean.StudentRecord" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int total = 0;
    List<StudentRecord> studentRecordList = null;
    try {
        long jobId = Long.parseLong(request.getParameter("job_id"));
        studentRecordList = DBTableStudents.INSTANCE.queryListOfMissingAssignments(jobId);
        total = studentRecordList.size();
    } catch (Exception e) {
        //e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html>

<head>
    <title>Bootstrap5 实例</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/form.css">
    <link rel="stylesheet" href="assets/css/styles.css">

    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery-3.6.1.min.js"></script>
    <script src="assets/js/sweetalert.min.js"></script>
</head>

<table class="table table-striped table-hover">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col">学号</th>
        <th scope="col">姓名</th>
    </tr>
    </thead>
    <tbody>

    <%
        if (studentRecordList != null) {
            for (int index = 0; index < studentRecordList.size(); ++index) {
                out.println("<tr>");
                out.println("<th scope=\"row\">" + (index + 1) + "</th>");
                out.println("<td>" + studentRecordList.get(index).getStudentId() + "</td>");
                out.println("<td>" + studentRecordList.get(index).getStudentName() + "</td>");
                out.println("</tr>");
            }
        }
    %>

    </tbody>
</table>

<div class="container py-4 py-xl-5">
    <div class="row mb-5">
        <div class="col-md-8 col-xl-6 text-center mx-auto">
            <h3>共查到<%=total%>条记录</h3>
        </div>
    </div>
</div>

</body>

</html>