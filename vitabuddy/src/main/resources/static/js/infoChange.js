// URL에서 userId 파라미터 추출
function getUserIdFromUrl() {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get("userId");
}

// 현재 사용자 ID를 전역 변수로 설정
const userId = getUserIdFromUrl();

if (!userId) {
  alert("사용자 ID가 올바르게 설정되지 않았습니다.");
} else {
  console.log("현재 사용자 ID:", userId); // 디버그용 로그
}

// 검색된 영양제 목록 표시
function searchSupplements() {
  const keyword = document.getElementById("keyword").value;
  const brand = document.getElementById("brand").value;

  $.ajax({
    url: "/supplement/search",
    method: "GET",
    data: { keyword: keyword, brand: brand },
    success: function (data) {
      console.log("검색 결과:", data); // 검색 결과 확인
      displaySupplementList(data);
    },
    error: function () {
      alert("영양제 검색에 실패했습니다.");
    },
  });
}

// 검색 결과를 표시하는 함수
function displaySupplementList(supplements) {
  const supplementList = document.getElementById("supplementList");
  supplementList.innerHTML = "";

  supplements.forEach((supplement) => {
    const listItem = document.createElement("li");
    listItem.textContent = `${supplement.supName} (${supplement.supBrand})`;

    // 선택 버튼 추가
    const selectButton = document.createElement("button");
    selectButton.textContent = "추가";
    selectButton.onclick = function () {
      addToCurrentList(supplement);
    };

    listItem.appendChild(selectButton);
    supplementList.appendChild(listItem);
  });
}

// 선택된 영양제를 복용 중인 리스트에 추가하고 삭제 버튼 생성
function addToCurrentList(supplement) {
  const currentList = document.getElementById("currentSupplementList");
  const listItem = document.createElement("li");
  listItem.textContent = `${supplement.supName} (${supplement.supBrand})`;
  listItem.setAttribute("data-id", supplement.supID);

  // 삭제 버튼 추가
  const deleteButton = document.createElement("button");
  deleteButton.textContent = "삭제";
  deleteButton.onclick = function () {
    removeFromCurrentList(supplement.supID);
  };

  listItem.appendChild(deleteButton);
  currentList.appendChild(listItem);

  $.ajax({
    url: "/supplement/add",
    method: "POST",
    data: { userId: userId, supId: supplement.supID }, // URL에서 추출한 userId가 삽입됨
    success: function (response) {
      console.log("영양제 추가 성공:", response);
      alert(response); // 성공 메시지 표시
    },
    error: function () {
      alert("영양제 추가에 실패했습니다.");
    },
  });
}

// 복용 중인 리스트에서 삭제
function removeFromCurrentList(supId) {
  const currentList = document.getElementById("currentSupplementList");

  // 서버에 삭제 요청
  $.ajax({
    url: `/supplement/delete?userId=${userId}&supId=${supId}`,
    method: "DELETE",
    success: function (response) {
      alert(response); // 성공 메시지 표시
      // 리스트에서 항목 제거
      const listItem = document.querySelector(
        `#currentSupplementList li[data-id='${supId}']`
      );
      if (listItem) {
        listItem.remove();
      }
    },
    error: function () {
      alert("영양제 삭제에 실패했습니다.");
    },
  });
}
/////////////////////////////////////////////////////////////////////////////////////////

function deleteSupplement(supId) {
  // 서버에 삭제 요청
  $.ajax({
    url: `/supplement/delete?userId=${userId}&supId=${supId}`, // userId는 전역 변수로 이미 설정되어 있음
    method: "DELETE",
    success: function (response) {
      alert(response); // 성공 메시지 표시
      // 페이지 새로고침
      location.reload(); // 삭제 후 페이지를 새로고침
    },
    error: function () {
      alert("영양제 삭제에 실패했습니다.");
    },
  });
}