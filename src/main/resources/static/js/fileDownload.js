$(document).ready(function () {
    $("#fileDownload").on("click", function (string, radix){
        //file, attachment id  가져와서 세팅
        var ids = [];
        var attachmentIds = [];
        $('input[name="checkbox1"]:checked').each(function() {
            ids.push($(this).val());
            var attachmentId = $(this).closest(".file-row").find(".file-id").val();
            attachmentIds.push(attachmentId);
        });

        var attachmentId = attachmentIds[0]; // attachment Ids의 첫번째 인덱스 값
        if (ids.length > 0) {
            // 파일 다운로드 URL 생성
            var downloadUrl = "/file/download?attachment=" + attachmentId + "&id=" + ids.join("&id=");
            console.log(downloadUrl);
            // 파일 다운로드 시작
            window.location.href = downloadUrl;
        } else {
            console.log("No file selected");
        }
    });
});

$(document).ready(function () {
    $("#attachmentDownload").on("click", function (string, radix){
        var attachmentId = 1; // attachment ID는 필요에 따라 변경하십시오.

        $('input[name="selectedAttachments"]').on('change', function() {
            // 모든 체크박스의 선택 해제
            $('input[name="selectedAttachments"]').not(this).prop('checked', false);
        });

        // "fileDownload" 버튼 클릭 시 실행되는 함수
        $("#fileDownload").on("click", function () {
            // 선택된 체크박스의 값을 저장할 변수
            var attachmentId = $('input[name="selectedAttachments"]:checked').val();

            // 선택된 체크박스의 값을 콘솔에 출력
            console.log("Selected Attachment ID:", attachmentId);
        });

        // 파일 다운로드 URL 생성
        var downloadUrl = "/attachment/download?attachment=" + attachmentId;
        console.log(downloadUrl);
        window.location.href = downloadUrl;
        console.log("No file selected");
    });
});

$(document).ready(function () {
    // 해당 아이디의 버튼을 클릭시 발생하는 이벤트
    $("#file-test-download").on("click", function (string, radix){

        var ids = [];
        var attachmentId = 1; // attachment ID는 필요에 따라 변경하십시오.

        // 체크박스가 선택된 항목의 각각 실행하는 이벤트
        $('.file-checkbox:checked').each(function(){
            ids.push($(this).data("id")); // ids에 id 삽입
        });
        if (ids.length > 0) {

            // 파일 다운로드 URL 생성
            var downloadUrl = "/file/download?attachment=" + attachmentId + "&id=" + ids.join("&id=");
            console.log(downloadUrl);

            // 파일 다운로드 시작
            window.location.href = downloadUrl;

        } else {
            console.log("No file selected");
        }
    });
});

$(document).ready(function () {
    // 해당 아이디의 버튼을 클릭시 발생하는 이벤트
    $("#attachment-test-download").on("click", function (string, radix){

        var attachmentId = 1; // attachment ID는 필요에 따라 변경하십시오.

        // 첨부목록 다운로드 URL 생성
        var downloadUrl = "/att/download?attachment=" + attachmentId;
        console.log(downloadUrl);

        // 첨부목록 다운로드 시작
        window.location.href = downloadUrl;
    });
});