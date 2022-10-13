function uploadTaskAjax() {
    document.forms[0].StudentID.value = document.forms[0].StudentID.value.replace(/\s/g, "")
    document.forms[0].Name.value = document.forms[0].Name.value.replace(/(^\s*)|(\s*$)/g, "")
    document.forms[0].Upload.disabled = true

    $.ajax({
        url: "job/upload",
        type: "post",
        data: new FormData(document.forms[0]),
        cache: false,
        processData: false,
        contentType: false,
        success: function (result) {
            document.forms[0].Upload.disabled = false
            if (result.code === 0) {
                swal("提交结果", result.message, "success");
            } else {
                swal("提交结果", result.message, "error");
            }
        },
        error: function () {
            document.forms[0].Upload.disabled = false
            swal("提交结果", "提交失败，请检查网络", "error");
        }
    })
}

function uploadTask(maxSize) {
    if (document.forms[0].JobID.value === "") {
        swal("表单不完整", "请选择正确的作业", "error");
        return;
    }
    if (document.forms[0].StudentID.value === "") {
        swal("表单不完整", "请先输入学号", "error");
        return;
    }
    if (document.forms[0].Name.value === "") {
        swal("表单不完整", "请先输入姓名", "error");
        return;
    }
    if (document.forms[0].File.files.length === 0) {
        swal("表单不完整", "请先选择文件", "error");
        return;
    }

    if (document.forms[0].File.files[0].size > maxSize) {
        swal("文件过大", "您所选择的文件尺寸超出最大限制(" + maxSize / 1024 / 1024 + "M)", "error");
        return;
    }

    let jobName = document.forms[0].JobID.options[parseInt(document.forms[0].JobID.selectedIndex)].text

    swal({
        title: jobName,
        text: "您当前选择提交的作业是「" + jobName + "」,请仔细确认选择无误后点击确认提交。",
        dangerMode: true,
        buttons: ["取消", "确认提交"]
    }).then((status) => {
        if (status) {
            uploadTaskAjax()
        }
    });
}

function studentIdChange() {
    document.forms[0].StudentID.value = document.forms[0].StudentID.value.replace(/[^\d]/g, '')

    if (document.forms[0].StudentID.value.length === 10) {
        document.forms[0].Name.disabled = true
        $.ajax({
            url: "student/query",
            type: "post",
            data: "query=student&student_id=" + document.forms[0].StudentID.value.replace(/(^\s*)|(\s*$)/g, ""),
            success: function (result) {
                document.forms[0].Name.disabled = false
                if (result.code === 0) {
                    document.forms[0].Name.value = result.record.student_name
                }
            },
            error: function () {
                document.forms[0].Name.disabled = false
            }
        })
    }
}

function nameChange() {
    if (document.forms[0].Name.value.length >= 2) {
        document.forms[0].StudentID.disabled = true
        let name = document.forms[0].Name.value.replace(/(^\s*)|(\s*$)/g, "")
        $.ajax({
            url: "student/query",
            type: "post",
            data: "query=student&student_name=" + name,
            success: function (result) {
                document.forms[0].StudentID.disabled = false
                if (result.code === 0) {
                    if (result.total === 1) {
                        document.forms[0].StudentID.value = result.students[0].student_id
                    } else if (result.total === 0) {
                        swal("未找到用户", "数据库未收录的用户：" + name, "error");
                    } else if (result.total > 1) {
                        swal("无法确定唯一结果", "查询到多个'" + name + "'，请输入学号确认具体用户", "error");
                    }
                }
            },
            error: function () {
                document.forms[0].StudentID.disabled = false
            }
        })
    }
}

document.forms[0].Name.onkeydown = function (event) {
    if (event.keyCode === 13) {
        nameChange()
    }
}
