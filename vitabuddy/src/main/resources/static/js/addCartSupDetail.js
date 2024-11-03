/**
 * 
 */
 
$(document).ready(function() {
    // HTML 요소에서 로그인 상태를 가져옴
    var isLoggedIn = $('#loginStatus').data('login');
    
    $('#addCart').on('click', function() {
        event.preventDefault(); // 버튼의 기본 동작 방지
        
        if (!isLoggedIn) { // 로그인 되지 않은 상태
            alert('로그인이 필요한 서비스입니다.');
            window.location.href = "/intro";
        } else { // 로그인 상태
            const supId = $(this).data('sup-id');
            const userId = $(this).data('user-id');

            $.ajax({
                type: 'post',
                url: '/api/addCart',
                data: JSON.stringify({
                    supId: supId,
                    userId: userId
                }),
                contentType: 'application/json',
                success: function(response) {
                    if (response == 1) { // if문 추가
                        alert('장바구니에 추가되었습니다');
                        // 장바구니 추가후 URL 변경 기록을 남기지 않고 URL을 업데이트
                        history.replaceState(null, null, window.location.href);
                    }
                },
                error: function() {
                    alert('장바구니 추가 실패');
                }
            });
        }
    });

    // 1025 - 뒤로 가기 버튼을 한 번 눌렀을 때 이전 상점 페이지로 넘어갈 수 있도록 하는 코드
    $(window).on("pageshow", function(event) {
        if (event.originalEvent.persisted) {
            window.location.reload();
        }
    });
});
