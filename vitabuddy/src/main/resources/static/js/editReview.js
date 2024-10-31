/**
 *
 */
$(document).ready(function () {
  $("#reviewForm").on("submit", function () {
    event.preventDefault(); // 기본 제출 방지

    // 리뷰 수정 확인 알림
    if (!confirm("리뷰를 수정하시겠습니까?")) {
      return; // '취소'를 누르면 폼 제출 취소
    }

    const form = this; // 현재 폼

    $.ajax({
      type: "POST",
      url: $(form).attr("action"),
      data: new FormData(form),
      processData: false,
      contentType: false,
      success: function () {
        alert("리뷰가 수정되었습니다.");

        // 부모 창 새로고침
        if (window.opener && !window.opener.closed) {
          window.opener.location.reload();
        }

        // 팝업 창 닫기
        window.close();
      },
      error: function (error) {
        console.error("폼 제출 오류: ", error);
        alert("수정 중 오류가 발생했습니다.");
      },
    });
  });
});