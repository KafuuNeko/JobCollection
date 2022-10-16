<%@ page import="cc.kafuu.jobcollection.dao.DBTableJobs" %>
<%@ page import="cc.kafuu.jobcollection.bean.JobRecord" %>
<%@ page import="java.util.List" %>
<%@ page import="cc.kafuu.jobcollection.Application" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>作业收集</title>
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/form.css">
    <link rel="stylesheet" href="assets/css/styles.css">
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
                <li class="nav-item"><a class="nav-link active" href="#">作业提交</a></li>
                <li class="nav-item"><a class="nav-link" href="missing.jsp">缺交情况</a></li>
            </ul>
            <button class="btn btn-primary" type="button" href="administer">管理</button>
        </div>
    </div>
</nav>

<section class="position-relative py-4 py-xl-5">
    <div class="container">
        <div class="row mb-5">
            <div class="col-md-8 col-xl-6 text-center mx-auto">
                <p class="w-lg-50">请务必仔细检查所选的作业并核对姓名学号</p>
                <p class="w-lg-50">输入学号可自动取得姓名，或输入姓名后回车自动取得学号。</p>
            </div>
        </div>

        <%List<JobRecord> jobs = DBTableJobs.INSTANCE.getAllJob(true);%>

        <div class="row d-flex justify-content-center">
            <div class="col-md-6 col-xl-4">
                <div class="card mb-5">
                    <div class="card-body d-flex flex-column align-items-center">
                        <form class="text-center" method="post" enctype="multipart/form-data">
                            <div class="mb-3"><select name="JobID" class="form-select">
                                <optgroup label=<%=(jobs.isEmpty() ? "当前没有作业" : "请选择作业")%>>
                                    <option selected hidden disabled value="">点击选择您要提交的作业</option>
                                    <%
                                        for (int i = 0; i < jobs.size(); ++i) {
                                            out.println("<option value=\"" + jobs.get(i).getJobId() + "\" selected=\"" + ((i == 0) ? "true" : "false") + "\">" + jobs.get(i).getJobName() + "</option>");
                                        }
                                    %>
                                </optgroup>
                            </select></div>
                            <div class="mb-3"><input class="form-control" type="text" name="StudentID" placeholder="学号" oninput="studentIdChange()"></div>
                            <div class="mb-3"><input class="form-control" type="text" name="Name" placeholder="姓名"></div>
                            <div class="mb-3"><input class="form-control" type="file" name="File"></div>
                            <div class="mb-3"><input class="btn btn-primary d-block w-100" type="button" name="Upload" onclick="uploadTask(<%=Application.INSTANCE.getUploadMaxSize()%>)" value="提交作业"></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<script src="assets/bootstrap/js/bootstrap.min.js"></script>
<script src="assets/js/jquery-3.6.1.min.js"></script>
<script src="assets/js/sweetalert.min.js"></script>
<script src="assets/js/upload.js?version=1.6"></script>

<%
    int jobSelectIndex = 0;
    try {
        long jobId = Long.parseLong(request.getParameter("job_id"));
        for (int i = 0; i < jobs.size(); ++i) {
            if (jobs.get(i).getJobId() == jobId) {
                jobSelectIndex = i + 1;
                break;
            }
        }
    } catch (Exception ignored) {
    }
%>

<script>
    document.forms[0].JobID.selectedIndex = <%=jobSelectIndex%>;
</script>

</body>

</html>