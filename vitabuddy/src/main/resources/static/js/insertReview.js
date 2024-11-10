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
    
    //1108 정합성 검사 수정 폼
    //1. 리뷰 제목
    const reviewTitle = $("input[name='reviewTitle']").val().trim();
    if (reviewTitle === "") {
      alert("리뷰 제목을 작성해주세요.");
      return;
    }
    
    //2. 별점 선택
    const selectedRating = $("input[name='rating']:checked").length
    //console.log(selectedRating);  테스트 출력 : 별점을 선택하지 않고 "수정완료" 버튼을 누르면 길이가 0이라고 출력된다
    if (selectedRating==0) {   //별점 length 가 0일 때
      alert("별점을 입력해주세요.");
      return;
    }
    
    
	//3. 해시태그
    if (!$("#hashtag").val()) {
      alert("해시태그를 선택해 주세요.");
      return;
    }

	//4. 복용기간
    const startDate = $("#date1").val();
    const endDate = $("#date2").val();
    if (!startDate || !endDate) {
      alert("복용기간을 입력해주세요.");
      return;
    }
    
    
    //5. 리뷰 컨텐츠
    const reviewContent = $("textarea[name='content']").val().trim();
    if (reviewContent === "") {
      alert("리뷰를 작성해주세요.");
      return;
    }
    
    //1108-------------------------

    // 사진 3장 제한 체크 => 3장 초과 시 더 이상 진행하지 않음
    if (files.length > 3) {
      alert("사진은 최대 3장까지 업로드 가능합니다.");
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