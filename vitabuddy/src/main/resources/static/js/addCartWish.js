/**
 * 
 */
 $(document).ready(function() {
    // 장바구니 추가
    $('.plusCartBtn').on('click', function() {
        const supId = $(this).data('sup-id');
        const userId = $(this).data('user-id');

        $.ajax({
            type: 'POST',
            url: '/supplement/wishList/toCart',  
            data: JSON.stringify({
                supId: supId,
                userId: userId
            }),
            contentType: 'application/json',
            success: function(response) {
                if (response === 1) {
                    alert('장바구니에 추가되었습니다');
                } else {
                    alert('장바구니 추가 실패');
                }
            },
            error: function() {
                alert('장바구니 추가 실패');
            }
        });
    });
});