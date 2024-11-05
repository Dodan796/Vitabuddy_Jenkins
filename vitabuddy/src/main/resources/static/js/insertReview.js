/**
 *
 */
$(document).ready(function () {
  $("#reviewForm").on("submit", function (e) {
    e.preventDefault();
    var form = $("#reviewForm")[0];
    var formData = new FormData(form);
    var files = $("#uploadFile")[0].files;
    var supId = $("input[name='supId']").val();

    // 사진 3장 제한 체크 => 3장 초과 시 더 이상 진행하지 않음
    if (files.length > 3) {
      alert("사진은 최대 3장까지 업로드 가능합니다.");
      return;
    } else if (files.length < 1){ //=> 최소 한장의 이미지 업로드를 위한 코드 추가 11.05 .
    	alert("최소 1장의 사진을 업로드 하세요.");
      return;
    }

    console.log("supId: ", "${supplementDetail.supId}");
    // Ajax 요청
    $.ajax({
      url: `/api/supplement/supplementDetail/${supId}/review`,
      type: "POST",
      data: formData,
      processData: false,
      contentType: false,
      success: function (response) {
        alert("리뷰가 성공적으로 작성되었습니다!");
        window.location.replace(`/api/supplement/supplementDetail/${supId}`);
      },
      error: function (xhr, status, error) {
        console.error("상태 코드:", xhr.status);
        console.error("에러 메시지:", error);
        alert("리뷰 작성에 실패했습니다: " + error);
      },
    });
  });

  // 뒤로가기 시 캐시 비활성화 => 캐시된 페이지를 다시 불러오지 않도록 새로고침
  $(window).on("pageshow", function (event) {
    if (event.originalEvent.persisted) {
      window.location.reload();
    }
  });
});

/////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 리뷰 요소 선택시 로그인 알림 팝업 설정
 */

$(document).ready(function () {
  // HTML 요소에서 로그인 상태를 가져옴
  var isLoggedIn = $("#loginStatus").data("login");

  // 입력 필드, select 태그에 포커스 갈 경우 (alert 창에서 문제 발생-input[type="date"] 제외)
  $('#reviewForm input:not([type="date"]), #reviewForm textarea').on(
    "focus",
    function () {
      if (!isLoggedIn) {
        $(this).blur(); // 현재 포커스 해제
        alert("리뷰 작성 시 로그인이 필요합니다.");
        //window.location.href = "/intro";
      }
    }
  );

  // 폼 제출하는 경우
  $("#reviewForm").on("submit", function () {
    if (!isLoggedIn) {
      event.preventDefault();
      alert("리뷰 작성 시 로그인이 필요합니다.");
    }
  });
});