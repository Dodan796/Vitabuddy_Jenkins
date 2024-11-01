/**
 * 
 */
 
$(document).ready(function() {
	// HTML 요소에서 로그인 상태를 가져옴
    var isLoggedIn = $('#loginStatus').data('login');
    
    $('#addCart').on('click', function() {
    if (!isLoggedIn) { // 로그인 되지 않은 상태
        alert('로그인이 필요한 서비스입니다.');
        }else{// 로그인 상태
        const supId = $(this).data('sup-id');
        const userId = $(this).data('user-id');

        $.ajax({
            type:'post',
            url:'/api/addCart',
            data:JSON.stringify({
                supId: supId,
                userId: userId
                
            }),
            contentType:'application/json',
            success:function(response) {
            	if(response == 1){    //수정사항 : if문 추가
                alert('장바구니에 추가되었습니다');
                }
            },
            error:function() {
                alert('장바구니 추가 실패');
            }
        });
        }
    });

//1025 - 뒤로가기 버튼 1번 눌렀을 때 한번에 상점 페이지로 넘어갈 수 있는 코드 (detail.jsp 페이지 href="" 로 수정함)
$(window).on("pageshow", function (event) {
    if (event.originalEvent.persisted) {
      window.location.reload();
    }
  });
  
  
});