//显示文件名称
function loadFile(file) {
    var name = file.name;
    $("#filename").val("  " + name);
}

//页面加载后执行默认查询
$(function () {
    onRead(1, 5);
    $("#file_del_btn").click(fileDel);
})

//全局id
var globalId = "";

//提交事件
$("#file_btn").click(fileAdd);

//删除事件


//选择页码进行分页查询
function pageJump() {
    var pageNum = $('#select_id option:selected').val();
    onRead(pageNum);
}

//下载
function fileDownFun(id) {
    //location.href = "http://pcaooyn7y.bkt.clouddn.com/"+;
    $.ajax({
        type: 'get',
        url: "file/findById?id=" + id,
        cache: false,
        dataType: 'json',
        processData: false,
        contentType: false,
        success: function (data) {
            location.href = "http://pcaooyn7y.bkt.clouddn.com/" + data.filePath;
        }
    })

}

//删除时获取文件ID
function fileDelFun(id) {
    globalId = id;
}


//删除文件
function fileDel() {
    $.ajax({
        type: 'get',
        url: "file/del?id=" + globalId,
        cache: false,
        dataType: 'json',
        processData: false,
        contentType: false,
        success: function (data) {
            location.reload();
            console.log(data);
        },
        error: function (data) {
            alert("失败");
            console.log(data);
        }
    });
}

function fileAdd() {
    $('#mark').css("display", "block")
    var formData = new FormData($('#file_from_id')[0]);
    alert(formData)
    $.ajax({
        type: 'post',
        //url: "fileUD/upload",
        url: "qiniu/upload",
        data: formData,
        cache: false,
        dataType: 'json',
        processData: false,
        contentType: false,
        success: function (data) {

            if (data.state == 1) {
                $('#mark').css("display", "none")
                alert("文件名重复!");
                return;
            } else if (data.state == 2) {
                $('#mark').css("display", "none")
                alert("检测到上传的文件为空!");
                return;
            }
            location.reload();
            console.log(data);
        },
        error: function (data) {
            $('#mark').css("display", "none")
            alert("失败");
            console.log(data);
        }
    });
}

//分页查询
function onRead(pageNum) {
    $.ajax({
        type: 'get',
        url: "file/find?pageNum=" + pageNum + "&pageSize=" + 5,
        cache: false,
        dataType: 'json',
        processData: false,
        contentType: false,
        success: function (data) {
            console.log(data)
            var dataList = data.data.list;
            var htmls = '';
            for (var i = 0; i < dataList.length; i++) {
                var index = dataList[i];
                htmls +=
                    '<div class="row">\n' +
                    '    <div class="text-center col-lg-2 col-md-2 col-sm-2 col-xs-2">\n' +
                    '        ' + index.fileCode +
                    '    </div>\n' +
                    '    <div class="text-center col-lg-4 col-md-4 col-sm-4 col-xs-4">\n' +
                    '        ' + index.fileName +
                    '    </div>\n' +
                    '    <div class="text-center col-lg-2 col-md-2 col-sm-2 col-xs-2">\n' +
                    '        ' + index.fileSize +
                    '    </div>\n' +
                    '    <div class="text-center col-lg-2 col-md-2 col-sm-2 col-xs-2">\n' +
                    '        ' + index.createDate +
                    '    </div>\n' +
                    '    <div class="text-center col-lg-2 col-md-2 col-sm-2 col-xs-2">\n' +
                    '        <button class="btn btn-primary btn-xs" onclick="fileDownFun(' + index.id + ')">下载</button>\n' +
                    '        <button class="btn btn-danger btn-xs" onclick="fileDelFun(' + index.id + ')" data-toggle="modal" data-target="#deleteSource">\n' +
                    '            删除\n' +
                    '        </button>\n' +
                    '    </div>\n' +
                    '</div>';
            }
            $("#rows_id").html(htmls);

            var pageHtml = '' +
                ' <ul class="pagination">\n' +
                '<li>\n' +
                '    <select id="select_id" >' +

                '    </select>\n' +
                '    页\n' +
                '</li>\n' +
                '<li class="gray">\n' +
                '    共' + data.data.pages + '页' +
                '</li>\n' +
                '<li>\n' +
                '    <i id="i_id_1" class="glyphicon glyphicon-menu-left">\n' +
                '    </i>\n' +
                '</li>\n' +
                '<li>\n' +
                '    <i id="i_id_2" class="glyphicon glyphicon-menu-right">\n' +
                '    </i>\n' +
                '</li>\n' +
                '</ul>';

            $("#file_page").html(pageHtml);

            var liHtml = '';
            for (var i = 1; i <= data.data.pages; i++) {
                liHtml += '<option>' + i + '</option>';
            }

            $("#select_id").html(liHtml);
            $("#")
            var select = document.getElementById('select_id');
            select[data.data.pageNum - 1].selected = true;
            $("#select_id").on('change', function () {
                pageJump()
            })

            //上一页
            $("#i_id_1").click(function () {
                    if (data.data.pageNum != 1) {
                        onRead(data.data.pageNum - 1);
                    }
                }
            );

            //下一页
            $("#i_id_2").click(function () {
                    if (data.data.pageNum < data.data.pages) {
                        onRead(data.data.pageNum + 1);
                    }
                }
            );

        },
        error: function (data) {
            alert("拉取数据失败");
            console.log(data);
        }
    });
}
