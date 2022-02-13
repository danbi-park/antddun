$(document).ready(function() {
   var count = 0;

    // 댓글 등록
    $(".btn-reply").click(function() {
        var jayuNo = $("#jayu_no").val();
        var replyText = $(".form-control").val();
        var data = {
            jayuNo: jayuNo,
            replyText: replyText
        }

        if(replyText == ""){
            alert("내용을 입력해주세요.");
            $('textarea[name="replyText"]').focus();
            return false;
        }

        $.ajax({
            url: '/antddun/jayu/reply/register',
            method: 'POST',
            dataType: 'json',
            data: JSON.stringify(data),
            contentType: 'application/json; charset=utf-8',
            success: function() {
                console.log(data);
                self.location.reload();
            }
        }) // ajax end.
    });

    //댓글 수정
    $(".reply-modify").click(function() {
        var parent = $(this).parent().parent();
        var replyNo = parent.children(".reply-no").val();
        var replyText = parent.children(".reply-text-box");
        var replier = parent.children(".replier");

        count++;

        if(count == 1){
            replyText.removeAttr("readonly");
            replyText.css('border','solid 1px gray');
            replyText.css('border-radius','5px');
            replyText.css('cursor','text');

            replyText.focus();

        } else if(count == 2) {

            var modify = {
                jayuRno: replyNo,
                replyText: replyText.val()
            }

            $.ajax({
                url: "/antddun/jayu/reply/modify/" + replyNo,
                method: "post",
                data: JSON.stringify(modify),
                contentType: "application/json; charset=utf-8",
                success: function(result) {
                    if(result === "modify") {
                        alert("댓글 수정");
                        console.log(replyText.val());
                        self.location.reload();
                    }
                }
            })
            count = 0;
        }
    });

    //댓글 삭제
    $(".reply-delete").click(function() {
        var parent = $(this).parent().parent();
        var replyNo = parent.children(".reply-no").val();

        $.ajax({
            url: "/antddun/jayu/reply/remove/" + replyNo,
            method: "delete",
            success: function(result) {
                    self.location.reload();
                }
        }) // ajax end.
    });

   //검색
   var qnaSearchForm = $("#jayuSearchForm");
   $('.searchButton').click(function(e){
        jayuSearchForm.submit();
   });

   //검색어 비우기
   $('.btn_truncate').click(function(){
    $('#search-bar').val('');
   });

}); // end.
