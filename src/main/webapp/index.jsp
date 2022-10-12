<%@ page import="cc.kafuu.dao.DBTableJobs" %>
<%@ page import="cc.kafuu.bean.JobRecord" %>
<%@ page import="java.util.List" %>
<%@ page import="cc.kafuu.Application" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>作业提交系统</title>
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/Login-Form-Basic.css">
    <link rel="stylesheet" href="assets/css/styles.css">
</head>

<body>
<section class="position-relative py-4 py-xl-5">
    <div class="container">
        <div class="row mb-5">
            <div class="col-md-8 col-xl-6 text-center mx-auto">
                <h2>作业提交</h2>
                <p class="w-lg-50">请务必仔细检查所选的作业并核对姓名学号</p>
                <p class="w-lg-50">输入学号可自动取得姓名，或输入姓名后回车自动取得学号。</p>
            </div>
        </div>
        <div class="row d-flex justify-content-center">
            <div class="col-md-6 col-xl-4">
                <div class="card mb-5">
                    <div class="card-body d-flex flex-column align-items-center">
                        <form class="text-center" method="post" enctype="multipart/form-data">
                            <div class="mb-3"><select name="JobID" class="form-select">
                                <optgroup label="请选择作业">
                                    <%
                                        List<JobRecord> jobs = DBTableJobs.INSTANCE.getAllJob(true);
                                        for (int i = 0; i < jobs.size(); ++i) {
                                            out.println("<option value=\"" + jobs.get(i).getJobId() + "\" selected=\"\">" + jobs.get(i).getJobName() + "</option>");
                                        }
                                    %>
                                </optgroup>
                            </select></div>
                            <div class="mb-3"><input class="form-control" type="text" name="StudentID" placeholder="学号"
                                                     oninput="studentIdChange()"></div>
                            <div class="mb-3"><input class="form-control" type="text" name="Name" placeholder="姓名"></div>
                            <div class="mb-3"><input class="form-control" type="file" name="File"></div>
                            <div class="mb-3"><input class="btn btn-primary d-block w-100" type="button" name="Upload"
                                                     onclick="uploadTask(<%=Application.INSTANCE.getUploadMaxSize()%>)" value="提交作业"></div>
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

<script src="assets/js/upload.js?version=1.2"></script>

</body>

</html>