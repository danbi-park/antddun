$(document).ready(function() {
    var qnaNo = $("#qnaNo").val();
    var mno = $("#mno").val();

    var count = 0; //use in both registerForm.html and read.html

//댓글 높이 자동 조절 미리 세팅
    $('.reply-text-box').each(function () {
      this.setAttribute('style', 'height:' + (this.scrollHeight) + 'px;overflow-y:hidden;');
    }).on('.reply-text-box', function () {
      this.style.height = 'auto';
      this.style.height = (this.scrollHeight) + 'px';
    });


/*---------------- registerForm.js -----------------*/

//뚠 도움말 토글
    var info_helper = $('.info_helper');

    $('.btn_helper').click(function(){
       count++;

        if(count == 1){
              info_helper.css('display','inline-block');
              console.log(count);
        } else if(count == 2) {
              info_helper.css('display','none');
              console.log(count);
              count = 0;
        }
    })

    $('.button_close').click(function(){
        info_helper.css('display','none');
        count = 0;
    });

//뚠 거래 버튼
    $("#btn-register").click(function(event) {
        var title = $('#title').val();
        var input_box = $(".input_ddun");
        var input_ddun = parseInt($("#input_ddun").val());
        var total_ddun = parseInt($("#total_ddun").text());

        console.log("input_ddun: "+input_ddun);
        console.log("typeof input_ddun: "+typeof(input_ddun));

        if(input_ddun > total_ddun){
            alert("뚠이 부족합니다.");
            input_box.focus();
            return false;

        } else if(input_ddun == 0 || isNaN(input_ddun)){
           var confirmNoDdun = confirm("뚠 없이 질문하시겠습니까?");
           if(confirmNoDdun){
               $('#input_ddun').val(0);
           } else {
           input_box.focus();
           return false;
           }
        } else if((input_ddun % 10) != 0) {
            alert("뚠은 10단위로 적어주세요.");
            input_box.focus();
            return false;
        } else if (input_ddun > 0) {
            var confirmRegi = confirm(input_ddun + "뚠으로 질문하시겠습니까?");
            if(!confirmRegi){
              event.preventDefault();
              input_box.focus();
              return false;
            }
        }

        $.ajax({
           url: "/antddun/member/qna/betDdun",
           type: "GET",
           data: {
               "amount": input_ddun,
               "title": title
           },
           success: function(data) {
               if(data === "구매 성공") {
                   alert("거래가 완료되었습니다.");
               }
           }
        }); // ajax end.

        $("form").submit();

    }); //#btn-register".click end




/*---------------- read.js -----------------*/

// 댓글 등록
    $("#btn_reply").click(function() {
        console.log($(".form-control").val());
        var replyData = {
            qnaNo: qnaNo,
            replyText: $(".form-control").val()
        }

        $.ajax({
            url: "/antddun/member/qna/list/replySave",
            method: "POST",
            dataType: "json",
            data: JSON.stringify(replyData),
            contentType: 'application/json; charset=utf-8',
            success: function() {
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

        console.log(replyNo);

        count++;

        if(count == 1){
            console.log(count);
            replyText.removeAttr("readonly");
            replyText.css('border','solid 1px gray');
            replyText.css('border-radius','5px');
            replyText.css('cursor','text');

            replyText.focus();

        } else if(count == 2) {

            var replyModify = {
                qnaRno: replyNo,
                replyText: replyText.val()
            }

            $.ajax({
                url: "/antddun/member/qna/list/read/replyModify/"+replyNo,
                method: "post",
                data: JSON.stringify(replyModify),
                contentType: "application/json; charset=utf-8",
                success: function(result) {
                    if(result === "replyModify") {
                        alert("댓글 수정");
                        self.location.reload();
                    }
                }
            })
            count = 0;
        }
    });

// 댓글 삭제
    $(".reply-delete").click(function() {

        if(!confirm("삭제하시겠습니까?")){
          return;
        }

        var parent = $(this).parent().parent();
        var replyNo = parent.children(".reply-no").val();

        $.ajax({
            url: "/antddun/member/qna/list/replyDelete/" + replyNo,
            method: "post",
            success: function(result) {
                if(result === "delete"){
                console.log(replyNo); //잘나옴
                    alert("댓글이 삭제되었습니다.");
                    self.location.reload();
                }
            }
        }) // ajax end.
    }); // reply-modify click event


}); // end.