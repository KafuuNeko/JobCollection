<%@ page import="cc.kafuu.jobcollection.bean.JobRecord" %>
<%@ page import="cc.kafuu.jobcollection.dao.DBTableJobs" %>
<%@ page import="java.util.List" %>
<%@ page import="cc.kafuu.jobcollection.bean.StudentRecord" %>
<%@ page import="cc.kafuu.jobcollection.dao.DBTableStudents" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int total = 0;
    List<StudentRecord> studentRecordList = null;
    JobRecord jobRecord = null;
    try {
        long jobId = Long.parseLong(request.getParameter("job_id"));

        jobRecord = DBTableJobs.INSTANCE.queryJobById(jobId);

        if (jobRecord != null) {
            studentRecordList = DBTableStudents.INSTANCE.queryListOfMissingAssignments(jobId);
            total = studentRecordList.size();
        }
    } catch (Exception e) {
        //e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>作业缺交情况查询</title>
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/styles.css">
    <link rel="stylesheet" href="assets/css/form.css">
</head>

<body>
<nav class="navbar navbar-light navbar-expand-md py-3">
    <div class="container"><a class="navbar-brand d-flex align-items-center" href="index.jsp"><span
            class="bs-icon-sm bs-icon-rounded bs-icon-primary d-flex justify-content-center align-items-center me-2 bs-icon"><svg
            xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16"
            class="bi bi-bezier">
                        <path fill-rule="evenodd"
                              d="M0 10.5A1.5 1.5 0 0 1 1.5 9h1A1.5 1.5 0 0 1 4 10.5v1A1.5 1.5 0 0 1 2.5 13h-1A1.5 1.5 0 0 1 0 11.5v-1zm1.5-.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1zm10.5.5A1.5 1.5 0 0 1 13.5 9h1a1.5 1.5 0 0 1 1.5 1.5v1a1.5 1.5 0 0 1-1.5 1.5h-1a1.5 1.5 0 0 1-1.5-1.5v-1zm1.5-.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1zM6 4.5A1.5 1.5 0 0 1 7.5 3h1A1.5 1.5 0 0 1 10 4.5v1A1.5 1.5 0 0 1 8.5 7h-1A1.5 1.5 0 0 1 6 5.5v-1zM7.5 4a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1z"></path>
                        <path d="M6 4.5H1.866a1 1 0 1 0 0 1h2.668A6.517 6.517 0 0 0 1.814 9H2.5c.123 0 .244.015.358.043a5.517 5.517 0 0 1 3.185-3.185A1.503 1.503 0 0 1 6 5.5v-1zm3.957 1.358A1.5 1.5 0 0 0 10 5.5v-1h4.134a1 1 0 1 1 0 1h-2.668a6.517 6.517 0 0 1 2.72 3.5H13.5c-.123 0-.243.015-.358.043a5.517 5.517 0 0 0-3.185-3.185z"></path>
                    </svg></span><span>作业收集</span></a>

        <button data-bs-toggle="collapse" class="navbar-toggler" data-bs-target="#navcol-1"><span
                class="visually-hidden">Toggle navigation</span><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse" id="navcol-1">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="index.jsp">作业提交</a></li>
                <li class="nav-item"><a class="nav-link active" href="#">缺交情况</a></li>
            </ul>
            <button class="btn btn-primary" type="button" href="administer">管理</button>
        </div>
    </div>
</nav>

<div class="container d-flex d-md-flex d-xl-flex align-items-md-center align-items-xl-start">
    <div class="navbar-nav-scroll">
        <ul class="nav nav-pills flex-column border rounded d-sm-flex">
            <%
                List<JobRecord> jobs = DBTableJobs.INSTANCE.getAllJob(false);
                for (int i = 0; i < jobs.size(); ++i) {
                    String active = "";
                    if (jobRecord != null && jobs.get(i).getJobId() == jobRecord.getJobId()) {
                        active = "active";
                    }
                    out.println("<li class=\"nav-item\"><a class=\"nav-link " + active + " \" href=\"?job_id=" + jobs.get(i).getJobId() + "\">" + jobs.get(i).getJobName() + "</a></li>");
                }
            %>
        </ul>
    </div>

    <div class="table-responsive border rounded flex-fill navbar-nav-scroll">
        <table class="table">
            <thead>
            <tr>
                <th>#</th>
                <th>学号</th>
                <th>姓名</th>
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
                    <p><%
                        if (jobRecord == null) {
                            out.println("请选择要查询的作业");
                        } else {
                            out.println("共查询到" + total + "条缺交记录");
                        }
                    %></p>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="assets/bootstrap/js/bootstrap.min.js"></script>
</body>

</html>