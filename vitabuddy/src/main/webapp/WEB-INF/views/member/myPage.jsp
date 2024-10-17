<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/mypage.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/head.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/footer.css'/>">
<c:import url="/WEB-INF/views/layout/head.jsp"/>
<meta charset="UTF-8">
<title>마이페이지</title>
</head>
<body>
<c:import url="/WEB-INF/views/layout/top.jsp" /> 

<section id="wrap">
    <div class="container">
        <div class="headers">
            <h1>마이페이지</h1>
        </div>
        
        <div class="horizontal_box" style="width:80%;padding-right:20%; padding-left:20%;">
            <form>
                <label>이름</label>
                <input type="text" value="${getMember.getUserName()}" class="name" name="userName" readonly />
                
                <label>복용 중인 영양제</label>
                <textarea rows="10%" cols="100%" value="${getMember.getUserTabletList()}" style="resize: none; width: 75%;" readonly></textarea>
                
                <div class="box_rowContents" style="display:flex; justify-content: space-between;padding-right:44%;"> 
                    <div class="horizontal_box" readonly>    
                        <label>추천 성분</label>
                        <textarea rows="20%" style="width:250%; resize: none;"></textarea>
                    </div>
                    <div class="horizontal_box" readonly>
                        <label>성분간 상호작용</label>
                        <textarea rows="20%" style="width:250%; resize: none;"></textarea>
                    </div>
                </div>
            </form>
        </div>

        <button class="submit-btn" onclick="location.href='/member/myInfoChangeForm?userId=${sessionScope.sid}'">회원 정보 수정하기</button>

        <!-- 내가 작성한 리뷰 목록 -->
        <div class="review-section" style="padding-right:20%; padding-left:20%;">
            <h3>내가 작성한 리뷰 목록</h3>
            <form action="/myPage/reviews" method="get">
                <label for="sort">정렬 기준:</label>
                <select name="sort" id="sort" onchange="this.form.submit()">
                    <option value="recent">최신순</option>
                    <option value="oldest">오래된 순</option>
                </select>
            </form>
            
            <table id="reviewList">
                <thead>
                    <tr>
                        <th>번호</th>
                        <th class="important">제목</th>
                        <th>상품명</th>
                        <th>작성일</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty reviews}">
			            <c:forEach var="review" items="${reviews}" varStatus="status">
			                <tr>
			                    <td>${status.index + 1}</td> <!-- 리뷰 번호를 1부터 시작 -->
			                    <td class="important">${review.reviewTitle}</td>
			                    <td>${review.supName}</td> <!-- 상품명을 표시할 컬럼 -->
			                    <td><fmt:formatDate value="${review.reviewDate}" pattern="yyyy-MM-dd" /></td>
			                </tr>
			            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4" align="center">작성하신 리뷰가 없습니다.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

        <!-- 구매 내역 -->
        <div class="purchase-section" style="padding-right:20%; padding-left:20%;">
            <h3>구매 내역</h3>
            <select>
                <option>현재 주문 처리 상태</option>
                <option>입금 전</option>
                <option>배송 준비 중</option>
                <option>배송 중</option>
                <option>배송 완료</option>
                <option>취소</option>
                <option>반품</option>
                <option>환불</option>
            </select>
            <table width="100%" id="productInfo">
                <thead>
                    <tr>
                        <th>주문일자</th>
                        <th>이미지</th>
                        <th>상품정보</th>
                        <th>수량</th>
                        <th>금액</th>
                        <th>주문 처리</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="6" align="center">주문 내역이 없습니다.</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</section>

<c:import url="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>
