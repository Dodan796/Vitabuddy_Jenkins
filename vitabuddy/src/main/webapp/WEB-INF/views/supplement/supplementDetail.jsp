<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>상품 상세 조회</title>
    <script src="<c:url value='/js/jquery-3.7.1.min.js'/>"></script>
    <script src="<c:url value='/js/rating.js'/>"></script>
    <script src="<c:url value='/js/deleteReview.js'/>"></script>
    <script src="<c:url value='/js/editReviewForm.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/supplementDetail.css'/>">
    <c:import url="/WEB-INF/views/layout/head.jsp" />
</head>
<body>
    <div id="wrap">
        <!-- top 메뉴 포함 -->
        <c:import url="/WEB-INF/views/layout/top.jsp" />

        <!-- 상품 프로필 -->
        <section class="prdProfile">
            <table>
                <tr>
                    <td colspan="4">
                        <h2>${supplementDetail.supName}</h2>
                    </td>
                    <td rowspan="6">
                        <!-- Base64 인코딩된 이미지 출력 -->
                        <img class="prdImg" src="data:image/png;base64,${supImgBase64}" width="300" height="300" alt="Product Image">
                    </td>
                </tr>

                <!-- 가격 정보 -->
                <tr>
                    <td>가격</td>
                    <td>
                        <span id="price" data-price="${supplementDetail.supPrice}">
                            <fmt:formatNumber value="${supplementDetail.supPrice}" pattern="#,###" />
                        </span> 원
                    </td>
                    <td></td><td></td>
                </tr>

                <!-- 브랜드 정보 -->
                <tr>
                    <td>브랜드</td>
                    <td>${supplementDetail.supBrand}</td>
                    <td></td><td></td>
                </tr>

                <!-- 찜목록 및 장바구니 추가 -->
                <tr>
                    <td colspan="2">
                        <a href="#" id="addWish" data-prd-id="${supplementDetail.supId}">찜목록 추가</a>
                    </td>
                    <td colspan="2">
                        <a href="#" id="addCart" data-prd-id="${supplementDetail.supId}">장바구니 추가</a>
                    </td>
                    <td></td>
                </tr>
            </table>
        </section>

        <br>

        <!-- 상품 상세 정보 -->
        <section class="prdInfo">
            <h2>상품 상세 정보</h2>
            <div>
                <!-- 상품 설명 -->
                <h3>상품설명</h3>
                <p>${supplementDetail.supDetail}</p>

                <!-- 복용법 -->
                <h3>복용법</h3>
                <p>${supplementDetail.supDosage}</p>

                <!-- 주의사항 -->
                <h3>주의사항</h3>
                <p>${supplementDetail.supPrecautions}</p>

                <!-- 영양 정보 -->
                <h3>영양정보</h3>
                <table>
                    <tr>
                        <td class="supNutri">${supplementDetail.supNutri}</td>
                        <td>${supplementDetail.supNutriInfo}</td>
                    </tr>
                </table>
            </div>
        </section>

        <!-- 리뷰 작성 -->
        <section class="reviewWrite">
            <form method="post" id="reviewForm" enctype="multipart/form-data" action="/supplement/supplementDetail/${supplementDetail.supId}/review">
                <input type="hidden" name="reviewNo" value="${review.reviewNo}">
                <input type="hidden" name="userId" value="${sessionScope.sid}">
                <input type="hidden" name="supId" value="${supplementDetail.supId}">
                
                <!-- 리뷰 제목 -->
                <label>리뷰제목</label>
                <br>
                <input class="reviewTitle" type="text" name="reviewTitle" value="${review.reviewTitle}">
                <br>

                <!-- 별점 -->
                <div class="rating">
                    <label class="ratingLabel" for="star1">
                        <input type="radio" id="star1" class="ratingInput" name="rating" value="1" ${review.rating == 1 ? 'checked' : ''}>
                        <span class="starIcon" data-value="1"><i class="fa-regular fa-star"></i></span>
                    </label>
                    <label class="ratingLabel" for="star2">
                        <input type="radio" id="star2" class="ratingInput" name="rating" value="2" ${review.rating == 2 ? 'checked' : ''}>
                        <span class="starIcon" data-value="2"><i class="fa-regular fa-star"></i></span>
                    </label>
                    <label class="ratingLabel" for="star3">
                        <input type="radio" id="star3" class="ratingInput" name="rating" value="3" ${review.rating == 3 ? 'checked' : ''}>
                        <span class="starIcon" data-value="3"><i class="fa-regular fa-star"></i></span>
                    </label>
                    <label class="ratingLabel" for="star4">
                        <input type="radio" id="star4" class="ratingInput" name="rating" value="4" ${review.rating == 4 ? 'checked' : ''}>
                        <span class="starIcon" data-value="4"><i class="fa-regular fa-star"></i></span>
                    </label>
                    <label class="ratingLabel" for="star5">
                        <input type="radio" id="star5" class="ratingInput" name="rating" value="5" ${review.rating == 5 ? 'checked' : ''}>
                        <span class="starIcon" data-value="5"><i class="fa-regular fa-star"></i></span>
                    </label>
                </div>

                <!-- 리뷰 내용 -->
                <br>
                <textarea class="reviewTxt" name="content" cols="150" rows="5">${review.content}</textarea>
                <br>

                <!-- 사진 첨부 -->
                <input class="Upload" type="file" id="uploadFile" name="reviewImg" multiple>

                <!-- submit 버튼 -->
                <input type="submit" value="작성하기" class="btn btnFilled">
            </form>
        </section>

        <!-- 리뷰 목록 -->
        <section class="reviews">
            <c:forEach var="review" items="${reviewList}">
                <table class="reviewItem">
                    <tr>
                        <td colspan="5"><h3>${review.reviewTitle}</h3></td>

                        <!-- 리뷰 수정 및 삭제 버튼 -->
                        <td colspan="2">
                            <c:if test="${review.userId == sessionScope.sid}">
                                <button class="deleteReview" data-review-id="${review.reviewNo}" data-sup-id="${supplementDetail.supId}">삭제</button>
                                <button class="editButton" data-sup-id="${supplementDetail.supId}" data-review-no="${review.reviewNo}">수정</button>
                            </c:if>
                        </td>
                    </tr>

                    <!-- 작성자 정보 -->
                    <tr>
                        <td colspan="4" class="userInfo">
                            <p>작성자: ${review.userId}</p>
                            <p>작성일: <fmt:formatDate value="${review.reviewDate}" pattern="yyyy-MM-dd" /></p>
                        </td>

                        <!-- 리뷰 이미지 -->
                        <td class="reviewImg" colspan="3">
                            <c:forEach var="img" items="${fn:split(review.reviewImg, ';')}">
                                <img class="reviewImg" src="/Review_Upload/${img}" alt="Review Image">
                            </c:forEach>
                        </td>
                    </tr>

                    <!-- 리뷰 내용 -->
                    <tr>
                        <td colspan="7" class="content">
                            <p>${review.content}</p>
                        </td>
                    </tr>

                    <!-- 기간 및 해시태그 -->
                    <tr class="supInfo">
                        <td colspan="3"></td>
                        <td colspan="2">복용기간: <fmt:formatDate value="${review.startDate}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${review.endDate}" pattern="yyyy-MM-dd" /></td>
                        <td>해시태그: ${review.reviewHashtag}</td>
                        <td>
                            <div class="rate">
                                <c:forEach var="i" begin="1" end="5">
                                    <i class="${i <= review.rating ? 'fa-solid fa-star' : 'fa-regular fa-star'}"></i>
                                </c:forEach>
                            </div>
                        </td>
                    </tr>

                    <!-- 신고하기 버튼 -->
                    <tr>
                        <td colspan="6"></td>
                        <td><a href="#" class="btn btnUnfilled">신고하기</a></td>
                    </tr>
                </table>
            </c:forEach>
        </section>

        <!-- 페이지네이션 -->
        <nav>
            <div class="pagination">
                <a href="#" class="prev <c:if test='${currentPage == 1}'>disabled</c:if>'" data-page="${currentPage - 1}">
                    <i class="fa-solid fa-caret-left"></i>
                </a>
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <a href="#" class="page" onclick="changePage(${i})">${i}</a>
                </c:forEach>
                <a href="#" class="next <c:if test='${currentPage == totalPages}'>disabled</c:if>'" data-page="${currentPage + 1}">
                    <i class="fa-solid fa-caret-right"></i>
                </a>
            </div>
        </nav>

        <!-- footer 포함 -->
        <c:import url="/WEB-INF/views/layout/footer.jsp" /> 
    </div>
</body>
</html>
