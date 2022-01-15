 $(document).ready(function() {
    var jayuNo = $('#jayu_no').val();
    var likesCnt = $('#likesCnt');


    $('#likeBtn').click(function(){
        var likeBtnImg = $(this).children("img");

        likeBtnImg.attr("src", function(index, attr){
            if(attr.match('_1')){
                return attr.replace('_1', '_0');
            } else {
                return attr.replace('_0', '_1');
            }
        });

        $.ajax({
            url : "/antddun/jayu/likes/" + jayuNo,
            type : 'POST',
            data: jayuNo,
            dataType: 'JSON',
            success : function(data){
                        if ( data.response == null) { //좋아요 해제 후 db에 있는 개수 반영
                            likesCnt.text(JSON.stringify(data.likesCnt));

                        } else { //좋아요, db에 있는 개수 반영
                            likesCnt.text(JSON.stringify(data.likesCnt));

                        }
            },
            error : function(data){
                console.log(data);
                alert("로그인 후 이용가능");
//                window.location.href = 'http://localhost:8080/antddun/login';
            }

            });


    });

});
