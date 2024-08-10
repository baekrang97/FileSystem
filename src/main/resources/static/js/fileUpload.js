// upload.js
$(document).ready(function () {
    let objDragAndDrop = $(".drag-and-drop-div");
    let fd = new FormData();

    $(document).on("dragenter", ".drag-and-drop-div", function (e) {
        e.stopPropagation();
        e.preventDefault();
        $(this).css('border', '2px solid #0B85A1');
    });

    $(document).on("dragover", ".drag-and-drop-div", function (e) {
        e.stopPropagation();
        e.preventDefault();
    });

    $(document).on("drop", ".drag-and-drop-div", function (e) {
        $(this).css('border', '2px dotted #0B85A1');
        e.preventDefault();
        let files = e.originalEvent.dataTransfer.files;
        handleFileUpload(files, objDragAndDrop);
    });

    $(document).on('dragenter', function (e) {
        e.stopPropagation();
        e.preventDefault();
    });

    $(document).on('dragover', function (e) {
        e.stopPropagation();
        e.preventDefault();
        objDragAndDrop.css('border', '2px dotted #5C5C5C');
    });

    $(document).on('drop', function (e) {
        e.stopPropagation();
        e.preventDefault();
    });

    objDragAndDrop.on('click', function (e) {
        $('input[type=file]').trigger('click');
    });

    $('input[type=file]').on('change', function (e) {
        let files = e.originalEvent.target.files;
        handleFileUpload(files, objDragAndDrop);
    });
    $(document).on('click', '.delete-file', function () {
        let panel = $(this).closest('.panel-default');
        let index = $('#fileList .panel-default').index(panel);
        if (index !== -1) {
            // 삭제할 파일의 인덱스가 유효한 경우 FormData 객체에서 해당 파일을 삭제합니다.
            fd.delete('file[' + index + ']');
        }
        panel.remove();
        let countFile = $('#file-list .panel-default').length;

        $('#count-file').html("<strong>" + countFile + "개 </strong>");

    });
    //FormData 정의 후 파일업로드시 저장.
    function handleFileUpload(files, obj) {
        console.log("zzzzzzzzzzzz")
        for (let i = 0; i < files.length; i++) {

            fd.append('file', files[i]);

            let status = new createStatusbar(obj);
            status.setFileNameSize(files[i].name, files[i].size);
            let countFile = $('#file-list .panel-default').length;
            console.log(countFile)
            $('#count-file').html("<strong>" + countFile + "개 </strong>");
            // sendFileToServer(fd, status);
        }
    }

    let rowCount = 0;

    function createStatusbar(obj) {

        console.log("createStatusbar 실행")
        this.pannelBody = $("<div class='panel-body row p-3'></div>");
        this.fileName = $("<div class='col-sm-8 file-name'></div>").appendTo(this.pannelBody);
        this.size = $("<div class='file-size col-sm-4' style='text-align: right;'></div>").appendTo(this.pannelBody);
        this.pannel = $("<div class='statusbar panel panel-default m-2'></div>")
        this.statusbar = this.pannel.append(this.pannelBody);
        // this.progressBar = $("<div class='progress' style='width:100%'><div class='progress-bar' role='progressbar' aria-valuenow='70' aria-valuemin='0' aria-valuemax='100' style='width:0%'>0%</div></div>").appendTo(this.pannel);

        this.descriptionInput = $("<div class='form-group p-3'><label>설명:</label><textarea class='form-control file-description' cols='30' rows='5' placeholder='설명을 입력하세요'></textarea></div>").appendTo(this.pannel);

        $("#file-list").append(this.statusbar)
        
        console.log("왜 안와");

        this.setFileNameSize = function (name, size) {
            let sizeStr = "";
            let sizeKB = size / 1024;
            if (parseInt(sizeKB) > 1024) {
                let sizeMB = sizeKB / 1024;
                sizeStr = sizeMB.toFixed(2) + " MB";
            } else {
                sizeStr = sizeKB.toFixed(2) + " KB";
            }

            let dotIndex = name.lastIndexOf('.');

            // 점 이후의 문자열이 확장자입니다.
            let extension = name.substr(dotIndex + 1) + ",";

            // 점 이전의 문자열이 파일 이름입니다.
            let filenameOnly = name.substr(0, dotIndex);

            let sizeFormat = "[" + extension + sizeStr + "]" + "&nbsp;&nbsp;<button class='btn btn-default btn-sm delete-file'>X 삭제</button>";

            this.fileName.html(filenameOnly);
            this.size.html(sizeFormat);
        };

        this.setAbort = function (jqxhr) {
            let sb = this.statusbar;
            this.abort.click(function () {
                jqxhr.abort();
                sb.hide();
            });
        };
    }

    //////////////////// default setting end
    function validateForm(attachmentName, systemId, userName, files) {
        if (attachmentName.trim() === '') {
            alert("제목을 입력하세요")
            return false;
        }
        if (systemId.trim() === '') {
            alert("시스템 ID를 입력하세요")
            return false;
        }
        if (userName.trim() === '') {
            alert("사용자 이름을 입력하세요")
            return false;
        }
        if (files.length === 0) {
            alert('파일을 첨부하세요.');
            return false;
        }
        return true;
    }


    $("#upload-button").on("click", function () {

        // attachment 제목
        let attachmentName = $("#attachment-name").val();

        let systemId = $("#system-id").val();
        let userName = $("#user-name").val();

        // attachment 내용
        let attachmentDescription = $("#attachment-description").val();

        // input:file 데이터
        let files = fd.getAll('file');

        //파일의 개수
        let uploadedFileCount = $(".statusbar").length;
        console.log("업로드된 파일 개수:", uploadedFileCount);
        let count = 0;

        let file = $("#file-upload").val();

         if (!validateForm(attachmentName, systemId, userName, files)) {
             return;
         }


        // metadata
        let metadata = {
            attachmentName: attachmentName,
            attachmentDescription: attachmentDescription,
            systemId: systemId,
            userName: userName,
            fileDescriptions: [],
        };

        let fileDescription = "";



        for (let i = 0; i < uploadedFileCount; i++) {
            fileDescription = ($(".statusbar").eq(i).find(".file-description").val());

            if (fileDescription == "" || fileDescription == null) {
                metadata.fileDescriptions.push("");
            } else {
                metadata.fileDescriptions.push(fileDescription); // 메타데이터에 추가
            }
        }

        console.log(metadata)
        console.log(file)


        // fd에 메타데이터 추가
        fd.append('metadata', JSON.stringify(metadata)); // 메타데이터 추가  
        for (let i = 0; i < files.length; i++) {
            fd.append('files', files[i]);
        }

        // AJAX
        $.ajax({
            type: "POST",
            url: "/api/attachments",
            data: fd,
            contentType: false,
            processData: false,
            cache: false,
            enctype: 'multipart/form-data',
            dataType: "json",
            success: function (result) {

                console.log(result);

                if (result.status >= 200 && result.status < 300) {

                    // 성공적인 응답 처리
                    console.log("요청이 성공적으로 처리되었습니다.");
                    location.href="attachmentlist";

                } else if (result.status >= 400 && result.status < 500) {

                    // 클라이언트 오류 처리
                    console.error("클라이언트 오류가 발생했습니다.");

                } else if (result.status >= 500 && result.status < 600) {

                    // 서버 오류 처리
                    console.error("서버 오류가 발생했습니다.");

                } else {

                    // 그 외의 상황 처리
                    console.error("알 수 없는 오류가 발생했습니다.");

                }
                alert(result.data)
            },
            error: function (xhr, status, error) {
                // 실패 시 처리
            }
        });//ajax endpoint
    });
});


