$('.file-manager-leftside li').on('click', function (e) {
    e.stopPropagation();
    $('.file-manager-leftside').find('li.active').removeClass('active');
    $(this).first().addClass('active');
});

$('.file-manager-leftside li > .list-holder .fa').on('click', function (e) {
    e.stopPropagation();
    if ($(this).hasClass('fa-chevron-right')) {
        $(this).removeClass('fa-chevron-right').addClass('fa-chevron-down');
        $(this).parents('.list-holder').next('ul').addClass('expanded');
    } else if ($(this).hasClass('fa-chevron-down')) {
        $(this).removeClass('fa-chevron-down').addClass('fa-chevron-right');
        $(this).parents('.list-holder').next('.expanded').removeClass('expanded');
    }
});
$(document).ready(function () {
    $(".file-row").click(function () {
        alert("test")
    });
});


//attachment title click event
$(document).ready(function () {
    $(".attachment-name").click(function () {
        var attachmentName = $(this).text();
        var attachmentId = $(this).data("attachment-id");

        //attachmentDetail 구현
        $.ajax({
            url: "/api/attachments/detail/" + attachmentId,
            method: "GET",
            contentType: "application/json",
            dataType: "json",
            success: function (result) {
                //초기화작업
                function clearDetailTable() {
                    $(".attachments-detail").empty();
                }

                function addDetailToTable(result) {

                    clearDetailTable();


                    var fileRow = $("<div>").addClass("file-row");

                    var fileNameCell = $("<div>").addClass("file-cell");
                    var fileName = $("<div>").addClass("file-name").text(result.userName);
                    fileNameCell.append(fileName);
                    fileRow.append(fileNameCell);

                    var fileDescriptionCell = $("<div>").addClass("file-cell");
                    var fileDescription = $("<div>").addClass("file-owner").text(result.description);
                    fileDescriptionCell.append(fileDescription);
                    fileRow.append(fileDescriptionCell);

                    var fileTypeCell = $("<div>").addClass("file-cell");
                    var fileType = $("<div>").addClass("file-date").text(result.createTime);
                    fileTypeCell.append(fileType);
                    fileRow.append(fileTypeCell);

                    var fileSizeCell = $("<div>").addClass("file-cell");
                    var fileSize = $("<div>").addClass("file-size").text(result.systemId);
                    fileSizeCell.append(fileSize);
                    fileRow.append(fileSizeCell);

                    $(".attachments-detail").html(fileRow);

                }

                addDetailToTable(result);


            },
            error: function (xhr, status, error) {
                alert("Error!");
                console.error(xhr, status, error);
            }
        });


        //ajax로 attachment안에 file목록 불러오기
        $.ajax({
            url: "/api/files/" + attachmentId,
            method: "GET",
            contentType: "application/json",
            dataType: "json",
            success: function (result) {
                //초기화작업
                function clearFileTable() {
                    $(".file-table-body").empty();
                }

                function addFilesToTable(result) {

                    clearFileTable();

                    var tableBody = $(".file-table-body");
                    var maxRows = 10;

                    for (var i = 0; i < maxRows; i++) {
                        var fileRow = $("<div>").addClass("file-row");

                        var fileAttIdCell = $("<div>").addClass("file-cell").css("display","none");
                        var fileAttId = $("<div>").addClass("file-id");
                        if (result[i]) {
                            fileAttId.val(result[i].attachmentId);
                        }
                        fileAttIdCell.append(fileAttId);
                        fileRow.append(fileAttIdCell);

                        var fileNameCell = $("<div>").addClass("file-cell");
                        var fileName = $("<div>").addClass("file-name");

                        // 체크박스 생성
                        var checkbox = $("<input>").attr({
                            type: "checkbox",
                            name: "checkbox1",
                            value: result.id,
                            style: "margin-right: 20px"
                        });
                        if (result[i]) {
                            // 체크박스 생성
                            var checkbox = $("<input>").attr({
                                type: "checkbox",
                                name: "checkbox1",
                                value: result[i].id,
                                style: "margin-right: 20px"
                            });

                            // 파일 이름 텍스트 생성
                            var fileNameText = document.createTextNode(result[i].name);

                            // 체크박스와 파일 이름을 fileName div에 추가
                            fileName.append(checkbox).append(fileNameText);
                        } else {
                            var fileNameText = document.createTextNode("");
                            fileName.append(fileNameText);
                        }

                        // fileName div를 fileNameCell에 추가
                        fileNameCell.append(fileName);
                        fileRow.append(fileNameCell);

                        var fileDescriptionCell = $("<div>").addClass("file-cell");
                        var fileDescription = $("<div>").addClass("file-description").text(result[i] ? result[i].description : "");
                        fileDescriptionCell.append(fileDescription);
                        fileRow.append(fileDescriptionCell);

                        var fileTypeCell = $("<div>").addClass("file-cell");
                        var fileType = $("<div>").addClass("file-type").text(result[i] ? result[i].type : "");
                        fileTypeCell.append(fileType);
                        fileRow.append(fileTypeCell);

                        var fileSizeCell = $("<div>").addClass("file-cell");
                        var fileSize = $("<div>").addClass("file-size").text(result[i] ? result[i].size : "");
                        fileSizeCell.append(fileSize);
                        fileRow.append(fileSizeCell);

                        tableBody.append(fileRow);
                    }
                }


                addFilesToTable(result);


            },
            error: function (xhr, status, error) {
                alert("Error!");
                console.error(xhr, status, error);
            }
        });

    });

});

function clearDetailTable() {
    $(".file-manager-rightside").empty();
}

$(document).ready(function () {
    $(document).on("click", ".file-row", function () {

        var fileName = $(this).find(".file-name").text();
        var fileDescription = $(this).find(".file-description").text();
        var fileType = $(this).find(".file-type").text();
        var filePath = $(this).find(".file-path").val();
        var fileSize = $(this).find(".file-size").text();

        $(".file-manager-rightside").html(`
            <img src="${getFileTypeImage(fileType)}" alt="상품 이미지">
            <h2>${fileName}</h2>
            <p>타입: ${fileType}</p>
            <p>크기: ${fileSize}</p>
            <p>설명: ${fileDescription}</p>
            <p>경로: ${filePath}</p>
        `);

        function getFileTypeImage(fileType) {
            if (fileType === "zip" || fileType===".zip") {
                return "images/zip.png";
            } else if (fileType === "hwp" || fileType===".hwp") {
                return "images/hwp.png";
            } else if (fileType === "xlsx" || fileType===".xlsx") {
                return "images/excel.png";
            }else if (fileType === "pdf" || fileType===".pdf") {
                return "images/pdf.png";
            }  else {
                return "images/images.png";
            }
        }
    });
});



