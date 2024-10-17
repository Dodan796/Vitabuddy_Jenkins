$(document).ready(function () {
  $(".loginForm").on("submit", function (event) {  // event를 인자로 추가
    event.preventDefault();
    $.ajax({
      type: "post",
      url: "/intro/login",
      data: { 
        id: $("#id").val(), 
        pwd: $("#pwd").val() 
      },
      dataType: "text",
      success: function (result) {
        if (result === "success") {  // strict equality 사용
          window.location.href = "/home";
        } else {
          alert("회원정보가 일치하지 않습니다.");
        }
      },
      error: function () {
        alert("요청 실패");
      }
    });
  });
});
