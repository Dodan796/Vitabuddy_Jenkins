$(document).ready(function() {
    // 폼 제출 시 Ajax 호출
    $('#searchForm').on('submit', function(event) {
        event.preventDefault();  // 폼 기본 제출 방지
        var keyword = $('#keyword').val().trim();  // 입력한 키워드 값, 양옆 공백 제거
        var page = 1;

        // 검색 URL로 상태 업데이트 (replaceState로 중복 방지)
        var searchUrl = `/supplement/supplementList?keyword=${encodeURIComponent(keyword)}&page=${page}`;
        history.replaceState({ keyword: keyword, page: page }, '', searchUrl);  // 검색 최초 실행 시 replaceState
        performSearch(keyword, page, false);  // 검색 수행 (isHistoryUpdate는 false로 설정)
    });

    // 페이지네이션 설정 함수(검색 시 페이지네이션 생성)
    function setupPagination(currentPage, totalPages, keyword) {
        $('.pagination').empty(); // 기존 페이지네이션 초기화

        if (totalPages > 0) {
            // 이전 버튼
            $('.pagination').append(`<button class="prev ${currentPage === 1 ? 'disabled' : ''}" data-page="${currentPage - 1}" ${currentPage === 1 ? 'disabled' : ''}>
                <i class="fa-solid fa-caret-left"></i>
            </button>`);

            // 페이지 번호 버튼 생성
            for (let i = 1; i <= totalPages; i++) {
                const activeClass = (i == currentPage) ? 'active' : ''; // 현재 페이지 버튼에 active 클래스 추가
                $('.pagination').append(`<button class="page-btn ${activeClass}" data-page="${i}">${i}</button>`);
            }

            // 다음 버튼
            $('.pagination').append(`<button class="next ${currentPage === totalPages ? 'disabled' : ''}" data-page="${currentPage + 1}" ${currentPage === totalPages ? 'disabled' : ''}>
                <i class="fa-solid fa-caret-right"></i>
            </button>`);
        }

        // 페이지 버튼 클릭 시
        $('.page-btn').off('click').on('click', function(event) {
            event.preventDefault(); // 기본 동작 방지
            const page = $(this).data('page');
            const keywordFromUrl = new URLSearchParams(window.location.search).get('keyword'); // URL에서 keyword를 가져옴
            performSearch(keywordFromUrl, page);  // 페이지 변경 시 pushState 필요 없음
        });

        // 이전 버튼 클릭
        $('.prev').off('click').on('click', function(event) {
            event.preventDefault();
            if (currentPage > 1) {
                const keywordFromUrl = new URLSearchParams(window.location.search).get('keyword'); // URL에서 keyword를 가져옴
                performSearch(keywordFromUrl, currentPage - 1);  // 이전 페이지로 이동
            }
        });

        // 다음 버튼 클릭
        $('.next').off('click').on('click', function(event) {
            event.preventDefault();
            if (currentPage < totalPages) {
                const keywordFromUrl = new URLSearchParams(window.location.search).get('keyword'); // URL에서 keyword를 가져옴
                performSearch(keywordFromUrl, currentPage + 1);  // 다음 페이지로 이동
            }
        });
    }

    // 새로고침 시에도 페이지가 유지되도록 설정
    $(document).ready(function() {
        const urlParams = new URLSearchParams(window.location.search);  // URL 파라미터 가져오기
        const keyword = urlParams.get('keyword');  // URL에서 keyword 값 가져오기
        const page = urlParams.get('page') || 1;  // URL에서 페이지 번호 가져오고 없으면 기본 1로 설정

        if (keyword) {
            // 새로고침 감지
            if (performance.navigation.type === performance.navigation.TYPE_RELOAD) {
                // 새로고침 시에는 전체 페이지를 다시 로드하여 HTML 구조가 정상적으로 로드되도록 설정
                window.location.href = `/supplement/supplementList?keyword=${encodeURIComponent(keyword)}&page=${page}`;
            } else {
                // AJAX로 검색 수행
                performSearch(keyword, page, false);  // 새로고침 시 pushState 필요 없음
            }
        }
    });

    // performSearch 함수 (AJAX 요청)
    function performSearch(keyword, page, isHistoryUpdate = true) {
        keyword = keyword || '';  // keyword가 undefined 또는 null인 경우 빈 문자열로 설정

        // URL을 검색 조건에 맞게 업데이트
        var searchUrl = `/supplement/supplementList?keyword=${encodeURIComponent(keyword)}&page=${page}`;

        // 브라우저 URL 및 히스토리 상태 업데이트 (isHistoryUpdate가 true일 때만 실행)
        if (isHistoryUpdate) {
            history.pushState({ keyword: keyword, page: page }, '', searchUrl);  // 중복 방지를 위해 필요할 때만 pushState
        }

        // AJAX 요청을 통해 검색 결과를 서버로부터 받아옴
        $.ajax({
            url: '/search',
            type: 'GET',
            data: { keyword: keyword, page: page },
            success: function(response) {
                $('.products').empty(); // 기존 상품 목록 초기화

                if (response.pagingkeywordsupList && response.pagingkeywordsupList.length <= 0) {
                    $('.products').append('<h4>검색 결과가 없습니다</h4>');  // 검색 결과가 없을 경우 처리
                } else if (response.pagingkeywordsupList) {
                    response.pagingkeywordsupList.forEach(function(keywordsup) {
                        // 검색 결과 상품을 리스트로 표시
                        $('.products').append(`
                            <div class="productItem">
                                <a href="/api/supplement/supplementDetail/${keywordsup.supId}">
                                    <img class="prdImg" src="data:image/png;base64,${keywordsup.base64SupImg}" width="300" height="300">
                                </a>
                                <p>${keywordsup.supName}</p>
                                <p>${new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW' }).format(keywordsup.supPrice)}</p>
                                <p>${keywordsup.supBrand}</p>
                            </div> 
                        `);
                    });
                    // 페이지네이션 설정 호출
                    setupPagination(response.currentPage, response.totalPages, keyword);

                    // 페이지 이동 후 화면을 최상단으로 스크롤
                    window.scrollTo(0, 0);
                }
            },
            error: function() {
                alert('오류가 발생했습니다.');
            }
        });
    }

    // 브라우저 히스토리 상태 변경 시 (뒤로 가기 또는 앞으로 가기 버튼 클릭 시) 처리
    window.addEventListener('popstate', function(event) {
        if (event.state) {
            // 히스토리 상태에서 저장된 키워드와 페이지 정보를 가져옴
            var keyword = event.state.keyword || '';
            var page = event.state.page || 1;

            // AJAX로 검색 결과 다시 로드 (isHistoryUpdate를 false로 설정하여 중복 저장 방지)
            performSearch(keyword, page, false);
        }
    });
});